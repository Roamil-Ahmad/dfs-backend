package com.wallet.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.dto.CardLinkingRequest;
import com.wallet.transaction.dto.GetCardLinkingRequest;
import com.wallet.transaction.dto.SetDefaultRequest;
import com.wallet.transaction.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CardLinkingService {
    HashMap<String, Object> linkCard(CardLinkingRequest cardLinkingRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> getAllLinkingCards(GetCardLinkingRequest getCardLinkingRequest, Request request, String authorization, BigDecimal userId);

    HashMap<String, Object> setCardDefault(SetDefaultRequest setDefaultRequest, Request request, String authorization, BigDecimal userId);

    HashMap<String, Object> deLinkCard(SetDefaultRequest setDefaultRequest, Request request, String authorization, BigDecimal userId);
}
