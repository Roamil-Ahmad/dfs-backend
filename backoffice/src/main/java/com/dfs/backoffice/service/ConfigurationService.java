package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.LkpAccountType;
import com.dfs.backoffice.model.TblCustomer;
import com.dfs.backoffice.model.TblTransDoc;
import com.dfs.backoffice.model.VwMiniStatement;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConfigurationService {

    Response saveAccountType(AccountTypeCreation accountTypeCreation, HttpServletRequest request);

    Response updateAccountType(AccountTypeUpdate accountTypeUpdate, HttpServletRequest request);

    List<LkpAccountType> getAllLkpAccountType();

    LkpAccountType getAllLkpAccountTypeByid(long parseLong);

    Response saveTransDocs(TransDocsRequest transDocsRequest, HttpServletRequest request);

    Response updateTransDocs(TransDocsRequest transDocsRequest, HttpServletRequest request);

    List<TblTransDoc> getAllTransDocs(SearchTransDocs searchTransDocs);

    TblTransDoc getTransDocsById(String id);
}
