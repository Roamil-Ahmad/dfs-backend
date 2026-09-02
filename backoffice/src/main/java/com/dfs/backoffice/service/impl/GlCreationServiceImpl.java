package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.GlCreationRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblGlAccount;
import com.dfs.backoffice.repo.TblGlAccountRepo;
import com.dfs.backoffice.service.GlCreationService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class GlCreationServiceImpl extends HelperClass implements GlCreationService {
    @Autowired
    private TblGlAccountRepo tblGlAccountRepo;

    @Override
    public TblGlAccount getGlAccountById(Long glAccountId) {
        return tblGlAccountRepo.findById(glAccountId).orElse(null);
    }

    @Override
    public TblGlAccount getChildGlAgainstParent(long glAccountId) {
        TblGlAccount tblGlAccount;
        List<TblGlAccount> tblGlAccounts = tblGlAccountRepo.getChildGlAgainstParent(glAccountId);
        if (!tblGlAccounts.isEmpty()) {
            tblGlAccount = tblGlAccounts.get(0);
        } else {
            return null;
        }
        return tblGlAccount;
    }

    @Override
    public Response createGlAccount(GlCreationRequest glCreationRequest, HttpServletRequest request) {
        Response response = new Response();
        TblGlAccount tblGlAccount = new TblGlAccount();
        tblGlAccount.setAccountTypeId(glCreationRequest.getAccountTypeId());
        tblGlAccount.setCurrentBalance(BigDecimal.ZERO);
        tblGlAccount.setGlAccountDescr(glCreationRequest.getGlAccountDescr());
        tblGlAccount.setOverdrawn((glCreationRequest.getOverdrawn() != null && glCreationRequest.getOverdrawn().equalsIgnoreCase("Y")) ? BigDecimal.ONE : BigDecimal.ZERO);
        if (!isNullOrEmpty(glCreationRequest.getParRefId())) {
            tblGlAccount.setParRefId(glCreationRequest.getParRefId());
            TblGlAccount tblGlAccount1 = getGlAccountById(glCreationRequest.getParRefId().longValue());
            tblGlAccount.setDepth(new BigDecimal(2));
            TblGlAccount tblGlAccounts = getChildGlAgainstParent(tblGlAccount1.getGlAccountId());
            long glAccountCode = 0;
            if (tblGlAccounts != null) {
                glAccountCode = Long.parseLong(tblGlAccounts.getGlAccountCode()) + 1;
            } else {
                glAccountCode = Long.parseLong(tblGlAccount1.getGlAccountCode()) + 1;
            }
            tblGlAccount.setGlAccountCode(String.valueOf(glAccountCode));

        } else {
            tblGlAccount.setDepth(new BigDecimal(2));
            tblGlAccount.setGlAccountCode(glCreationRequest.getGlAccountCode());
        }
        tblGlAccount.setAccountStatusId(new BigDecimal(1));
        tblGlAccount.setIsActive(glCreationRequest.getIsActive());
        tblGlAccount.setStatusId(new BigDecimal(2));
        tblGlAccount.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblGlAccount.setCreatedate(new Date());
        tblGlAccount = tblGlAccountRepo.save(tblGlAccount);
        if (tblGlAccount != null) {
            setResponse(response, Constants.ONE, tblGlAccount, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblGlAccount, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateGlAccount(GlCreationRequest glCreationRequest, HttpServletRequest request) {
        Response response = new Response();
        TblGlAccount tblGlAccount = tblGlAccountRepo.findById(glCreationRequest.getGlAccountId()).orElseThrow(() -> new CustomException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode()));
        tblGlAccount.setOverdrawn((glCreationRequest.getOverdrawn() != null && glCreationRequest.getOverdrawn().equalsIgnoreCase("Y")) ? BigDecimal.ONE : BigDecimal.ZERO);
        tblGlAccount.setIsActive(glCreationRequest.getIsActive());
        tblGlAccount.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblGlAccount.setLastupdatedate(new Date());
        tblGlAccount.setUpdateindex(tblGlAccount.getUpdateindex() == null ? new BigDecimal(1)
                : new BigDecimal(tblGlAccount.getUpdateindex().intValue() + 1));
        tblGlAccount = tblGlAccountRepo.save(tblGlAccount);
        if (tblGlAccount != null) {
            setResponse(response, Constants.ONE, tblGlAccount, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblGlAccount, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public List<TblGlAccount> getAllParentGeneralLedgers() {
        return tblGlAccountRepo.getAllParentGeneralLedgers();
    }

    @Override
    public List<TblGlAccount> getChildGlsAgainstParent(long glAccountId) {
        List<TblGlAccount> tblGlAccounts = tblGlAccountRepo.getChildGlAgainstParent(glAccountId);
        if (!tblGlAccounts.isEmpty()) {
            return tblGlAccounts;
        } else {
            return null;
        }
    }
}
