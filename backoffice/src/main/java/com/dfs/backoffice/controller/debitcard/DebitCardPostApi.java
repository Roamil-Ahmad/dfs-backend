package com.dfs.backoffice.controller.debitcard;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.CardInquiryRequest;
import com.dfs.backoffice.dto.ChangeCardStatusRequest;
import com.dfs.backoffice.dto.FetchLimitsRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.service.DebitCardService;
import com.dfs.backoffice.utils.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DebitCardPostApi extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(DebitCardPostApi.class);
    @Autowired
    private DebitCardService debitCardService;

    @PostMapping("/v1/cardInquiry")
    public ResponseEntity<Response> cardInquiry(@RequestBody CardInquiryRequest cardInquiryRequest) {
        RequestValidator.validateCardInquiry(cardInquiryRequest);
        Response response = debitCardService.cardInquiry(cardInquiryRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping("/v1/changeCardStatus")
    public ResponseEntity<Response> changeCardStatus(@RequestBody ChangeCardStatusRequest changeCardStatusRequest) {
        RequestValidator.validateChangeCardStatus(changeCardStatusRequest);
        Response response = debitCardService.changeCardStatus(changeCardStatusRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping("/v1/fetchLimits")
    public ResponseEntity<Response> fetchLimits(@RequestBody FetchLimitsRequest fetchLimitsRequest) {
        RequestValidator.validateFetchLimits(fetchLimitsRequest);
        Response response = debitCardService.fetchLimits(fetchLimitsRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }
}
