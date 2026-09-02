/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.service.impl

Class Name: TransactionsServiceImpl

Date and Time:1/3/2025 10:53 PM

Version:1.0
*/
package com.wallet.transaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.WalletToWalletResp;
import com.wallet.transaction.dto.WalletToWalletRqst;
import com.wallet.transaction.dto.*;
import com.wallet.transaction.dto.aps.*;
import com.wallet.transaction.dto.common.CashInAgentRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.dto.common.Response;
import com.wallet.transaction.model.*;
import com.wallet.transaction.repo.*;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.service.NotificationService;
import com.wallet.transaction.service.ProcedureService;
import com.wallet.transaction.service.TransactionsService;
import com.wallet.transaction.util.*;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.wallet.transaction.switching.common.MwChannelCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TransactionsServiceImpl extends HelperClass implements TransactionsService {

    private static final SecureRandom REFERENCE_RANDOM = new SecureRandom();

    private static final Logger LOG = LoggerFactory.getLogger(TransactionsServiceImpl.class);

    /**
     * P_FTTYPE for PKG_MW - both sides of a local transfer are DFS wallets.
     */
    private static final String FT_TYPE_WALLET_TO_WALLET = "W2W";
    /**
     * P_IBFTIDENTIFIER for PKG_MW.TITLE_FETCH. Only I, O and B are recognised (incoming IBFT,
     * outgoing IBFT, bill inquiry); anything else takes the local funds transfer branch, which is
     * the one this service wants. L is used because it says so.
     */
    private static final String IBFT_IDENTIFIER_LOCAL = "00";
    /**
     * Matches the value the wallet-to-wallet call has always sent, so both agree.
     */
    private static final String ACQ_INST_CODE_LOCAL = "221133";
    /**
     * Matches the currency the wallet-to-wallet call has always sent, so both agree.
     */
    private static final String TRANSACTION_CURRENCY_LOCAL = "586";

    @Autowired
    private MwChannelCredentials mwCredentials;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private LkpChannelRepo lkpChannelRepo;

    @Autowired
    private TblRequestRepo tblRequestRepo;

    @Autowired
    private TblResponseRepo tblResponseRepo;

    @Autowired
    private CommonService commonService;

    HelperClass helperClass = new HelperClass();

    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;

    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;

    @Autowired
    private TblRequestMoneyRepo tblRequestMoneyRepo;

    @Value("${account.type.wallet}")
    private String accountTypeWallet;
    @Value("${account.type.agent}")
    private String accountTypeAgent;

    @Value("${agent.mpin.verification.url}")
    private String agentMpinVerificationUrl;
    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;

    @Autowired
    private AESencryption aeSencryption;
    @Value("${generate.otp.url}")
    private String generateOtpUrl;
    @Value("${sms.template.type.qr.trxn}")
    private String smsTemplateTypeTrxn;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private NotificationService notificationService;
    @Value("${verify.otp.url}")
    private String verifyOtpUrl;
    @Value("${card.titlefetch.url}")
    private String cardTitleFetchUrl;
    @Value("${wallet.to.card.url}")
    private String walletToCardUrl;
    @Value("${mpin.verification.url}")
    private String mpinUrl;
    @Value("${card.to.wallet.url}")
    private String cardToWalletUrl;
    @Value("${card.balance.inquiry.url}")
    private String balanceInquiryUrl;
    @Value("${card.to.card.url}")
    private String cardToCardUrl;
    @Value("${card.to.card.cp.url}")
    private String cardToCardCpUrl;
    @Value("${bill.info.url}")
    private String billInfoUrl;
    @Value("${bill.payment.url}")
    private String billPaymentUrl;
    @Value("${cashin.url}")
    private String cashinUrl;
    @Value("${cashout.url}")
    private String cashoutUrl;
    @Autowired
    private LkpBankRepo lkpBankRepo;
    @Autowired
    private ProcedureService procedureService;
    @Value("${mobile.channelCode}")
    private String mobileChannelCode;
    @Value("${mobile.clientSecret}")
    private String mobileClientSecret;
    ObjectMapper mapper = new ObjectMapper();
    @Value("${wallet.to.wallet.url}")
    private String walletToWalletUrl;
    @Value("${dfs.bin}")
    private String dfsBin;
    @Autowired
    private LocationService locationService;
    @Value("${pos.channelCode}")
    private String posChannelCode;
    @Value("${pos.clientSecret}")
    private String posClientSecret;
    @Value("${purchase.url}")
    private String purchaseUrl;
    @Value("${agent.channelCode}")
    private String agentChannelCode;
    @Value("${agent.clientSecret}")
    private String agentClientSecret;

    @Override
    public HashMap<String, Object> initiateLocalFT(InitiateLocalFTRequest initiateLocalFTRequest, Request request,
                                                   String token) throws JsonProcessingException {
        TblResponse tblResponse = new TblResponse();
        TblRequest tblRequest = saveRequestTitleFetch(initiateLocalFTRequest, request);
        TblRequest saveTblRequest = tblRequestRepo.save(tblRequest);
        tblResponse.setTblRequest(saveTblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));

        String stan = tblAccountRepo.getStan();

        TitleFetchProcResponse beneTblAccount = callTitleFetch(initiateLocalFTRequest, request);

        String responseCode = "";
        String responseDescr = "";
        responseCode = beneTblAccount.getResponseCode();
        responseDescr = beneTblAccount.getResponseDescr();

        if (responseCode.equals("000")) {
            return titleFetchSuccessRespose(initiateLocalFTRequest, tblResponse, saveTblRequest, stan, beneTblAccount,
                    responseCode, responseDescr, request, token);

        } else {
            tblResponse.setResponseCode(responseCode);
            tblResponse.setAdditionalData(responseDescr);
            tblResponse.setAccountNo(initiateLocalFTRequest.getAccountNo());
            tblResponse.setTblRequest(saveTblRequest);
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT.getResponseCode(), null);
        }

    }

    private HashMap<String, Object> titleFetchSuccessRespose(InitiateLocalFTRequest initiateLocalFTRequest,
                                                             TblResponse tblResponse, TblRequest saveTblRequest, String stan, TitleFetchProcResponse beneTblAccount,
                                                             String responseCode, String responseDescr, Request request, String token) throws JsonProcessingException {
        InitiateLocalFTResponce initiateLocalFTResponce = new InitiateLocalFTResponce();
        GenerateOtpResponse generateOtpResponse;
        initiateLocalFTResponce.setResponseDescr(responseDescr);
        initiateLocalFTResponce.setAuthIdResponse(stan);
        initiateLocalFTResponce.setResponseCode(responseCode);
        initiateLocalFTResponce.setFee(beneTblAccount.getFee());
        String accountTitle = aeSencryption.decrypt(beneTblAccount.getAccountTitle());
        initiateLocalFTResponce.setAccountTitle(accountTitle);
        initiateLocalFTResponce.setAvailableBalance(beneTblAccount.getAvailableBalance());
        initiateLocalFTResponce.setActualBalance(beneTblAccount.getActualBalance());
        initiateLocalFTResponce.setAccountNo(initiateLocalFTRequest.getAccountNo());
        tblResponse.setAdditionalData("Title Fetch Successfully");
        tblResponse.setResponseCode(responseCode);
        tblResponse.setAccountTitle(accountTitle);
        tblResponse.setAccountNo(initiateLocalFTRequest.getAccountNo());
        tblResponse.setTblRequest(saveTblRequest);
        tblResponse.setStan(stan);
        tblResponseRepo.save(tblResponse);
        if (!isNullOrEmpty(initiateLocalFTRequest.getType())
                && initiateLocalFTRequest.getType().equalsIgnoreCase("QR")) {
            TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName("QR_PAYMENT_THRESHOLD");
            if (tblGlobalConfig != null) {
                String keyValue = tblGlobalConfig.getKeyValue();
                BigDecimal qrThreshold = new BigDecimal(keyValue);
                BigDecimal trxnAmount = new BigDecimal(initiateLocalFTRequest.getAmount());
                if (trxnAmount.compareTo(qrThreshold) > 0) {
                    Response response = generateOtp(initiateLocalFTRequest.getMobileNumber(), Constants.EMPTY, "FT",
                            "S", smsTemplateTypeTrxn, "C",
                            request, token);
                    if (response != null) {
                        if (response.getResponsecode()
                                .equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
                            generateOtpResponse = fromJson(convertObjecttoJson(response.getData()),
                                    GenerateOtpResponse.class);
                            initiateLocalFTResponce.setGenerateOtpResponse(generateOtpResponse);
                        }

                    }
                }

            }

        }
        HashMap<String, Object> purposeOfPayment = commonService.getPurposeOfPayment();
        List<LovResponse> dataList = (List<LovResponse>) purposeOfPayment.get("data");
        initiateLocalFTResponce.setTransPurposes(dataList);

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), initiateLocalFTResponce);
    }

    private TblRequest saveRequestTitleFetch(InitiateLocalFTRequest initiateLocalFTRequest, Request request) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(initiateLocalFTRequest));
        tblRequest.setCnic(initiateLocalFTRequest.getNidNo());
        tblRequest.setImei(request.getImieNo());
        tblRequest.setToAccount(initiateLocalFTRequest.getAccountNo());
        tblRequest.setAccountType(initiateLocalFTRequest.getAccountType());
        tblRequest.setFromAccount(initiateLocalFTRequest.getAccountNo());
        tblRequest.setAmount(initiateLocalFTRequest.getAmount());
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest.setEndPoint("initiateLocalFT");
        tblRequest.setCreateuser(new BigDecimal(1));
        return tblRequest;
    }

    @Override
    public HashMap<String, Object> fundsTransferLocal(FundTransferRequest fundTransferRequest, Request request,
                                                      String token) throws Exception {
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(fundTransferRequest.getMobileNumber());
        FundTransferResponce fundTransferResponce = new FundTransferResponce();
        if (tblAccount != null) {
            TblRequest tblRequest = saveRequestFundTransfer(fundTransferRequest, request, tblAccount);

            TblRequest saveTblRequest = tblRequestRepo.save(tblRequest);

            TblResponse tblResponse = new TblResponse();
            tblResponse.setTblRequest(saveTblRequest);
            tblResponse.setCreateuser(new BigDecimal(1));

            String convAmount = getFormattedAmount(fundTransferRequest.getAmount(), "C");
            Date date = new Date();

            SimpleDateFormat dateFomat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String transDate = dateFomat.format(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
            String strDate = dateFormatter.format(date);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
            String strTime = timeFormatter.format(date);

            SimpleDateFormat rrnDateFormatter = new SimpleDateFormat("yyMMdd");
            String rrnDate = rrnDateFormatter.format(date);

            String stan = tblAccountRepo.getStan();

            if (!fundTransferRequest.getAccountNo().isEmpty()) {
                WalletToWalletReq wallettowalletReq = setDataWalletToWalletReq(fundTransferRequest, tblAccount,
                        convAmount, strDate, strTime, rrnDate, stan, false);
                // The MPIN belongs to whichever service owns the user. App holds a customer's,
                // AgentApp holds an agent's, and each only knows its own - so an agent verified
                // against the customer endpoint is rejected however correct the PIN is. MOB and
                // every other channel keep the customer endpoint they already used.
                String reultMpin = agentChannelCode.equalsIgnoreCase(request.getChannel())
                        ? this.checkAgentMpinValidation(tblAccount.getAccountNo(), fundTransferRequest.getMpin(),
                                token, request.getImieNo(), agentMpinVerificationUrl)
                        : checkMpinValidation(tblAccount.getAccountNo(), fundTransferRequest.getMpin(), token,
                                request.getImieNo());
                JSONObject jsonObject1 = new JSONObject(reultMpin);
                if (!jsonObject1.getString("responsecode").equalsIgnoreCase("000")) {
                    tblResponse.setAdditionalData("Invalid Mpin");
                    tblResponse.setResponseCode("");
                    tblResponse = tblResponseRepo.save(tblResponse);
                    return commonService.getResponseWithOutDB(GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),
                            jsonObject1.getString("messages"), null);
                }
                WalletToWalletResp walletToWalletResp = WalletToWallet(wallettowalletReq, request);

                String responseCode = "";
                String responseDescr = "";
                if (walletToWalletResp != null && walletToWalletResp.getErrorresponse().equals("000")) {
                    responseCode = "000";
                    responseDescr = "Funds Transfer Successfully";
                } else {
                    responseCode = walletToWalletResp != null ? walletToWalletResp.getErrorresponse()
                            : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode();
                    responseDescr = walletToWalletResp != null ? walletToWalletResp.getResponseDescr()
                            : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage();
                }

                fundTransferResponce.setResponseDescr(responseDescr);
                fundTransferResponce.setAuthIdResponse(stan);
                fundTransferResponce.setResponseCode(responseCode);
                fundTransferResponce.setTransDate(transDate);
                if (responseCode.equals("000")) {
                    saveSmsMessageTemplateFundTransfer(fundTransferRequest, tblAccount, transDate, walletToWalletResp,
                            new BigDecimal(6));
                    tblResponse.setTblRequest(saveTblRequest);
                    tblResponse.setAuthIdResponse(stan);
                    tblResponse.setResponseCode(responseCode);
                    tblResponse.setTransDate(transDate);
                    tblResponse.setStan(stan);
                    tblResponse.setRrn(rrnDate + stan);
                    tblResponse = tblResponseRepo.save(tblResponse);
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            fundTransferResponce);
                } else {
                    tblResponse.setAdditionalData(responseDescr);
                    tblResponse.setResponseCode(responseCode);
                    tblResponse = tblResponseRepo.save(tblResponse);
                    return commonService.getResponseWithOutDB(GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),
                            responseDescr, null);
                }
            } else {
                System.out.println("Here We Code Wallet to core ");
                return null;
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    private void saveSmsMessageTemplateFundTransfer(FundTransferRequest fundTransferRequest, TblAccount tblAccount,
                                                    String transDate, WalletToWalletResp walletToWalletResp, BigDecimal transDocId) {
        String msg = null;
        String maskAcct1 = helperClass.maskNumber(tblAccount.getAccountNo());
        String maskAcct2 = helperClass.maskNumber(fundTransferRequest.getAccountNo());

        List<TblSmsMessageTemplate> tblSmsMessageTemplates = tblSmsMessageTemplateRepo
                .getByTransDocsId(transDocId);
        if (tblSmsMessageTemplates != null) {
            for (TblSmsMessageTemplate tblSmsMessageTemplate : tblSmsMessageTemplates) {
                if (tblSmsMessageTemplate.getIdentifier().equals("S")) {
                    TblAccount tblAccountForBalance = tblAccountRepo.findById(tblAccount.getAccountId()).orElse(null);
                    TblAppUser tblAppUser;
                    if (tblAccount.getTblCustomer() == null) {
                        tblAppUser = tblAppUserRepo.findByAgentId(tblAccount.getTblAgent().getAgentId());
                    } else {
                        tblAppUser = tblAppUserRepo.findByCustomerId(tblAccount.getTblCustomer().getCustomerId());
                    }
                    // Every placeholder is filled through safe(): String.replace throws a
                    // NullPointerException on a null replacement, and not every flow supplies
                    // every value - a cash-in, for instance, sends no accountTitle. An unfilled
                    // placeholder is a cosmetic problem; an exception here is not, because this
                    // runs after the money has already moved.
                    msg = tblSmsMessageTemplate.getMessageTemplate();
                    msg = msg.replace("%AMT%", safe(helperClass.amountformat(fundTransferRequest.getAmount())));
                    msg = msg.replace("%SNAME%", safe(aeSencryption.decrypt(tblAccount.getAccountTitle())));
                    msg = msg.replace("%SMOBNO%", safe(maskAcct1));
                    msg = msg.replace("%RNAME%", safe(fundTransferRequest.getAccountTitle()));
                    msg = msg.replace("%RMOBNO%", safe(maskAcct2));
                    msg = msg.replace("%RBAL%", tblAccountForBalance == null
                            || tblAccountForBalance.getCurrentBalance() == null
                            ? ""
                            : safe(helperClass.amountformat(tblAccountForBalance.getCurrentBalance().toString())));
                    msg = msg.replace("%TRANSDATE%", safe(transDate));

                    TblSmsMessage message = new TblSmsMessage();
                    message.setMessage(msg);
                    message.setMobileNo(tblAccount.getMobileNo());
                    message.setSendFlag(new BigDecimal(0).toString());
                    message.setTransHeadId(BigDecimal.valueOf(walletToWalletResp.getTransHeadId()));
                    message.setSmsMessageTemplateId(new BigDecimal(tblSmsMessageTemplate.getSmsMessageTemplateId()));
                    saveSms(message, new BigDecimal(1));
                    notificationService.saveNotification(msg, tblAppUser, "T", "Funds Transfer", msg);
                    notificationService.sendNotification(msg, tblAppUser.getFireBaseToken());
                } else if (tblSmsMessageTemplate.getIdentifier().equals("R")) {
                    TblAccount receiverAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(
                            fundTransferRequest.getAccountNo(), accountTypeWallet);
                    if (receiverAccount != null) {
                        TblAppUser tblAppUser;
                        if (tblAccount.getTblCustomer() == null) {
                            tblAppUser = tblAppUserRepo.findByAgentId(tblAccount.getTblAgent().getAgentId());
                        } else {
                            tblAppUser = tblAppUserRepo.findByCustomerId(tblAccount.getTblCustomer().getCustomerId());
                        }
                        msg = tblSmsMessageTemplate.getMessageTemplate();
                        msg = msg.replace("%AMT%", helperClass.amountformat(fundTransferRequest.getAmount()));
                        msg = msg.replace("%SNAME%", aeSencryption.decrypt(tblAccount.getAccountTitle()));
                        msg = msg.replace("%SMOBNO%", maskAcct1);
                        msg = msg.replace("%TRANSDATE%", transDate);
                        msg = msg.replace("%RMOBNO%", maskAcct2);
                        msg = msg.replace("%RBAL%",
                                helperClass.amountformat(receiverAccount.getCurrentBalance().toString()));

                        TblSmsMessage message = new TblSmsMessage();
                        message.setMessage(msg);
                        message.setMobileNo(receiverAccount.getMobileNo());
                        message.setSendFlag(new BigDecimal(0).toString());
                        message.setSmsMessageTemplateId(new BigDecimal(
                                tblSmsMessageTemplate.getSmsMessageTemplateId()));
                        message.setTransHeadId(BigDecimal.valueOf(walletToWalletResp.getTransHeadId()));
                        message.setSmsMessageTemplateId(
                                new BigDecimal(tblSmsMessageTemplate.getSmsMessageTemplateId()));
                        saveSms(message, new BigDecimal(1));
                        notificationService.saveNotification(msg, tblAppUser, "T", "Funds Transfer", msg);
                        notificationService.sendNotification(msg, tblAppUser.getFireBaseToken());
                    }
                }
            }
        }
    }

    private WalletToWalletReq setDataWalletToWalletReq(FundTransferRequest fundTransferRequest, TblAccount tblAccount,
                                                       String convAmount, String strDate, String strTime, String rrnDate, String stan, boolean reversible) {

        String fromAccountNumber;
        String toAccountNumber;
        String fromAccountType;
        String toAccountType;
        String relationshipId;

        if (reversible) {
            fromAccountNumber = fundTransferRequest.getAccountNo();
            fromAccountType = "10";
            toAccountNumber = tblAccount.getAccountNo();
            toAccountType = fundTransferRequest.getAccountType();
            relationshipId = tblAccountRepo.findNidNoByAccountNo(fundTransferRequest.getAccountNo());
        } else {
            toAccountNumber = fundTransferRequest.getAccountNo();
            toAccountType = "10";
            fromAccountNumber = tblAccount.getAccountNo();
            fromAccountType = fundTransferRequest.getAccountType();
            relationshipId = aeSencryption.encryptwith256(fundTransferRequest.getNidNo());
        }

        WalletToWalletReq wallettowalletReq = new WalletToWalletReq();
        wallettowalletReq.setChannelId(fundTransferRequest.getAppUserId());
        wallettowalletReq.setRelationshipid(relationshipId);
        wallettowalletReq.setTransmissiondate(strDate);
        wallettowalletReq.setTransmissiontime(strTime);
        wallettowalletReq.setStan(stan);
        wallettowalletReq.setRrn(rrnDate + stan);
        wallettowalletReq.setDatelocaltran(strDate);
        wallettowalletReq.setTimelocaltran(strTime);
        wallettowalletReq.setAcqinstcode("221133");
        wallettowalletReq.setMerchanttype("");
        wallettowalletReq.setPosentrymode("");
        wallettowalletReq.setCardacceptornamelocation("");
        wallettowalletReq.setCardacceptorterminalid("");
        wallettowalletReq.setFromaccounttype(fromAccountType);
        wallettowalletReq.setFromaccountcurrency("586");
        wallettowalletReq.setToaccountnumber(toAccountNumber);
        wallettowalletReq.setFromaccountnumber(fromAccountNumber);
        wallettowalletReq.setToaccounttype(toAccountType);
        wallettowalletReq.setToaccountcurrency("586");
        wallettowalletReq.setTransactionamount(convAmount);
        wallettowalletReq.setTransactioncurrency("586");
        wallettowalletReq.setTransactionfee("");
        wallettowalletReq.setReserved1("");
        wallettowalletReq.setReserved2(fundTransferRequest.getTransPurposeId());
        wallettowalletReq.setReserved3("");
        wallettowalletReq.setReserved4("");
        wallettowalletReq.setReserved5("");
        return wallettowalletReq;
    }

    private TblRequest saveRequestFundTransfer(FundTransferRequest fundTransferRequest, Request request,
                                               TblAccount tblAccount) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(fundTransferRequest));
        tblRequest.setCnic(fundTransferRequest.getNidNo());
        tblRequest.setImei(request.getImieNo());
        tblRequest.setToAccount(fundTransferRequest.getAccountNo());
        tblRequest.setFromAccount(tblAccount.getAccountNo());
        tblRequest.setAmount(fundTransferRequest.getAmount());
        tblRequest.setPurposeOfPayment(fundTransferRequest.getTransPurposeId());
        tblRequest.setNarration(fundTransferRequest.getNarration());
        tblRequest.setAccountTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
        tblRequest.setBeneficiaryName(fundTransferRequest.getBeneficiaryName());
        tblRequest.setBeneficiaryMobile(fundTransferRequest.getBeneficiaryMobile());
        tblRequest.setBeneficiaryEmail(fundTransferRequest.getBeneficiaryEmail());
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest.setEndPoint("fundsTransferLocal");
        tblRequest.setCreateuser(new BigDecimal(1));
        return tblRequest;
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

    public WalletToWalletResp WalletToWallet(WalletToWalletReq walletToWalletReq, Request request) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_AUTHIDRESPONSE = new String[1];
            final String[] P_ERRORRESPONSE = new String[1];
            final String[] P_AVAILABLEBALANCE = new String[1];
            final String[] P_ACTUALBALANCE = new String[1];
            final String[] P_UDF1 = new String[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final Integer[] P_RESONSESTATUS = new Integer[1];
            final String[] P_CHECKPOINT = new String[1];
            final Integer[] P_TRANSHEADID = new Integer[1];
            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall(
                            "{call PKG_MW.WALLET_TO_WALLET(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

                    // PKG_MW validates these three together against TBL_MW_CHANNEL.
                    call.setString(1, mwCredentials.getClientSecret());
                    call.setString(2, request.getChannel());
                    // Position 3 is P_USER_ID here - the MW service account, not the app user.
                    if (mwCredentials.getUserId() == null) {
                        call.setNull(3, Types.NUMERIC);
                    } else {
                        call.setLong(3, mwCredentials.getUserId());
                    }
                    call.setString(4, walletToWalletReq.getRelationshipid());
                    call.setString(5, walletToWalletReq.getTransmissiondate());
                    call.setString(6, walletToWalletReq.getTransmissiontime());
                    call.setString(7, walletToWalletReq.getStan());
                    call.setString(8, walletToWalletReq.getRrn());
                    call.setString(9, walletToWalletReq.getDatelocaltran());
                    call.setString(10, walletToWalletReq.getTimelocaltran());
                    call.setString(11, walletToWalletReq.getAcqinstcode());
                    call.setString(12, walletToWalletReq.getMerchanttype());
                    call.setString(13, walletToWalletReq.getPosentrymode());
                    call.setString(14, walletToWalletReq.getCardacceptornamelocation());
                    call.setString(15, walletToWalletReq.getCardacceptorterminalid());
                    call.setString(16, walletToWalletReq.getFromaccountnumber());
                    call.setString(17, walletToWalletReq.getFromaccounttype());
                    call.setString(18, walletToWalletReq.getFromaccountcurrency());
                    call.setString(19, walletToWalletReq.getToaccountnumber());
                    call.setString(20, walletToWalletReq.getToaccounttype());
                    call.setString(21, walletToWalletReq.getToaccountcurrency());
                    call.setString(22, walletToWalletReq.getTransactionamount());
                    call.setString(23, walletToWalletReq.getTransactioncurrency());
                    call.setString(24, walletToWalletReq.getTransactionfee());
                    call.setString(25, walletToWalletReq.getReserved1());
                    call.setString(26, walletToWalletReq.getReserved2());
                    call.setString(27, walletToWalletReq.getReserved3());
                    call.setString(28, walletToWalletReq.getReserved4());
                    call.setString(29, walletToWalletReq.getReserved5());

                    call.registerOutParameter(25, Types.VARCHAR); // in/out
                    call.registerOutParameter(30, Types.VARCHAR);
                    call.registerOutParameter(31, Types.VARCHAR);
                    call.registerOutParameter(32, Types.VARCHAR);
                    call.registerOutParameter(33, Types.VARCHAR);
                    call.registerOutParameter(34, Types.VARCHAR);
                    call.registerOutParameter(35, Types.INTEGER);
                    call.registerOutParameter(36, Types.VARCHAR);
                    call.registerOutParameter(37, Types.INTEGER);

                    call.execute();

                    P_AUTHIDRESPONSE[0] = call.getString(30);
                    P_ERRORRESPONSE[0] = call.getString(31);
                    P_AVAILABLEBALANCE[0] = call.getString(32);
                    P_ACTUALBALANCE[0] = call.getString(33);
                    P_UDF1[0] = call.getString(25);
                    P_RESPONSEDESCR[0] = call.getString(34);
                    P_RESONSESTATUS[0] = call.getInt(35);
                    P_CHECKPOINT[0] = call.getString(36);
                    P_TRANSHEADID[0] = call.getInt(37);

                }
            });
            System.out.println("\nWallet to Wallet Completed with status  " + P_AUTHIDRESPONSE[0]);
            if (P_AUTHIDRESPONSE != null && P_AUTHIDRESPONSE.length > 0) {
                WalletToWalletResp walletToWalletResp = new WalletToWalletResp();
                walletToWalletResp.setAuthidresponse(P_AUTHIDRESPONSE[0]);
                walletToWalletResp.setErrorresponse(P_ERRORRESPONSE[0]);
                walletToWalletResp.setAvailablebalance(P_AVAILABLEBALANCE[0]);
                walletToWalletResp.setActualbalance(P_ACTUALBALANCE[0]);
                walletToWalletResp.setReserved1(P_UDF1[0]);
                walletToWalletResp.setCheckPoint(P_CHECKPOINT[0]);
                walletToWalletResp.setResponseDescr(P_RESPONSEDESCR[0]);
                walletToWalletResp.setResponseStatus(P_RESONSESTATUS[0]);
                walletToWalletResp.setTransHeadId(P_TRANSHEADID[0]);

                return walletToWalletResp;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() != null) {
                String e1 = e.getCause().getLocalizedMessage();
                if (e1.contains("ORA")) {
                    if (e1.contains("ORA-01013")) {
                        WalletToWalletResp fetchResp = new WalletToWalletResp();
                        fetchResp.setAuthidresponse(walletToWalletReq.getStan());
                        fetchResp.setErrorresponse("058");
                        fetchResp.setAvailablebalance("");
                        fetchResp.setActualbalance("");
                        fetchResp.setReserved1("");

                        return fetchResp;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            return null;
        }
    }

    public void saveSms(TblSmsMessage message, BigDecimal userId) {
        try {
            if (message.getSmsMessageId() <= 0) {
                message.setCreateuser(userId);
            } else {
                message.setLastupdateuser(userId);
                message.setLastupdatedate(new Date());
                message.setUpdateindex(message.getUpdateindex() == null ? new BigDecimal(1)
                        : new BigDecimal(message.getUpdateindex().intValue() + 1));
            }
            tblSmsMessageRepo.saveAndFlush(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, Object> titleFetchForRequestMoney(TitleFetchRequetMoneyRequest requetMoneyRequest,
                                                             Request request) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(requetMoneyRequest.toString()));
        tblRequest.setEndPoint("titleFetchForRequestMoney");
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest.setCreateuser(new BigDecimal(1));
        tblRequest = tblRequestRepo.save(tblRequest);
        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));
        tblResponse.setCreatedate(new Date());
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(requetMoneyRequest.getRequesteMobileNo());
        if (tblAccount != null) {
            TitleFetchRequetMoneyResponse requetMoneyResponse = new TitleFetchRequetMoneyResponse();
            requetMoneyResponse.setAccountTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
            requetMoneyResponse.setAccountNo(tblAccount.getAccountNo());
            requetMoneyResponse.setRequesteAccountId(String.valueOf(tblAccount.getAccountId()));
            tblResponse.setResponseCode(GenericResponseCode.SUCCESS.getResponseCode());
            tblResponse.setAdditionalData(commonService
                    .getResponse(GenericResponseCode.SUCCESS.getResponseCode(), requetMoneyResponse).toString());
            tblResponse.setAccountTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), requetMoneyResponse);
        } else {
            tblResponse.setResponseCode(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            tblResponse.setAdditionalData(commonService
                    .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> requestMoney(RequestMoneyRequest requetMoneyRequest, Request request, String token)
            throws Exception {

        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(requetMoneyRequest));
        tblRequest.setEndPoint("requestMoney");
        tblRequest.setCreateuser(new BigDecimal(1));
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest = tblRequestRepo.save(tblRequest);
        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));
        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(requetMoneyRequest.getNidNo()), request.getImieNo(), accountTypeWallet);
        if (tblAccount != null) {
            String reultMpin = checkMpinValidation(tblAccount.getAccountNo(), requetMoneyRequest.getMpin(), token,
                    request.getImieNo());
            JSONObject jsonObject1 = new JSONObject(reultMpin);
            if (!jsonObject1.getString("responsecode").equalsIgnoreCase("000")) {
                tblResponse.setAdditionalData("Invalid Mpin");
                tblResponse.setResponseCode("");
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponseWithOutDB(GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),
                        jsonObject1.getString("messages"), null);
            }
            TblRequestMoney tblRequestMoney = new TblRequestMoney();
            tblRequestMoney.setAmount(new BigDecimal(requetMoneyRequest.getAmount()));
            tblRequestMoney.setComments("");
            TblAccount newTblAccount = tblAccountRepo.findByAccountNo(requetMoneyRequest.getRequesteeAccountNo());
            if (newTblAccount != null) {
                tblRequestMoney.setTblAccount1(tblAccount);
                tblRequestMoney.setTblAccount2(newTblAccount);
                tblRequestMoney.setCreateuser(new BigDecimal(1));
                tblRequestMoney.setCreatedate(new Date());
                tblRequestMoney.setStatus("P");
                tblRequestMoney = tblRequestMoneyRepo.save(tblRequestMoney);
                tblResponse.setResponseCode(GenericResponseCode.SUCCESS.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney);
            } else {
                tblResponse.setResponseCode(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
            }

        } else {
            tblResponse.setResponseCode(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            tblResponse.setAdditionalData(commonService
                    .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> getReceivedRequests(GetReceivedMoneyRequest getReceivedMoneyRequest,
                                                       Request request) {

        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(getReceivedMoneyRequest));
        tblRequest.setEndPoint("getReceivedRequests");
        tblRequest.setCreateuser(new BigDecimal(1));
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest = tblRequestRepo.save(tblRequest);

        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));

        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(getReceivedMoneyRequest.getNidNo()), request.getImieNo(),
                accountTypeWallet);
        if (tblAccount != null) {
            List<RequestMoney> tblRequestMoney = findRequestMoneyAgainstRequesteeAccount(tblAccount.getAccountId());
            if (tblRequestMoney != null) {
                tblResponse.setResponseCode(GenericResponseCode.SUCCESS.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney);
            } else {
                tblResponse.setResponseCode(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
            }
        } else {
            tblResponse.setResponseCode(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            tblResponse.setAdditionalData(commonService
                    .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    public List<RequestMoney> findRequestMoneyAgainstRequesteeAccount(long accountId) {
        try {
            List<RequestMoney> tblRequestMoneys = new ArrayList<RequestMoney>();
            RequestMoney tblRequestMoney = null;
            List<Object> moneyRequest = tblRequestMoneyRepo.findRequestMoneyAgainstRequesteeAccount(accountId);
            if (moneyRequest != null && moneyRequest.size() > 0) {
                for (Object record : moneyRequest) {
                    tblRequestMoney = new RequestMoney();
                    Object[] row = (Object[]) record;

                    tblRequestMoney.setRequesterMoneyId(((BigDecimal) row[0]));
                    tblRequestMoney.setAmount(((BigDecimal) row[1]));
                    tblRequestMoney.setStatus(((char) row[2]));
                    tblRequestMoney.setStatusDescr(((String) row[3]));
                    tblRequestMoney.setAccountTitle(aeSencryption.decrypt((String) row[4]));
                    tblRequestMoney.setComments(((String) row[5]));
                    tblRequestMoney.setRequestDate((String) row[6]);
                    tblRequestMoney.setAccountNo((String) row[7]);

                    tblRequestMoneys.add(tblRequestMoney);
                }
                if (tblRequestMoneys.size() > 0) {
                    return tblRequestMoneys;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HashMap<String, Object> getSentRequest(GetReceivedMoneyRequest getReceivedMoneyRequest, Request request) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(getReceivedMoneyRequest));
        tblRequest.setEndPoint("getSentRequest");
        tblRequest.setCreateuser(new BigDecimal(1));
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest = tblRequestRepo.save(tblRequest);

        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));

        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(getReceivedMoneyRequest.getNidNo()), request.getImieNo(),
                accountTypeWallet);
        if (tblAccount != null) {
            List<RequestMoney> tblRequestMoney = findRequestMoneyAgainstRequesterAccount(tblAccount.getAccountId());
            if (tblRequestMoney != null) {
                tblResponse.setResponseCode(GenericResponseCode.SUCCESS.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney);
            } else {
                tblResponse.setResponseCode(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
            }
        } else {
            tblResponse.setResponseCode(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            tblResponse.setAdditionalData(commonService
                    .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    public List<RequestMoney> findRequestMoneyAgainstRequesterAccount(long accountId) {
        try {
            List<RequestMoney> tblRequestMoneys = new ArrayList<RequestMoney>();
            RequestMoney tblRequestMoney = null;
            List<Object> moneyRequest = tblRequestMoneyRepo.findRequestMoneyAgainstRequesterAccount(accountId);
            if (moneyRequest != null && !moneyRequest.isEmpty()) {
                for (Object record : moneyRequest) {
                    tblRequestMoney = new RequestMoney();
                    Object[] row = (Object[]) record;

                    tblRequestMoney.setRequesterMoneyId(((BigDecimal) row[0]));
                    tblRequestMoney.setAmount(((BigDecimal) row[1]));
                    tblRequestMoney.setStatus(((char) row[2]));
                    tblRequestMoney.setStatusDescr(((String) row[3]));
                    tblRequestMoney.setAccountTitle(aeSencryption.decrypt((String) row[4]));
                    tblRequestMoney.setComments(((String) row[5]));
                    tblRequestMoney.setRequestDate((String) row[6]);

                    tblRequestMoneys.add(tblRequestMoney);
                }
                if (tblRequestMoneys.size() > 0) {
                    return tblRequestMoneys;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HashMap<String, Object> updateRequestStatus(UpdateReceivedMoneyRequest updateReceivedMoneyRequest,
                                                       Request request, String token) throws Exception {

        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(updateReceivedMoneyRequest));
        tblRequest.setEndPoint("updateRequestStatus");
        tblRequest.setCreateuser(new BigDecimal(1));
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest = tblRequestRepo.save(tblRequest);

        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));

        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(updateReceivedMoneyRequest.getNidNo()), request.getImieNo(),
                accountTypeWallet);
        if (tblAccount != null) {
            TblRequestMoney tblRequestMoney = tblRequestMoneyRepo
                    .findById(Long.parseLong(updateReceivedMoneyRequest.getRequestMoneyId())).orElse(null);
            if (tblRequestMoney != null) {
                if (updateReceivedMoneyRequest.getStatus().equalsIgnoreCase("A")) {
                    ///// Call Funds Transfer
                    FundTransferRequest fundTransferRequest = new FundTransferRequest();
                    fundTransferRequest.setAccountNo(updateReceivedMoneyRequest.getAccountNo());
                    fundTransferRequest.setAccountType("1");
                    fundTransferRequest.setAmount(updateReceivedMoneyRequest.getAmount());
                    fundTransferRequest.setAppUserId(updateReceivedMoneyRequest.getAppUserId());
                    fundTransferRequest.setMpin(updateReceivedMoneyRequest.getMpin());
                    fundTransferRequest.setNidNo(updateReceivedMoneyRequest.getNidNo());
                    fundTransferRequest.setNarration("Transaction for Requested Friend payment");
                    fundTransferRequest.setTransPurposeId("17");
                    fundTransferRequest.setAccountTitle(updateReceivedMoneyRequest.getAccountTitle());
                    HashMap<String, Object> response = fundsTransferLocal(fundTransferRequest, request, token);
                    if (response != null) {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1 != null && jsonObject1.get("responsecode").toString().equalsIgnoreCase("000")) {
                            tblRequestMoney.setStatus(updateReceivedMoneyRequest.getStatus());
                            tblRequestMoney = tblRequestMoneyRepo.save(tblRequestMoney);
                            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                                    tblRequestMoney);
                        } else {
                            return commonService.getResponseWithOutDB(
                                    GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),
                                    jsonObject1.get("messages").toString(), null);
                        }
                    } else {
                        return commonService.getResponseWithOutDB(
                                GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), "Wallet To Wallet Failed",
                                null);
                    }
                } else {
                    tblRequestMoney.setStatus(updateReceivedMoneyRequest.getStatus());
                    tblRequestMoney = tblRequestMoneyRepo.save(tblRequestMoney);
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney);
                }

            } else {
                return commonService.getResponseWithOutDB(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(),
                        "Invalid Request Money Request", null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> getRequestMoneyHistory(GetReceivedMoneyRequest getReceivedMoneyRequest,
                                                          Request request) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(getReceivedMoneyRequest));
        tblRequest.setEndPoint("getRequestMoneyHistory");
        tblRequest.setCreateuser(new BigDecimal(1));
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest = tblRequestRepo.save(tblRequest);

        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(new BigDecimal(1));

        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(getReceivedMoneyRequest.getNidNo()), request.getImieNo(),
                accountTypeWallet);
        if (tblAccount != null) {
            List<RequestMoney> tblRequestMoney = findRequestMoneyHistory(tblAccount.getAccountId());
            if (tblRequestMoney != null && !tblRequestMoney.isEmpty()) {
                tblResponse.setResponseCode(GenericResponseCode.SUCCESS.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblRequestMoney);
            } else {
                tblResponse.setResponseCode(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
                tblResponse.setAdditionalData(commonService
                        .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
                tblResponse = tblResponseRepo.save(tblResponse);
                return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
            }
        } else {
            tblResponse.setResponseCode(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            tblResponse.setAdditionalData(commonService
                    .getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null).toString());
            tblResponse = tblResponseRepo.save(tblResponse);
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    private List<RequestMoney> findRequestMoneyHistory(long accountId) {
        List<RequestMoney> tblRequestMoneys = new ArrayList<RequestMoney>();
        try {

            RequestMoney tblRequestMoney = null;
            List<Object> moneyRequest = tblRequestMoneyRepo.findRequestMoneyHistory(accountId);
            if (moneyRequest != null && moneyRequest.size() > 0) {
                for (Object record : moneyRequest) {
                    tblRequestMoney = new RequestMoney();
                    Object[] row = (Object[]) record;

                    tblRequestMoney.setRequesterMoneyId(((BigDecimal) row[0]));
                    tblRequestMoney.setAmount(((BigDecimal) row[1]));
                    tblRequestMoney.setStatus(((char) row[2]));
                    tblRequestMoney.setStatusDescr(((String) row[3]));
                    tblRequestMoney.setAccountTitle(aeSencryption.decrypt((String) row[4]));
                    tblRequestMoney.setComments(((String) row[5]));
                    tblRequestMoney.setRequestDate((String) row[6]);
                    tblRequestMoney.setAccountNo((String) row[7]);
                    tblRequestMoney.setHistoryStatus((Character) row[8] == 'E' ? "Requestee" : "Requester");

                    tblRequestMoneys.add(tblRequestMoney);
                }
                if (tblRequestMoneys.size() > 0) {
                    return tblRequestMoneys;
                } else {
                    return tblRequestMoneys;
                }
            } else {
                return tblRequestMoneys;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return tblRequestMoneys;
        }
    }

    /**
     * Local title fetch through {@code PKG_MW.TITLE_FETCH}.
     *
     * <p>Both parties are DFS customers, so this takes the procedure's local branch: any
     * P_IBFTIDENTIFIER other than I/O/B falls into "TITLE FETCH FOR LOCAL FUNDS TRANSFER", and
     * P_FTTYPE selects W2W within it. That branch sets MW_REQUEST_TYPE_ID 1 and TRANS_DOCS_ID 107 -
     * the same 107 the previous PKG_PAYMENTS call passed by hand.</p>
     *
     * <p>The beneficiary title is not an OUT parameter here. The procedure packs it into
     * P_RECORDDATA at a fixed offset: account number 20, title 30, source IMD 11,
     * destination IMD 11, identifier 1.</p>
     */
    public TitleFetchProcResponse callTitleFetch(InitiateLocalFTRequest initiateLocalFTRequest, Request request) {
        try {
            // The payer is identified by the national ID, matched against TBL_CUSTOMER.NID_NO,
            // which stores ciphertext - so the plain number from the request is encrypted first.
            final String relationshipId = aeSencryption.encryptwith256(initiateLocalFTRequest.getNidNo());

            Date now = new Date();
            final String strDate = new SimpleDateFormat("yyyyMMdd").format(now);
            final String strTime = new SimpleDateFormat("HHmmss").format(now);
            final String rrnDate = new SimpleDateFormat("yyMMdd").format(now);
            final String stan = tblAccountRepo.getStan();
            final String rrn = rrnDate + stan;
            // 'N' inside the procedure divides by 100, so the amount goes in as minor units -
            // the same conversion the wallet-to-wallet call already applies.
            final String convAmount = getFormattedAmount(initiateLocalFTRequest.getAmount(), "C");
            final String fromAccountNo = initiateLocalFTRequest.getMobileNumber();
            final String toAccountNo = initiateLocalFTRequest.getAccountNo();

            Session session = em.unwrap(Session.class);

            final String[] P_RECORDDATA = new String[1];
            final String[] P_ERRORRESPONSE = new String[1];
            final String[] P_AVAILABLEBALANCE = new String[1];
            final String[] P_ACTUALBALANCE = new String[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_CHECKPOINT = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall(
                            "{call PKG_MW.TITLE_FETCH(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, mwCredentials.getClientSecret());
                    call.setString(2, request.getChannel());
                    if (mwCredentials.getUserId() == null) {
                        call.setNull(3, Types.NUMERIC);
                    } else {
                        call.setLong(3, mwCredentials.getUserId());
                    }
                    call.setString(4, relationshipId);
                    call.setString(5, strDate);
                    call.setString(6, strTime);
                    call.setString(7, stan);
                    call.setString(8, rrn);
                    call.setString(9, strDate);
                    call.setString(10, strTime);
                    call.setString(11, ACQ_INST_CODE_LOCAL);
                    call.setString(12, "001");
                    // Not I, O or B: that is what selects the local funds transfer branch.
                    call.setString(13, "");
                    call.setString(14, "");
                    call.setString(15, fromAccountNo);
                    call.setString(16, toAccountNo);
                    call.setString(17, "");
                    call.setString(18, "");
                    call.setString(19, convAmount);
                    call.setString(20, TRANSACTION_CURRENCY_LOCAL);

                    // 21 and 24 are IN OUT.
                    call.setString(21, null);
                    call.registerOutParameter(21, Types.VARCHAR);
                    call.setString(22, "");
                    call.setString(23, FT_TYPE_WALLET_TO_WALLET);
                    call.setString(24, null);
                    call.registerOutParameter(24, Types.VARCHAR);
                    call.setString(25, "");
                    call.setString(26, "");
                    call.setString(27, "");
                    call.setString(28, "");

                    call.registerOutParameter(29, Types.VARCHAR); // P_AUTHIDRESPONSE
                    call.registerOutParameter(30, Types.VARCHAR); // P_ERRORRESPONSE
                    call.registerOutParameter(31, Types.VARCHAR); // P_AVAILABLEBALANCE
                    call.registerOutParameter(32, Types.VARCHAR); // P_ACTUALBALANCE
                    call.registerOutParameter(33, Types.VARCHAR); // P_MOBILENUMBER
                    call.registerOutParameter(34, Types.VARCHAR); // P_BRANCHCODE
                    call.registerOutParameter(35, Types.VARCHAR); // P_BRANCHNAME
                    call.registerOutParameter(36, Types.VARCHAR); // P_RESPONSEDESCR
                    call.registerOutParameter(37, Types.INTEGER); // P_RESPONSESTATUS
                    call.registerOutParameter(38, Types.VARCHAR); // P_CHECKPOINT
                    call.registerOutParameter(39, Types.NUMERIC); // P_TRANS_HEAD_ID

                    call.execute();

                    P_RECORDDATA[0] = call.getString(21);
                    P_ERRORRESPONSE[0] = call.getString(30);
                    P_AVAILABLEBALANCE[0] = call.getString(31);
                    P_ACTUALBALANCE[0] = call.getString(32);
                    P_RESPONSEDESCR[0] = call.getString(36);
                    int status = call.getInt(37);
                    P_RESPONSESTATUS[0] = call.wasNull() ? null : status;
                    P_CHECKPOINT[0] = call.getString(38);
                }
            });

            LOG.info("PKG_MW.TITLE_FETCH completed | STAN:{} | RRN:{} | status:{} | code:{} | checkpoint:{}",
                    stan, rrn, P_RESPONSESTATUS[0], P_ERRORRESPONSE[0], P_CHECKPOINT[0]);

            TitleFetchProcResponse titleFetchProcResponse = new TitleFetchProcResponse();
            titleFetchProcResponse.setResponseCode(P_ERRORRESPONSE[0]);
            titleFetchProcResponse.setResponseStatus(P_RESPONSESTATUS[0]);
            titleFetchProcResponse.setResponseDescr(P_RESPONSEDESCR[0]);
            titleFetchProcResponse.setAvailableBalance(P_AVAILABLEBALANCE[0]);
            titleFetchProcResponse.setActualBalance(P_ACTUALBALANCE[0]);
            titleFetchProcResponse.setAccountTitle(beneficiaryTitleFrom(P_RECORDDATA[0], toAccountNo));
            return titleFetchProcResponse;

        } catch (Exception e) {
            LOG.error("PKG_MW.TITLE_FETCH failed | account:{} | {}",
                    initiateLocalFTRequest.getAccountNo(), rootCauseMessage(e), e);
            if (e.getCause() != null) {
                String cause = e.getCause().getLocalizedMessage();
                if (cause != null && cause.contains("ORA-01013")) {
                    TitleFetchProcResponse fetchResp = new TitleFetchProcResponse();
                    fetchResp.setResponseCode("058");
                    fetchResp.setAvailableBalance("");
                    fetchResp.setActualBalance("");
                    return fetchResp;
                }
            }
            return null;
        }
    }

    /**
     * The beneficiary title, still encrypted, out of P_RECORDDATA.
     *
     * <p>Layout written by the procedure: account number 20, title 30, source IMD 11,
     * destination IMD 11, identifier 1 - so the title occupies characters 21 to 50.</p>
     *
     * <p>The procedure clips that field to 30 characters, but TBL_ACCOUNT.ACCOUNT_TITLE holds
     * ciphertext that can be longer, and a clipped ciphertext will not decrypt. When that happens
     * the title is read from the account row rather than returned empty. The value handed back is
     * still encrypted, because the caller decrypts it.</p>
     */
    private String beneficiaryTitleFrom(String recordData, String toAccountNo) {
        if (recordData != null && recordData.length() >= 50) {
            String encrypted = recordData.substring(20, 50).trim();
            String decrypted = aeSencryption.decrypt(encrypted);
            if (decrypted != null && !decrypted.isEmpty()) {
                return encrypted;
            }
            LOG.warn("Beneficiary title from P_RECORDDATA did not decrypt, most likely clipped to 30 "
                    + "characters; reading it from the account row instead");
        }
        TblAccount beneficiary = tblAccountRepo.findByAccountNo(toAccountNo);
        return beneficiary == null ? null : beneficiary.getAccountTitle();
    }

    private String rootCauseMessage(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

    @Override
    public HashMap<String, Object> fundsTransferAgent(FundTransferRequest fundTransferRequest, Request request,
                                                      String token) throws Exception {
        TblAccount tblAccount = tblAccountRepo.getAgentAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(fundTransferRequest.getNidNo()), request.getImieNo(), accountTypeAgent);
        FundTransferResponce fundTransferResponce = new FundTransferResponce();
        if (tblAccount != null) {
            TblRequest tblRequest = saveRequestFundTransfer(fundTransferRequest, request, tblAccount);

            TblRequest saveTblRequest = tblRequestRepo.save(tblRequest);

            TblResponse tblResponse = new TblResponse();
            tblResponse.setTblRequest(saveTblRequest);
            tblResponse.setCreateuser(new BigDecimal(1));

            String convAmount = getFormattedAmount(fundTransferRequest.getAmount(), "C");
            Date date = new Date();

            SimpleDateFormat dateFomat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String transDate = dateFomat.format(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
            String strDate = dateFormatter.format(date);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
            String strTime = timeFormatter.format(date);

            SimpleDateFormat rrnDateFormatter = new SimpleDateFormat("yyMMdd");
            String rrnDate = rrnDateFormatter.format(date);

            String stan = tblAccountRepo.getStan();

            if (!fundTransferRequest.getAccountNo().isEmpty()) {
                WalletToWalletReq wallettowalletReq = setDataWalletToWalletReq(fundTransferRequest, tblAccount,
                        convAmount, strDate, strTime, rrnDate, stan, false);
                String reultMpin = this.checkAgentMpinValidation(tblAccount.getAccountNo(),
                        fundTransferRequest.getMpin(), token, request.getImieNo(), agentMpinVerificationUrl);
                JSONObject jsonObject1 = new JSONObject(reultMpin);
                if (!jsonObject1.getString("responsecode").equalsIgnoreCase("000")) {
                    tblResponse.setAdditionalData("Invalid Mpin");
                    tblResponse.setResponseCode("");
                    tblResponse = tblResponseRepo.save(tblResponse);
                    return commonService.getResponseWithOutDB(GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),
                            jsonObject1.getString("messages"), null);
                }
                WalletToWalletResp walletToWalletResp = CashInOut(wallettowalletReq, request, "AGENT_CASH_IN");

                String responseCode = "";
                String responseDescr = "";
                if (walletToWalletResp != null && walletToWalletResp.getResponseStatus() != 0) {
                    responseCode = "000";
                    responseDescr = "Funds Transfer Successfully";
                } else {
                    responseCode = walletToWalletResp != null ? walletToWalletResp.getErrorresponse()
                            : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode();
                    responseDescr = walletToWalletResp != null ? walletToWalletResp.getResponseDescr()
                            : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage();
                }

                fundTransferResponce.setResponseDescr(responseDescr);
                fundTransferResponce.setAuthIdResponse(stan);
                fundTransferResponce.setResponseCode(responseCode);
                fundTransferResponce.setTransDate(transDate);

                if (responseCode.equals("000")) {
                    saveSmsMessageTemplateFundTransfer(fundTransferRequest, tblAccount, transDate, walletToWalletResp,
                            new BigDecimal(173));
                    tblResponse.setTblRequest(saveTblRequest);
                    tblResponse.setAuthIdResponse(stan);
                    tblResponse.setResponseCode(responseCode);
                    tblResponse.setTransDate(transDate);
                    tblResponse.setStan(stan);
                    tblResponse.setRrn(rrnDate + stan);
                    tblResponse = tblResponseRepo.save(tblResponse);
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            fundTransferResponce);
                } else {
                    tblResponse.setAdditionalData(responseDescr);
                    tblResponse.setResponseCode(responseCode);
                    tblResponse = tblResponseRepo.save(tblResponse);
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            fundTransferResponce);
                }
            } else {
                return null;
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    /**
     * PKG_FUNDS_TRANSFER.FT_CASH_IN - agent cash-in, replacing PKG_PAYMENTS1.AGENT_CASH_IN.
     *
     * <p>This is not a rename: the two procedures take different things. The old one was framed
     * around the ISO-style envelope (client secret, channel code, STAN, RRN, transmission date and
     * time, account numbers as text). The new one is framed around the deposit itself - the target
     * account id, the agent, the amount, and who physically handed the cash over. It also returns a
     * transaction reference number the old one did not.</p>
     *
     * <pre>
     *    1 P_T_ACCOUNT_ID     NUMBER    target (customer) account id
     *    2 P_BVS              VARCHAR2  biometric verification result
     *    3 P_COMMENTS         VARCHAR2  narration
     *    4 P_TRANS_AMOUNT     NUMBER
     *    5 P_AGENT_ID         NUMBER    the agent taking the cash
     *    6 P_CHANNEL_ID       NUMBER
     *    7 P_CASH_IN_TYPE     CHAR
     *    8 P_DEPOSITOR_NID    NUMBER
     *    9 P_DEPOSITOR_MOB    VARCHAR2
     *   10 P_DEPOSITOR_NAME   VARCHAR2
     *   11 P_DEPOSITOR_DOB    DATE
     *   12 P_TRANS_REFNUM     OUT NUMBER
     *   13 P_TRANS_HEAD_ID    OUT NUMBER
     *   14 P_STATUS           OUT NUMBER
     *   15 P_STATUS_DESCR     OUT VARCHAR2
     * </pre>
     *
     * <p>The result is mapped onto {@link WalletToWalletResp} so the rest of the cash-in flow -
     * response codes, SMS, TBL_RESPONSE - is untouched. FT_CASH_IN returns no separate response
     * code, only a status and a description, so a failure is reported as a technical issue carrying
     * the database's own description rather than a fabricated code.</p>
     */
    private WalletToWalletResp ftCashIn(FundTransferRequest fundTransferRequest, TblAccount agentAccount,
                                        Request request) {
        final Long[] transRefNum = new Long[1];
        final Integer[] transHeadId = new Integer[1];
        final Integer[] status = new Integer[1];
        final String[] statusDescr = new String[1];

        try {
            TblAccount toAccount = tblAccountRepo.findByAccountNo(fundTransferRequest.getAccountNo());
            if (toAccount == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            }
            if (agentAccount.getTblAgent() == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            }
            long agentId = agentAccount.getTblAgent().getAgentId();
            long accountId = toAccount.getAccountId();

            // The envelope carries the channel as a code (AGNT, MOB, ...); the procedure wants the
            // LKP_CHANNEL id, so it is resolved here rather than being sent by the caller.
            LkpChannel lkpChannel = lkpChannelRepo.findByChannelCodeAndIsActive(request.getChannel(), Constants.YES);
            if (lkpChannel == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
            }
            long channelId = lkpChannel.getChannelId();

            Session session = em.unwrap(Session.class);
            session.doWork(connection -> {
                // try-with-resources: the statement is closed even when the call fails, so a
                // repeated failure cannot exhaust the database's open cursors.
                try (CallableStatement call = connection
                        .prepareCall("{call PKG_FUNDS_TRANSFER.FT_CASH_IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}")) {

                    call.setLong(1, accountId);
                    call.setString(2, fundTransferRequest.getBvs());
                    call.setString(3, fundTransferRequest.getNarration());
                    call.setBigDecimal(4, new BigDecimal(fundTransferRequest.getAmount().trim()));
                    call.setLong(5, agentId);
                    call.setLong(6, channelId);
                    call.setString(7, fundTransferRequest.getCashInType());
                    setNumericOrNull(call, 8, fundTransferRequest.getDepositorNid());
                    // The account being credited is the depositor's mobile number.
                    call.setString(9, fundTransferRequest.getAccountNo());
                    call.setString(10, fundTransferRequest.getDepositorName());
                    setDateOrNull(call, 11, fundTransferRequest.getDepositorDob());

                    call.registerOutParameter(12, Types.NUMERIC);
                    call.registerOutParameter(13, Types.NUMERIC);
                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);

                    call.execute();

                    transRefNum[0] = call.getLong(12);
                    transHeadId[0] = call.getInt(13);
                    status[0] = call.getInt(14);
                    statusDescr[0] = call.getString(15);
                }
            });

            System.out.println("\nFT_CASH_IN completed with status " + status[0]
                    + " | transRefNum " + transRefNum[0] + " | " + statusDescr[0]);

            WalletToWalletResp resp = new WalletToWalletResp();
            resp.setResponseStatus(status[0] == null ? 0 : status[0]);
            resp.setResponseDescr(statusDescr[0]);
            resp.setTransHeadId(transHeadId[0]);
            resp.setAuthidresponse(transRefNum[0] == null ? null : String.valueOf(transRefNum[0]));
            if (status[0] == null || status[0] == 0) {
                // No response-code OUT parameter exists on this procedure; report the failure
                // honestly rather than inventing one.
                resp.setErrorresponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
            }
            return resp;

        } catch (CustomDataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * PKG_FUNDS_TRANSFER.FT_CASH_OUT - agent cash-out, replacing PKG_PAYMENTS1.AGENT_CASH_OUT.
     *
     * <p>Shorter than its cash-in counterpart: a cash-out takes money out of the customer's own
     * account, so there is no depositor to record. Parameter 1 is the FROM account, where the
     * cash-in procedure took a TO account; both come from the same request field.</p>
     *
     * <pre>
     *    1 P_F_ACCOUNT_ID   NUMBER    customer account being debited
     *    2 P_BVS            VARCHAR2  biometric verification result
     *    3 P_COMMENTS       VARCHAR2  narration
     *    4 P_TRANS_AMOUNT   NUMBER
     *    5 P_AGENT_ID       NUMBER    the agent paying the cash out
     *    6 P_CHANNEL_ID     NUMBER
     *    7 P_TRANS_REFNUM   OUT NUMBER
     *    8 P_TRANS_HEAD_ID  OUT NUMBER
     *    9 P_STATUS         OUT NUMBER
     *   10 P_STATUS_DESCR   OUT VARCHAR2
     * </pre>
     */
    private WalletToWalletResp ftCashOut(FundTransferRequest fundTransferRequest, TblAccount agentAccount,
                                         Request request) {
        final Long[] transRefNum = new Long[1];
        final Integer[] transHeadId = new Integer[1];
        final Integer[] status = new Integer[1];
        final String[] statusDescr = new String[1];

        try {
            TblAccount fromAccount = tblAccountRepo.findByAccountNo(fundTransferRequest.getAccountNo());
            if (fromAccount == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            }
            if (agentAccount.getTblAgent() == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
            }
            long agentId = agentAccount.getTblAgent().getAgentId();
            long accountId = fromAccount.getAccountId();

            LkpChannel lkpChannel = lkpChannelRepo.findByChannelCodeAndIsActive(request.getChannel(), Constants.YES);
            if (lkpChannel == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
            }
            long channelId = lkpChannel.getChannelId();

            Session session = em.unwrap(Session.class);
            session.doWork(connection -> {
                try (CallableStatement call = connection
                        .prepareCall("{call PKG_FUNDS_TRANSFER.FT_CASH_OUT(?,?,?,?,?,?,?,?,?,?)}")) {

                    call.setLong(1, accountId);
                    call.setString(2, fundTransferRequest.getBvs());
                    call.setString(3, fundTransferRequest.getNarration());
                    call.setBigDecimal(4, new BigDecimal(fundTransferRequest.getAmount().trim()));
                    call.setLong(5, agentId);
                    call.setLong(6, channelId);

                    call.registerOutParameter(7, Types.NUMERIC);
                    call.registerOutParameter(8, Types.NUMERIC);
                    call.registerOutParameter(9, Types.INTEGER);
                    call.registerOutParameter(10, Types.VARCHAR);

                    call.execute();

                    transRefNum[0] = call.getLong(7);
                    transHeadId[0] = call.getInt(8);
                    status[0] = call.getInt(9);
                    statusDescr[0] = call.getString(10);
                }
            });

            System.out.println("\nFT_CASH_OUT completed with status " + status[0]
                    + " | transRefNum " + transRefNum[0] + " | " + statusDescr[0]);

            WalletToWalletResp resp = new WalletToWalletResp();
            resp.setResponseStatus(status[0] == null ? 0 : status[0]);
            resp.setResponseDescr(statusDescr[0]);
            resp.setTransHeadId(transHeadId[0]);
            resp.setAuthidresponse(transRefNum[0] == null ? null : String.valueOf(transRefNum[0]));
            if (status[0] == null || status[0] == 0) {
                resp.setErrorresponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
            }
            return resp;

        } catch (CustomDataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Empty rather than null, so String.replace never sees a null replacement.
     */
    private String safe(String value) {
        return value == null ? "" : value;
    }

    /**
     * Latitude and longitude as they arrive on the request envelope.
     *
     * <p>The apps send these as empty strings when the device has no fix, and
     * {@code Double.parseDouble("")} throws NumberFormatException("empty String") - which used to
     * surface to the caller as response code 113 with the message "empty String", killing the
     * transaction before it reached the database. A missing coordinate is simply absent, so it is
     * stored as NULL. A malformed one is treated the same way rather than failing the payment.</p>
     */
    private Double parseCoordinate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Binds a numeric procedure parameter, or NULL when the request did not supply one.
     */
    private void setNumericOrNull(CallableStatement call, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            call.setNull(index, Types.NUMERIC);
            return;
        }
        try {
            call.setBigDecimal(index, new BigDecimal(value.trim()));
        } catch (NumberFormatException e) {
            call.setNull(index, Types.NUMERIC);
        }
    }

    /**
     * Binds a yyyy-MM-dd date parameter, or NULL when absent or unparseable.
     */
    private void setDateOrNull(CallableStatement call, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            call.setNull(index, Types.DATE);
            return;
        }
        try {
            java.util.Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(value.trim());
            call.setDate(index, new java.sql.Date(parsed.getTime()));
        } catch (ParseException e) {
            call.setNull(index, Types.DATE);
        }
    }

    private WalletToWalletResp CashInOut(WalletToWalletReq walletToWalletReq, Request request, String procName) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_ERRORRESPONSE = new String[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final Integer[] P_RESONSESTATUS = new Integer[1];
            final Integer[] P_TRANSHEADID = new Integer[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall(
                            "{call PKG_PAYMENTS1." + procName + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

                    call.setString(1, agentClientSecret);
                    call.setString(2, request.getChannel());
                    call.setBigDecimal(3, new BigDecimal(walletToWalletReq.getChannelId()));
                    call.setString(4, walletToWalletReq.getRelationshipid());
                    call.setString(5, walletToWalletReq.getTransmissiondate());
                    call.setString(6, walletToWalletReq.getTransmissiontime());
                    call.setString(7, walletToWalletReq.getStan());
                    call.setString(8, walletToWalletReq.getRrn());
                    call.setString(9, walletToWalletReq.getFromaccountnumber());
                    call.setString(10, walletToWalletReq.getToaccountnumber());
                    call.setString(11, walletToWalletReq.getTransactionamount());
                    call.setString(12, request.getLatitude());
                    call.setString(13, request.getLongitude());

                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);
                    call.registerOutParameter(16, Types.INTEGER);
                    call.registerOutParameter(17, Types.VARCHAR);

                    call.execute();
                    P_TRANSHEADID[0] = call.getInt(14);
                    P_ERRORRESPONSE[0] = call.getString(15);
                    P_RESONSESTATUS[0] = call.getInt(16);
                    P_RESPONSEDESCR[0] = call.getString(17);

                }
            });
            System.out.println("\nWallet to Wallet Completed with status  " + P_RESONSESTATUS[0]);
            if (P_RESONSESTATUS != null && P_RESONSESTATUS.length > 0) {
                WalletToWalletResp walletToWalletResp = new WalletToWalletResp();
                walletToWalletResp.setErrorresponse(P_ERRORRESPONSE[0]);
                walletToWalletResp.setResponseDescr(P_RESPONSEDESCR[0]);
                walletToWalletResp.setResponseStatus(P_RESONSESTATUS[0]);
                walletToWalletResp.setTransHeadId(P_TRANSHEADID[0]);

                return walletToWalletResp;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() != null) {
                String e1 = e.getCause().getLocalizedMessage();
                if (e1.contains("ORA")) {
                    if (e1.contains("ORA-01013")) {
                        WalletToWalletResp fetchResp = new WalletToWalletResp();
                        fetchResp.setAuthidresponse(walletToWalletReq.getStan());
                        fetchResp.setErrorresponse("058");
                        fetchResp.setAvailablebalance("");
                        fetchResp.setActualbalance("");
                        fetchResp.setReserved1("");

                        return fetchResp;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            return null;
        }
    }

    public Response generateOtp(String mobileNumber, String email, String type, String identifier, String docTypeCode,
                                String userType, Request request, String authToken) throws JsonProcessingException {
        GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
        generateOtpRequest.setEmail(email);
        generateOtpRequest.setMobileNumber(mobileNumber);
        generateOtpRequest.setOtpType(type);
        generateOtpRequest.setUserType(userType);
        generateOtpRequest.setIdentifier(identifier);
        generateOtpRequest.setSmsTemplateCode(docTypeCode);
        request.setPayload(generateOtpRequest);
        return sendRequestAndGetResponse(request, authToken, generateOtpUrl);

    }

    @Override
    public HashMap<String, Object> qrVerifyOtp(VerifyOtpRequest verifyOtpRequest, Request request,
                                               HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Response verifyOtp = verifyOtp(verifyOtpRequest, request, httpServletRequest.getHeader("Authorization"));
        if (verifyOtp == null) {
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), verifyOtp);
        }

        if (!GenericResponseCode.SUCCESS.getResponseCode().equalsIgnoreCase(verifyOtp.getResponsecode())) {
            return commonService.getResponse(GenericResponseCode.WRONG_OTP.getResponseCode(), verifyOtp);
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
    }

    public Response verifyOtp(VerifyOtpRequest verifyOtpRequest, Request request, String authToken)
            throws JsonProcessingException {
        request.setPayload(verifyOtpRequest);
        return sendRequestAndGetResponse(request, authToken, verifyOtpUrl);
    }

    @Override
    public HashMap<String, Object> initiateCardTitleFetch(CardTitleFetchAppRequest cardTitleFetchAppRequest,
                                                          Request request, String token, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAppUserId(userId);
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }
        return cardTitleFetch(cardTitleFetchAppRequest);
    }

    public HashMap<String, Object> cardTitleFetch(CardTitleFetchAppRequest cardTitleFetchAppRequest)
            throws JsonProcessingException {
        CardTitleFetchRqst cardTitleFetchRqst = new CardTitleFetchRqst();
        cardTitleFetchRqst.setPan(cardTitleFetchAppRequest.getPan());
        cardTitleFetchRqst.setMerchantType("6012");
        cardTitleFetchRqst.setCardAcceptorIdentification("00000001");
        cardTitleFetchRqst.setCardAcceptorCode("123456789101475");
        cardTitleFetchRqst.setCvcPresent("000");
        cardTitleFetchRqst.setTransactionType("1");
        cardTitleFetchRqst.setTerminalType("2");
        cardTitleFetchRqst.setTransactionCurrencyCode("971");

        PointOfService pointOfService = new PointOfService();
        pointOfService.setCardDataInputCapability("5");
        pointOfService.setCardholderAuthenticationCapability("1");
        pointOfService.setCardCaptureCapability("0");
        pointOfService.setOperatingEnvironment("1");
        pointOfService.setCardholderPresenceIndicator("0");
        pointOfService.setCardPresence("1");
        pointOfService.setCardDataInputMode("2");
        pointOfService.setCardholderAuthenticationMethod("5");
        pointOfService.setCardholderAuthenticationEntity("4");
        pointOfService.setCardDataOutputCapability("1");
        pointOfService.setTerminalOutputCapability("4");
        pointOfService.setPinCaptureCapability("6");
        cardTitleFetchRqst.setPointOfService(pointOfService);

        CardAcceptorNameAndLocation cardAcceptor = new CardAcceptorNameAndLocation();
        cardAcceptor.setName("DFS");
        cardAcceptor.setStreet("1234");
        cardAcceptor.setCity("KABUL");
        cardAcceptor.setState("KBL");
        cardAcceptor.setCountry("AFG");
        cardAcceptor.setPostalCode("123456789");
        cardTitleFetchRqst.setCardAcceptorNameAndLocation(cardAcceptor);

        String json = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cardTitleFetchRqst, cardTitleFetchUrl);

        if (json == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        TransactionResponseDto<CardTitleFetchApsResponse> responseDto = mapper.readValue(json, mapper.getTypeFactory()
                .constructParametricType(TransactionResponseDto.class, CardTitleFetchApsResponse.class));

        if (responseDto != null && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
            // calculate fee using proceedure
            BigDecimal fee = BigDecimal.ZERO;
            HashMap<String, Object> purposeOfPayment = commonService.getPurposeOfPayment();
            List<LovResponse> dataList = (List<LovResponse>) purposeOfPayment.get("data");
            responseDto.getData().setTransPurposes(dataList);
            responseDto.getData()
                    .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
            responseDto.getData().setFee(fee.toString());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), responseDto.getData());
        } else {
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                    responseDto != null ? responseDto.getData() : null);
        }
    }

    @Override
    public HashMap<String, Object> fundsTransferCard(FundTransferCardRequest fundTransferRequest, Request request,
                                                     String authorization, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(fundTransferRequest.getNidNo()), request.getImieNo(), accountTypeWallet);
        FundTransferResponce fundTransferResponce = new FundTransferResponce();
        if (tblAccount != null) {
            TblRequest tblRequest = saveRequestFundTransferCard(fundTransferRequest, request, tblAccount);

            TblRequest saveTblRequest = tblRequestRepo.save(tblRequest);

            TblResponse tblResponse = new TblResponse();
            tblResponse.setTblRequest(saveTblRequest);
            tblResponse.setCreateuser(new BigDecimal(1));
            WalletToCardRqst walletToCardRqst = new WalletToCardRqst();
            walletToCardRqst.setStan(DateTools.generateStan());
            walletToCardRqst.setRrn(DateTools.generateStan() + walletToCardRqst.getStan());
            walletToCardRqst.setPan(fundTransferRequest.getPan());
            walletToCardRqst.setMerchantType("6012");
            walletToCardRqst.setAmount(fundTransferRequest.getAmount());
            walletToCardRqst.setTract2Data("");
            walletToCardRqst.setPin("");
            walletToCardRqst.setCardAcceptorIdentification("00000001");
            walletToCardRqst.setCardAcceptorCode("123456789101475");

            // Set PointOfService
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("S");
            pos.setCardholderAuthenticationCapability("0");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("5");
            pos.setCardholderPresenceIndicator("5");
            pos.setCardPresence("0");
            pos.setCardDataInputMode("T");
            pos.setCardholderAuthenticationMethod("0");
            pos.setCardholderAuthenticationEntity("0");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("0");
            walletToCardRqst.setPointOfService(pos);

            // Set CardAcceptorNameAndLocation
            CardAcceptorNameAndLocation location = new CardAcceptorNameAndLocation();
            location.setName("DFS");
            location.setStreet("1234");
            location.setCity("KABUL");
            location.setState("KBL");
            location.setCountry("AFG");
            location.setPostalCode("123456789");
            walletToCardRqst.setCardAcceptorNameAndLocation(location);

            // Set remaining fields
            walletToCardRqst.setCvcPresent("000");
            walletToCardRqst.setCvc2("");
            walletToCardRqst.setCard2Expiration("");
            walletToCardRqst.setTransactionType("1");
            walletToCardRqst.setTerminalType("8");
            walletToCardRqst
                    .setCard2Number("900419" + String.format("%010d", Integer.parseInt(tblAccount.getAccountNo())));
            walletToCardRqst.setCurrencyCodeTransaction("971");
            walletToCardRqst.setCurrencyCodeCardBilling("971");
            ProcResponse procResponse = procedureService.walletToCardAcquirer(tblAccount.getAccountId(),
                    walletToCardRqst.getCard2Number(), "", fundTransferRequest.getAccountTitle(),
                    new BigDecimal(fundTransferRequest.getAmount()), userId.longValue(), walletToCardRqst.getStan(),
                    walletToCardRqst.getRrn());
            if (procResponse != null && procResponse.getResponseCode().equals("0000")) {

                String walletToCardResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""),
                        walletToCardRqst, walletToCardUrl);
                TransactionResponseDto<WalletToCardApsResponse> responseDto = mapper.readValue(walletToCardResponse,
                        mapper.getTypeFactory()
                                .constructParametricType(TransactionResponseDto.class, WalletToCardApsResponse.class));
                if (responseDto != null
                        && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                    // calculate fee using proceedure
                    BigDecimal fee = BigDecimal.ZERO;
                    responseDto.getData().setFee(fee.toString());
                    responseDto.getData()
                            .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            responseDto.getData());
                } else {
                    return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                            responseDto != null ? responseDto.getData() : null);
                }
            } else {
                WalletToCardApsResponse wallet = new WalletToCardApsResponse();
                wallet.setDescr(procResponse.getResponseDescription() != null ? procResponse.getResponseDescription()
                        : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage());
                wallet.setResponseCode(procResponse.getResponseCode() != null ? procResponse.getResponseCode()
                        : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
                return commonService.getResponse(wallet.getResponseCode(), wallet);
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> cardTransferFund(CardTransferFundRequest cardTransferFundRequest, Request request,
                                                    String authorization, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo
                .getAccountByAccountNumberAndAccountType(cardTransferFundRequest.getAccountNo(), accountTypeWallet);
        if (tblAccount != null) {
            TblRequest tblRequest = saveRequestCardTransferFund(cardTransferFundRequest, request, tblAccount);
            TblRequest saveTblRequest = tblRequestRepo.save(tblRequest);
            TblResponse tblResponse = new TblResponse();
            tblResponse.setTblRequest(saveTblRequest);
            tblResponse.setCreateuser(new BigDecimal(1));
            CardToWalletRqst cardToWalletRqst = new CardToWalletRqst();
            cardToWalletRqst.setStan(DateTools.generateStan());
            cardToWalletRqst.setRrn(DateTools.generateStan() + cardToWalletRqst.getStan());
            cardToWalletRqst.setPan(cardTransferFundRequest.getPan());
            cardToWalletRqst.setMerchantType("6012");
            cardToWalletRqst.setAmount(cardTransferFundRequest.getAmount());
            cardToWalletRqst.setTract2Data("");
            cardToWalletRqst.setPin("");
            cardToWalletRqst.setCardAcceptorIdentification("00000001");
            cardToWalletRqst.setCardAcceptorCode("123456789101475");
            cardToWalletRqst.setCvcPresent("001");
            cardToWalletRqst.setCvc2(cardTransferFundRequest.getCvc());
            cardToWalletRqst.setDateExpiration(cardTransferFundRequest.getExpiry());
            cardToWalletRqst.setTransactionType("1");
            cardToWalletRqst.setTerminalType("8");
            String card2Number = "900419" + String.format("%010d", Long.parseLong(tblAccount.getAccountNo()));
            cardToWalletRqst.setCard2Number(card2Number);
            cardToWalletRqst.setCurrencyCodeTransaction("971");
            cardToWalletRqst.setCurrencyCodeCardBilling("971");
            cardToWalletRqst.setOriginalRrn(cardTransferFundRequest.getRrn());
            cardToWalletRqst.setOtpPin(cardTransferFundRequest.getOtpPin());

            // PointOfService
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("S");
            pos.setCardholderAuthenticationCapability("0");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("5");
            pos.setCardholderPresenceIndicator("5");
            pos.setCardPresence("0");
            pos.setCardDataInputMode("T");
            pos.setCardholderAuthenticationMethod("0");
            pos.setCardholderAuthenticationEntity("0");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("0");
            cardToWalletRqst.setPointOfService(pos);

            // CardAcceptorNameAndLocation
            CardAcceptorNameAndLocation location = new CardAcceptorNameAndLocation();
            location.setName("DFS");
            location.setStreet("1234");
            location.setCity("KABUL");
            location.setState("KBL");
            location.setCountry("AFG");
            location.setPostalCode("123456789");
            cardToWalletRqst.setCardAcceptorNameAndLocation(location);
            String walletToCardResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cardToWalletRqst,
                    cardToWalletUrl);
            TransactionResponseDto<CardToWalltResp> responseDto = mapper.readValue(walletToCardResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, CardToWalltResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData().setFee(fee.toString());
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
                // }else if (responseDto != null &&
                // responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())){
            } else if (responseDto != null && responseDto.getCode().equals("000")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                // call proceedure here
                responseDto.getData().setFee(fee.toString());
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                LkpBank lkpBank = lkpBankRepo.findById(Long.valueOf(cardTransferFundRequest.getBankId())).orElseThrow(
                        () -> new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
                ProcResponse procResponse = procedureService.cardToWalletAcquirer(cardToWalletRqst.getPan(),
                        lkpBank.getBankName(), cardTransferFundRequest.getAccountTitle(),
                        String.valueOf(tblAccount.getAccountId()), new BigDecimal(cardTransferFundRequest.getAmount()),
                        userId.longValue(), cardToWalletRqst.getStan(), cardToWalletRqst.getRrn(), mobileClientSecret,
                        mobileChannelCode);

                if (procResponse != null && procResponse.getResponseCode().equals("0000")) {
                    responseDto.getData().setDescr(procResponse.getResponseDescription());
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            responseDto.getData());
                } else {
                    responseDto.getData()
                            .setDescr(procResponse.getResponseDescription() != null
                                    ? procResponse.getResponseDescription()
                                    : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage());
                    responseDto.getData()
                            .setResponseCode(procResponse.getResponseCode() != null ? procResponse.getResponseCode()
                                    : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
                    return commonService.getResponse(responseDto.getData().getResponseCode(), responseDto.getData());
                }

            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> balanceInquiry(CardBalanceInquiryRequest cardBalanceInquiryRequest, Request request,
                                                  String token, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAppUserId(userId);
        if (tblAccount != null) {
            BalanceInquiryRqst rqst = new BalanceInquiryRqst();

            rqst.setPan(cardBalanceInquiryRequest.getPan());
            rqst.setMerchantType("4411");
            rqst.setTrack2Data("");
            rqst.setPinData("");
            rqst.setCardAcceptorIdentification("00000001");
            rqst.setCardAcceptorCode("123456789101475");
            rqst.setCvcPresent("000");
            rqst.setCvc2Data("");
            rqst.setDateExpiration(cardBalanceInquiryRequest.getDateExpiration());
            rqst.setTerminalType("2");
            rqst.setCurrencyCodeTransaction("971");

            // PointOfService
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");
            rqst.setPointOfService(pos);

            // CardAcceptorNameAndLocation
            CardAcceptorNameAndLocation loc = new CardAcceptorNameAndLocation();
            loc.setName("DFS");
            loc.setStreet("1234");
            loc.setCity("KABUL");
            loc.setState("KBL");
            loc.setCountry("AFG");
            loc.setPostalCode("123456789");
            rqst.setCardAcceptorNameAndLocation(loc);
            String walletToCardResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), rqst,
                    balanceInquiryUrl);
            TransactionResponseDto<BalanceInquiryResp> responseDto = mapper.readValue(walletToCardResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, BalanceInquiryResp.class));

            if (responseDto != null && responseDto.getCode().equals("000")) {
                CardBalanceInquiryResponse cardBalanceInquiryResponse = new CardBalanceInquiryResponse();
                cardBalanceInquiryResponse.setCurrencyAvailable(responseDto.getData().getCurrencyAvailable());
                cardBalanceInquiryResponse
                        .setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                        cardBalanceInquiryResponse);

            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> cardTocard(CardToCardRequest cardToCardRequest, Request request,
                                              String authorization, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(cardToCardRequest.getAccountNo(),
                accountTypeWallet);
        if (tblAccount != null) {
            CardToCardRqst cardToCardRqst = new CardToCardRqst();
            cardToCardRqst.setStan(DateTools.generateStan());
            cardToCardRqst.setRrn(DateTools.generateStan() + cardToCardRqst.getStan());
            // Set simple fields
            cardToCardRqst.setPan(cardToCardRequest.getFromPan());
            cardToCardRqst.setMerchantType("6012");
            cardToCardRqst.setAmount(cardToCardRequest.getAmount());
            cardToCardRqst.setCardAcceptorIdentification("00000001");
            cardToCardRqst.setCardAcceptorCode("123456789101475");
            cardToCardRqst.setCvcPresent("001");
            cardToCardRqst.setCvc2(cardToCardRequest.getCvc());
            cardToCardRqst.setDateExpiration(cardToCardRequest.getExpiry());
            cardToCardRqst.setTransactionType("1");
            cardToCardRqst.setTerminalType("8");
            cardToCardRqst.setCard2Number(cardToCardRequest.getToPan());
            cardToCardRqst.setCurrencyCodeTransaction("971");
            cardToCardRqst.setCurrencyCodeCardBilling("971");
            cardToCardRqst.setNetworkReferenceNumber("");
            cardToCardRqst.setOtpPin(cardToCardRequest.getOtpPin());
            cardToCardRqst.setOriginalRrn(cardToCardRequest.getRrn());

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("S");
            pos.setCardholderAuthenticationCapability("0");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("5");
            pos.setCardholderPresenceIndicator("5");
            pos.setCardPresence("0");
            pos.setCardDataInputMode("T");
            pos.setCardholderAuthenticationMethod("0");
            pos.setCardholderAuthenticationEntity("0");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("0");

            cardToCardRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            cardToCardRqst.setCardAcceptorNameAndLocation(acceptor);
            String cardToCardResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cardToCardRqst,
                    cardToCardUrl);
            TransactionResponseDto<CardToCardResp> responseDto = mapper.readValue(cardToCardResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, CardToCardResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData().setFee(fee.toString());
                responseDto.getData().setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else if (responseDto != null
                    && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                // call proceedure here
                CardToCardProcRequest cardToCardProcRequest = new CardToCardProcRequest();
                cardToCardProcRequest.setFromCardNo(cardToCardRequest.getFromPan());
                cardToCardProcRequest.setFromBankName("");
                cardToCardProcRequest.setFromAccountTitle(cardToCardRequest.getFromCardTitle());
                cardToCardProcRequest.setToCardNo(cardToCardRequest.getToPan());
                cardToCardProcRequest.setToBankName("");
                cardToCardProcRequest.setToAccountTitle(cardToCardRequest.getToCardTitle());
                cardToCardProcRequest.setTransAmount(new BigDecimal(cardToCardRequest.getAmount()));
                cardToCardProcRequest.setAppUserId(userId.longValue());
                cardToCardProcRequest.setStan(cardToCardRqst.getStan());
                cardToCardProcRequest.setRrn(cardToCardRqst.getRrn());
                cardToCardProcRequest.setClientSecret(mobileClientSecret);
                cardToCardProcRequest.setChannelCode(mobileChannelCode);

                ProcResponse procResponse = procedureService.cardToCard(cardToCardProcRequest);
                if (procResponse != null && procResponse.getResponseCode().equals("0000")) {
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            responseDto.getData());
                } else {
                    responseDto.getData()
                            .setResponseCode(procResponse.getResponseCode() != null ? procResponse.getResponseCode()
                                    : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
                    return commonService.getResponse(responseDto.getData().getResponseCode(), responseDto.getData());
                }
            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> fetchBill(FetchBillRequest fetchBillRequest, Request request, String token, BigDecimal userId)
            throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAppUserId(userId);
        if (tblAccount != null) {
            SavePaymentInfoRqst savePaymentInfoRqst = new SavePaymentInfoRqst();

            // Set simple fields
            savePaymentInfoRqst.setPan(fetchBillRequest.getPan());
            savePaymentInfoRqst.setMerchantType("6012");
            savePaymentInfoRqst.setCardAcceptorIdentification("00000001");
            savePaymentInfoRqst.setCardAcceptorCode("123456789101475");
            savePaymentInfoRqst.setCvcPresent("000");
            savePaymentInfoRqst.setTransactionType("1");
            savePaymentInfoRqst.setTransactionCurrencyCode("971");
            savePaymentInfoRqst.setTerminalType("2");
            savePaymentInfoRqst.setServiceId(fetchBillRequest.getServiceId());
            savePaymentInfoRqst.setBillRefNo(fetchBillRequest.getBillRefNo());

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");

            savePaymentInfoRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            savePaymentInfoRqst.setCardAcceptorNameAndLocation(acceptor);

            String savePaymentResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), savePaymentInfoRqst,
                    billInfoUrl);
            ObjectMapper mapper = new ObjectMapper();
            TransactionResponseDto<SavePaymentInfoResp> responseDto = mapper.readValue(savePaymentResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, SavePaymentInfoResp.class));

            if (responseDto != null && responseDto.getCode().equals("000")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                // call proceedure here

                HashMap<String, Object> purposeOfPayment = commonService.getPurposeOfPayment();
                List<LovResponse> dataList = (List<LovResponse>) purposeOfPayment.get("data");
                responseDto.getData().setBillAmount(isoToNormal(responseDto.getData().getBillAmount()));
                responseDto.getData().setTransPurposes(dataList);
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                responseDto.getData().setFee(fee.toString());
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), responseDto.getData());

            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> payBill(PayBillRequest payBillRequest, Request request, String token,
                                           BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAppUserId(userId);
        if (tblAccount != null) {
            SavePaymentRqst savePaymentRqst = new SavePaymentRqst();
            savePaymentRqst.setStan(DateTools.generateStan());
            savePaymentRqst.setRrn(DateTools.generateStan() + savePaymentRqst.getStan());
            // Set simple fields
            savePaymentRqst.setPan(payBillRequest.getPan());
            savePaymentRqst.setMerchantType("6012");
            savePaymentRqst.setCardAcceptorIdentification("00000001");
            savePaymentRqst.setCardAcceptorCode("123456789101475");
            savePaymentRqst.setAmount(payBillRequest.getAmount());
            savePaymentRqst.setCvcPresent("001");
            savePaymentRqst.setCvc2(payBillRequest.getCvv());
            savePaymentRqst.setDateExpiration(payBillRequest.getExpiry());
            savePaymentRqst.setTransactionType("1");
            savePaymentRqst.setTransactionCurrencyCode("971");
            savePaymentRqst.setTerminalType("2");
            savePaymentRqst.setServiceId(payBillRequest.getServiceId());
            savePaymentRqst.setBillRefNo(payBillRequest.getBillRefNo());
            savePaymentRqst.setBillAmount(payBillRequest.getBillAmount());
            savePaymentRqst.setRequestId(payBillRequest.getRequestId());
            savePaymentRqst.setOtpPin(payBillRequest.getOtpPin());
            savePaymentRqst.setOriginalRrn(payBillRequest.getRrn());

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");

            savePaymentRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            savePaymentRqst.setCardAcceptorNameAndLocation(acceptor);

            String billPaymentResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), savePaymentRqst,
                    billPaymentUrl);
            TransactionResponseDto<SavePaymentResp> responseDto = mapper.readValue(billPaymentResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, SavePaymentResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData().setFee(fee.toString());
                responseDto.getData().setBillAmount(isoToNormal(responseDto.getData().getBillAmount()));
                responseDto.getData().setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else if (responseDto != null
                    && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                // call proceedure heresavePaymentRqst
                ProcResponse procResponseAcquirer;
                if (payBillRequest.getPan() != null && !payBillRequest.getPan().startsWith("900419")) {

                    BillPaymentAcquirerRequest billPaymentAcquirerRequest = new BillPaymentAcquirerRequest();
                    billPaymentAcquirerRequest.setClientSecret(mobileClientSecret);
                    billPaymentAcquirerRequest.setChannelCode(mobileChannelCode);
                    billPaymentAcquirerRequest.setFromCardNo(payBillRequest.getPan());
                    billPaymentAcquirerRequest.setFromCardTitle(payBillRequest.getCardTitle());
                    billPaymentAcquirerRequest.setUtilityCompanyCode(payBillRequest.getServiceId());
                    billPaymentAcquirerRequest.setUtilityConsumerNo(payBillRequest.getBillRefNo());
                    billPaymentAcquirerRequest.setTransAmount(new BigDecimal(payBillRequest.getBillAmount()));
                    billPaymentAcquirerRequest.setAppUserId(userId.longValue());
                    billPaymentAcquirerRequest.setStan(savePaymentRqst.getStan());
                    billPaymentAcquirerRequest.setRrn(savePaymentRqst.getRrn());

                    procResponseAcquirer = procedureService.billPaymentAcquirer(billPaymentAcquirerRequest);

                }
                responseDto.getData().setFee(fee.toString());
                responseDto.getData().setBillAmount(isoToNormal(responseDto.getData().getBillAmount()));
                responseDto.getData().setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("000", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    private TblRequest saveRequestFundTransferCard(FundTransferCardRequest fundTransferCardRequest, Request request,
                                                   TblAccount tblAccount) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(fundTransferCardRequest));
        tblRequest.setCnic(fundTransferCardRequest.getNidNo());
        tblRequest.setImei(request.getImieNo());
        tblRequest.setToAccount(fundTransferCardRequest.getAccountNo());
        tblRequest.setFromAccount(tblAccount.getAccountNo());
        tblRequest.setAmount(fundTransferCardRequest.getAmount());
        tblRequest.setPurposeOfPayment(fundTransferCardRequest.getTransPurposeId());
        tblRequest.setNarration(fundTransferCardRequest.getNarration());
        tblRequest.setAccountTitle(fundTransferCardRequest.getAccountTitle());
        tblRequest.setBeneficiaryName(fundTransferCardRequest.getBeneficiaryName());
        tblRequest.setBeneficiaryMobile(fundTransferCardRequest.getBeneficiaryMobile());
        tblRequest.setBeneficiaryEmail(fundTransferCardRequest.getBeneficiaryEmail());
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest.setEndPoint("fundsTransferCard");
        tblRequest.setCreateuser(new BigDecimal(1));
        return tblRequest;
    }

    private TblRequest saveRequestCardTransferFund(CardTransferFundRequest cardTransferFundRequest, Request request,
                                                   TblAccount tblAccount) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(helperClass.convertObjecttoJson(cardTransferFundRequest));
        tblRequest.setAccountNo(cardTransferFundRequest.getAccountNo());
        tblRequest.setImei(request.getImieNo());
        tblRequest.setToAccount(cardTransferFundRequest.getAccountNo());
        tblRequest.setFromAccount(tblAccount.getAccountNo());
        tblRequest.setAmount(cardTransferFundRequest.getAmount());
        tblRequest.setPurposeOfPayment(cardTransferFundRequest.getTransPurposeId());
        tblRequest.setNarration(cardTransferFundRequest.getNarration());
        tblRequest.setAccountTitle(cardTransferFundRequest.getAccountTitle());
        tblRequest.setBeneficiaryName(cardTransferFundRequest.getBeneficiaryName());
        tblRequest.setBeneficiaryMobile(cardTransferFundRequest.getBeneficiaryMobile());
        tblRequest.setBeneficiaryEmail(cardTransferFundRequest.getBeneficiaryEmail());
        tblRequest.setLatitude(parseCoordinate(request.getLatitude()));
        tblRequest.setLongitude(parseCoordinate(request.getLongitude()));
        tblRequest.setEndPoint("cardTransferFundRequest");
        tblRequest.setCreateuser(new BigDecimal(1));
        return tblRequest;
    }

    @Override
    public String checkMpinValidation(String mobNo, String mPin, String Token, String imei) throws Exception {

        // Get the URL from the environment properties
        MpinRequest mpinRequest = new MpinRequest();
        MpinPayload payload = new MpinPayload();
        payload.setMobileNumber(mobNo);
        payload.setMpin(mPin);
        mpinRequest.setImieNo(imei);
        mpinRequest.setPayload(payload);
        String url = mpinUrl;
        // Send a POST request to the specified URL with the request payload
        String result = getResponseFromPostAPILms(createHeaderMap(Token), mpinRequest, url);
        return result;

    }

    @Override
    public HashMap<String, Object> cashInAgent(CashInAgentRequest cashInAgentRequest, Request request,
                                               String authorization, BigDecimal userId) throws Exception {
        TblAccount tblAccount = tblAccountRepo
                .getAccountByAccountNumberAndAccountType(cashInAgentRequest.getAccountNo(), accountTypeAgent);
        if (tblAccount != null) {

            String reultMpin = this.checkAgentMpinValidation(tblAccount.getAccountNo(),
                    cashInAgentRequest.getPin(), authorization, request.getImieNo(), agentMpinVerificationUrl);
            JSONObject jsonObject1 = new JSONObject(reultMpin);
            if (!jsonObject1.getString("responsecode").equalsIgnoreCase("000")) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_PIN.getResponseCode());
            }

            CashInAgentRqst cashInAgentRqst = new CashInAgentRqst();
            cashInAgentRqst.setStan(DateTools.generateStan());
            cashInAgentRqst.setRrn(DateTools.generateStan() + cashInAgentRqst.getStan());
            // Set simple fields
            cashInAgentRqst.setPan(cashInAgentRequest.getPan());
            cashInAgentRqst.setMerchantType("6012");
            cashInAgentRqst.setCardAcceptorIdentification("00000001");
            cashInAgentRqst.setCardAcceptorCode("123456789101475");
            cashInAgentRqst.setAmount(cashInAgentRequest.getAmount());
            cashInAgentRqst.setCvcPresent("000");
            cashInAgentRqst
                    .setCard2Number("900419" + String.format("%010d", Integer.parseInt(tblAccount.getAccountNo())));
            cashInAgentRqst.setCvc2("");
            cashInAgentRqst.setCard2Expiration("");
            cashInAgentRqst.setTransactionType("1");
            cashInAgentRqst.setCurrencyCodeTransaction("971");
            cashInAgentRqst.setTerminalType("2");

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");

            cashInAgentRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            cashInAgentRqst.setCardAcceptorNameAndLocation(acceptor);

            String cashInResp = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cashInAgentRqst, cashinUrl);
            TransactionResponseDto<CashInResp> responseDto = mapper.readValue(cashInResp, mapper.getTypeFactory()
                    .constructParametricType(TransactionResponseDto.class, CashInResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else if (responseDto != null
                    && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                String province = "";
                String district = "";
                if (isNullOrEmpty(request.getLatitude())) {
                    request.setLatitude("38.10");
                }
                if (isNullOrEmpty(request.getLongitude())) {
                    request.setLongitude("70.90");
                }
                LocationResponse locationResponse = locationService.findLocation(
                        Double.parseDouble(request.getLatitude()), Double.parseDouble(request.getLongitude()));
                if (locationResponse != null) {
                    province = locationResponse.getProvince();
                    district = locationResponse.getDistrict();
                }
                // call proceedure here
                ProcResponse procResponse = procedureService.cashinAcquirer(cashInAgentRequest.getPan(), "",
                        cashInAgentRequest.getAccountTitle(), String.valueOf(tblAccount.getAccountId()),
                        new BigDecimal(cashInAgentRequest.getAmount()), userId.longValue(), cashInAgentRqst.getStan(),
                        cashInAgentRqst.getRrn(), province, district);
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("000", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> cashOutAgent(CashOutAgentRequest cashOutAgentRequest, Request request,
                                                String authorization, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo
                .getAccountByAccountNumberAndAccountType(cashOutAgentRequest.getAccountNo(), accountTypeAgent);
        if (tblAccount != null) {
            CashOutRqst cashOutRqst = new CashOutRqst();
            cashOutRqst.setStan(DateTools.generateStan());
            cashOutRqst.setRrn(DateTools.generateStan() + cashOutRqst.getStan());
            // Set simple fields
            cashOutRqst.setPan(cashOutAgentRequest.getPan());
            cashOutRqst.setMerchantType("6012");
            cashOutRqst.setCardAcceptorIdentification("00000001");
            cashOutRqst.setCardAcceptorCode("123456789101475");
            cashOutRqst.setAmount(cashOutAgentRequest.getAmount());
            cashOutRqst.setCvcPresent(isNullOrEmpty(cashOutRqst.getCvcPresent()) ? "000" : cashOutRqst.getCvcPresent());
            cashOutRqst.setCvc2(cashOutAgentRequest.getCvc2());
            cashOutRqst.setDateExpiration(cashOutAgentRequest.getCard2Expiration());
            String card2Number = "900419" + String.format("%010d", Long.parseLong(tblAccount.getAccountNo()));
            cashOutRqst.setCard2Number(card2Number);
            cashOutRqst.setTransactionType("1");
            cashOutRqst.setCurrencyCodeTransaction("971");
            cashOutRqst.setCurrencyCodeCardBilling("971");
            cashOutRqst.setTerminalType("2");

            cashOutRqst.setOriginalRrn(cashOutAgentRequest.getRrn());
            cashOutRqst.setOtpPin(cashOutAgentRequest.getOtpPin());

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");

            cashOutRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            cashOutRqst.setCardAcceptorNameAndLocation(acceptor);

            String cashOutResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cashOutRqst,
                    cashoutUrl);
            TransactionResponseDto<CashOutResp> responseDto = mapper.readValue(cashOutResponse, mapper.getTypeFactory()
                    .constructParametricType(TransactionResponseDto.class, CashOutResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else if (responseDto != null
                    && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                String province = "";
                String district = "";
                if (isNullOrEmpty(request.getLatitude())) {
                    request.setLatitude("38.10");
                }
                if (isNullOrEmpty(request.getLongitude())) {
                    request.setLongitude("70.90");
                }
                LocationResponse locationResponse = locationService.findLocation(
                        Double.parseDouble(request.getLatitude()), Double.parseDouble(request.getLongitude()));
                if (locationResponse != null) {
                    province = locationResponse.getProvince();
                    district = locationResponse.getDistrict();
                }
                // call proceedure here
                ProcResponse procResponse = procedureService.cashoutAcquirer(cashOutAgentRequest.getPan(), "", "",
                        String.valueOf(tblAccount.getAccountId()), new BigDecimal(cashOutAgentRequest.getAmount()),
                        userId.longValue(), cashOutRqst.getStan(), cashOutRqst.getRrn(), province, district);
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("000", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    public static String formatDateTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTime.format(outputFormatter);
    }

    public static String isoToNormal(String isoAmount) {
        if (isoAmount == null || isoAmount.trim().isEmpty()) {
            return "0.00";
        }

        // Remove leading zeros
        String cleaned = isoAmount.replaceFirst("^0+", "");

        // Handle case like "000000000000"
        if (cleaned.isEmpty()) {
            return "0.00";
        }

        // Ensure at least 3 digits
        if (cleaned.length() < 3) {
            cleaned = String.format("%03d", Integer.parseInt(cleaned));
        }

        int len = cleaned.length();
        return cleaned.substring(0, len - 2) + "." + cleaned.substring(len - 2);
    }

    @Override
    public HashMap<String, Object> cashIn(FundTransferRequest fundTransferRequest,
                                          Request request,
                                          String authorization) throws Exception {

        TblAccount tblAccount = tblAccountRepo.getAgentAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(fundTransferRequest.getNidNo()),
                request.getImieNo(),
                accountTypeAgent);

        if (tblAccount == null) {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

        TblRequest savedRequest = tblRequestRepo.save(
                saveRequestFundTransfer(fundTransferRequest, request, tblAccount));

        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(savedRequest);
        tblResponse.setCreateuser(BigDecimal.ONE);

        // Format dates
        DateUtils.DateBundle dates = DateUtils.buildFormattedDates();
        String stan = tblAccountRepo.getStan();
//        String formattedAmount = getFormattedAmount(fundTransferRequest.getAmount(), "C");

        if (fundTransferRequest.getAccountNo().isEmpty()) {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

        WalletToWalletReq walletReq = setDataWalletToWalletReq(
                fundTransferRequest, tblAccount, fundTransferRequest.getAmount(),
                dates.yyyyMMdd, dates.HHmmss, dates.rrnDate, stan,
                false);

        // Validate MPIN (returns an error response if invalid)
        HashMap<String, Object> mpinValidationResponse = validateMpin(tblAccount, fundTransferRequest, request,
                tblResponse, authorization);

        if (mpinValidationResponse != null) {
            return mpinValidationResponse;
        }

        // Agent cash-in now goes through PKG_FUNDS_TRANSFER.FT_CASH_IN rather than
        // PKG_PAYMENTS1.AGENT_CASH_IN; cash-out and the other flows are unchanged.
        WalletToWalletResp walletResp = ftCashIn(fundTransferRequest, tblAccount, request);

        // Process wallet response
        ResponseBundle resp = processWalletResponse(walletResp);

        FundTransferResponce responseDto = new FundTransferResponce();
        responseDto.setResponseCode(resp.code);
        responseDto.setResponseDescr(resp.desc);
        responseDto.setAuthIdResponse(stan);
        responseDto.setTransDate(dates.transDate);

        // Fill DB entity
        tblResponse.setAuthIdResponse(stan);
        tblResponse.setTransDate(dates.transDate);
        tblResponse.setStan(stan);
        tblResponse.setRrn(dates.rrnDate + stan);

        if (resp.code.equals("000")) {
            // The transaction has already been posted by the procedure at this point. The SMS and
            // push notification are after-effects: if one of them fails, that must not turn a
            // completed transaction into an error the agent will retry. Logged, not propagated.
            try {
                saveSmsMessageTemplateFundTransfer(
                        fundTransferRequest, tblAccount, dates.transDate, walletResp, new BigDecimal(173));
            } catch (Exception e) {
                System.out.println("\nTransaction posted but the notification failed | stan " + stan
                        + " | " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            tblResponse.setAdditionalData(resp.desc);
        }

        // Save DB response
        saveDbResponse(tblResponse, resp.code);

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), responseDto);
    }

    @Override
    public HashMap<String, Object> cashOutTitleFetch(InitiateLocalFTRequest initiateLocalFTRequest, Request request,
                                                     String token) throws JsonProcessingException {
        HashMap<String, Object> response = initiateLocalFT(initiateLocalFTRequest, request, token);
        Response otpResponse = generateOtp(initiateLocalFTRequest.getMobileNumber(), Constants.EMPTY, "FT", "S",
                smsTemplateTypeTrxn, "C",
                request, token);
        if (otpResponse == null
                || !otpResponse.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        GenerateOtpResponse generateOtpResponse = fromJson(convertObjecttoJson(otpResponse.getData()),
                GenerateOtpResponse.class);
        response.put("otp", generateOtpResponse);

        return response;
    }

    private HashMap<String, Object> validateMpin(TblAccount tblAccount,
                                                 FundTransferRequest req,
                                                 Request request,
                                                 TblResponse tblResponse, String token) throws Exception {

        String mpinResult = this.checkAgentMpinValidation(
                tblAccount.getAccountNo(),
                req.getMpin(),
                token,
                request.getImieNo(), agentMpinVerificationUrl);

        JSONObject mpinJson = new JSONObject(mpinResult);

        if (!mpinJson.getString("responsecode").equalsIgnoreCase("000")) {

            tblResponse.setAdditionalData("Invalid Mpin");
            tblResponse.setResponseCode("");
            tblResponseRepo.save(tblResponse);

            return commonService.getResponseWithOutDB(
                    GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),
                    mpinJson.getString("messages"),
                    null);
        }

        return null; // MPIN valid
    }

    private static class ResponseBundle {
        String code;
        String desc;

        ResponseBundle(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    private ResponseBundle processWalletResponse(WalletToWalletResp resp) {

        if (resp != null && resp.getResponseStatus() != 0) {
            return new ResponseBundle("000", "Funds Transfer Successfully");
        }

        String code = resp != null
                ? resp.getErrorresponse()
                : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode();

        String desc = resp != null
                ? resp.getResponseDescr()
                : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage();

        return new ResponseBundle(code, desc);
    }

    private void saveDbResponse(TblResponse tblResponse, String responseCode) {
        tblResponse.setResponseCode(responseCode);
        tblResponseRepo.save(tblResponse);
    }

    @Override
    public HashMap<String, Object> cashOut(FundTransferRequest fundTransferRequest,
                                           VerifyOtpRequest verifyOtpRequest,
                                           Request request,
                                           String authorization) throws Exception {

        TblAccount tblAccount = tblAccountRepo.getAgentAccountByCnicImeiAndAccountType(
                aeSencryption.encryptwith256(fundTransferRequest.getNidNo()),
                request.getImieNo(),
                accountTypeAgent);

        if (tblAccount == null) {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

        TblRequest savedRequest = tblRequestRepo.save(
                saveRequestFundTransfer(fundTransferRequest, request, tblAccount));

        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(savedRequest);
        tblResponse.setCreateuser(BigDecimal.ONE);

        // Format dates
        DateUtils.DateBundle dates = DateUtils.buildFormattedDates();
        String stan = tblAccountRepo.getStan();
        // getFormattedAmount calls PKG_PAYMENTS.GET_AMOUNT_FORMATTED, whose package body is
        // invalid; FT_CASH_OUT takes the amount as a number anyway, so the raw value is used.
        // Same change as on the cash-in side.

        if (fundTransferRequest.getAccountNo().isEmpty()) {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

        WalletToWalletReq walletReq = setDataWalletToWalletReq(
                fundTransferRequest, tblAccount, fundTransferRequest.getAmount(),
                dates.yyyyMMdd, dates.HHmmss, dates.rrnDate, stan,
                true);

        // Validate Otp

        Response verifyOtp = verifyOtp(verifyOtpRequest, request, authorization);
        if (verifyOtp == null) {
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), verifyOtp);
        }

        if (!GenericResponseCode.SUCCESS.getResponseCode().equalsIgnoreCase(verifyOtp.getResponsecode())) {
            return commonService.getResponse(GenericResponseCode.WRONG_OTP.getResponseCode(), verifyOtp);
        }

        // Agent cash-out now goes through PKG_FUNDS_TRANSFER.FT_CASH_OUT rather than
        // PKG_PAYMENTS1.AGENT_CASH_OUT.
        WalletToWalletResp walletResp = ftCashOut(fundTransferRequest, tblAccount, request);

        // Process wallet response
        ResponseBundle resp = processWalletResponse(walletResp);

        FundTransferResponce responseDto = new FundTransferResponce();
        responseDto.setResponseCode(resp.code);
        responseDto.setResponseDescr(resp.desc);
        responseDto.setAuthIdResponse(stan);
        responseDto.setTransDate(dates.transDate);

        // Fill DB entity
        tblResponse.setAuthIdResponse(stan);
        tblResponse.setTransDate(dates.transDate);
        tblResponse.setStan(stan);
        tblResponse.setRrn(dates.rrnDate + stan);

        if (resp.code.equals("000")) {
            // The transaction has already been posted by the procedure at this point. The SMS and
            // push notification are after-effects: if one of them fails, that must not turn a
            // completed transaction into an error the agent will retry. Logged, not propagated.
            try {
                saveSmsMessageTemplateFundTransfer(
                        fundTransferRequest, tblAccount, dates.transDate, walletResp, new BigDecimal(173));
            } catch (Exception e) {
                System.out.println("\nTransaction posted but the notification failed | stan " + stan
                        + " | " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            tblResponse.setAdditionalData(resp.desc);
        }

        // Save DB response
        saveDbResponse(tblResponse, resp.code);

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), responseDto);
    }

    @Override
    public HashMap<String, Object> walletToWallet(WalletToWalletRqst walletToWalletReq, Request request, String token,
                                                  BigDecimal userId) throws Exception {
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(walletToWalletReq.getAccountNo(),
                accountTypeWallet);
        if (tblAccount != null) {
            String reultMpin = checkMpinValidation(tblAccount.getAccountNo(), walletToWalletReq.getMpin(), token,
                    request.getImieNo());
            JSONObject jsonObject1 = new JSONObject(reultMpin);
            if (!jsonObject1.getString("responsecode").equalsIgnoreCase("000")) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_PIN.getResponseCode());
            }
            com.wallet.transaction.dto.aps.WalletToWalletRqst walletToWalletRqst = new com.wallet.transaction.dto.aps.WalletToWalletRqst();
            walletToWalletRqst.setStan(DateTools.generateStan());
            walletToWalletRqst.setRrn(DateTools.generateStan() + walletToWalletRqst.getStan());
            ProcResponse procResponse = procedureService.walletToWalletAcquirer(
                    String.valueOf(tblAccount.getAccountId()), walletToWalletReq.getPan(),
                    walletToWalletReq.getAmount(), userId, walletToWalletReq.getToBankName(),
                    walletToWalletRqst.getStan(), walletToWalletRqst.getRrn());
            if (procResponse != null && procResponse.getResponseCode().equals("0000")) {

                // Set simple fields
                walletToWalletRqst.setPan(walletToWalletReq.getPan());
                walletToWalletRqst.setMerchantType("6012");
                walletToWalletRqst.setCardAcceptorIdentification("00000001");
                walletToWalletRqst.setCardAcceptorCode("123456789101475");
                walletToWalletRqst.setAmount(walletToWalletReq.getAmount());
                walletToWalletRqst.setCard2Number(
                        dfsBin + String.format("%010d", Integer.parseInt(tblAccount.getAccountNo())));
                walletToWalletRqst.setCurrencyCodeTransaction("971");
                walletToWalletRqst.setCurrencyCodeCardBilling("971");
                walletToWalletRqst.setNetworkReferenceNumber(walletToWalletRqst.getRrn() + "1100");
                walletToWalletRqst.setTerminalType("2");
                walletToWalletRqst.setOriginalRrn(walletToWalletRqst.getRrn());

                // Build PointOfService object
                PointOfService pos = new PointOfService();
                pos.setCardDataInputCapability("S");
                pos.setCardholderAuthenticationCapability("0");
                pos.setCardCaptureCapability("0");
                pos.setOperatingEnvironment("5");
                pos.setCardholderPresenceIndicator("5");
                pos.setCardPresence("0");
                pos.setCardDataInputMode("T");
                pos.setCardholderAuthenticationMethod("0");
                pos.setCardholderAuthenticationEntity("0");
                pos.setCardDataOutputCapability("1");
                pos.setTerminalOutputCapability("4");
                pos.setPinCaptureCapability("0");
                walletToWalletRqst.setPointOfService(pos);

                // Build CardAcceptorNameAndLocation object
                CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
                acceptor.setName("DFS");
                acceptor.setStreet("1234");
                acceptor.setCity("KABUL");
                acceptor.setState("KBL");
                acceptor.setCountry("AFG");
                acceptor.setPostalCode("123456789");

                walletToWalletRqst.setCardAcceptorNameAndLocation(acceptor);

                String wallettoWalletRequestAps = getResponseFromPostAPI(this.createHeaderMapBackOffice(""),
                        walletToWalletRqst, walletToWalletUrl);
                TransactionResponseDto<com.wallet.transaction.dto.aps.WalletToWalletResp> responseDto = mapper
                        .readValue(wallettoWalletRequestAps, mapper.getTypeFactory()
                                .constructParametricType(TransactionResponseDto.class,
                                        com.wallet.transaction.dto.aps.WalletToWalletResp.class));

                if (responseDto != null
                        && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                    // calculate fee using proceedure
                    BigDecimal fee = BigDecimal.ZERO;
                    // call proceedure here
                    responseDto.getData()
                            .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                    return commonService.getResponseWithOutDB("000", GenericResponseCode.SUCCESS.getResponseMessage(),
                            responseDto.getData());
                } else {
                    ReversalRequest reversalRequest = new ReversalRequest();
                    reversalRequest.setStan(walletToWalletRqst.getStan());
                    reversalRequest.setRrn(walletToWalletRqst.getRrn());
                    reversalRequest.setChannelCode(mobileChannelCode);
                    reversalRequest.setClientSecret(mobileClientSecret);
                    procedureService.reversal(reversalRequest);
                    return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                            responseDto != null ? responseDto.getData() : null);
                }
            } else {
                return commonService.getResponseWithOutDB(
                        (procResponse != null && procResponse.getResponseCode() != null)
                                ? procResponse.getResponseCode()
                                : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        (procResponse != null && procResponse.getResponseDescription() != null)
                                ? procResponse.getResponseDescription()
                                : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage(),
                        null);
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> purchase(PurchaseRequest purchaseRequest, Request request, BigDecimal userId) throws JsonProcessingException {

        TblAccount tblAccount = tblAccountRepo.findAgentByAppUserId(userId);
        if (tblAccount == null) {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
        String stan = DateTools.generateStan();
        String rrn = DateTools.generateStan() + stan;
        CardTrackData cardTrackData = extractCardData(purchaseRequest.getTrack2data());
        if (cardTrackData.getPan().startsWith("900419")) {
            ProcResponse procResponse = procedureService.purchase(tblAccount.getAccountId(),
                    cardTrackData.getPan(),
                    "",
                    cardTrackData.getFirstName() + " " + cardTrackData.getLastName(),
                    new BigDecimal(purchaseRequest.getAmount()),
                    userId.longValue(), stan, rrn,
                    "", "", posClientSecret, posChannelCode);
            if (procResponse != null && procResponse.getResponseCode().equals("0000")) {
                String formattedAmount = formatPurchaseAmount(purchaseRequest.getAmount());
                String maskedCardNumber = maskPan(purchaseRequest.getTrack2data());
                String referenceNumber = generatePurchaseReferenceNumber();
                String message = String.format(
                        "A total amount of AFN %s has been deducted from your card ending with %s.%nReference Number: %s",
                        formattedAmount,
                        maskedCardNumber,
                        referenceNumber);
                return commonService.getResponseWithOutDB(GenericResponseCode.SUCCESS.getResponseCode(), message, null);
            } else {
                return commonService.getResponseWithOutDB((procResponse != null && procResponse.getResponseCode() != null)
                                ? procResponse.getResponseCode()
                                : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        (procResponse != null && procResponse.getResponseDescription() != null)
                                ? procResponse.getResponseDescription()
                                : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage(),
                        null);
            }
        } else {
            // Call procedure Acquirer for incase of other Card purchase from our machine
            ProcResponse procResponse = procedureService.purchaseAcquirer(tblAccount.getAccountId(),
                    cardTrackData.getPan(),
                    "",
                    cardTrackData.getFirstName() + " " + cardTrackData.getLastName(),
                    new BigDecimal(purchaseRequest.getAmount()),
                    userId.longValue(), stan, rrn, "", "");
            if (procResponse != null && procResponse.getResponseCode().equals("0000")) {
                PurchaseRqst purchaseRqst = new PurchaseRqst();
                // Set simple fields
                purchaseRqst.setPan(cardTrackData.getPan());
                purchaseRqst.setMerchantType("4411");
                purchaseRqst.setCardAcceptorIdentification("00000001");
                purchaseRqst.setCardAcceptorCode("123456789101475");
                purchaseRqst.setAmount(purchaseRequest.getAmount());
                purchaseRqst.setCard2Number(dfsBin + String.format("%010d", Integer.parseInt(tblAccount.getAccountNo())));
                purchaseRqst.setPin(purchaseRequest.getPin());
                purchaseRqst.setDateExpiration(cardTrackData.getExpiry());
                purchaseRqst.setCurrencyCodeTransaction("971");
                purchaseRqst.setCurrencyCodeCardBilling("971");
                purchaseRqst.setTract2Data(cardTrackData.getTrack2data());
                purchaseRqst.setTerminalType("2");
                purchaseRqst.setOriginalRrn(rrn);
                purchaseRqst.setCvcPresent("000");


                // PointOfService
                PointOfService pos = new PointOfService();
                pos.setCardDataInputCapability("S");
                pos.setCardholderAuthenticationCapability("0");
                pos.setCardCaptureCapability("0");
                pos.setOperatingEnvironment("5");
                pos.setCardholderPresenceIndicator("5");
                pos.setCardPresence("0");
                pos.setCardDataInputMode("T");
                pos.setCardholderAuthenticationMethod("0");
                pos.setCardholderAuthenticationEntity("0");
                pos.setCardDataOutputCapability("1");
                pos.setTerminalOutputCapability("4");
                pos.setPinCaptureCapability("0");
                purchaseRqst.setPointOfService(pos);

                // Build CardAcceptorNameAndLocation object
                CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
                acceptor.setName("DFS");
                acceptor.setStreet("1234");
                acceptor.setCity("KABUL");
                acceptor.setState("KBL");
                acceptor.setCountry("AFG");
                acceptor.setPostalCode("123456789");

                purchaseRqst.setCardAcceptorNameAndLocation(acceptor);

                String purchaseRequestAps = getResponseFromPostAPI(this.createHeaderMapBackOffice(""),
                        purchaseRqst, purchaseUrl);
                TransactionResponseDto<PurchaseResp> responseDto = mapper
                        .readValue(purchaseRequestAps, mapper.getTypeFactory()
                                .constructParametricType(TransactionResponseDto.class,
                                        PurchaseResp.class));

                if (responseDto != null
                        && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                    // calculate fee using proceedure
                    BigDecimal fee = BigDecimal.ZERO;
                    // call proceedure here
                    responseDto.getData()
                            .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                    String formattedAmount = formatPurchaseAmount(purchaseRequest.getAmount());
                    String maskedCardNumber = maskPan(purchaseRequest.getTrack2data());
                    String referenceNumber = generatePurchaseReferenceNumber();
                    String message = String.format(
                            "A total amount of AFN %s has been deducted from your card ending with %s.%nReference Number: %s",
                            formattedAmount,
                            maskedCardNumber,
                            referenceNumber);
                    return commonService.getResponseWithOutDB(GenericResponseCode.SUCCESS.getResponseCode(), message, null);
                } else {
                    ReversalRequest reversalRequest = new ReversalRequest();
                    reversalRequest.setStan(stan);
                    reversalRequest.setRrn(rrn);
                    reversalRequest.setChannelCode(posChannelCode);
                    reversalRequest.setClientSecret(posClientSecret);
                    procedureService.reversal(reversalRequest);
                    return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                            responseDto != null ? responseDto.getData() : null);
                }
            } else {
                return commonService.getResponseWithOutDB(
                        (procResponse != null && procResponse.getResponseCode() != null)
                                ? procResponse.getResponseCode()
                                : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        (procResponse != null && procResponse.getResponseDescription() != null)
                                ? procResponse.getResponseDescription()
                                : GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage(),
                        null);
            }
        }
    }

    private String formatPurchaseAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return "0";
        }
        try {
            BigDecimal parsedAmount = new BigDecimal(amount.trim());
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
            return decimalFormat.format(parsedAmount.stripTrailingZeros());
        } catch (NumberFormatException ex) {
            return amount;
        }
    }

    private String generatePurchaseReferenceNumber() {
        return String.format("%06d", REFERENCE_RANDOM.nextInt(900000) + 100000);
    }

    private String maskPan(String pan) {
        if (pan == null || pan.trim().isEmpty()) {
            return "**";
        }

        String trimmedPan = pan.trim();
        int separatorIndex = trimmedPan.indexOf('=');
        if (separatorIndex < 0) {
            separatorIndex = trimmedPan.indexOf('D');
        }

        String extractedPan = separatorIndex >= 0 ? trimmedPan.substring(0, separatorIndex) : trimmedPan;
        String digitsOnlyPan = extractedPan.replaceAll("\\D", "");
        if (digitsOnlyPan.isEmpty()) {
            return "**";
        }

        String visibleDigits = digitsOnlyPan.length() <= 4
                ? digitsOnlyPan
                : digitsOnlyPan.substring(digitsOnlyPan.length() - 4);

        return "**" + visibleDigits;
    }


    public static CardTrackData extractCardData(String input) {

        CardTrackData dto = new CardTrackData();

        if (input == null || input.isEmpty()) {
            return dto;
        }

        try {
            // -------- TRACK 1 --------
            int t1Start = input.indexOf("TRACK1:");
            if (t1Start != -1) {
                int t1End = input.indexOf("||", t1Start);
                String track1 = input.substring(t1Start, t1End != -1 ? t1End : input.length())
                        .replace("TRACK1:", "")
                        .trim();

                if (track1.startsWith("B")) {
                    String[] parts = track1.split("\\^");

                    if (parts.length >= 3) {

                        // PAN
                        dto.setPan(parts[0].substring(1).trim());

                        // Name
                        String name = parts[1].trim();
                        if (name.contains("/")) {
                            String[] nameParts = name.split("/");
                            dto.setLastName(nameParts[0].trim());
                            dto.setFirstName(nameParts.length > 1 ? nameParts[1].trim() : null);
                        }

                        // Expiry + Service Code + Discretionary
                        String rest = parts[2].trim();
                        if (rest.length() >= 7) {
                            dto.setExpiry(rest.substring(0, 4));         // YYMM
                            dto.setServiceCode(rest.substring(4, 7));    // 226
                            dto.setDiscretionaryData(rest.substring(7)); // remaining
                        }
                    }
                }
            }

            // -------- TRACK 2 (fallback) --------
            int t2Start = input.indexOf("TRACK2:");
            if (t2Start != -1) {
                int t2End = input.indexOf("||", t2Start);
                String track2 = input.substring(t2Start, t2End != -1 ? t2End : input.length())
                        .replace("TRACK2:", "")
                        .trim();
                // ✅ STORE AS-IS
                dto.setTrack2data(track2);
                if (track2.contains("=")) {
                    String[] parts = track2.split("=");

                    if (parts.length == 2) {
                        String pan = parts[0];
                        String rest = parts[1];

                        if (rest.length() >= 7) {

                            if (dto.getPan() == null) {
                                dto.setPan(pan);
                            }
                            if (dto.getExpiry() == null) {
                                dto.setExpiry(rest.substring(0, 4));
                            }
                            if (dto.getServiceCode() == null) {
                                dto.setServiceCode(rest.substring(4, 7));
                            }
                            if (dto.getDiscretionaryData() == null) {
                                dto.setDiscretionaryData(rest.substring(7));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            // optional: log error
        }

        return dto;
    }

    @Override
    public HashMap<String, Object> cardTocardCp(CardToCardCpRequest cardToCardRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(cardToCardRequest.getAccountNo(),
                accountTypeAgent);
        if (tblAccount != null) {
            CardToCardCpRqst cardToCardRqst = new CardToCardCpRqst();
            cardToCardRqst.setStan(DateTools.generateStan());
            cardToCardRqst.setRrn(DateTools.generateStan() + cardToCardRqst.getStan());
            // Set simple fields
            cardToCardRqst.setPan(cardToCardRequest.getFromPan());
            cardToCardRqst.setMerchantType("6012");
            cardToCardRqst.setAmount(cardToCardRequest.getAmount());
            cardToCardRqst.setCardAcceptorIdentification("00000001");
            cardToCardRqst.setCardAcceptorCode("123456789101475");
            cardToCardRqst.setCvcPresent("000");
            cardToCardRqst.setPin(cardToCardRequest.getPin());
            cardToCardRqst.setTract2Data(cardToCardRequest.getTrack2data());
            cardToCardRqst.setCvc2(cardToCardRequest.getCvc());
            cardToCardRqst.setDateExpiration(cardToCardRequest.getExpiry());
            cardToCardRqst.setTransactionType("1");
            cardToCardRqst.setTerminalType("8");
            cardToCardRqst.setCard2Number(cardToCardRequest.getToPan());
            cardToCardRqst.setCurrencyCodeTransaction("971");
            cardToCardRqst.setCurrencyCodeCardBilling("971");
            cardToCardRqst.setNetworkReferenceNumber("");
            cardToCardRqst.setOtpPin(cardToCardRequest.getOtpPin());
            cardToCardRqst.setOriginalRrn(cardToCardRqst.getRrn());

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("S");
            pos.setCardholderAuthenticationCapability("0");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("5");
            pos.setCardholderPresenceIndicator("5");
            pos.setCardPresence("0");
            pos.setCardDataInputMode("T");
            pos.setCardholderAuthenticationMethod("0");
            pos.setCardholderAuthenticationEntity("0");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("0");

            cardToCardRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            cardToCardRqst.setCardAcceptorNameAndLocation(acceptor);
            String cardToCardResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cardToCardRqst,
                    cardToCardCpUrl);
            TransactionResponseDto<CardToCardResp> responseDto = mapper.readValue(cardToCardResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, CardToCardResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData().setFee(fee.toString());
                responseDto.getData().setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else if (responseDto != null
                    && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                // call proceedure here
                CardToCardProcRequest cardToCardProcRequest = new CardToCardProcRequest();
                cardToCardProcRequest.setFromCardNo(cardToCardRequest.getFromPan());
                cardToCardProcRequest.setFromBankName("");
                cardToCardProcRequest.setFromAccountTitle(cardToCardRequest.getFromCardTitle());
                cardToCardProcRequest.setToCardNo(cardToCardRequest.getToPan());
                cardToCardProcRequest.setToBankName("");
                cardToCardProcRequest.setToAccountTitle(cardToCardRequest.getToCardTitle());
                cardToCardProcRequest.setTransAmount(new BigDecimal(cardToCardRequest.getAmount()));
                cardToCardProcRequest.setAppUserId(userId.longValue());
                cardToCardProcRequest.setStan(cardToCardRqst.getStan());
                cardToCardProcRequest.setRrn(cardToCardRqst.getRrn());
                cardToCardProcRequest.setClientSecret(agentClientSecret);
                cardToCardProcRequest.setChannelCode(agentChannelCode);

                ProcResponse procResponse = procedureService.cardToCard(cardToCardProcRequest);
                if (procResponse != null && procResponse.getResponseCode().equals("0000")) {
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                            responseDto.getData());
                } else {
                    responseDto.getData()
                            .setResponseCode(procResponse.getResponseCode() != null ? procResponse.getResponseCode()
                                    : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
                    return commonService.getResponse(responseDto.getData().getResponseCode(), responseDto.getData());
                }
            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> cardBalanceInquiryCp(CardBalanceInquiryCpRequest cardBalanceInquiryRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAppUserId(userId);
        if (tblAccount != null) {
            BalanceInquiryRqst rqst = new BalanceInquiryRqst();

            rqst.setPan(cardBalanceInquiryRequest.getPan());
            rqst.setMerchantType("4411");
            rqst.setTrack2Data(cardBalanceInquiryRequest.getTrack2data());
            rqst.setPinData(cardBalanceInquiryRequest.getPin());
            rqst.setCardAcceptorIdentification("00000001");
            rqst.setCardAcceptorCode("123456789101475");
            rqst.setCvcPresent("000");
            rqst.setCvc2Data("");
            rqst.setDateExpiration(cardBalanceInquiryRequest.getDateExpiration());
            rqst.setTerminalType("2");
            rqst.setCurrencyCodeTransaction("971");

            // PointOfService
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");
            rqst.setPointOfService(pos);

            // CardAcceptorNameAndLocation
            CardAcceptorNameAndLocation loc = new CardAcceptorNameAndLocation();
            loc.setName("DFS");
            loc.setStreet("1234");
            loc.setCity("KABUL");
            loc.setState("KBL");
            loc.setCountry("AFG");
            loc.setPostalCode("123456789");
            rqst.setCardAcceptorNameAndLocation(loc);
            String walletToCardResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), rqst,
                    balanceInquiryUrl);
            TransactionResponseDto<BalanceInquiryResp> responseDto = mapper.readValue(walletToCardResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, BalanceInquiryResp.class));

            if (responseDto != null && responseDto.getCode().equals("000")) {
                CardBalanceInquiryResponse cardBalanceInquiryResponse = new CardBalanceInquiryResponse();
                cardBalanceInquiryResponse.setCurrencyAvailable(responseDto.getData().getCurrencyAvailable());
                cardBalanceInquiryResponse
                        .setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                        cardBalanceInquiryResponse);

            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }

        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> payBillCp(PayBillCpRequest payBillRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAppUserId(userId);
        if (tblAccount != null) {
            SavePaymentRqst savePaymentRqst = new SavePaymentRqst();
            savePaymentRqst.setStan(DateTools.generateStan());
            savePaymentRqst.setRrn(DateTools.generateStan() + savePaymentRqst.getStan());
            // Set simple fields
            savePaymentRqst.setPan(payBillRequest.getPan());
            savePaymentRqst.setMerchantType("6012");
            savePaymentRqst.setCardAcceptorIdentification("00000001");
            savePaymentRqst.setCardAcceptorCode("123456789101475");
            savePaymentRqst.setAmount(payBillRequest.getAmount());
            savePaymentRqst.setCvcPresent("001");
            savePaymentRqst.setCvc2(payBillRequest.getCvv());
            savePaymentRqst.setDateExpiration(payBillRequest.getExpiry());
            savePaymentRqst.setTransactionType("1");
            savePaymentRqst.setTransactionCurrencyCode("971");
            savePaymentRqst.setTerminalType("2");
            savePaymentRqst.setServiceId(payBillRequest.getServiceId());
            savePaymentRqst.setBillRefNo(payBillRequest.getBillRefNo());
            savePaymentRqst.setBillAmount(payBillRequest.getBillAmount());
            savePaymentRqst.setRequestId(payBillRequest.getRequestId());
            savePaymentRqst.setOtpPin(payBillRequest.getOtpPin());
            savePaymentRqst.setOriginalRrn(payBillRequest.getRrn());
            savePaymentRqst.setPin(payBillRequest.getPin());
            savePaymentRqst.setTrack2Data(payBillRequest.getTrack2data());

            // Build PointOfService object
            PointOfService pos = new PointOfService();
            pos.setCardDataInputCapability("5");
            pos.setCardholderAuthenticationCapability("1");
            pos.setCardCaptureCapability("0");
            pos.setOperatingEnvironment("1");
            pos.setCardholderPresenceIndicator("0");
            pos.setCardPresence("1");
            pos.setCardDataInputMode("2");
            pos.setCardholderAuthenticationMethod("5");
            pos.setCardholderAuthenticationEntity("4");
            pos.setCardDataOutputCapability("1");
            pos.setTerminalOutputCapability("4");
            pos.setPinCaptureCapability("6");

            savePaymentRqst.setPointOfService(pos);

            // Build CardAcceptorNameAndLocation object
            CardAcceptorNameAndLocation acceptor = new CardAcceptorNameAndLocation();
            acceptor.setName("DFS");
            acceptor.setStreet("1234");
            acceptor.setCity("KABUL");
            acceptor.setState("KBL");
            acceptor.setCountry("AFG");
            acceptor.setPostalCode("123456789");

            savePaymentRqst.setCardAcceptorNameAndLocation(acceptor);

            String billPaymentResponse = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), savePaymentRqst,
                    billPaymentUrl);
            TransactionResponseDto<SavePaymentResp> responseDto = mapper.readValue(billPaymentResponse,
                    mapper.getTypeFactory()
                            .constructParametricType(TransactionResponseDto.class, SavePaymentResp.class));

            if (responseDto != null && responseDto.getCode().equals("100")) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                responseDto.getData().setFee(fee.toString());
                responseDto.getData().setBillAmount(isoToNormal(responseDto.getData().getBillAmount()));
                responseDto.getData().setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("100", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else if (responseDto != null
                    && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                // calculate fee using proceedure
                BigDecimal fee = BigDecimal.ZERO;
                // call proceedure heresavePaymentRqst
                ProcResponse procResponseAcquirer;
                if (payBillRequest.getPan() != null && !payBillRequest.getPan().startsWith("900419")) {
                    BillPaymentAcquirerRequest billPaymentAcquirerRequest = new BillPaymentAcquirerRequest();
                    billPaymentAcquirerRequest.setClientSecret(agentClientSecret);
                    billPaymentAcquirerRequest.setChannelCode(agentChannelCode);
                    billPaymentAcquirerRequest.setFromCardNo(payBillRequest.getPan());
                    billPaymentAcquirerRequest.setFromCardTitle(payBillRequest.getCardTitle());
                    billPaymentAcquirerRequest.setUtilityCompanyCode(payBillRequest.getServiceId());
                    billPaymentAcquirerRequest.setUtilityConsumerNo(payBillRequest.getBillRefNo());
                    billPaymentAcquirerRequest.setTransAmount(new BigDecimal(payBillRequest.getBillAmount()));
                    billPaymentAcquirerRequest.setAppUserId(userId.longValue());
                    billPaymentAcquirerRequest.setStan(savePaymentRqst.getStan());
                    billPaymentAcquirerRequest.setRrn(savePaymentRqst.getRrn());
                    procResponseAcquirer = procedureService.billPaymentAcquirer(billPaymentAcquirerRequest);

                }
                responseDto.getData().setFee(fee.toString());
                responseDto.getData().setBillAmount(isoToNormal(responseDto.getData().getBillAmount()));
                responseDto.getData().setAvailableBalance(isoToNormal(responseDto.getData().getAvailableBalance()));
                responseDto.getData()
                        .setTimeLocalTransaction(formatDateTime(responseDto.getData().getTimeLocalTransaction()));
                return commonService.getResponseWithOutDB("000", GenericResponseCode.SUCCESS.getResponseMessage(),
                        responseDto.getData());
            } else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                        responseDto != null ? responseDto.getData() : null);
            }
        } else {
            return commonService.getResponse(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode(), null);
        }
    }
}
