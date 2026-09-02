package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Calls the switch gateway, which owns the ISO 8583 link to 1LINK.
 *
 * This is transport only. It decides nothing about the transaction; a failure is reported to the
 * caller and is never retried here.
 */
@Component
public class SwitchGatewayClient {

    private static final Logger log = LoggerFactory.getLogger(SwitchGatewayClient.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${switch.gateway.title-fetch.url}")
    private String titleFetchUrl;

    @Value("${switch.gateway.advice.url}")
    private String adviceUrl;

    @Value("${switch.gateway.bill-inquiry.url}")
    private String billInquiryUrl;

    @Value("${switch.gateway.bill-payment.url}")
    private String billPaymentUrl;

    @Value("${switch.gateway.timeout-ms}")
    private int timeoutMs;

    /** 0200 / 0210 with processing code 620000. */
    public GatewayResponse<GatewayTitleFetchResponse> titleFetch(GatewayTitleFetchRequest request) {
        log.info("Title fetch to switch gateway | STAN:{} | RRN:{} | toBankImd:{}",
                request.getStan(), request.getRrn(), request.getToBankImd());
        return call(titleFetchUrl, request, new TypeReference<GatewayResponse<GatewayTitleFetchResponse>>() {
        }, "title fetch");
    }

    /** 0220 / 0230 with processing code 480000. */
    public GatewayResponse<GatewayAdviceResponse> advice(GatewayAdviceRequest request) {
        log.info("IBFT advice to switch gateway | STAN:{} | RRN:{} | toBankImd:{}",
                request.getStan(), request.getRrn(), request.getToBankImd());
        return call(adviceUrl, request, new TypeReference<GatewayResponse<GatewayAdviceResponse>>() {
        }, "IBFT advice");
    }

    /** 0200 / 0210 Bill Inquiry - asks the biller what is due, and moves no money. */
    public GatewayResponse<GatewayBillResponse> billInquiry(GatewayBillRequest request) {
        log.info("Bill inquiry to switch gateway | STAN:{} | RRN:{} | company:{}",
                request.getStan(), request.getRrn(), request.getUtilityCompanyCode());
        return call(billInquiryUrl, request, new TypeReference<GatewayResponse<GatewayBillResponse>>() {
        }, "bill inquiry");
    }

    /** 0200 / 0210 Bill Payment - tells the biller the bill has been settled. */
    public GatewayResponse<GatewayBillResponse> billPayment(GatewayBillRequest request) {
        log.info("Bill payment to switch gateway | STAN:{} | RRN:{} | company:{}",
                request.getStan(), request.getRrn(), request.getUtilityCompanyCode());
        return call(billPaymentUrl, request, new TypeReference<GatewayResponse<GatewayBillResponse>>() {
        }, "bill payment");
    }

    private <T> GatewayResponse<T> call(String url, Object payload, TypeReference<GatewayResponse<T>> type,
                                        String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate().postForEntity(url, entity, String.class);
            String body = response.getBody();
            if (body == null || body.isBlank()) {
                log.error("Switch gateway returned an empty body for {} | url:{}", description, url);
                return null;
            }
            GatewayResponse<T> parsed = objectMapper.readValue(body, type);
            log.info("Switch gateway answered {} | code:{} | DE-39:{}",
                    description, parsed.getCode(), parsed.getResponseCode());
            return parsed;
        } catch (Exception e) {
            log.error("Switch gateway call failed for {} | url:{}", description, url, e);
            return null;
        }
    }

    private RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMs);
        factory.setReadTimeout(timeoutMs);
        return new RestTemplate(factory);
    }
}
