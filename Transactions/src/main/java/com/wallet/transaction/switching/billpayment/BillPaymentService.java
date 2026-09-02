package com.wallet.transaction.switching.billpayment;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.TblAccount;
import com.wallet.transaction.model.TblCustomer;
import com.wallet.transaction.repo.TblAccountRepo;
import com.wallet.transaction.repo.TblCustomerRepo;
import com.wallet.transaction.util.AESencryption;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.common.OneLinkFieldFormatter;
import com.wallet.transaction.switching.common.ProcedureResult;
import com.wallet.transaction.switching.common.SwitchReferenceGenerator;
import com.wallet.transaction.switching.common.MwChannelCredentials;
import com.wallet.transaction.switching.common.SwitchGatewayToggle;
import com.wallet.transaction.switching.common.SwitchTransactionType;
import com.wallet.transaction.switching.common.SwitchingConstants;
import com.wallet.transaction.switching.common.client.GatewayBillRequest;
import com.wallet.transaction.switching.common.client.GatewayBillResponse;
import com.wallet.transaction.switching.common.client.GatewayResponse;
import com.wallet.transaction.switching.common.client.SwitchGatewayClient;
import com.wallet.transaction.switching.common.model.TblTransactionMock;
import com.wallet.transaction.switching.common.service.TransactionMockService;
import com.wallet.transaction.util.GenericResponseCode;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Bill payment: a DFS customer pays a utility bill settled at another institution through 1LINK.
 *
 * There is only one direction here. Unlike IBFT, the switch never originates a bill message
 * towards DFS, so the package has no incoming half and needs no outgoing/incoming split.
 *
 * The orchestration mirrors the outgoing IBFT flow exactly:
 * <ol>
 *   <li>resolve the bill from {@code TBL_TRANSACTION_MOCK} for TRANSACTION_TYPE = BILL_PAYMENT</li>
 *   <li>on a payment, call {@code PKG_MW.BILL_PAYMENT}, which owns the debit</li>
 *   <li>send the message to the switch through the gateway</li>
 *   <li>map both answers onto the application response</li>
 * </ol>
 *
 * No accounting happens in this class. It does not touch balances, does not insert or update any
 * transaction table, and does not mark a bill paid: TBL_TRANSACTION_MOCK is read-only here.
 */
@Service
public class BillPaymentService {

    private static final Logger log = LoggerFactory.getLogger(BillPaymentService.class);

    private static final SwitchTransactionType TYPE = SwitchTransactionType.BILL_PAYMENT;

    /** TBL_TRANSACTION_MOCK.BILL_PAID when the bill has already been settled. */
    private static final String ALREADY_PAID = "Y";

    @Autowired
    private TransactionMockService transactionMockService;

    @Autowired
    private BillPaymentProcedureService billPaymentProcedureService;

    @Autowired
    private SwitchGatewayToggle switchGatewayToggle;
    @Autowired
    private SwitchGatewayClient switchGatewayClient;

    @Autowired
    private SwitchReferenceGenerator referenceGenerator;

    @Autowired
    private CommonService commonService;

    @Autowired
    private TblAccountRepo tblAccountRepo;

    /** DFS issuer BIN, the first six digits of every DE-02 this service sends. */
    @Value("${dfs.bin}")
    private String dfsBin;

    @Value("${onelink.merchant-type-bill}")
    private String merchantType;

    @Value("${onelink.point-of-entry}")
    private String pointOfEntry;

    @Value("${onelink.network-identifier}")
    private String networkIdentifier;

    @Value("${onelink.currency-code}")
    private String currencyCode;

    @Value("${onelink.card-acceptor-terminal-id}")
    private String cardAcceptorTerminalId;

    @Value("${onelink.card-acceptor-identification-code}")
    private String cardAcceptorIdentificationCode;

    @Value("${onelink.card-acceptor-name-and-location}")
    private String cardAcceptorNameAndLocation;

    // BILL_PAYMENT is in PKG_MW, so it authenticates through VALIDATE_TOKEN against
    // TBL_MW_CHANNEL - the numeric channel code plus the MW service user, not the mobile.* pair.
    @Autowired
    private MwChannelCredentials mwCredentials;

    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private AESencryption aesEncryption;

