package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.LkpAccountStatus;
import com.dfs.backoffice.model.TblAccount;
import com.dfs.backoffice.model.TblCustomer;
import com.dfs.backoffice.model.VwMiniStatement;
import com.dfs.backoffice.repo.TblAccountRepo;
import com.dfs.backoffice.repo.TblCustomerRepo;
import com.dfs.backoffice.repo.VwMiniStatementRepo;
import com.dfs.backoffice.service.CustomerMaintenanceService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerMaintenanceServiceImpl extends HelperClass implements CustomerMaintenanceService {
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private VwMiniStatementRepo vwMiniStatementRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;

    @Override
    public List<SearchCustomerResponse> searchCustomer(SearchCustomerRequest searchCustomerRequest) {
        String dateFromInput = null;
        String dateToInput = null;
        if (searchCustomerRequest.getFromDate() != null && !(searchCustomerRequest.getFromDate().equals(""))) {
            dateFromInput = searchCustomerRequest.getFromDate() + " 00:00:00";
        }
        if (searchCustomerRequest.getToDate() != null && !(searchCustomerRequest.getToDate().equals(""))) {
            dateToInput = searchCustomerRequest.getToDate() + " 23:59:59";
        }
        searchCustomerRequest.setNidNo(!isNullOrEmpty(searchCustomerRequest.getNidNo()) ? encryptWithAes(searchCustomerRequest.getNidNo()) : "");
        List<Object> tblCustomer = tblCustomerRepo.searchCustomer(searchCustomerRequest, dateFromInput, dateToInput);
        return getSearchCustomer(tblCustomer);
    }

    private List<SearchCustomerResponse> getSearchCustomer(List<Object> objects) {
        List<SearchCustomerResponse> tblCustomers = new ArrayList<>();
        if (!isNullOrEmpty(objects)) {
            for (Object object : objects) {
                Object[] row = (Object[]) object;

                if (!isNullOrEmpty(row)) {
                    SearchCustomerResponse rec = new SearchCustomerResponse();
                    rec.setCustomerId((BigDecimal) row[0]);
                    rec.setFirstName(row[1] != null ? decrypttWithAes((String) row[1]) : "");
                    rec.setMiddleName(row[2] != null ? decrypttWithAes((String) row[2]) : "");
                    rec.setLastName(row[3] != null ? decrypttWithAes((String) row[3]) : "");
                    rec.setFatherName(row[4] != null ? decrypttWithAes((String) row[4]) : "");
                    rec.setGrandfatherName(row[5] != null ? decrypttWithAes((String) row[5]) : "");
                    rec.setNidNo(row[6] != null ? decrypttWithAes((String) row[6]) : "");
                    rec.setDob((Date) row[7]);
                    rec.setEmail(row[8] != null ? decrypttWithAes((String) row[8]) : "");
                    rec.setAddressC(row[9] != null ? decrypttWithAes((String) row[9]) : "");
                    rec.setAddressM(row[10] != null ? decrypttWithAes((String) row[10]) : "");
                    rec.setAddressP(row[11] != null ? decrypttWithAes((String) row[11]) : "");
                    rec.setCreatedate((Date) row[12]);
                    rec.setFullName(row[13] != null ? decrypttWithAes((String) row[13]) : "");
                    rec.setGender((String) row[14]);
                    rec.setIsActive((String) row[15]);
                    rec.setPob(row[16] != null ? decrypttWithAes((String) row[16]) : "");
                    rec.setNidExpiryDate((Date) row[17]);
                    rec.setNidIssueDate((Date) row[18]);
                    rec.setAccountId((BigDecimal) row[19]);
                    rec.setAccountLevelDescr((String) row[20]);
                    rec.setAccountStatusDescr((String) row[21]);
                    rec.setAccountNo((String) row[22]);
                    rec.setNationality((String) row[23]);
                    rec.setOccupationDescr((String) row[24]);
                    rec.setSegmentDescr((String) row[25]);
                    rec.setRiskProfile((String) row[26]);
                    rec.setMobileNo((String) row[27]);
                    rec.setProvinceDescr((String) row[28]);
                    rec.setDistrictDescr((String) row[29]);
                    rec.setCityDescr((String) row[30]);
                    rec.setCurrentBalance((BigDecimal) row[31]);
                    rec.setNidFront((String) row[32]);
                    rec.setNidBack((String) row[33]);
                    rec.setProofOfAddress((String) row[34]);
                    rec.setSelfie((String) row[35]);
                    rec.setSignature((String) row[36]);
                    rec.setDeviceId((String) row[37]);
                    rec.setDailyAmtLimitDr((BigDecimal) row[38]);
                    rec.setMonthlyAmtLimitDr((BigDecimal) row[39]);
                    rec.setYearlyAmtLimitDr((BigDecimal) row[40]);
                    rec.setDailyAmtLimitCr((BigDecimal) row[41]);
                    rec.setMonthlyAmtLimitCr((BigDecimal) row[42]);
                    rec.setYearlyAmtLimitCr((BigDecimal) row[43]);
                    rec.setDailyTransLimitDr((BigDecimal) row[44]);
                    rec.setMonthlyTransLimitDr((BigDecimal) row[45]);
                    rec.setYearlyTransLimitDr((BigDecimal) row[46]);
                    rec.setDailyTransLimitCr((BigDecimal) row[47]);
                    rec.setMonthlyTransLimitCr((BigDecimal) row[48]);
                    rec.setYearlyTransLimitCr((BigDecimal) row[49]);
                    tblCustomers.add(rec);
                }
            }
        }
        return tblCustomers;
    }

    @Override
    public List<VwMiniStatement> getLastTenTransactions(MiniStatementRequest miniStatementRequest, HttpServletRequest request) {
        return vwMiniStatementRepo.getLastTenTransactions(miniStatementRequest.getFromAccountId());
    }

    @Override
    public Response updateCustomer(UpdateCustomerRequest updateCustomerRequest, HttpServletRequest request) {
        Response response = new Response();
        TblCustomer tblCustomer = tblCustomerRepo.findCustomerByNidNo(encryptWithAes(updateCustomerRequest.getNidNo()));
        if (tblCustomer != null) {
            tblCustomer.setFullName(encryptWithAes(updateCustomerRequest.getFullName()));
            tblCustomer.setFatherName(encryptWithAes(updateCustomerRequest.getFatherName()));
            tblCustomer.setGrandfatherName(encryptWithAes(updateCustomerRequest.getGrandFatherName()));
            tblCustomer.setGender(updateCustomerRequest.getGender());
            tblCustomer.setPob(encryptWithAes(updateCustomerRequest.getPlaceOfBirth()));
            tblCustomer.setAddressP(encryptWithAes(updateCustomerRequest.getPermanentAddress()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format if needed
            try {
                Date nidExpiryDate = formatter.parse(updateCustomerRequest.getNidExpiryDate());
                Date nidIssueDate = formatter.parse(updateCustomerRequest.getNidIssueDate());
                Date dob = formatter.parse(updateCustomerRequest.getDob());

                tblCustomer.setNidExpiryDate(nidExpiryDate);
                tblCustomer.setNidIssueDate(nidIssueDate);
                tblCustomer.setDob(dob);
            } catch (ParseException e) {
                // Handle parse exception, e.g., log error or throw custom exception
                e.printStackTrace();
            }
            tblCustomer.setLastupdatedate(new Date());
            tblCustomer.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            tblCustomer.setUpdateindex(tblCustomer.getUpdateindex() == null ? new BigDecimal(1)
                    : new BigDecimal(tblCustomer.getUpdateindex().intValue() + 1));
            tblCustomerRepo.save(tblCustomer);
            setResponse(response, Constants.ONE, null, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.CUSTOMER_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    public TblCustomer getCustomerByNidNo(String nidNo) {
        return tblCustomerRepo.findCustomerByNidNo(encryptWithAes(nidNo));
    }

    @Override
    public Response updateAccountStatus(UpdateStatusRequest updateStatusRequest, HttpServletRequest request) {
        Response response = new Response();
        TblAccount tblAccount = tblAccountRepo.findById(updateStatusRequest.getAccountId()).orElseThrow(() -> new CustomException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode()));
        LkpAccountStatus lkpAccountStatus = new LkpAccountStatus();
        lkpAccountStatus.setAccountStatusId(updateStatusRequest.getIsActive().equalsIgnoreCase("Y") ? 1 : 9);
        tblAccount.setLkpAccountStatus(lkpAccountStatus);
        tblAccount.setLastupdatedate(new Date());
        tblAccount.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblAccount.setUpdateindex(tblAccount.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(tblAccount.getUpdateindex().intValue() + 1));
        tblAccountRepo.save(tblAccount);
        setResponse(response, Constants.ONE, null, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        return response;
    }

    @Override
    public List<VwMiniStatement> getCardLastTransactions(MiniStatementRequest miniStatementRequest, HttpServletRequest request) {
        return vwMiniStatementRepo.getCardLastTransactions(miniStatementRequest.getFromAccountId());
    }
}
