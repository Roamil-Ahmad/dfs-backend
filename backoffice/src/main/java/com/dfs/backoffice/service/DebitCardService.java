package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.CardInquiryRequest;
import com.dfs.backoffice.dto.ChangeCardStatusRequest;
import com.dfs.backoffice.dto.FetchLimitsRequest;
import com.dfs.backoffice.dto.Response;

public interface DebitCardService {
    Response cardInquiry(CardInquiryRequest cardInquiryRequest);
    Response changeCardStatus(ChangeCardStatusRequest changeCardStatusRequest);
    Response fetchLimits(FetchLimitsRequest fetchLimitsRequest);
}
