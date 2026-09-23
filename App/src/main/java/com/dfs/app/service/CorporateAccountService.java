package com.dfs.app.service;

import com.dfs.app.dto.AccountDetailsRequest;
import com.dfs.app.dto.common.Request;

import java.util.HashMap;

public interface CorporateAccountService {

    HashMap<String, Object> accountDetails(AccountDetailsRequest accountDetailsRequest, Request request);
}
