package com.dfs.app.service;

import com.dfs.app.dto.BulkAccountRequest;
import com.dfs.app.dto.common.Request;

import java.util.HashMap;

public interface BulkAccountService {

    HashMap<String, Object> saveBulkAccounts(BulkAccountRequest bulkAccountRequest, Request request);
}
