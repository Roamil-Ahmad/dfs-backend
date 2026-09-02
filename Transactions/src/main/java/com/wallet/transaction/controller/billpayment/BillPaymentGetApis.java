package com.wallet.transaction.controller.billpayment;

import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.CardLinkingRequest;
import com.wallet.transaction.dto.GetSavedBillsRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.BillPaymentService;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillPaymentGetApis extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private BillPaymentService billPaymentService;
    @PostMapping("/v1/getsavedbills")
    public ResponseEntity<HashMap<String, Object>> getsavedbills(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        GetSavedBillsRequest getSavedBillsRequest = fromJson(convertObjecttoJson(request.getPayload()), GetSavedBillsRequest.class);
        HashMap<String, Object> response = billPaymentService.getsavedbills(getSavedBillsRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getbeneficiaries");

    }

    @GetMapping("/v1/getbiller")
    public ResponseEntity<HashMap<String, Object>> getbiller() {
        HashMap<String, Object> response = billPaymentService.getbiller();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getbiller");

    }

}
