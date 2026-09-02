package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.service.DebitCardService;
import com.dfs.backoffice.dto.CardInquiryRequest;
import com.dfs.backoffice.dto.ChangeCardStatusRequest;
import com.dfs.backoffice.dto.FetchLimitsRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.repo.TblDebitCardRepo;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DebitCardServiceImpl extends HelperClass implements DebitCardService {

    @Autowired
    private TblDebitCardRepo tblDebitCardRepo;

    @Value("${card.inquiry.url}")
    private String cardInquiryUrl;

    @Override
    public Response cardInquiry(CardInquiryRequest cardInquiryRequest) {
        Response response = new Response();
        try {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            headerMap.put(Constants.ACCEPT, Constants.APPLICATION_JSON);

            String url = cardInquiryUrl + "/inquiry";
            String result = getResponseFromPostAPI(headerMap, cardInquiryRequest, url);

            @SuppressWarnings("unchecked")
            Map<String, Object> internalResponse = fromJson(result, Map.class);

            if (internalResponse != null && internalResponse.get("responseCode") != null &&
                    (internalResponse.get("responseCode").toString().equals("0") || internalResponse.get("responseCode").toString().equals("0.0"))) {
                
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) internalResponse.get("responseBody");
                if (responseBody != null) {
                    responseBody.remove("expiryDate");
                }
                
                setResponse(response, Constants.ONE, responseBody, GenericResponseCode.SUCCESS.getResponseCode(), (String) internalResponse.get("responseMessage"));
            } else {
                String errorMsg = internalResponse != null ? (String) internalResponse.get("responseMessage") : "Internal Service Error";
                setResponse(response, Constants.ZERO, null, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setResponse(response, Constants.ZERO, null, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        return response;
    }

    @Override
    public Response changeCardStatus(ChangeCardStatusRequest changeCardStatusRequest) {
        Response response = new Response();
        try {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            headerMap.put(Constants.ACCEPT, Constants.APPLICATION_JSON);

            String url = cardInquiryUrl + "/update-status";
            String result = getResponseFromPostAPI(headerMap, changeCardStatusRequest, url);

            @SuppressWarnings("unchecked")
            Map<String, Object> internalResponse = fromJson(result, Map.class);

            if (internalResponse != null && internalResponse.get("responseCode") != null &&
                    (internalResponse.get("responseCode").toString().equals("0") || internalResponse.get("responseCode").toString().equals("0.0"))) {
                setResponse(response, Constants.ONE, internalResponse.get("responseBody"), GenericResponseCode.SUCCESS.getResponseCode(), (String) internalResponse.get("responseMessage"));
            } else {
                String errorMsg = internalResponse != null ? (String) internalResponse.get("responseMessage") : "Internal Service Error";
                setResponse(response, Constants.ZERO, null, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setResponse(response, Constants.ZERO, null, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        return response;
    }

    @Override
    public Response fetchLimits(FetchLimitsRequest fetchLimitsRequest) {
        Response response = new Response();
        try {
            Object accountLimitsRaw = tblDebitCardRepo.viewLimits(Long.parseLong(fetchLimitsRequest.getAccountId()));
            List<Object[]> cardLimitsRaw = tblDebitCardRepo.fetchCardLimits(fetchLimitsRequest.getPan());

            Map<String, Object> accountLimitsMap = new HashMap<>();
            if (accountLimitsRaw instanceof Object[]) {
                Object[] row = (Object[]) accountLimitsRaw;
                accountLimitsMap.put("accountId", row[0]);
                accountLimitsMap.put("dailyDrTotal", row[1]);
                accountLimitsMap.put("monthlyDrTotal", row[2]);
                accountLimitsMap.put("yearlyDrTotal", row[3]);
                accountLimitsMap.put("dailyCrTotal", row[4]);
                accountLimitsMap.put("monthlyCrTotal", row[5]);
                accountLimitsMap.put("yearlyCrTotal", row[6]);
                accountLimitsMap.put("dailyDrRemaining", row[7]);
                accountLimitsMap.put("monthlyDrRemaining", row[8]);
                accountLimitsMap.put("yearlyDrRemaining", row[9]);
                accountLimitsMap.put("dailyCrRemaining", row[10]);
                accountLimitsMap.put("monthlyCrRemaining", row[11]);
                accountLimitsMap.put("yearlyCrRemaining", row[12]);
            }

            List<Map<String, Object>> cardLimitsList = new ArrayList<>();
            if (cardLimitsRaw != null) {
                for (Object[] row : cardLimitsRaw) {
                    Map<String, Object> cardMap = new HashMap<>();
                    cardMap.put("accountNum", row[0]);
                    cardMap.put("pan", row[1]);
                    cardMap.put("cardTitle", row[2]);
                    cardMap.put("expiryDate", row[3]);
                    cardMap.put("cardStatusName", row[4]);
                    cardMap.put("maxLimit", row[5]);
                    cardMap.put("singleTranLimit", row[6]);
                    cardMap.put("dailyAvailableSpending", row[7]);
                    cardMap.put("monthlyAvailableSpending", row[8]);
                    cardLimitsList.add(cardMap);
                }
            }

            Map<String, Object> finalPayload = new HashMap<>();
            finalPayload.put("accountLimits", accountLimitsMap);
            finalPayload.put("cardLimits", cardLimitsList);

            setResponse(response, Constants.ONE, finalPayload, GenericResponseCode.SUCCESS.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
            setResponse(response, Constants.ZERO, null, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        return response;
    }
}
