package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.GenerateOtpResponse;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.Response;
import com.dfs.agentapp.dto.common.VerifyOtpRequest;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.AccountDetailService;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.service.ThirdPartyService;
import com.dfs.agentapp.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private TblAgentRepo tblAgentRepo;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private DocumentService documentService;
    @Value("${doc.complaint.code}")
    private String complaintDoc;
    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Autowired
    private TblComplaintRepo tblComplaintRepo;

    @Override
    public HashMap<String, Object> mpinVerifcation(MpinVerificationRequest mpinVerificationRequest, Request request) {
        TblAppUser tblAppUser = tblAppUserRepo.findByMobileNumber(aeSencryption.encryptwith256(mpinVerificationRequest.getMobileNumber()));
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        if (tblAppUser.getPassword().equals(aeSencryption.encryptwith256(mpinVerificationRequest.getMpin()))) {
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        } else {
            return commonService.getResponse(GenericResponseCode.WRONG_MPIN.getResponseCode(), null);
        }

    }


    @Override
    public HashMap<String, Object> getBalance(GetBalanceRequest getBalanceRequest, Request request) {
        TblAccount tblAccount = tblAccountRepo.findByMobileNoOrCnicAndAccountLevelCode(aeSencryption.encryptwith256(getBalanceRequest.getMobileNumber()), Constants.EMPTY, getBalanceRequest.getAccountLevelCode());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        GetBalanceResponse getBalanceResponse = new GetBalanceResponse(tblAccount.getCurrentBalance());

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), getBalanceResponse);

    }

    @Override
    public HashMap<String, Object> changeMpin(ChangeMpinRequest changeMpinRequest, BigDecimal userid) {
        TblAppUser tblAppUser = tblAppUserRepo.findByMobileNumber(aeSencryption.encryptwith256(changeMpinRequest.getMobileNumber()));
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        if (tblAppUser.getPassword().equals(aeSencryption.encryptwith256(changeMpinRequest.getCurrentMpin()))) {
            tblAppUser.setPassword(aeSencryption.encryptwith256(changeMpinRequest.getNewMpin()));
            tblAppUser.setPasswordUpdateFlag(Constants.YES);
            tblAppUserRepo.saveAndFlush(tblAppUser);
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        } else {
            return commonService.getResponse(GenericResponseCode.WRONG_MPIN.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> viewLimits(GetBalanceRequest getBalanceRequest, Request request) {
        TblAccount tblAccount = tblAccountRepo.findByMobileNoOrCnicAndAccountLevelCode(aeSencryption.encryptwith256(getBalanceRequest.getMobileNumber()), Constants.EMPTY, getBalanceRequest.getAccountLevelCode());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        Object limits = tblAccountRepo.viewLimits(tblAccount.getAccountId());
        if (limits == null) {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }
        Object[] result = (Object[]) limits;
        ViewLimitResponse viewLimitResponse = new ViewLimitResponse(
                ((Number) result[0]).longValue(),
                getBigDecimal(result[1]),
                getBigDecimal(result[2]),
                getBigDecimal(result[3]),
                getBigDecimal(result[4]),
                getBigDecimal(result[5]),
                getBigDecimal(result[6]),
                getBigDecimal(result[7]),
                getBigDecimal(result[8]),
                getBigDecimal(result[9]),
                getBigDecimal(result[10]),
                getBigDecimal(result[11]),
                getBigDecimal(result[12])
        );


        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), viewLimitResponse);
    }

    @Override
    public HashMap<String, Object> miniStatement(MiniStatementRequest miniStatementRequest, Request request) throws ParseException {
        List<MiniStatement> miniStatements = new ArrayList<MiniStatement>();

        List<Object> ministatment = null;
        TblAccount tblAccount = tblAccountRepo.findByMobileNoOrCnicAndAccountLevelCode(aeSencryption.encryptwith256(miniStatementRequest.getMobileNumber()), Constants.EMPTY, miniStatementRequest.getAccountLevelCode());
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
        miniStatements.addAll(
                ministatment.parallelStream()
                        .map(record -> {
                            Object[] row = (Object[]) record;
                            MiniStatement statement = new MiniStatement();

                            // Use helper methods
                            statement.setFAccountId(getBigDecimal(row[0]));
                            statement.setFAccountType(row[1] != null ? row[1].toString() : null);
                            statement.setFromAccountNo(getString(row[2]));
                            statement.setFromAccountTitle(aeSencryption.decrypt(getString(row[3])));
                            statement.setTaccountId(getBigDecimal(row[4]));
                            statement.setTAccountType(row[5] != null ? row[5].toString() : null);
                            statement.setToAccountNo(getString(row[6]));
                            statement.setToAccountTitle(aeSencryption.decrypt(getString(row[7])));
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

                            // Set rowkey safely in parallel processing
                            statement.setRowkey(count.getAndIncrement());
                            return statement;
                        })
                        .collect(Collectors.toList())
        );

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
    public HashMap<String, Object> updateEmail(UpdateEmailRequest updateEmailRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HashMap<String, Object> resp;
        Response response;
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(updateEmailRequest.getMobileNumber());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        List<TblAgent> tblAgents = tblAgentRepo.findByEmail(aeSencryption.encryptwith256(updateEmailRequest.getEmail()));
        if (tblAgents != null && !tblAgents.isEmpty()) {
            throw new CustomDataNotFoundException(GenericResponseCode.EMAIL_ALREADY_EXISTS.getResponseCode());
        }
        response = thirdPartyService.generateOtp(updateEmailRequest.getMobileNumber(), updateEmailRequest.getEmail(), "EPU", "S", "UEM", "A", request, httpServletRequest.getHeader(Constants.AUTHORIZATION));
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
            TblAgent tblAgent = tblAgentRepo.findByAgentId(tblAccount.getTblAgent().getAgentId());
            tblAgent.setEmail(aeSencryption.encryptwith256(verifyOtpRequest.getEmail()));
            tblAgentRepo.save(tblAgent);
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

}
