package com.dfs.switchgateway.controller;

import com.dfs.switchgateway.dto.EchoDto;
import com.dfs.switchgateway.dto.IbftAdviceRqst;
import com.dfs.switchgateway.dto.TSDtos.BillRqst;
import com.dfs.switchgateway.dto.TSDtos.OTIBFTTitleFetchRequest;
import com.dfs.switchgateway.dto.TSDtos.ResponseDto;
import com.dfs.switchgateway.dto.TSDtos.responseDto.BillResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.EchoResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.IBFTAdviceResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.IBFTTitleFetchResponse;
import com.dfs.switchgateway.service.SwitchRouteService;
import com.dfs.switchgateway.utils.Constant;
import com.dfs.switchgateway.validations.CommunicationServiceValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The gateway's only inbound surface. It carries messages to the switch and nothing else - the
 * IBFT business APIs live in the transaction layer.
 */
@RestController
@RequestMapping("/outgoing")
public class SwitchGatewayController {

    private static final Logger log = LoggerFactory.getLogger(SwitchGatewayController.class);

    @Autowired
    private SwitchRouteService switchRouteService;

    @PostMapping(value = "/sign-in", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<EchoResponse> signIn(@RequestBody EchoDto request) {
        log.info("Sign-on requested | STAN:{}", request.getStan());
        return switchRouteService.networkManagement(request);
    }

    @PostMapping(value = "/sign-off", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<EchoResponse> signOff(@RequestBody EchoDto request) {
        log.info("Sign-off requested | STAN:{}", request.getStan());
        return switchRouteService.networkManagement(request);
    }

    @PostMapping(value = "/echo", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<EchoResponse> echo(@RequestBody EchoDto request) {
        log.debug("Echo requested | STAN:{}", request.getStan());
        return switchRouteService.networkManagement(request);
    }

    @PostMapping(value = "/title-fetch", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<IBFTTitleFetchResponse> titleFetch(@RequestBody OTIBFTTitleFetchRequest request) {
        log.info("Title fetch received | STAN:{} | RRN:{}", request.getStan(), request.getRrn());
        try {
            if (request.getTransactionDateTime() == null) {
                request.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
            }
            return switchRouteService.titleFetch(request);
        } catch (CommunicationServiceValidationException e) {
            log.warn("Title fetch rejected by validation | STAN:{} | {}", request.getStan(), e.getMessage());
            return failure(e.getMessage());
        } catch (Exception e) {
            log.error("Title fetch failed | STAN:{} | RRN:{}", request.getStan(), request.getRrn(), e);
            return failure(e.getMessage());
        }
    }

    @PostMapping(value = "/advice", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<IBFTAdviceResponse> advice(@RequestBody IbftAdviceRqst request) {
        log.info("IBFT advice received | STAN:{} | RRN:{}", request.getStan(), request.getRrn());
        try {
            return switchRouteService.advice(request);
        } catch (CommunicationServiceValidationException e) {
            log.warn("IBFT advice rejected by validation | STAN:{} | {}", request.getStan(), e.getMessage());
            return failure(e.getMessage());
        } catch (Exception e) {
            log.error("IBFT advice failed | STAN:{} | RRN:{}", request.getStan(), request.getRrn(), e);
            return failure(e.getMessage());
        }
    }

    @PostMapping(value = "/bill-inquiry", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<BillResponse> billInquiry(@RequestBody BillRqst request) {
        log.info("Bill inquiry received | STAN:{} | RRN:{} | company:{}",
                request.getStan(), request.getRrn(), request.getUtilityCompanyCode());
        try {
            if (request.getTransactionDateTime() == null) {
                request.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
            }
            return switchRouteService.billInquiry(request);
        } catch (CommunicationServiceValidationException e) {
            log.warn("Bill inquiry rejected by validation | STAN:{} | {}", request.getStan(), e.getMessage());
            return failure(e.getMessage());
        } catch (Exception e) {
            log.error("Bill inquiry failed | STAN:{} | RRN:{}", request.getStan(), request.getRrn(), e);
            return failure(e.getMessage());
        }
    }

    @PostMapping(value = "/bill-payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<BillResponse> billPayment(@RequestBody BillRqst request) {
        log.info("Bill payment received | STAN:{} | RRN:{} | company:{}",
                request.getStan(), request.getRrn(), request.getUtilityCompanyCode());
        try {
            if (request.getTransactionDateTime() == null) {
                request.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
            }
            return switchRouteService.billPayment(request);
        } catch (CommunicationServiceValidationException e) {
            log.warn("Bill payment rejected by validation | STAN:{} | {}", request.getStan(), e.getMessage());
            return failure(e.getMessage());
        } catch (Exception e) {
            log.error("Bill payment failed | STAN:{} | RRN:{}", request.getStan(), request.getRrn(), e);
            return failure(e.getMessage());
        }
    }

    private <T> ResponseDto<T> failure(String message) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setCode(Constant.BAD_REQUEST_CODE);
        response.setMessage(message);
        return response;
    }
}
