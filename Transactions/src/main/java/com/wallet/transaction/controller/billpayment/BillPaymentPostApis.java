package com.wallet.transaction.controller.billpayment;

import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.GetSavedBillsRequest;
import com.wallet.transaction.dto.SaveTemplateRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.BillPaymentService;
import com.wallet.transaction.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillPaymentPostApis extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private BillPaymentService billPaymentService;
    @PostMapping("/v1/savetemplate")
    public ResponseEntity<HashMap<String, Object>> savetemplate(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        SaveTemplateRequest getSavedBillsRequest = fromJson(convertObjecttoJson(request.getPayload()), SaveTemplateRequest.class);
        HashMap<String, Object> response = billPaymentService.savetemplate(getSavedBillsRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/linkcard");

    }
}
