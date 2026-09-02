package com.workflow.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.workflow.controller.AbstractApi;
import com.workflow.dto.Response;
import com.workflow.dto.UseCaseDetailRequest;
import com.workflow.dto.UseCaseRequest;
import com.workflow.dto.UseCaseSearch;
import com.workflow.modal.*;
import com.workflow.repo.LkpMcFormNameRepo;
import com.workflow.repo.TblMcConfigDetailRepo;
import com.workflow.repo.TblMcConfigRepo;
import com.workflow.repo.TblMcRequestRepo;
import com.workflow.service.UseCaseManagementService;
import com.workflow.utils.Constants;
import com.workflow.utils.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class UseCaseManagementImpl extends AbstractApi implements UseCaseManagementService {
    @Autowired
    private TblMcConfigRepo tblMcConfigRepo;
    @Autowired
    private TblMcConfigDetailRepo tblMcConfigDetailRepo;
    @PersistenceContext
    EntityManager em;
    @Autowired
    private LkpMcFormNameRepo lkpMcFormNameRepo;

    @Override
    public TblMcConfig getUseCaseById(long useCaseId) {
        return tblMcConfigRepo.findById(useCaseId).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public TblMcConfig saveusecase(UseCaseRequest useCaseRequest, BigDecimal userId) {
        TblMcConfig tblMcConfig = new TblMcConfig();
        tblMcConfig.setTableName(useCaseRequest.getProcessName().getTableName());
        tblMcConfig.setFormName(useCaseRequest.getProcessName().getFormName());
        tblMcConfig.setRequestType(useCaseRequest.getProcessName().getRequestType());
        tblMcConfig.setIsActive("Y");
        tblMcConfig.setCreatedate(new Date());
        tblMcConfig.setCreateuser(userId);
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(useCaseRequest.getStatusId());
        tblMcConfig.setLkpStatus(lkpStatus);
        tblMcConfig.setConfigName(useCaseRequest.getUseCaseName());
        tblMcConfig.setConfigDescription(useCaseRequest.getUseCaseDescr());
        tblMcConfig.setViewDetailUrl(useCaseRequest.getProcessName().getViewDetailUrl());
        tblMcConfig.setEditDetailUrl(useCaseRequest.getProcessName().getEditDetailUrl());
        tblMcConfig.setComments(useCaseRequest.getUseCaseComments());
        tblMcConfig = tblMcConfigRepo.save(tblMcConfig);
        if (!useCaseRequest.getUseCaseDetailRequests().isEmpty()) {
            int seq = 1;
            for (UseCaseDetailRequest useCaseDetailRequest : useCaseRequest.getUseCaseDetailRequests()) {
                TblMcConfigDetail tblMcConfigDetail = new TblMcConfigDetail();
                tblMcConfigDetail.setTblMcConfig(tblMcConfig);
                tblMcConfigDetail.setCreatedate(new Date());
                tblMcConfigDetail.setCreateuser(userId);
                if (useCaseDetailRequest.getRoleId() != null && !useCaseDetailRequest.getRoleId().equals(Constants.EMPTY)) {
                    TblRole tblRole = new TblRole();
                    tblRole.setRoleId(Long.parseLong(useCaseDetailRequest.getRoleId()));
                    tblMcConfigDetail.setTblRole(tblRole);
                }
                if (useCaseDetailRequest.getUserId() != null && !useCaseDetailRequest.getUserId().isEmpty()) {
                    TblUser tblUser = new TblUser();
                    tblUser.setUserId(Long.parseLong(useCaseDetailRequest.getUserId()));
                    tblMcConfigDetail.setTblUser(tblUser);
                }
                tblMcConfigDetail.setIsActive("Y");
                tblMcConfigDetail.setApprovalType(useCaseDetailRequest.getApprovalType());
                tblMcConfigDetail.setIntimateOnly(useCaseDetailRequest.getIntimateOnly());
                tblMcConfigDetail.setSeq(new BigDecimal(seq));
                seq++;
                tblMcConfigDetailRepo.save(tblMcConfigDetail);
            }
        }
        return tblMcConfig;
    }

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public TblMcConfig updateusecase(UseCaseRequest useCaseRequest, BigDecimal userId) {
        TblMcConfig tblMcConfig = tblMcConfigRepo.findById(useCaseRequest.getMcConfigId()).orElse(null);
        if (tblMcConfig != null) {
            tblMcConfig.setTableName(useCaseRequest.getProcessName().getTableName());
            tblMcConfig.setFormName(useCaseRequest.getProcessName().getFormName());
            tblMcConfig.setRequestType(useCaseRequest.getProcessName().getRequestType());
            LkpStatus lkpStatus = new LkpStatus();
            lkpStatus.setStatusId(useCaseRequest.getStatusId());
            tblMcConfig.setLkpStatus(lkpStatus);
            tblMcConfig.setIsActive("Y");
            tblMcConfig.setLastupdateuser(userId);
            tblMcConfig.setLastupdatedate(new Date());
            tblMcConfig.setUpdateindex(tblMcConfig.getUpdateindex() == null ? new BigDecimal(1)
                    : new BigDecimal(tblMcConfig.getUpdateindex().intValue() + 1));
            tblMcConfig.setConfigName(useCaseRequest.getUseCaseName());
            tblMcConfig.setConfigDescription(useCaseRequest.getUseCaseDescr());
            tblMcConfig.setViewDetailUrl(useCaseRequest.getProcessName().getViewDetailUrl());
            tblMcConfig.setEditDetailUrl(useCaseRequest.getProcessName().getEditDetailUrl());
            tblMcConfig.setComments(useCaseRequest.getUseCaseComments());
            tblMcConfig = tblMcConfigRepo.save(tblMcConfig);
            if (!useCaseRequest.getUseCaseDetailRequests().isEmpty()) {
                processUseCaseDetails(useCaseRequest, userId, tblMcConfig);
            }
        }
        return tblMcConfig;
    }

    private void processUseCaseDetails(UseCaseRequest useCaseRequest, BigDecimal userId, TblMcConfig tblMcConfig) {
        for (UseCaseDetailRequest useCaseDetailRequest : useCaseRequest.getUseCaseDetailRequests()) {
            TblMcConfigDetail tblMcConfigDetail = tblMcConfigDetailRepo.findById(useCaseDetailRequest.getMcConfigDetailId()).orElse(null);
            if (tblMcConfigDetail != null && tblMcConfigDetail.getMcConfigDetailId() == useCaseDetailRequest.getMcConfigDetailId()) {
                processUseCaseDetailsEqualId(userId, tblMcConfig, useCaseDetailRequest, tblMcConfigDetail);
            } else {
                processUseCaseDetailsNotEqualId(userId, tblMcConfig, useCaseDetailRequest);

            }
        }
    }

    private void processUseCaseDetailsNotEqualId(BigDecimal userId, TblMcConfig tblMcConfig, UseCaseDetailRequest useCaseDetailRequest) {
        List<TblMcConfigDetail> tblMcConfigDetailss = tblMcConfigDetailRepo.findByTblMcConfigMcConfigId(tblMcConfig.getMcConfigId());
        int maxSeq = Integer.MIN_VALUE;
        for (TblMcConfigDetail tblMcConfigDetail1 : tblMcConfigDetailss) {
            int currentSeq = tblMcConfigDetail1.getSeq().intValue();
            if (currentSeq > maxSeq) {
                maxSeq = currentSeq;
            }
        }
        TblMcConfigDetail tblMcConfigDetails = new TblMcConfigDetail();
        tblMcConfigDetails.setTblMcConfig(tblMcConfig);
        tblMcConfigDetails.setCreatedate(new Date());
        tblMcConfigDetails.setCreateuser(userId);
        if (useCaseDetailRequest.getRoleId() != null && !useCaseDetailRequest.getRoleId().equals(Constants.EMPTY)) {
            TblRole tblRole = new TblRole();
            tblRole.setRoleId(Long.parseLong(useCaseDetailRequest.getRoleId()));
            tblMcConfigDetails.setTblRole(tblRole);
            tblMcConfigDetails.setTblUser(null);
        } else {
            tblMcConfigDetails.setTblRole(null);
        }
        if (useCaseDetailRequest.getUserId() != null && useCaseDetailRequest.getUserId().equals(Constants.EMPTY)) {
            TblUser tblUser = new TblUser();
            tblUser.setUserId(Long.parseLong(useCaseDetailRequest.getUserId()));
            tblMcConfigDetails.setTblUser(tblUser);
            tblMcConfigDetails.setTblRole(null);
        } else {
            tblMcConfigDetails.setTblUser(null);
        }
        tblMcConfigDetails.setSeq(new BigDecimal(maxSeq + 1));
        tblMcConfigDetails.setIsActive("Y");
        tblMcConfigDetails.setApprovalType(useCaseDetailRequest.getApprovalType());
        tblMcConfigDetails.setIntimateOnly(useCaseDetailRequest.getIntimateOnly());
        tblMcConfigDetailRepo.save(tblMcConfigDetails);
    }

    private void processUseCaseDetailsEqualId(BigDecimal userId, TblMcConfig tblMcConfig, UseCaseDetailRequest useCaseDetailRequest, TblMcConfigDetail tblMcConfigDetail) {
        tblMcConfigDetail.setTblMcConfig(tblMcConfig);
        tblMcConfigDetail.setLastupdateuser(userId);
        tblMcConfigDetail.setLastupdatedate(new Date());
        tblMcConfigDetail.setUpdateindex(tblMcConfigDetail.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(tblMcConfigDetail.getUpdateindex().intValue() + 1));
        if (useCaseDetailRequest.getRoleId() != null && !useCaseDetailRequest.getRoleId().isEmpty()) {
            TblRole tblRole = new TblRole();
            tblRole.setRoleId(Long.parseLong(useCaseDetailRequest.getRoleId()));
            tblMcConfigDetail.setTblRole(tblRole);
            tblMcConfigDetail.setTblUser(null);
        } else {
            tblMcConfigDetail.setTblRole(null);
        }
        if (useCaseDetailRequest.getUserId() != null && !useCaseDetailRequest.getUserId().equals(Constants.EMPTY)) {
            TblUser tblUser = new TblUser();
            tblUser.setUserId(Long.parseLong(useCaseDetailRequest.getUserId()));
            tblMcConfigDetail.setTblUser(tblUser);
            tblMcConfigDetail.setTblRole(null);
        } else {
            tblMcConfigDetail.setTblUser(null);
        }
        tblMcConfigDetail.setIsActive(useCaseDetailRequest.getIsActive());
        tblMcConfigDetail.setApprovalType(useCaseDetailRequest.getApprovalType());
        tblMcConfigDetail.setIntimateOnly(useCaseDetailRequest.getIntimateOnly());
        tblMcConfigDetailRepo.save(tblMcConfigDetail);
    }

    @Override
    public TblMcConfig inactiveusecase(UseCaseRequest useCaseRequest, BigDecimal userId) {
        TblMcConfig tblMcConfig = tblMcConfigRepo.findById(useCaseRequest.getMcConfigId()).orElse(null);
        if (tblMcConfig != null) {
            tblMcConfig.setIsActive(useCaseRequest.getIsActive());
            tblMcConfig.setLastupdateuser(userId);
            tblMcConfig.setLastupdatedate(new Date());
            tblMcConfig.setUpdateindex(tblMcConfig.getUpdateindex() == null ? new BigDecimal(1)
                    : new BigDecimal(tblMcConfig.getUpdateindex().intValue() + 1));
            tblMcConfig = tblMcConfigRepo.save(tblMcConfig);
        }
        return tblMcConfig;
    }

    @Override
    public BigDecimal checkUseCaseExistance(String useCaseName, String formName, String requestType) {
        String countSql = "SELECT COUNT(*) FROM TBL_MC_CONFIG\n" +
                "WHERE UPPER(CONFIG_NAME) = UPPER(?)";

        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter(1, useCaseName);
        BigDecimal consentCount = (BigDecimal) countQuery.getSingleResult();

        String countSql1 = "SELECT COUNT(*) FROM TBL_MC_CONFIG\n" +
                "                WHERE UPPER(FORM_NAME) = UPPER(?) AND REQUEST_TYPE=? AND IS_ACTIVE=?";

        Query countQuery1 = em.createNativeQuery(countSql1);
        countQuery1.setParameter(1, formName);
        countQuery1.setParameter(2, requestType);
        countQuery1.setParameter(3, "Y");
        BigDecimal consentCount1 = (BigDecimal) countQuery1.getSingleResult();
        if (consentCount1.longValue() > 0 || consentCount.longValue() > 0) {
            return new BigDecimal(1);
        } else {
            return new BigDecimal(0);
        }
    }

    public List<TblMcConfig> getAllUseCases(String useCaseName, String createdBy, String updatedBy, String statusId, String dateFrom, String dateTo) {
        String dateFromInput = Constants.UNHANDLE_EXCEPTION;
        String dateToInput = Constants.UNHANDLE_EXCEPTION;
        if (dateFrom != null && !(dateFrom.equals(Constants.EMPTY))) {
            dateFromInput = dateFrom + " 00:00:00";
        }
        if (dateTo != null && !(dateTo.equals(Constants.EMPTY))) {
            dateToInput = dateTo + " 23:59:59";
        }
        return tblMcConfigRepo.getallusecases(useCaseName, createdBy, updatedBy, statusId, dateFromInput, dateToInput);
    }

    @Override
    public ResponseEntity<Response> processSaveUseCase(HttpServletRequest request, UseCaseRequest useCaseRequest) {
        BigDecimal existTransLimit = checkUseCaseExistance(useCaseRequest.getUseCaseName(), useCaseRequest.getProcessName().getFormName(), useCaseRequest.getProcessName().getRequestType());
        if (existTransLimit.longValue() <= 0) {
            return processSaveUseCaseRequest(request, useCaseRequest);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Already Exists", null);
        }
    }

    private ResponseEntity<Response> processSaveUseCaseRequest(HttpServletRequest request, UseCaseRequest useCaseRequest) {
        useCaseRequest.setIsActive("Y");
        useCaseRequest.setStatusId(2L);
        TblMcConfig tblMcConfig = saveusecase(useCaseRequest, new BigDecimal ((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (!isNullOrEmpty(tblMcConfig)) {
            return getResponseFormat(HttpStatus.OK, "Record Saved", tblMcConfig);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Saved", null);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Response> processUpdateUseCase(HttpServletRequest request, UseCaseRequest useCaseRequest) {
        useCaseRequest.setIsActive("Y");
        useCaseRequest.setStatusId(2L);
        TblMcConfig tblMcConfig = updateusecase(useCaseRequest, new BigDecimal ((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (!isNullOrEmpty(tblMcConfig)) {
            return getResponseFormat(HttpStatus.OK, "Record Updated", tblMcConfig);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Updated", null);
        }
    }

    @Override
    public ResponseEntity<Response> processInactiveUseCase(HttpServletRequest request, UseCaseRequest useCaseRequest, BigDecimal loggedUserDetail) throws JsonProcessingException {
        TblMcConfig tblMcConfig = inactiveusecase(useCaseRequest, loggedUserDetail);
        if (!isNullOrEmpty(tblMcConfig)) {
            return getResponseFormat(HttpStatus.OK, "Record Updated", tblMcConfig);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Updated", null);
        }
    }

    @Override
    public ResponseEntity<Response> getAllUseCasesByFilter(UseCaseSearch useCaseSearch) {
//        List<TblMcConfig> tblMcConfigs = getAllUseCases(useCaseSearch.getUseCaseName(), useCaseSearch.getCreatedBy(), useCaseSearch.getUpdatedBy(), useCaseSearch.getStatusId(), useCaseSearch.getDateFrom(), useCaseSearch.getDateTo());
        List<TblMcConfig> tblMcConfigs = tblMcConfigRepo.findAll();
        if (!isNullOrEmpty(tblMcConfigs)) {
            return getResponseFormat(HttpStatus.OK, "Record Found", tblMcConfigs);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", tblMcConfigs);
        }
    }

    @Override
    public List<TblMcConfigDetail> getMcConfigDetailByMcConfigId(long mcConfigId) {
        return tblMcConfigDetailRepo.findByTblMcConfigMcConfigId(mcConfigId);
    }

    @Override
    public List<LkpMcFormName> getprocessname() {
        return lkpMcFormNameRepo.findByIsActive("Y");
    }
}
