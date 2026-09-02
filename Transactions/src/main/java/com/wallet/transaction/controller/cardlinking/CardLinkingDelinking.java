package com.wallet.transaction.controller.cardlinking;

import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.CardLinkingRequest;
import com.wallet.transaction.dto.GetCardLinkingRequest;
import com.wallet.transaction.dto.SetDefaultRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.CardLinkingService;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

@RestController()
@RequestMapping("/cardlinking")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CardLinkingDelinking extends HelperClass {
    @Autowired
    private CardLinkingService cardLinkingService;
    @Autowired
    private CommonService commonService;
    @PostMapping("/v1/linkcard")
    public ResponseEntity<HashMap<String, Object>> linkCard(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CardLinkingRequest cardLinkingRequest = fromJson(convertObjecttoJson(request.getPayload()), CardLinkingRequest.class);
        RequestValidator.validateCardLinkingRequest(cardLinkingRequest, request);
        HashMap<String, Object> response = cardLinkingService.linkCard(cardLinkingRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/linkcard");

    }
    @PostMapping("/v1/getlinkedcards")
    public ResponseEntity<HashMap<String, Object>> getAllLinkedCards(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        GetCardLinkingRequest getCardLinkingRequest = fromJson(convertObjecttoJson(request.getPayload()), GetCardLinkingRequest.class);
        RequestValidator.validateGetCardLinkingRequest(getCardLinkingRequest, request);
        HashMap<String, Object> response = cardLinkingService.getAllLinkingCards(getCardLinkingRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getlinkedcards");
    }
    @PostMapping("/v1/setdefault")
    public ResponseEntity<HashMap<String, Object>> setdefault(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        SetDefaultRequest setDefaultRequest = fromJson(convertObjecttoJson(request.getPayload()), SetDefaultRequest.class);
        RequestValidator.validateSetDefaultRequest(setDefaultRequest, request);
        HashMap<String, Object> response = cardLinkingService.setCardDefault(setDefaultRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getlinkedcards");
    }
    @PostMapping("/v1/delinkcard")
    public ResponseEntity<HashMap<String, Object>> deLinkCard(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        SetDefaultRequest setDefaultRequest = fromJson(convertObjecttoJson(request.getPayload()), SetDefaultRequest.class);
        RequestValidator.validateSetDefaultRequest(setDefaultRequest, request);
        HashMap<String, Object> response = cardLinkingService.deLinkCard(setDefaultRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/deLinkCard");
    }


}
