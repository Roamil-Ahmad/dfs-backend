package com.wallet.transaction.switching.ibft.outgoing;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.TblAccount;
import com.wallet.transaction.model.TblCustomer;
import com.wallet.transaction.repo.TblAccountRepo;
import com.wallet.transaction.repo.TblCustomerRepo;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.common.MwChannelCredentials;
import com.wallet.transaction.switching.common.OneLinkFieldFormatter;
import com.wallet.transaction.switching.common.ProcedureResult;
import com.wallet.transaction.switching.common.SwitchReferenceGenerator;
import com.wallet.transaction.switching.common.SwitchGatewayToggle;
import com.wallet.transaction.switching.common.SwitchTransactionType;
import com.wallet.transaction.switching.common.SwitchingConstants;
import com.wallet.transaction.switching.common.client.GatewayAdviceRequest;
import com.wallet.transaction.switching.common.client.GatewayAdviceResponse;
import com.wallet.transaction.switching.common.client.GatewayResponse;
import com.wallet.transaction.switching.common.client.GatewayTitleFetchRequest;
import com.wallet.transaction.switching.common.client.GatewayTitleFetchResponse;
import com.wallet.transaction.switching.common.client.SwitchGatewayClient;
import com.wallet.transaction.model.LkpBank;
import com.wallet.transaction.repo.LkpBankRepo;
import com.wallet.transaction.switching.common.model.TblTransactionMock;
import com.wallet.transaction.switching.common.service.TransactionMockService;
import com.wallet.transaction.switching.ibft.IbftProcedureRequest;
import com.wallet.transaction.switching.ibft.IbftProcedureService;
import com.wallet.transaction.util.AESencryption;
import com.wallet.transaction.util.Constants;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Outgoing IBFT: DFS is the acquirer, the funds leave to another bank through 1LINK.
 *
 * The orchestration is:
 * <ol>
 *   <li>resolve the beneficiary from {@code TBL_TRANSACTION_MOCK} for TRANSACTION_TYPE = IBFT</li>
 *   <li>call {@code PKG_MW.OUTGOING_IBFT}, which owns the customer debit</li>
 *   <li>send the IBFT advice to the switch through the gateway</li>
 *   <li>map both answers onto the application response</li>
 * </ol>
 *
 * No accounting happens in this class. It does not touch balances, does not insert or update any
 * transaction table and does not reimplement anything the procedure is responsible for.
 */
@Service
public class OutgoingIbftService {

    private static final Logger log = LoggerFactory.getLogger(OutgoingIbftService.class);

    private static final SwitchTransactionType TYPE = SwitchTransactionType.IBFT;

    @Autowired
    private TransactionMockService transactionMockService;

    @Autowired
    private LkpBankRepo lkpBankRepo;

    @Autowired
    private IbftProcedureService ibftProcedureService;

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

    @Value("${onelink.acquirer-institution-code}")
    private String acquirerInstitutionCode;

    @Value("${onelink.merchant-type}")
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

    @Autowired
    private SwitchGatewayToggle switchGatewayToggle;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private AESencryption aesEncryption;

    /** PKG_MW's TBL_MW_CHANNEL triple; see MwChannelCredentials for why it is all three. */
    @Autowired
    private MwChannelCredentials mwCredentials;
    @PersistenceContext
    EntityManager em;

