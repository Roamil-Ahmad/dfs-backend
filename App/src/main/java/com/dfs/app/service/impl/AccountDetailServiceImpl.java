package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.*;
import com.dfs.app.dto.common.GenerateOtpResponse;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.Response;
import com.dfs.app.dto.common.VerifyOtpRequest;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.AccountDetailService;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.DocumentService;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.AESencryption;
import com.dfs.app.util.Constants;
import com.dfs.app.util.CustomDataNotFoundException;
import com.dfs.app.util.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jasperreports.engine.JRException;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AccountDetailServiceImpl extends HelperClass implements AccountDetailService {
    @Autowired
    private TblAppUserRepo tblAppUserRepo;

    @Autowired
    private CommonService commonService;

    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Value("${doc.complaint.code}")
    private String complaintDoc;
    @Autowired
    private TblComplaintRepo tblComplaintRepo;
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public HashMap<String, Object> mpinVerifcation(MpinVerificationRequest mpinVerificationRequest, Request request) {
        TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(mpinVerificationRequest.getMobileNumber());
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        if (tblAppUser.getPassword().equals(aeSencryption.encryptwith256(mpinVerificationRequest.getMpin()))) {
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        } else {
            return commonService.getResponse(GenericResponseCode.WRONG_MPIN.getResponseCode(), null);
        }

    }


    @Override
    public HashMap<String, Object> getBalance(GetBalanceRequest getBalanceRequest, Request request) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoOrCnicAndAccountLevelCode(getBalanceRequest.getMobileNumber(), Constants.EMPTY, getBalanceRequest.getAccountLevelCode());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        GetBalanceResponse getBalanceResponse = new GetBalanceResponse(tblAccount.getCurrentBalance());

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), getBalanceResponse);

    }

    @Override
    public HashMap<String, Object> changeMpin(ChangeMpinRequest changeMpinRequest, BigDecimal userid) {
        TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(changeMpinRequest.getMobileNumber());
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        // The 3-changes-per-day throttle is not enforced here any more. It counted rows in
        // barkatpay_audit.tbl_app_user, an Afghanistan-era audit schema that does not exist in the
        // DFS database, so the query failed with ORA-00942 and no customer could change their MPIN
        // at all. DFS has no audit trail of password changes to count instead - TBL_APP_USER keeps
        // only current state, and the audit triggers are absent. Restore the limit once the DB team
        // provides a source; AgentApp has never had this check.
        if (tblAppUser.getPassword().equals(aeSencryption.encryptwith256(changeMpinRequest.getCurrentMpin()))) {
            tblAppUser.setPassword(aeSencryption.encryptwith256(changeMpinRequest.getNewMpin()));
            tblAppUserRepo.saveAndFlush(tblAppUser);
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        }
        if (tblAppUser.getPassword().equals(aeSencryption.encryptwith256(changeMpinRequest.getNewMpin()))) {
            return commonService.getResponse(GenericResponseCode.SAME_MPIN.getResponseCode(), null);
        } else {
            return commonService.getResponse(GenericResponseCode.WRONG_MPIN.getResponseCode(), null);
        }
    }


    @Override
    public HashMap<String, Object> viewLimits(GetBalanceRequest getBalanceRequest, Request request) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoOrCnicAndAccountLevelCode(getBalanceRequest.getMobileNumber(), Constants.EMPTY, getBalanceRequest.getAccountLevelCode());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        Object limits = tblAccountRepo.viewLimits(tblAccount.getAccountId());
        if (limits == null) {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }
        Object[] result = (Object[]) limits;
        ViewLimitResponse viewLimitResponse = new ViewLimitResponse(((Number) result[0]).longValue(), getBigDecimal(result[1]), getBigDecimal(result[2]), getBigDecimal(result[3]), getBigDecimal(result[4]), getBigDecimal(result[5]), getBigDecimal(result[6]), getBigDecimal(result[7]), getBigDecimal(result[8]), getBigDecimal(result[9]), getBigDecimal(result[10]), getBigDecimal(result[11]), getBigDecimal(result[12]));


        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), viewLimitResponse);
    }

    @Override
    public HashMap<String, Object> miniStatement(MiniStatementRequest miniStatementRequest, Request request) throws ParseException {
        List<MiniStatement> miniStatements = new ArrayList<MiniStatement>();

        List<Object> ministatment = null;
        TblAccount tblAccount = tblAccountRepo.findByAccountNoOrCnicAndAccountLevelCode(miniStatementRequest.getMobileNumber(), Constants.EMPTY, miniStatementRequest.getAccountLevelCode());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }

        if (!miniStatementRequest.getFromDate().isEmpty() && !miniStatementRequest.getToDate().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date dateFrom = formatter.parse(miniStatementRequest.getFromDate());
            Date dateTO = formatter.parse(miniStatementRequest.getToDate());

            long diffDays = (dateTO.getTime() - dateFrom.getTime()) / (24 * 60 * 60 * 1000);

            if (diffDays > 180) {
                throw new CustomDataNotFoundException(GenericResponseCode.DAY_MUST_BE_WITHIN_THIRTY_DAYS.getResponseCode());
            }
        }
        if (miniStatementRequest.getFromDate().isEmpty() && miniStatementRequest.getToDate().isEmpty()) {
            ministatment = tblAccountRepo.miniStatementFetchNineRecord(tblAccount.getAccountId(), miniStatementRequest.getFromDate(), miniStatementRequest.getToDate());
        } else {
            ministatment = tblAccountRepo.miniStatement(tblAccount.getAccountId(), miniStatementRequest.getFromDate(), miniStatementRequest.getToDate());
        }

        if (ministatment == null || ministatment.isEmpty()) {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }
        AtomicInteger count = new AtomicInteger();
        miniStatements.addAll(ministatment.parallelStream().map(record -> {
            Object[] row = (Object[]) record;
            MiniStatement statement = new MiniStatement();

            // Use helper methods
            statement.setFAccountId(getBigDecimal(row[0]));
            statement.setFAccountType(row[1] != null ? row[1].toString() : null);
            statement.setFromAccountNo(getString(row[2]));
            statement.setFromAccountTitle((getString(row[3])));
            statement.setTaccountId(getBigDecimal(row[4]));
            statement.setTAccountType(row[5] != null ? row[5].toString() : null);
            statement.setToAccountNo(getString(row[6]));
            statement.setToAccountTitle((getString(row[7])));
            statement.setTransDate((Date) row[8]);
            statement.setValueDate(getString(row[9]));

            BigDecimal refNum = getBigDecimal(row[10]);
            statement.setRefNum(refNum);
            if (refNum != null) {
                statement.setTransRefnum(refNum.toString());
            }

            statement.setTransDocsCode(getString(row[11]));
            statement.setTransDocsDescr(getString(row[12]));
            statement.setOpeningbalance(getBigDecimal(row[13]));
            statement.setClosingBalance(getBigDecimal(row[14]));
            statement.setTxnAmt(getBigDecimal(row[15]));
            statement.setAmountType(getString(row[16]));
            statement.setAmountType2(getString(row[17]));
            statement.setChargesId(getBigDecimal(row[18]));
            statement.setComments(getString(row[19]));
            statement.setStan(getString(row[20]));
            statement.setRrn(getString(row[21]));
            statement.setSourceBank(getString(row[22]));
            statement.setDestinationBank(getString(row[23]));
            statement.setChannel(getString(row[24]));
            statement.setFeeAmt(getBigDecimal(row[25]));
            statement.setCategoryId(getBigDecimal(row[35]));

            // Set rowkey safely in parallel processing
            statement.setRowkey(count.getAndIncrement());
            return statement;
        }).collect(Collectors.toList()));

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), miniStatements);
    }

    // Helper method for BigDecimal to avoid repetitive checks
    BigDecimal getBigDecimal(Object obj) {
        return obj instanceof BigDecimal ? (BigDecimal) obj : null;
    }

    // Helper method for String to avoid repetitive checks
    String getString(Object obj) {
        return obj instanceof String ? (String) obj : null;
    }

    @Override
    public HashMap<String, Object> emailAccountStatement(AccountStatementRequest accountStatementRequest, Request request) throws JRException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, MessagingException {
        commonService.generateAccountStatement(accountStatementRequest);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> updateEmail(UpdateEmailRequest updateEmailRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HashMap<String, Object> resp;
        Response response;
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(updateEmailRequest.getMobileNumber());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        List<TblCustomer> tblCustomer = tblCustomerRepo.findByEmail(aeSencryption.encryptwith256(updateEmailRequest.getEmail()));
        if (tblCustomer != null && !tblCustomer.isEmpty()) {
            throw new CustomDataNotFoundException(GenericResponseCode.EMAIL_ALREADY_EXISTS.getResponseCode());
        }
        response = thirdPartyService.generateOtp(updateEmailRequest.getMobileNumber(), updateEmailRequest.getEmail(), "EPU", "S", "UEM", "C", request, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        if (response.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
            GenerateOtpResponse generateOtpResponse = fromJson(convertObjecttoJson(response.getData()), GenerateOtpResponse.class);
            resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), generateOtpResponse);

        } else {
            resp = commonService.getResponse(response.getResponsecode(), null);

        }
        return resp;
    }

    @Override
    public HashMap<String, Object> verifyUpdateEmail(VerifyOtpRequest verifyOtpRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HashMap<String, Object> resp = null;
        Response response = thirdPartyService.verifyOtp(verifyOtpRequest, request, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        if (GenericResponseCode.SUCCESS.getResponseCode().equalsIgnoreCase(response.getResponsecode())) {
            TblAccount tblAccount = tblAccountRepo.findByAccountNo(verifyOtpRequest.getMobileNumber());
            if (tblAccount == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
            }
            TblCustomer tblCustomer = tblCustomerRepo.findByCustomerId(tblAccount.getTblCustomer().getCustomerId());
            tblCustomer.setEmail(aeSencryption.encryptwith256(verifyOtpRequest.getEmail()));
            tblCustomerRepo.save(tblCustomer);
            resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        } else {
            resp = commonService.getResponse(response.getResponsecode(), null);
        }

        return resp;
    }

    @Override
    public HashMap<String, Object> sumbitComplaint(ComplaintRequest complaintRequest, Request request, BigDecimal userId) {
        TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(complaintRequest.getMobileNumber());
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        if (complaintRequest.getDocuments() != null && !complaintRequest.getDocuments().isEmpty()) {
            UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
            uploadDocumentRequest.setDocuments(complaintRequest.getDocuments());
            uploadDocumentRequest.setMobileNumber(complaintRequest.getMobileNumber());
            uploadDocumentRequest.setAccountLevelCode(complaintRequest.getAccountLevelCode());
            documentService.uploadDocument(uploadDocumentRequest, request, userId);
        }
        TblDocument tblDocument = tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(tblAppUser.getAppUserId(), complaintDoc, complaintRequest.getAccountLevelCode());
        TblComplaint tblComplaint = new TblComplaint();
        tblComplaint.setComplaintDocumentId(tblDocument != null ? new BigDecimal(tblDocument.getDocumentId()) : null);
        tblComplaint.setCreatedate(new Date());
        tblComplaint.setCreateuser(userId);
        tblComplaint.setAppUserId(new BigDecimal(tblAppUser.getAppUserId()));
        tblComplaint.setContactOption(complaintRequest.getContactOption());
        tblComplaint.setIssueDescr(complaintRequest.getIssueDecr());
        tblComplaint.setStatus("P");
        tblComplaint.setTicketId(getRandomTicketId());
        LkpIssueType lkpIssueType = new LkpIssueType();
        lkpIssueType.setIssueTypeId(complaintRequest.getIssueTypeId());
        tblComplaint.setLkpIssueType(lkpIssueType);
        tblComplaint = tblComplaintRepo.save(tblComplaint);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblComplaint);
    }

    @Override
    public HashMap<String, Object> getContactList(ContactlistDTO contactlistDTO, Request request, BigDecimal userId) throws SQLException {
//        List<String> validContacts=getValidContacts(contactlistDTO.getContactList());
        List<String> contactList = getContactListStatus(contactlistDTO.getContactList(), "W");
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), contactList);
    }

    private List<String> getValidContacts(List<String> contactList) {
        Set<String> selectedContacts = new LinkedHashSet<>();

        for (String contact : contactList) {

            contact = contact.replace("(", "");
            contact = contact.replace(")", "");

            if (contact.startsWith("0092")) {
                contact = contact.replace("0092", "0");
            }
            if (contact.startsWith("+92")) {
                contact = contact.replace("+92", "0");
            }
            if (isValidMobileNumber(contact)) {
                selectedContacts.add(contact);
            }
        }

        List<String> uniqueContacts = new ArrayList<>(selectedContacts);

        return uniqueContacts;
    }

    public boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber != null
                && mobileNumber.matches("^03\\d{9}$")) {
            return true;
        }
        return false;
    }

    @Transactional
    public List<String> getContactListStatus(List<String> mobileNumbers, String WalletOrDebit) throws SQLException {
        List<String> walletAccResponseDTO = new ArrayList<>();

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("PROC_CHK_WALLET")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_MOBILE_NO", Types.ARRAY, "T_MOBILE_NO"),
                        new SqlParameter("P_DC_ACCOUNT", Types.VARCHAR),
                        new SqlOutParameter("P_ACCOUNT_YN", OracleTypes.ARRAY, "T_ACCOUNT_YN"),
                        new SqlOutParameter("P_STATUS", Types.NUMERIC),
                        new SqlOutParameter("P_STATUS_DESCR", Types.VARCHAR));

        Map<String, Object> inParams = new HashMap<>();

        int listSize = mobileNumbers.size();

        String[] mobileArray = new String[listSize];
        for (int i = 0; i < listSize; i++) {
            mobileArray[i] = mobileNumbers.get(i);
        }
        Connection conn = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);

            inParams.put("P_MOBILE_NO", createArray(oracleConn, mobileArray));
            inParams.put("P_DC_ACCOUNT", WalletOrDebit);


            Map<String, Object> result = simpleJdbcCall.execute(inParams);

            // Access output parameters
            BigDecimal status = (BigDecimal) result.get("P_STATUS");
            String statusDescr = (String) result.get("P_STATUS_DESCR");
            if (status.compareTo(BigDecimal.ZERO) != 0) {
                // throw new CustomDataNotFoundException(statusDescr);

                String[] yesNo = (String[]) ((Array) result.get("P_ACCOUNT_YN")).getArray();

                for (int i = 0; i < mobileNumbers.size(); i++) {
                    if (yesNo[i].equals("Y")) {
                        walletAccResponseDTO.add(mobileNumbers.get(i));
                    }
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // log or handle the exception as needed
                    e.printStackTrace();
                }
            }
        }
        return walletAccResponseDTO;

    }

    private ARRAY createArray(Connection connection, String[] data) throws SQLException {
        ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("T_MOBILE_NO", connection);
        return new ARRAY(descriptor, connection, data);
    }

}
