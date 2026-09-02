package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.SearchKycRequest;
import com.dfs.backoffice.dto.SearchKycResponse;
import com.dfs.backoffice.dto.UpdateKycRequest;
import com.dfs.backoffice.model.TblAccount;
import com.dfs.backoffice.model.TblAccountLevel;
import com.dfs.backoffice.model.TblAccountUpgrade;
import com.dfs.backoffice.model.TblCustomer;
import com.dfs.backoffice.repo.TblAccountRepo;
import com.dfs.backoffice.repo.TblAccountUpgradeRepo;
import com.dfs.backoffice.repo.TblCustomerRepo;
import com.dfs.backoffice.service.AccountUpgradeService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountUpgradeServiceImpl extends HelperClass implements AccountUpgradeService {
    @Autowired
    private TblAccountUpgradeRepo tblAccountUpgradeRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;

    @Override
    public Response searchKyc(SearchKycRequest searchKycRequest) {
        String dateFromInput = null;
        String dateToInput = null;
        if (searchKycRequest.getFromDate() != null && !(searchKycRequest.getFromDate().equals(""))) {
            dateFromInput = searchKycRequest.getFromDate() + " 00:00:00";
        }
        if (searchKycRequest.getToDate() != null && !(searchKycRequest.getToDate().equals(""))) {
            dateToInput = searchKycRequest.getToDate() + " 23:59:59";
        }
        List<Object> objects = tblAccountUpgradeRepo.searchKyc(searchKycRequest, dateFromInput, dateToInput);
        return getKycDataFromObject(objects);
    }

    private Response getKycDataFromObject(List<Object> objects) {
        Response response = new Response();
        List<SearchKycResponse> searchKycRespons = new ArrayList<>();
        if (!isNullOrEmpty(objects)) {
            for (Object object : objects) {
                Object[] row = (Object[]) object;
                if (!isNullOrEmpty(row)) {
                    SearchKycResponse rec = new SearchKycResponse();
                    rec.setAccountUpgradeId((BigDecimal) row[0]);
                    rec.setStatusId((BigDecimal) row[1]);
                    rec.setAccountId((BigDecimal) row[2]);
                    rec.setAccountLevelId((BigDecimal) row[3]);
                    rec.setAccountNo((String) row[4]);
                    rec.setAccountTitle(row[5] != null ? decrypttWithAes((String) row[5]) : "");
                    rec.setAccountLevelDescr((String) row[6]);
                    rec.setStatus((String) row[7]);
                    rec.setSourceOfIncome((String) row[8]);
                    rec.setProvince((String) row[9]);
                    rec.setOccupation((String) row[10]);
                    rec.setAccountPurpose((String) row[11]);
                    searchKycRespons.add(rec);
                }
            }
        }
        if (!searchKycRespons.isEmpty()) {
            setResponse(response, Constants.ONE, searchKycRespons, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, searchKycRespons, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    public Response searchKycById(String id) {
        List<Object> objects = tblAccountUpgradeRepo.searchKycById(id);
        return getKycDataByIdFromObject(objects);
    }

    private Response getKycDataByIdFromObject(List<Object> objects) {
        Response response = new Response();
        List<SearchKycResponse> searchKycRespons = new ArrayList<>();
        if (!isNullOrEmpty(objects)) {
            for (Object object : objects) {
                Object[] row = (Object[]) object;
                if (!isNullOrEmpty(row)) {
                    SearchKycResponse rec = new SearchKycResponse();
                    rec.setAccountUpgradeId((BigDecimal) row[0]);
                    rec.setStatusId((BigDecimal) row[1]);
                    rec.setAccountId((BigDecimal) row[2]);
                    rec.setAccountLevelId((BigDecimal) row[3]);
                    rec.setAccountNo((String) row[4]);
                    rec.setAccountTitle(row[5] != null ? decrypttWithAes((String) row[5]) : "");
                    rec.setAccountLevelDescr((String) row[6]);
                    rec.setStatus((String) row[7]);
                    rec.setSourceOfIncome((String) row[8]);
                    rec.setProvince((String) row[9]);
                    rec.setOccupation((String) row[10]);
                    rec.setAccountPurpose((String) row[11]);
                    rec.setNidFront((String) row[12]);
                    rec.setNidBack((String) row[13]);
                    rec.setSelfie((String) row[14]);
                    rec.setFingerprint((String) row[15]);
                    rec.setProofOfAddress((String) row[16]);
                    rec.setComments((String) row[17]);
                    rec.setUpgradeRequestDate(row[18] != null ? row[18].toString() : "");
                    rec.setCheckerName((String) row[19]);
                    rec.setCheckerComments((String) row[20]);
                    rec.setCheckDate(row[21] != null ? row[21].toString() : "");
                    searchKycRespons.add(rec);
                }
            }
        }
        if (!searchKycRespons.isEmpty()) {
            setResponse(response, Constants.ONE, searchKycRespons, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, searchKycRespons, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Response updateKyc(UpdateKycRequest updateKycRequest, HttpServletRequest request) {
        Response response = new Response();
        if (updateKycRequest.getStatusId() == 2) {
            TblAccount tblAccount = tblAccountRepo.findById(updateKycRequest.getAccountId()).orElseThrow(() -> new CustomException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode()));
            TblAccountLevel tblAccountLevel = new TblAccountLevel();
            tblAccountLevel.setAccountLevelId(updateKycRequest.getAccountLevelId());
            tblAccount.setTblAccountLevel(tblAccountLevel);
            tblAccount.setLastupdatedate(new Date());
            tblAccount.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            tblAccount.setUpdateindex(setUpdateIndex(tblAccount.getUpdateindex()));
            tblAccountRepo.save(tblAccount);
            TblCustomer tblCustomer = tblCustomerRepo.findById(tblAccount.getCustomerId()).orElseThrow(() -> new CustomException(GenericResponseCode.CUSTOMER_NOT_FOUND.getResponseCode()));
            tblCustomer.setIsKycVerified("Y");
            tblCustomer.setLastupdatedate(new Date());
            tblCustomer.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            tblCustomer.setUpdateindex(setUpdateIndex(tblCustomer.getUpdateindex()));
            tblCustomerRepo.save(tblCustomer);
        }
        TblAccountUpgrade tblAccountUpgrade = tblAccountUpgradeRepo.findById(updateKycRequest.getAccountUpgradeId()).orElseThrow(() -> new CustomException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode()));
        tblAccountUpgrade.setStatusId(new BigDecimal(updateKycRequest.getStatusId()));
        tblAccountUpgrade.setComments(updateKycRequest.getComments());
        tblAccountUpgrade.setLastupdatedate(new Date());
        tblAccountUpgrade.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblAccountUpgrade.setUpdateindex(setUpdateIndex(tblAccountUpgrade.getUpdateindex()));
        tblAccountUpgradeRepo.save(tblAccountUpgrade);
        setResponse(response, Constants.ONE, null, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        return response;
    }
}