    /**
     * Beneficiary lookup before a transfer is confirmed. The authoritative title comes from
     * TBL_TRANSACTION_MOCK; the switch round-trip proves the link and returns DE-39.
     *
     * Returns the standard transaction-layer envelope built by
     * {@code CommonService.getResponse}, exactly as the existing app transaction APIs do. The
     * ISO 8583 DE-39 stays inside {@code data.responseCode}; the envelope carries the DFS
     * generic response code.
     */
    /**
     * The bank picker the app shows before the customer types a beneficiary account number.
     *
     * <p>Sourced from LKP_BANK so the list is whatever the switch currently recognises, and
     * filtered to active banks only - a retired institution must not be offered. Rows without an
     * IMD are skipped: the app would have nothing to send back for them.</p>
     */
    public HashMap<String, Object> bankList() {
        List<IbftBankOption> banks = new ArrayList<>();
        for (LkpBank bank : lkpBankRepo.findAllByIsActive(Constants.YES)) {
            if (bank.getBankImd() == null || bank.getBankImd().trim().isEmpty()) {
                continue;
            }
            banks.add(new IbftBankOption(bank.getBankImd().trim(),
                    bank.getBankName() == null ? "" : bank.getBankName().trim()));
        }
        banks.sort(Comparator.comparing(IbftBankOption::getBankName));
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), banks);
    }

    public HashMap<String, Object> titleFetch(OutgoingIbftRequest request, Request envelope) {
        OutgoingIbftTitleFetchResponse response = new OutgoingIbftTitleFetchResponse();
        response.setBeneficiaryAccountNo(request.getBeneficiaryAccountNo());
        final String convAmount = getFormattedAmount(request.getAmount(), "C");
        response.setAmount(convAmount);

        // The customer picked the destination bank in the app. Resolve it first: the account
        // number alone cannot say which institution is meant.
        LkpBank bank = resolveSelectedBank(request.getBeneficiaryBankImd());
        if (bank == null) {
            response.setResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("Selected beneficiary bank is not recognised");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }
        response.setBeneficiaryBankName(bank.getBankName());
        response.setBeneficiaryBankImd(bank.getBankImd());

        TblTransactionMock beneficiary =
                transactionMockService.find(TYPE, request.getBeneficiaryAccountNo());
        if (beneficiary == null) {
            response.setResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("Beneficiary account is not registered for IBFT");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }
        if (!beneficiaryBelongsToSelectedBank(beneficiary, bank)) {
            response.setResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("Beneficiary account is not held at the selected bank");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }

        String stan = referenceGenerator.nextStan();
        String rrn = referenceGenerator.rrnFor(stan);
        response.setStan(stan);
        response.setRrn(rrn);
        response.setBeneficiaryAccountTitle(beneficiary.getBeneficiaryAccountTitle());

        if (!switchGatewayToggle.isEnabled()) {
            // Gateway off: the beneficiary was still resolved and cross-checked against the
            // selected bank above, so the inquiry answers from records rather than failing.
            // No beneficiaryId is set - only the switch issues one, and inventing it would let a
            // caller believe 1LINK confirmed this account.
            log.info("Switch gateway disabled, title fetch answered from records | STAN:{} | RRN:{}", stan, rrn);
            response.setResponseCode(SwitchingConstants.RC_PROCESSED_OK);
            response.setResponseDescription("Beneficiary resolved without a switch inquiry");
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
        }

        GatewayResponse<GatewayTitleFetchResponse> gatewayResponse =
                switchGatewayClient.titleFetch(buildTitleFetchRequest(request, bank, convAmount, stan, rrn));

        if (gatewayResponse == null || gatewayResponse.getData() == null) {
            log.error("Switch gateway gave no title fetch answer | STAN:{} | RRN:{}", stan, rrn);
            response.setResponseCode(SwitchingConstants.RC_HOST_LINK_DOWN);
            response.setResponseDescription("No answer from the payment switch");
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
        }

        GatewayTitleFetchResponse data = gatewayResponse.getData();
        boolean verified = SwitchingConstants.RC_PROCESSED_OK.equals(data.getResponseCode());
        response.setResponseCode(data.getResponseCode());
        response.setBeneficiaryId(data.getBeneficiaryId());
        response.setResponseDescription(verified
                ? "Beneficiary verified"
                : "Switch declined the title inquiry");
        return commonService.getResponse(verified
                ? GenericResponseCode.SUCCESS.getResponseCode()
                : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
    }

    /**
     * The transfer itself: mock lookup, then the debit procedure, then the advice to the switch.
     *
     * The advice is only sent when the procedure reported success, so the switch is never told
     * about a transfer the core did not post.
     *
     * Returns the standard transaction-layer envelope built by
     * {@code CommonService.getResponse}, exactly as the existing app transaction APIs do. The
     * ISO 8583 DE-39 stays inside {@code data.switchResponseCode} and the core's own outcome in
     * {@code data.coreResponseCode}; the envelope carries the DFS generic response code.
     */
    public HashMap<String, Object> transfer(OutgoingIbftRequest request, Request envelope, BigDecimal userId) {
        final String convAmount = getFormattedAmount(request.getAmount(), "C");
        request.setAmount(convAmount);
        OutgoingIbftResponse response = new OutgoingIbftResponse();
        response.setFromAccountNo(request.getFromAccountNo());
        response.setBeneficiaryAccountNo(request.getBeneficiaryAccountNo());
        response.setAmount(request.getAmount());
        response.setTransactionReference(request.getTransactionReference());

        // 1 - the destination bank is the customer's choice, validated against LKP_BANK
        LkpBank bank = resolveSelectedBank(request.getBeneficiaryBankImd());
        if (bank == null) {
            response.setSwitchResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("Selected beneficiary bank is not recognised");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }
        response.setBeneficiaryBankName(bank.getBankName());
        response.setBeneficiaryBankImd(bank.getBankImd());

        // 2 - beneficiary details, never hardcoded
        TblTransactionMock beneficiary =
                transactionMockService.find(TYPE, request.getBeneficiaryAccountNo());
        if (beneficiary == null) {
            response.setSwitchResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("Beneficiary account is not registered for IBFT");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }
        if (!beneficiaryBelongsToSelectedBank(beneficiary, bank)) {
            response.setSwitchResponseCode(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
            response.setResponseDescription("Beneficiary account is not held at the selected bank");
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), response);
        }
        response.setBeneficiaryAccountTitle(beneficiary.getBeneficiaryAccountTitle());

        String stan = referenceGenerator.nextStan();
        String rrn = referenceGenerator.rrnFor(stan);
        response.setStan(stan);
        response.setRrn(rrn);

        // 3 - the customer debit belongs to the database
        ProcedureResult procedure = ibftProcedureService.outgoingIbft(
                buildProcedureRequest(request, bank, envelope, stan, rrn));

        response.setCoreResponseCode(procedure.getErrorResponse());
        response.setAuthIdResponse(procedure.getAuthIdResponse());
        response.setTransactionId(procedure.getTransHeadId());
        response.setResponseDescription(procedure.getResponseDescription());

        if (!procedure.isApproved()) {
            log.error("Outgoing IBFT stopped, PKG_MW.OUTGOING_IBFT did not succeed | STAN:{} | RRN:{} | status:{} | descr:{}",
                    stan, rrn, procedure.getResponseStatus(), procedure.getResponseDescription());
            response.setSwitchResponseCode(SwitchingConstants.RC_UNABLE_TO_PROCESS);
            // The core's own code and description go back untouched. Translating them into a
            // generic technical-issue message threw away the only account of what actually went
            // wrong - which validator refused, and any ORA- error behind it.
            return commonService.getResponseWithOutDB(
                    procedure.failureCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
                    procedure.failureDescription("PKG_MW.OUTGOING_IBFT did not complete"),
                    response);
        }

        if (!switchGatewayToggle.isEnabled()) {
            // Gateway off. The debit above already happened and stands; only the advice to 1LINK
            // is skipped, so the outcome reported is the core's.
            log.info("Switch gateway disabled, advice not sent | STAN:{} | RRN:{} | transHeadId:{}",
                    stan, rrn, procedure.getTransHeadId());
            response.setSwitchResponseCode(SwitchingConstants.RC_PROCESSED_OK);
            response.setResponseDescription("Transfer posted without a switch advice");
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
        }

        // 5 - tell the switch
        GatewayResponse<GatewayAdviceResponse> gatewayResponse = switchGatewayClient.advice(
                buildAdviceRequest(request, bank, beneficiary, stan, rrn, procedure.getAuthIdResponse()));

        if (gatewayResponse == null || gatewayResponse.getData() == null) {
            log.error("Switch gateway gave no advice answer | STAN:{} | RRN:{}", stan, rrn);
            response.setSwitchResponseCode(SwitchingConstants.RC_HOST_LINK_DOWN);
            response.setResponseDescription("No answer from the payment switch");
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
        }

        GatewayAdviceResponse data = gatewayResponse.getData();
        response.setSwitchResponseCode(data.getResponseCode());
        if (data.getAuthIdResp() != null && !data.getAuthIdResp().isBlank()) {
            response.setAuthIdResponse(data.getAuthIdResp());
        }
        log.info("Outgoing IBFT completed | STAN:{} | RRN:{} | DE-39:{} | transHeadId:{}",
                stan, rrn, data.getResponseCode(), procedure.getTransHeadId());
        return commonService.getResponse(
                SwitchingConstants.RC_PROCESSED_OK.equals(data.getResponseCode())
                        ? GenericResponseCode.SUCCESS.getResponseCode()
                        : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
    }

    /**
     * The destination bank is whatever the customer picked in the app, resolved against LKP_BANK.
     *
     * <p>It is deliberately not derived from the account number: an account number is only unique
     * within a bank, so deriving it would mean DFS guessing where the money should go. The IMD is
     * still validated here rather than trusted, so a client cannot route a transfer to an unknown
     * or retired institution.</p>
     *
     * @return the active LKP_BANK row, or null when the IMD is unknown or the bank is inactive
     */
    private LkpBank resolveSelectedBank(String beneficiaryBankImd) {
        if (beneficiaryBankImd == null || beneficiaryBankImd.trim().isEmpty()) {
            return null;
        }
        LkpBank bank = lkpBankRepo.findByBankImdAndIsActive(beneficiaryBankImd.trim(), Constants.YES);
        if (bank == null) {
            log.warn("Beneficiary bank not in LKP_BANK or not active | imd:{}", beneficiaryBankImd);
        }
        return bank;
    }

    /**
     * The beneficiary account must actually be held at the bank the customer chose. Without this a
     * customer could select bank A, hit an account that lives at bank B, and have DFS post the
     * transfer against A - a wrong-bank credit.
     */
    private boolean beneficiaryBelongsToSelectedBank(TblTransactionMock beneficiary, LkpBank bank) {
        String held = beneficiary.getImdNo() == null ? "" : beneficiary.getImdNo().trim();
        String chosen = bank.getBankImd() == null ? "" : bank.getBankImd().trim();
        boolean matches = !held.isEmpty() && held.equals(chosen);
        if (!matches) {
            log.warn("Beneficiary account is not held at the selected bank | account imd:{} | selected imd:{}",
                    held, chosen);
        }
        return matches;
    }
    /**
     * DE-120 sub-field 8, the sender's name, taken from the customer's own account row. Never
     * fabricated: an account DFS cannot find contributes an empty name rather than a made-up one.
     */
    private String senderName(String fromAccountNo) {
        if (fromAccountNo == null || fromAccountNo.trim().isEmpty()) {
            return "";
        }
        TblAccount account = tblAccountRepo.findByAccountNo(fromAccountNo.trim());
        if (account == null || account.getAccountTitle() == null) {
            log.warn("No account title found for the sending account, DE-120 sender name goes out empty");
            return "";
        }
        return account.getAccountTitle();
    }

    private GatewayTitleFetchRequest buildTitleFetchRequest(OutgoingIbftRequest request,
                                                            LkpBank bank,
                                                            String convAmount,
                                                            String stan, String rrn) {
        GatewayTitleFetchRequest message = new GatewayTitleFetchRequest();
        // DE-02 is the 16-digit sender PAN, not the wallet account number.
        message.setPan(OneLinkFieldFormatter.pan(dfsBin, request.getFromAccountNo()));
        message.setTransactionAmount(convAmount);
        message.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        message.setMerchantType(merchantType);
        message.setPointOfEntry(pointOfEntry);
        message.setNetworkIdentifier(networkIdentifier);
        message.setCardAcceptorTerminalId(cardAcceptorTerminalId);
        message.setCardAcceptorIdentificationCode(cardAcceptorIdentificationCode);
        message.setCardAcceptorNameAndLocation(
                OneLinkFieldFormatter.fixedWidth(cardAcceptorNameAndLocation, 40));
        message.setPurposeOfPayment(OneLinkFieldFormatter.purposeOfPayment(request.getPurposeOfPayment()));
        message.setCurrencyCode(currencyCode);
        message.setAccountNo1(request.getFromAccountNo());
        message.setAccountNo2(request.getBeneficiaryAccountNo());
        // Routing goes to the institution the customer selected, resolved from LKP_BANK.
        message.setToBankImd(bank.getBankImd());
        message.setStan(stan);
        message.setRrn(rrn);
        return message;
    }

    private GatewayAdviceRequest buildAdviceRequest(OutgoingIbftRequest request,
                                                    LkpBank bank,
                                                    TblTransactionMock beneficiary,
                                                    String stan, String rrn, String authIdResponse) {
        GatewayAdviceRequest message = new GatewayAdviceRequest();
        // DE-02 is the 16-digit sender PAN, not the wallet account number.
        message.setPan(OneLinkFieldFormatter.pan(dfsBin, request.getFromAccountNo()));
        message.setTransactionAmount(request.getAmount());
        message.setMerchantType(merchantType);
        message.setPointOfEntry(pointOfEntry);
        message.setAuthIdResp(authIdResponse);
        message.setCardAcceptorTerminalId(cardAcceptorTerminalId);
        message.setCardAcceptorIdentificationCode(cardAcceptorIdentificationCode);
        message.setCardAcceptorNameAndLocation(
                OneLinkFieldFormatter.fixedWidth(cardAcceptorNameAndLocation, 40));
        message.setPurposeOfPayment(OneLinkFieldFormatter.purposeOfPayment(request.getPurposeOfPayment()));
        message.setCurrencyCode(currencyCode);
        message.setAccountNo1(request.getFromAccountNo());
        message.setAccountNo2(request.getBeneficiaryAccountNo());
        // DE-120 sub-fields. The bank is the customer's selection, the account title comes from the
        // mock record and sender values from the customer's own account row; the widths are what
        // the 1LINK record layout allows.
        message.setAccountTitle(OneLinkFieldFormatter.atMost(beneficiary.getBeneficiaryAccountTitle(), 30));
        message.setAccountBankName(OneLinkFieldFormatter.atMost(bank.getBankName(), 20));
        // TBL_TRANSACTION_MOCK holds no branch, and the sender's CNIC is not part of this request.
        // Both are optional sub-fields in the 1LINK record layout, so they go out blank rather than
        // being invented.
        message.setAccountBranchName("");
        message.setSenderName(OneLinkFieldFormatter.atMost(senderName(request.getFromAccountNo()), 30));
        message.setSenderId("");
        // Routing goes to the institution the customer selected, resolved from LKP_BANK.
        message.setToBankImd(bank.getBankImd());
        message.setStan(stan);
        message.setRrn(rrn);
        message.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        message.setNetworkIdentifier(networkIdentifier);
        return message;
    }

    private IbftProcedureRequest buildProcedureRequest(OutgoingIbftRequest request,
                                                       LkpBank bank,
                                                       Request request1,
                                                       String stan, String rrn) {
        Date now = new Date();
        IbftProcedureRequest procedure = new IbftProcedureRequest();
        procedure.setClientSecret(mwCredentials.getClientSecret());
        procedure.setChannelCode(request1.getChannel());
        procedure.setUserId(mwCredentials.getUserId());
        procedure.setRelationshipId(aesEncryption.encryptwith256(request.getFromAccountNid().trim()));
        procedure.setTransmissionDate(new SimpleDateFormat("yyyyMMdd").format(now));
        procedure.setTransmissionTime(new SimpleDateFormat("HHmmss").format(now));
        procedure.setStan(stan);
        procedure.setRrn(rrn);
        procedure.setDateLocalTran(new SimpleDateFormat("yyyyMMdd").format(now));
        procedure.setTimeLocalTran(new SimpleDateFormat("HHmmss").format(now));
        procedure.setAcquiringInstitutionCode(acquirerInstitutionCode);
        procedure.setMerchantType(merchantType);
        procedure.setPosEntryMode(pointOfEntry);
        procedure.setFromAccountNumber(request.getFromAccountNo());
        procedure.setFromAccountType("10");
        procedure.setFromAccountCurrency(currencyCode);
        procedure.setToAccountNumber(request.getBeneficiaryAccountNo());
        procedure.setCardAcceptorNameLocation(cardAcceptorNameAndLocation);
        procedure.setCardAcceptorTerminalId(cardAcceptorTerminalId);
        procedure.setTransactionAmount(request.getAmount());
        procedure.setTransactionCurrency(currencyCode);
        procedure.setTransactionPurpose(request.getPurposeOfPayment());
        procedure.setSourceImd(acquirerInstitutionCode);
        // Routing goes to the institution the customer selected, resolved from LKP_BANK.
        procedure.setDestinationImd(bank.getBankImd());
        // DFS is the paying side of an outgoing transfer.
        procedure.setIdentifier(SwitchingConstants.IDENTIFIER_DEBIT);
        procedure.setRecordData(null);
        procedure.setTransactionFee(null);
        procedure.setUdf1(request.getTransactionReference());
        return procedure;
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
