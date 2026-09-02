package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.GlTransactionRequest;
import com.dfs.backoffice.dto.GlTransactionResponse;
import com.dfs.backoffice.dto.TitleFetchRequest;
import com.dfs.backoffice.model.TblAccount;

import javax.servlet.http.HttpServletRequest;

public interface GlTransactionService {
    GlTransactionResponse glToGlTransfer(GlTransactionRequest glTransactionRequest, HttpServletRequest request);

    GlTransactionResponse walletToGlTransfer(GlTransactionRequest glTransactionRequest, HttpServletRequest request);

    GlTransactionResponse glToWalletTransfer(GlTransactionRequest glTransactionRequest, HttpServletRequest request);

    TblAccount getAccountAgainstAccountNo(TitleFetchRequest titleFetchRequest);
}
