package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.repo.*;
import com.dfs.backoffice.service.AgentOnBoardingService;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.service.NotificationService;
import com.dfs.backoffice.service.QrService;
import com.dfs.backoffice.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AgentOnBoardingServiceImpl extends HelperClass implements AgentOnBoardingService {

    @Autowired
    private TblAgentRepo tblAgentRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private LkpRegistrationTypeRepo lkpRegistrationTypeRepo;
    @Autowired
    private LkpAccountStatusRepo lkpAccountStatusRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private LkpAccountTypeRepo lkpAccountTypeRepo;
    @Value("${account.level.three}")
    private String accountLevelThree;
    @Value("${account.reg.type.code}")
    private String accountRegTypeCode;
    @Value("${account.status.active}")
    private String accountStatusActive;
    @Value("${account.type.code}")
    private String accountTypeAgent;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private QrService qrService;
    @Autowired
    private NotificationService notificationService;
    @Value("${bank.code}")
    private String barakatCode;
    @Autowired
    private LkpProvinceRepo lkpProvinceRepo;
    @Autowired
    private LkpDistrictRepo lkpDistrictRepo;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public Response createAgentAccount(CreateAgentAccountRequest createAgentAccountRequest, HttpServletRequest request) {
        Response response = new Response();
        List<TblAgent> checkAgentExistance = tblAgentRepo.checkAgentExistance(encryptWithAes(createAgentAccountRequest.getAgentNidNo()), encryptWithAes(createAgentAccountRequest.getAgentMobileNo()));
        if (!checkAgentExistance.isEmpty()) {
            throw new CustomException(GenericResponseCode.AGENT_CNIC_MOBILE_ALREADY_REG.getResponseCode());
        }
        TblAccount tblAccount = tblAccountRepo.checkAccountExistence(createAgentAccountRequest.getAgentMobileNo());
        if (tblAccount != null) {
            throw new CustomException(GenericResponseCode.AGENT_CNIC_MOBILE_ALREADY_REG.getResponseCode());
        }
        boolean blockedStatus = commonService.checkBlockListStatus(Long.parseLong(createAgentAccountRequest.getAgentNidNo()));
        if (blockedStatus) {
            throw new CustomException(GenericResponseCode.AGENT_BLACKLISTED.getResponseCode());
        }
        TblAppUser tblAppUserNameExist = tblAppUserRepo.findAgentUsername(encryptWithAes(createAgentAccountRequest.getAgentMobileNo()));
        if (tblAppUserNameExist != null) {
            throw new CustomException(GenericResponseCode.AGENT_CNIC_MOBILE_ALREADY_REG.getResponseCode());
        }
        TblAgent tblAgent = new TblAgent();

        String password = autoGenerate("Numeric", 4);
        String encPassword = encryptWithAes(password);
        String msg = "Your Account has been created successfully. Your login password/ MPIN is " + password;
        tblAgent.setPermanentAddress(encryptWithAes(createAgentAccountRequest.getAgentAddress()));
        tblAgent.setNidNo(encryptWithAes(createAgentAccountRequest.getAgentNidNo()));
        tblAgent.setFatherHusbandName(encryptWithAes(createAgentAccountRequest.getFatherHusbandName()));
        tblAgent.setMobile(encryptWithAes(createAgentAccountRequest.getAgentMobileNo()));
        tblAgent.setName(encryptWithAes(createAgentAccountRequest.getAgentName()));
        tblAgent.setNok(createAgentAccountRequest.getNok());
        tblAgent.setOccupation(createAgentAccountRequest.getOccupation());
        tblAgent.setPhone(createAgentAccountRequest.getAgentTelephoneNo());
        tblAgent.setResidentialAddress(encryptWithAes(createAgentAccountRequest.getResidentialAddress()));
        tblAgent.setIsActive(createAgentAccountRequest.getIsActive());
        tblAgent.setEmail(encryptWithAes(createAgentAccountRequest.getEmail()));
        tblAgent.setAgentSms(msg);
        List<TblAgentCommissionDistribution> tblAgentCommissionDistributions = new ArrayList<>();
        if (!isNullOrEmpty(createAgentAccountRequest.getParentAgentid())) {
            TblAgent parentAgent = new TblAgent();
            tblAgent.setAgentType("C");
            parentAgent.setAgentId(Long.parseLong(createAgentAccountRequest.getParentAgentid()));
            tblAgent.setTblAgent(parentAgent);

            int countAgentLevel = 1;
            for (int i = 0; i < createAgentAccountRequest.getTblAgentCommissionDistributions().size(); i++) {
                TblAgentCommissionDistribution tblAgentCommissionDistribution = new TblAgentCommissionDistribution();
                tblAgentCommissionDistribution.setTblAgent(tblAgent);
                tblAgentCommissionDistribution.setStatus(Constants.YES);
                tblAgentCommissionDistribution.setAgentLevel(new BigDecimal(countAgentLevel++));
                tblAgentCommissionDistribution.setCommissionPercentage(new BigDecimal(createAgentAccountRequest.getTblAgentCommissionDistributions().get(i).getCommissionPercentage()));
                tblAgentCommissionDistribution.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
                tblAgentCommissionDistribution.setCreatedate(new Date());
                tblAgentCommissionDistributions.add(tblAgentCommissionDistribution);
            }
        } else {
            tblAgent.setAgentType("P");
            TblAgentCommissionDistribution tblAgentCommissionDistribution = new TblAgentCommissionDistribution();
            tblAgentCommissionDistribution.setTblAgent(tblAgent);
            tblAgentCommissionDistribution.setStatus(Constants.YES);
            tblAgentCommissionDistribution.setAgentLevel(new BigDecimal(1));
            tblAgentCommissionDistribution.setCommissionPercentage(new BigDecimal(100));
            tblAgentCommissionDistribution.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            tblAgentCommissionDistribution.setCreatedate(new Date());
            tblAgentCommissionDistributions.add(tblAgentCommissionDistribution);
        }
        TblAgentClass tblAgentClass = new TblAgentClass();
        if (!isNullOrEmpty(createAgentAccountRequest.getCityId())) {
            long cityId = Long.parseLong(createAgentAccountRequest.getCityId());
            if (cityId > 0) {
                tblAgent.setCityId(new BigDecimal(cityId));
            }
        }

        LkpProvince lkpProvince = lkpProvinceRepo.findByProvinceIdAndIsActive(Long.parseLong(createAgentAccountRequest.getProvinceId()), Constants.YES);
        if (lkpProvince == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.PROVINCE_NOT_FOUND.getResponseCode());
        }
        LkpDistrict lkpDistrict = lkpDistrictRepo.findByDistrictIdAndIsActive(Long.parseLong(createAgentAccountRequest.getDistrictId()), Constants.YES);
        if (lkpDistrict == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.DISTRICT_NOT_FOUND.getResponseCode());
        }
        tblAgent.setLkpDistrict(lkpDistrict);
        tblAgent.setLkpProvince(lkpProvince);


        tblAgentClass.setAgentClassId(Long.parseLong(createAgentAccountRequest.getAgentClassId()));
        tblAgent.setAgentLevel(new BigDecimal(createAgentAccountRequest.getAgentLevel()));
        tblAgent.setStatusId(new BigDecimal(2));
        tblAgent.setNidIssueDate(createAgentAccountRequest.getNidIssueDate());
        tblAgent.setNidExpiryDate(createAgentAccountRequest.getNidExpiryDate());
        tblAgent.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblAgent.setCreatedate(new Date());
        tblAgentRepo.save(tblAgent);

        tblAccount = new TblAccount();
        tblAccount.setAgentAccount("Y");

        LkpRegistrationType lkpRegistrationType = lkpRegistrationTypeRepo.findByRegistrationTypeCode(accountRegTypeCode);
        tblAccount.setLkpRegistrationType(lkpRegistrationType);
        LkpAccountStatus lkpAccountStatus = lkpAccountStatusRepo.findByAccountStatusCode(accountStatusActive);
        tblAccount.setLkpAccountStatus(lkpAccountStatus);
        TblAccountLevel tblAccountLevel = tblAccountLevelRepo.findByAccountLevelCode(accountLevelThree);
        tblAccount.setTblAccountLevel(tblAccountLevel);
        LkpAccountType lkpAccountType = lkpAccountTypeRepo.findByAccountTypeCode(accountTypeAgent);
        tblAccount.setLkpAccountType(lkpAccountType);
        tblAccount.setTblAgent(tblAgent);
        tblAccount.setAccountNo(createAgentAccountRequest.getAgentMobileNo());
        tblAccount.setAccountTitle(encryptWithAes(createAgentAccountRequest.getAgentName()));
        tblAccount.setBalanceDate(new Date());
        tblAccount.setBranchAccount("N");
        tblAccount.setCreatedate(new Date());
        tblAccount.setMobileNo(createAgentAccountRequest.getAgentMobileNo());
        tblAccount.setCreateuser(BigDecimal.ONE);
        tblAccount.setCurrentBalance(BigDecimal.ZERO);
        tblAccount.setIsActive("Y");
        tblAccount.setIban(IbanGenerator.generateIban(createAgentAccountRequest.getAgentMobileNo()));
        tblAccount.setDailyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setDailyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setMonthlyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setMonthlyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setYearlyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setYearlyAmtLimitDr(BigDecimal.ZERO);
        qrService.generateStaticQrForP2P(tblAccount);
        TblAppUser tblAppUser = tblAppUserRepo.findByAgentId(new BigDecimal(tblAgent.getAgentId()));
        tblAppUser = tblAppUser == null ? new TblAppUser() : tblAppUser;
        tblAppUser.setCreatedate(new Date());
        tblAppUser.setCreateuser(BigDecimal.ONE);
        tblAppUser.setAgentId(new BigDecimal(tblAgent.getAgentId()));
        tblAppUser.setUsername(encryptWithAes(createAgentAccountRequest.getAgentMobileNo()));
        tblAppUser.setMobileNo(encryptWithAes(createAgentAccountRequest.getAgentMobileNo()));
        tblAppUser.setPassword(encPassword);
        tblAppUser.setPasswordUpdateFlag("N");
        tblAppUserRepo.saveAndFlush(tblAppUser);
        if (tblAccount.getAccountId() > 0) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode());
            TblSmsMessage message = new TblSmsMessage();
            message.setMessage(msg);
            message.setMobileNo(tblAccount.getMobileNo());
            message.setSendFlag("0");
            message.setTransHeadId(null);
            message.setCreateuser(new BigDecimal(tblAppUser.getAppUserId()));
            commonService.saveSmSMessage(message);
            GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
            generateNotificationRequest.setEmail(tblAgent.getEmail());
            generateNotificationRequest.setSubject("DFS Agent Email");
            generateNotificationRequest.setSms(msg);
            generateNotificationRequest.setType("E");
            notificationService.notify(generateNotificationRequest, new BigDecimal(tblAppUser.getAppUserId()));
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.GENERAL_PROCESSING_ERROR.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateAgentAccount(CreateAgentAccountRequest createAgentAccountRequest, HttpServletRequest request) {
        Response response = new Response();
        TblAgent tblAgent = tblAgentRepo.findById(createAgentAccountRequest.getAgentid()).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        tblAgent.setNok(createAgentAccountRequest.getNok());
        tblAgent.setOccupation(createAgentAccountRequest.getOccupation());
        tblAgent.setPhone(createAgentAccountRequest.getAgentTelephoneNo());
        tblAgent.setResidentialAddress(encryptWithAes(createAgentAccountRequest.getResidentialAddress()));
        tblAgent.setFatherHusbandName(encryptWithAes(createAgentAccountRequest.getFatherHusbandName()));
        tblAgent.setEmail(encryptWithAes(createAgentAccountRequest.getEmail()));
        tblAgent.setLastupdatedate(new Date());
        tblAgent.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblAgent.setUpdateindex(tblAgent.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(tblAgent.getUpdateindex().intValue() + 1));
        tblAgentRepo.save(tblAgent);
        setResponse(response, Constants.ONE, null, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        return response;
    }

    @Override
    public TblAgent getAgentById(String agentId) {
        return tblAgentRepo.findById(Long.parseLong(agentId)).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }

    @Override
    public List<TblAgent> getAllAgents(SearchAgent searchAgent) {
        String dateFromInput = null;
        String dateToInput = null;
        if (searchAgent.getFromDate() != null && !(searchAgent.getFromDate().equals(""))) {
            dateFromInput = searchAgent.getFromDate() + " 00:00:00";
        }
        if (searchAgent.getToDate() != null && !(searchAgent.getToDate().equals(""))) {
            dateToInput = searchAgent.getToDate() + " 23:59:59";
        }
        if (!isNullOrEmpty(searchAgent.getMobileNo())) {
            searchAgent.setMobileNo(encryptWithAes(searchAgent.getMobileNo()));
        }
        if (!isNullOrEmpty(searchAgent.getCnic())) {
            searchAgent.setCnic(encryptWithAes(searchAgent.getCnic()));
        }
        return tblAgentRepo.getAllAgents(searchAgent, dateFromInput, dateToInput);
    }

    @Override
    public Response agentApproval(AgentApprovalRequest agentApprovalRequest, HttpServletRequest request) throws JsonProcessingException {
        Request jsonRequest = new Request();
        Response response = new Response();
        ObjectMapper objectMapper = new ObjectMapper();
        TblAppUser tblAppUser = tblAppUserRepo.findById(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)).longValue()).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        String mcAction = mcAction(jsonRequest, request.getHeader(Constants.AUTHORIZATION), agentApprovalRequest.getMcActionRequest(), tblAppUser.getUserId());
        if (mcAction != null) {
            Response response2 = objectMapper.readValue(mcAction, Response.class);
            McActionResponse mcResponse = objectMapper.readValue(Objects.requireNonNull(convertObjecttoJson(response2.getPayload())), McActionResponse.class);
            if (mcResponse != null && agentApprovalRequest.getMcActionRequest().getAction().equals("2") && Constants.A.equals(mcResponse.getRequestStatus()) && mcResponse.getStatus() == 1) {
                response = agentApprovalCheckerAction(request, response, agentApprovalRequest, mcResponse);
            } else {
                setResponse(response, Constants.ONE, mcResponse, GenericResponseCode.SUCCESS.getResponseCode(), mcResponse != null ? mcResponse.getStatusDecsr() : GenericResponseCode.GENERAL_PROCESSING_ERROR.getResponseMessage());
            }
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    private Response agentApprovalCheckerAction(HttpServletRequest request, Response response, AgentApprovalRequest entityActionRequest, McActionResponse mcResponse) {
        entityActionRequest.setStatusId(new BigDecimal(2));
        TblAgent tblAgent = updateAgentAccountAfterApproval(entityActionRequest.getAgentId(), entityActionRequest.getStatusId(), new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (!isNullOrEmpty(tblAgent)) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode(), mcResponse != null ? mcResponse.getStatusDecsr() : Constants.GENERAL_PROCESSING_ERROR);
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    private TblAgent updateAgentAccountAfterApproval(Long agentId, BigDecimal statusId, BigDecimal userId) {
        TblAgent tblAgent = tblAgentRepo.findById(agentId).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        tblAgent.setStatusId(statusId);
        tblAgent.setIsActive(Constants.YES);
        tblAgent.setLastupdatedate(new Date());
        tblAgent.setLastupdateuser(userId);
        tblAgent.setUpdateindex(tblAgent.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(tblAgent.getUpdateindex().intValue() + 1));
        return tblAgentRepo.save(tblAgent);
    }
}