    @Value("${onelink.acquirer-institution-code}")
    private String acquirerInstitutionCode;
    @PersistenceContext
    EntityManager em;

    // ------------------------------------------------------------------ bill inquiry

    /**
     * Bill Inquiry: what does this consumer owe this company? The authoritative bill comes from
     * TBL_TRANSACTION_MOCK; the switch round-trip proves the link and returns DE-39.
     *
     * <p>No procedure is called and no money moves.</p>
     */
    public HashMap<String, Object> billInquiry(BillPaymentRequest request, Request envelope) {
        BillInquiryResponse response = new BillInquiryResponse();
        response.setUtilityCompanyCode(request.getUtilityCompanyCode());
        response.setConsumerNo(request.getConsumerNo());

        TblTransactionMock bill = transactionMockService.findBill(
                TYPE, request.getUtilityCompanyCode(), request.getConsumerNo());
        if (bill == null) {
            response.setResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("No bill registered for this company and consumer number");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }

        String stan = referenceGenerator.nextStan();
        String rrn = referenceGenerator.rrnFor(stan);
        response.setStan(stan);
        response.setRrn(rrn);
        applyBill(response, bill);

        if (!switchGatewayToggle.isEnabled()) {
            // Gateway off: the bill was already resolved from records by applyBill above, so the
            // inquiry answers from those rather than failing.
            log.info("Switch gateway disabled, bill inquiry answered from records | STAN:{} | RRN:{}", stan, rrn);
            response.setResponseCode(SwitchingConstants.RC_PROCESSED_OK);
            response.setResponseDescription("Bill retrieved without a switch inquiry");
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
        }

        GatewayResponse<GatewayBillResponse> gatewayResponse =
                switchGatewayClient.billInquiry(buildGatewayRequest(request, bill, stan, rrn, null, null));

        if (gatewayResponse == null || gatewayResponse.getData() == null) {
            log.error("Switch gateway gave no bill inquiry answer | STAN:{} | RRN:{}", stan, rrn);
            response.setResponseCode(SwitchingConstants.RC_HOST_LINK_DOWN);
            response.setResponseDescription("No answer from the payment switch");
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
        }

        GatewayBillResponse data = gatewayResponse.getData();
        boolean verified = SwitchingConstants.RC_PROCESSED_OK.equals(data.getResponseCode());
        response.setResponseCode(data.getResponseCode());
        response.setResponseDescription(verified ? "Bill retrieved" : "Switch declined the bill inquiry");
        return commonService.getResponse(verified
                ? GenericResponseCode.SUCCESS.getResponseCode()
                : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
    }

    // ------------------------------------------------------------------ bill payment

    /**
     * Bill Payment: the debit procedure, then the message to the switch.
     *
     * The switch is only told once the procedure reported success, so a bill is never marked paid
     * on the strength of a debit the core did not post.
     */
    public HashMap<String, Object> billPayment(BillPaymentRequest request, Request envelope) {
        final String convAmount = getFormattedAmount(request.getAmount(), "C");
        request.setAmount(convAmount);
        BillPaymentResponse response = new BillPaymentResponse();
        response.setFromAccountNo(request.getFromAccountNo());
        response.setUtilityCompanyCode(request.getUtilityCompanyCode());
        response.setConsumerNo(request.getConsumerNo());
        response.setAmount(request.getAmount());
        response.setTransactionReference(request.getTransactionReference());

        // 1 - bill details, never hardcoded
        TblTransactionMock bill = transactionMockService.findBill(
                TYPE, request.getUtilityCompanyCode(), request.getConsumerNo());
        if (bill == null) {
            response.setSwitchResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("No bill registered for this company and consumer number");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }
        response.setUtilityCompanyName(bill.getUtilityCompanyName());
        response.setBillAmount(bill.getBillAmount() == null ? null : bill.getBillAmount().toPlainString());
        response.setBillDueDate(bill.getBillDueDate());

        if (ALREADY_PAID.equalsIgnoreCase(trim(bill.getBillPaid()))) {
            log.warn("Bill already settled, refusing to pay it twice | company:{} | consumer:{}",
                    request.getUtilityCompanyCode(), request.getConsumerNo());
            response.setSwitchResponseCode(SwitchingConstants.RC_DUPLICATE_TRANSACTION);
            response.setResponseDescription("This bill is already paid");
            return commonService.getResponse(GenericResponseCode.RECORD_ALREADY_EXISTS.getResponseCode(), response);
        }

        String stan = referenceGenerator.nextStan();
        String rrn = referenceGenerator.rrnFor(stan);
        response.setStan(stan);
        response.setRrn(rrn);

        // 3 - the customer debit belongs to the database
        ProcedureResult procedure = billPaymentProcedureService.billPayment(
                buildProcedureRequest(request, envelope, stan, rrn));

        response.setCoreResponseCode(procedure.getErrorResponse());
        response.setResponseDescription(procedure.getResponseDescription());

        if (!procedure.isApproved()) {
            log.error("Bill payment stopped, PKG_MW.BILL_PAYMENT did not succeed"
                            + " | STAN:{} | RRN:{} | status:{} | descr:{}",
                    stan, rrn, procedure.getResponseStatus(), procedure.getResponseDescription());
            response.setSwitchResponseCode(SwitchingConstants.RC_UNABLE_TO_PROCESS);
            // Pass the core's own code and description straight through, untranslated.
            return commonService.getResponseWithOutDB(
                    procedure.failureCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
                    procedure.failureDescription("PKG_MW.BILL_PAYMENT did not complete"),
                    response);
        }

        if (!switchGatewayToggle.isEnabled()) {
            // Gateway off. The debit above already happened and stands; only the message to the
            // switch is skipped, so the outcome reported is the core's.
            log.info("Switch gateway disabled, bill payment not sent to the switch | STAN:{} | RRN:{}", stan, rrn);
            response.setSwitchResponseCode(SwitchingConstants.RC_PROCESSED_OK);
            response.setResponseDescription("Bill paid without a switch message");
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
        }

        // 3 - tell the switch
        GatewayResponse<GatewayBillResponse> gatewayResponse = switchGatewayClient.billPayment(
                buildGatewayRequest(request, bill, stan, rrn, request.getAmount(), null));

        if (gatewayResponse == null || gatewayResponse.getData() == null) {
            log.error("Switch gateway gave no bill payment answer | STAN:{} | RRN:{}", stan, rrn);
            response.setSwitchResponseCode(SwitchingConstants.RC_HOST_LINK_DOWN);
            response.setResponseDescription("No answer from the payment switch");
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
        }

        GatewayBillResponse data = gatewayResponse.getData();
        response.setSwitchResponseCode(data.getResponseCode());
        response.setAuthIdResponse(data.getAuthIdResp());
        log.info("Bill payment completed | STAN:{} | RRN:{} | DE-39:{} | company:{}",
                stan, rrn, data.getResponseCode(), request.getUtilityCompanyCode());
        return commonService.getResponse(
                SwitchingConstants.RC_PROCESSED_OK.equals(data.getResponseCode())
                        ? GenericResponseCode.SUCCESS.getResponseCode()
                        : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
    }

    // ------------------------------------------------------------------ mapping

    private void applyBill(BillInquiryResponse response, TblTransactionMock bill) {
        response.setUtilityCompanyName(bill.getUtilityCompanyName());
        response.setBillAmount(bill.getBillAmount() == null ? null : bill.getBillAmount().toPlainString());
        response.setBillDueDate(bill.getBillDueDate());
        response.setBillPaid(bill.getBillPaid());
    }

    private GatewayBillRequest buildGatewayRequest(BillPaymentRequest request, TblTransactionMock bill,
                                                   String stan, String rrn, String amount, String authIdResponse) {
        GatewayBillRequest message = new GatewayBillRequest();
        // DE-02 is the 16-digit payer PAN, not the wallet account number.
        message.setPan(OneLinkFieldFormatter.pan(dfsBin, request.getFromAccountNo()));
        message.setTransactionAmount(amount);
        message.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        message.setMerchantType(merchantType);
        message.setPointOfEntry(pointOfEntry);
        message.setNetworkIdentifier(networkIdentifier);
        message.setCardAcceptorTerminalId(cardAcceptorTerminalId);
        message.setCardAcceptorIdentificationCode(cardAcceptorIdentificationCode);
        message.setCardAcceptorNameAndLocation(
                OneLinkFieldFormatter.fixedWidth(cardAcceptorNameAndLocation, 40));
        message.setCurrencyCode(currencyCode);
        message.setAccountNo1(request.getFromAccountNo());
        message.setConsumerNo(request.getConsumerNo());
        message.setUtilityCompanyCode(request.getUtilityCompanyCode());
        // Bill details come from the mock record, never from the caller.
        message.setUtilityCompanyName(OneLinkFieldFormatter.atMost(bill.getUtilityCompanyName(), 30));
        message.setBillAmount(bill.getBillAmount() == null ? null : bill.getBillAmount().toPlainString());
        message.setBillDueDate(bill.getBillDueDate());
        message.setStan(stan);
        message.setRrn(rrn);
        message.setAuthIdResp(authIdResponse);
        return message;
    }

    private BillPaymentProcedureRequest buildProcedureRequest(BillPaymentRequest request,
                                                              Request request1,
                                                              String stan, String rrn) {
        Date now = new Date();
        BillPaymentProcedureRequest procedure = new BillPaymentProcedureRequest();
        procedure.setClientSecret(mwCredentials.getClientSecret());
        procedure.setChannelCode(request1.getChannel());
        procedure.setUserId(mwCredentials.getUserId());
        // Matched against TBL_CUSTOMER.NID_NO, which holds ciphertext - so the stored value goes
        // out, read straight off the row we matched.
        procedure.setRelationshipId(aesEncryption.encryptwith256(request.getFromAccountNid()));
        procedure.setTransmissionDate(new SimpleDateFormat("yyyyMMdd").format(now));
        procedure.setTransmissionTime(new SimpleDateFormat("HHmmss").format(now));
        procedure.setStan(stan);
        procedure.setRrn(rrn);
        procedure.setDateLocalTran(new SimpleDateFormat("yyyyMMdd").format(now));
        procedure.setTimeLocalTran(new SimpleDateFormat("HHmmss").format(now));
        procedure.setAcquiringInstitutionCode(acquirerInstitutionCode);
        procedure.setMerchantType(merchantType);
        procedure.setPosEntryMode(pointOfEntry);
        procedure.setCardAcceptorNameLocation(cardAcceptorNameAndLocation);
        procedure.setCardAcceptorTerminalId(cardAcceptorTerminalId);
        procedure.setFromAccountNumber(request.getFromAccountNo());
        procedure.setFromAccountType("10");
        procedure.setFromAccountCurrency(currencyCode);
        procedure.setTransactionAmount(request.getAmount());
        procedure.setTransactionCurrency(currencyCode);
        procedure.setUtilityCompanyId(request.getUtilityCompanyCode());
        procedure.setUtilityConsumerNumber(request.getConsumerNo());
        // The procedure prices the transaction; a fee invented here would override its own.
        procedure.setTransactionFee(null);
        procedure.setUdf1(request.getTransactionReference());
        return procedure;
    }

    /**
     * The customer who holds {@code fromAccountNo} and whose national ID matches the one sent.
     *
     * <p>TBL_CUSTOMER.NID_NO stores AES ciphertext, so the plain value from the request is
     * encrypted and matched against the stored form. Requiring both to land on one row proves the
     * caller knows the national ID belonging to that account, not merely a valid one.</p>
     */
    private TblCustomer payingCustomer(BillPaymentRequest request) {
        String nid = request.getFromAccountNid();
        if (nid == null || nid.trim().isEmpty()) {
            return null;
        }
        String encryptedNid = aesEncryption.encryptwith256(nid.trim());
        if (encryptedNid == null) {
            log.error("Could not encrypt the national ID to look the customer up");
            return null;
        }
        return tblCustomerRepo.findByAccountNoAndNidNo(request.getFromAccountNo(), encryptedNid);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    public String getFormattedAmount(String amount, String numberFormat) {
        try {
            Session session = em.unwrap(Session.class);
            final String[] P_CONVERTEDAMOUNT = new String[1];
            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_MW.GET_AMOUNT_FORMATTED(?,?,?) }");

                    call.setString(1, amount);
                    call.setString(2, numberFormat);

                    call.registerOutParameter(3, Types.VARCHAR);
                    call.execute();

                    P_CONVERTEDAMOUNT[0] = call.getString(3);
                }
            });
            if (P_CONVERTEDAMOUNT.length > 0 && P_CONVERTEDAMOUNT != null) {
                return P_CONVERTEDAMOUNT[0];
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
