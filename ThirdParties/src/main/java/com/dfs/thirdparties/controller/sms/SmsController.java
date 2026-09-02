package com.dfs.thirdparties.controller.sms;

import com.dfs.thirdparties.commons.HelperClass;
import com.dfs.thirdparties.dto.SmsRequestDto;
import com.dfs.thirdparties.dto.common.Request;
import com.dfs.thirdparties.model.TblGlobalConfig;
import com.dfs.thirdparties.model.TblSmsMessage;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.service.OtpService;
import com.dfs.thirdparties.util.JWTSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SmsController extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(SmsController.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    JWTSecurity jwtSecurity;
    @Autowired
    private OtpService otpService;


    @GetMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        SmsRequestDto smsRequestDto = fromJson(convertObjecttoJson(request.getPayload()), SmsRequestDto.class);
        try {
            boolean smsResponse = sendSms(smsRequestDto.getMobileNumber(), smsRequestDto.getMessage());
            if (smsResponse) {
                saveMessage(smsRequestDto.getMobileNumber(), smsRequestDto.getMessageTemplateId(), userId, "1");
                return ResponseEntity.ok("SMS Sent Successfully");
            } else {
                saveMessage(smsRequestDto.getMobileNumber(), smsRequestDto.getMessageTemplateId(), userId, "0");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send SMS");
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveMessage(smsRequestDto.getMobileNumber(), smsRequestDto.getMessageTemplateId(), userId, "0");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while sending SMS");
        }
    }

    private void saveMessage(String mobileNumber, String messageTemplateId, BigDecimal userId, String flag) {
        TblSmsMessage tblSmsMessage = new TblSmsMessage();
        tblSmsMessage.setMobileNo(mobileNumber);
        tblSmsMessage.setSmsMessageTemplateId(new BigDecimal(messageTemplateId));
        tblSmsMessage.setSendFlag(flag);
        tblSmsMessage.setCreatedate(new Date());
        tblSmsMessage.setCreateuser(userId);
        TblSmsMessage saveMessage = otpService.saveMessage(tblSmsMessage);
    }

    private boolean sendSms(String mobileNumber, String message) {
        try {
            TblGlobalConfig getCountryCode = otpService.findByKeyName("COUNTRY_CODE");
            String updatedNumber = getCountryCode.getKeyValue() + mobileNumber.substring(1);
            System.out.println("SMS Sent Successfully! SID: " + updatedNumber);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
