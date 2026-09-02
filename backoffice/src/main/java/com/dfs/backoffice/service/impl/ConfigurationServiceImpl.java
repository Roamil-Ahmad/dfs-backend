package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.LkpAccountType;
import com.dfs.backoffice.model.LkpStatus;
import com.dfs.backoffice.model.TblTransDoc;
import com.dfs.backoffice.repo.LkpAccountTypeRepo;
import com.dfs.backoffice.repo.TblTransDocRepo;
import com.dfs.backoffice.service.ConfigurationService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ConfigurationServiceImpl extends HelperClass implements ConfigurationService {

    @Autowired
    LkpAccountTypeRepo lkpAccountTypeRepo;
    @Autowired
    private TblTransDocRepo tblTransDocRepo;

    @Override
    public Response saveAccountType(AccountTypeCreation accountTypeCreation, HttpServletRequest request) {
        Response response = new Response();
        LkpAccountType lkpAccountType = new LkpAccountType();
        lkpAccountType.setAccountType(accountTypeCreation.getAccountType());
        lkpAccountType.setAccountTypeCode(accountTypeCreation.getAccountTypeCode());
        lkpAccountType.setAccountTypeDescr(accountTypeCreation.getAccountTypeDescr());
        lkpAccountType.setIsActive(accountTypeCreation.getIsActive());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2);
        lkpAccountType.setLkpStatus(lkpStatus);
        lkpAccountType.setSortSeq(lkpAccountTypeRepo.findMaxSortSeq().add(new BigDecimal(1)));
        lkpAccountType.setCreatedate(new Date());
        lkpAccountType.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        lkpAccountType = lkpAccountTypeRepo.save(lkpAccountType);
        setResponse(response, Constants.ONE, lkpAccountType, GenericResponseCode.SUCCESS.getResponseCode());
        return response;
    }

    @Override
    public Response updateAccountType(AccountTypeUpdate accountTypeUpdate, HttpServletRequest request) {
        Response response = new Response();
        LkpAccountType lkpAccountType = lkpAccountTypeRepo.findById(accountTypeUpdate.getAccountTypeId()).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        ;
        lkpAccountType.setAccountType(accountTypeUpdate.getAccountType());
        lkpAccountType.setAccountTypeCode(accountTypeUpdate.getAccountTypeCode());
        lkpAccountType.setAccountTypeDescr(accountTypeUpdate.getAccountTypeDescr());
        lkpAccountType.setIsActive(accountTypeUpdate.getIsActive());
        lkpAccountType.setLastupdatedate(new Date());
        lkpAccountType.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        lkpAccountType.setUpdateindex(lkpAccountType.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(lkpAccountType.getUpdateindex().intValue() + 1));
        lkpAccountType = lkpAccountTypeRepo.save(lkpAccountType);
        setResponse(response, Constants.ONE, lkpAccountType, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        return response;
    }

    @Override
    public List<LkpAccountType> getAllLkpAccountType() {
        return lkpAccountTypeRepo.findAll();
    }

    @Override
    public LkpAccountType getAllLkpAccountTypeByid(long parseLong) {
        return lkpAccountTypeRepo.findById(parseLong).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }

    @Override
    public Response saveTransDocs(TransDocsRequest transDocsRequest, HttpServletRequest request) {
        Response response = new Response();
        TblTransDoc tblTransDoc = new TblTransDoc();
        tblTransDoc.setGlAccountId(transDocsRequest.getGlAccountId());
        tblTransDoc.setLimitYn(transDocsRequest.getLimitYn());
        tblTransDoc.setTaxRegimeId(transDocsRequest.getTaxRegimeId());
        tblTransDoc.setTransDocsCode(transDocsRequest.getTransDocsCode());
        tblTransDoc.setTransDocsDescr(transDocsRequest.getTransDocsDescr());
        tblTransDoc.setTransTypeId(BigDecimal.ONE);
        tblTransDoc.setDailyAmtLimitCr(transDocsRequest.getDailyAmtLimitCr());
        tblTransDoc.setDailyAmtLimitDr(transDocsRequest.getDailyAmtLimitDr());
        tblTransDoc.setMonthlyAmtLimitCr(transDocsRequest.getMonthlyAmtLimitCr());
        tblTransDoc.setMonthlyAmtLimitDr(transDocsRequest.getMonthlyAmtLimitDr());
        tblTransDoc.setIsActive(transDocsRequest.getIsActive());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2);
        tblTransDoc.setLkpStatus(lkpStatus);
        tblTransDoc.setCreatedate(new Date());
        tblTransDoc.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblTransDoc = tblTransDocRepo.save(tblTransDoc);
        setResponse(response, Constants.ONE, tblTransDoc, GenericResponseCode.SUCCESS.getResponseCode());
        return response;
    }

    @Override
    public Response updateTransDocs(TransDocsRequest transDocsRequest, HttpServletRequest request) {
        Response response = new Response();
        TblTransDoc tblTransDoc = tblTransDocRepo.findById(transDocsRequest.getTransDocsId()).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        tblTransDoc.setGlAccountId(transDocsRequest.getGlAccountId());
        tblTransDoc.setLimitYn(transDocsRequest.getLimitYn());
        tblTransDoc.setTaxRegimeId(transDocsRequest.getTaxRegimeId());
        tblTransDoc.setTransDocsCode(transDocsRequest.getTransDocsCode());
        tblTransDoc.setTransDocsDescr(transDocsRequest.getTransDocsDescr());
        tblTransDoc.setDailyAmtLimitCr(transDocsRequest.getDailyAmtLimitCr());
        tblTransDoc.setDailyAmtLimitDr(transDocsRequest.getDailyAmtLimitDr());
        tblTransDoc.setMonthlyAmtLimitCr(transDocsRequest.getMonthlyAmtLimitCr());
        tblTransDoc.setMonthlyAmtLimitDr(transDocsRequest.getMonthlyAmtLimitDr());
        tblTransDoc.setIsActive(transDocsRequest.getIsActive());
        tblTransDoc.setLastupdatedate(new Date());
        tblTransDoc.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblTransDoc.setUpdateindex(tblTransDoc.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(tblTransDoc.getUpdateindex().intValue() + 1));
        tblTransDoc = tblTransDocRepo.save(tblTransDoc);
        setResponse(response, Constants.ONE, tblTransDoc, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        return response;
    }

    @Override
    public List<TblTransDoc> getAllTransDocs(SearchTransDocs searchTransDocs) {
        String dateFromInput = null;
        String dateToInput = null;
        if (searchTransDocs.getFromDate() != null && !(searchTransDocs.getFromDate().equals(""))) {
            dateFromInput = searchTransDocs.getFromDate() + " 00:00:00";
        }
        if (searchTransDocs.getToDate() != null && !(searchTransDocs.getToDate().equals(""))) {
            dateToInput = searchTransDocs.getToDate() + " 23:59:59";
        }
        return tblTransDocRepo.getAllTransDocs(searchTransDocs, dateFromInput, dateToInput);
    }

    @Override
    public TblTransDoc getTransDocsById(String id) {
        return tblTransDocRepo.findById(Long.parseLong(id)).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }
}
