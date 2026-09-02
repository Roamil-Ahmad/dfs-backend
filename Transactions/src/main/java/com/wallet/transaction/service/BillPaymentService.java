package com.wallet.transaction.service;

import com.wallet.transaction.dto.GetSavedBillsRequest;
import com.wallet.transaction.dto.SaveTemplateRequest;
import com.wallet.transaction.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface BillPaymentService {
    HashMap<String, Object> getsavedbills(GetSavedBillsRequest getSavedBillsRequest, Request request, String authorization, BigDecimal userId);
    HashMap<String, Object> savetemplate(SaveTemplateRequest getSavedBillsRequest, Request request, String authorization, BigDecimal userId) throws Exception;
    HashMap<String, Object> getbiller();
}
