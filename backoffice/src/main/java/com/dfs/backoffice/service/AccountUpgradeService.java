package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.SearchKycRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.UpdateKycRequest;

import javax.servlet.http.HttpServletRequest;

public interface AccountUpgradeService {
    Response searchKyc(SearchKycRequest searchKycRequest);

    Response searchKycById(String id);

    Response updateKyc(UpdateKycRequest updateKycRequest, HttpServletRequest request);
}
