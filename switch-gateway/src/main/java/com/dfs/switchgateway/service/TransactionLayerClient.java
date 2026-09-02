package com.dfs.switchgateway.service;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.dto.TransactionLayerResponse;
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
 * Carries switch-originated (incoming) messages to the DFS transaction layer and brings its answer
 * back. All services run on the same host, so the call is direct - there is no proxy.
 *
 * This class transports; it decides nothing. A failure is logged and reported as a null answer, and
 * is never retried here.
 */
@Component
public class TransactionLayerClient {

    private static final Logger log = LoggerFactory.getLogger(TransactionLayerClient.class);

    @Value("${transaction.layer.incoming.title-fetch.url}")
    private String incomingTitleFetchUrl;

    @Value("${transaction.layer.incoming.advice.url}")
    private String incomingAdviceUrl;

    @Value("${transaction.layer.timeout-ms}")
    private int timeoutMs;

    public BasePDU incomingTitleFetch(BasePDU request) {
        return post(incomingTitleFetchUrl, request, "incoming title fetch");
    }

    public BasePDU incomingAdvice(BasePDU request) {
        return post(incomingAdviceUrl, request, "incoming IBFT advice");
    }

    private BasePDU post(String url, BasePDU request, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BasePDU> entity = new HttpEntity<>(request, headers);

            log.info("Calling transaction layer for {} | url:{} | STAN:{} | RRN:{}",
                    description, url, request.getStan(), request.getRrn());

            ResponseEntity<TransactionLayerResponse> response =
                    restTemplate().postForEntity(url, entity, TransactionLayerResponse.class);
            TransactionLayerResponse envelope = response.getBody();

            // The transaction layer answers with its standard envelope; the message to send back
            // down the link is the data member.
            if (envelope == null || envelope.getData() == null) {
                log.error("Transaction layer answered {} with an empty body | STAN:{} | RRN:{} | responsecode:{}",
                        description, request.getStan(), request.getRrn(),
                        envelope == null ? null : envelope.getResponsecode());
                return null;
            }
            BasePDU body = envelope.getData();
            log.info("Transaction layer answered {} | STAN:{} | RRN:{} | responsecode:{} | DE-39:{}",
                    description, body.getStan(), body.getRrn(), envelope.getResponsecode(),
                    body.getResponseCode());
            return body;
        } catch (Exception e) {
            log.error("Transaction layer call failed for {} | url:{} | STAN:{} | RRN:{}",
                    description, url, request.getStan(), request.getRrn(), e);
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
