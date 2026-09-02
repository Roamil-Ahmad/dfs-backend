package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.GlCreationRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblGlAccount;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GlCreationService {
    TblGlAccount getGlAccountById(Long glAccountId);

    TblGlAccount getChildGlAgainstParent(long glAccountId);

    Response createGlAccount(GlCreationRequest glCreationRequest, HttpServletRequest request);

    Response updateGlAccount(GlCreationRequest glCreationRequest, HttpServletRequest request);

    List<TblGlAccount> getAllParentGeneralLedgers();

    List<TblGlAccount> getChildGlsAgainstParent(long glAccountId);
}
