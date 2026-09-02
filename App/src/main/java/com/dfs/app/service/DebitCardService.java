package com.dfs.app.service;

import com.dfs.app.dto.UpdateCardRequest;
import com.dfs.app.dto.card.*;
import com.dfs.app.dto.common.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.HashMap;

public interface DebitCardService {
    HashMap<String, Object> newCardRequest(NewRequest newCardRequest, Request request, BigDecimal userId)
            throws JsonProcessingException;

    HashMap<String, Object> changeCardPin(ChangePinRequest changePinRequest, Request request)
            throws JsonProcessingException;

    HashMap<String, Object> forgotCardPin(GeneratePinRequest forgotPinRequest, Request request)
            throws JsonProcessingException;

    HashMap<String, Object> updateCardStatus(UpdateStatusRequest updateStatusRequest, Request request)
            throws JsonProcessingException;

    HashMap<String, Object> cardInquiry(InquiryRequest inquiryRequest, Request request) throws JsonProcessingException;

    HashMap<String, Object> cardLov() throws JsonProcessingException;

    HashMap<String, Object> updateCard(UpdateCardRequest updateCardRequest);

    HashMap<String, Object> checkCardStatus(String accountId);

    HashMap<String, Object> fetchLimits(FetchLimitRequest fetchLimitRequest, Request request)
            throws JsonProcessingException;
}
