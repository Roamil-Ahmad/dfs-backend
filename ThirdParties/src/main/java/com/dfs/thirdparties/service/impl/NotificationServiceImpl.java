package com.dfs.thirdparties.service.impl;

import com.dfs.thirdparties.dto.GenerateNotificationRequest;
import com.dfs.thirdparties.model.TblGlobalConfig;
import com.dfs.thirdparties.model.TblSmsMessage;
import com.dfs.thirdparties.model.TblSmsMessageTemplate;
import com.dfs.thirdparties.repo.TblGlobalConfigRepo;
import com.dfs.thirdparties.repo.TblSmsMessageRepo;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.service.NotificationService;
import com.dfs.thirdparties.util.CustomException;
import com.dfs.thirdparties.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;
    @Autowired
    private CommonService commonService;


    @Override
    public HashMap<String, Object> notify(GenerateNotificationRequest generateNotificationRequest, BigDecimal userId) {
        HashMap<String, Object> response = new HashMap<>();
        if(generateNotificationRequest.getType().equalsIgnoreCase("E")){
       if(sendSimpleEmail(generateNotificationRequest.getEmail(),generateNotificationRequest.getSubject(),generateNotificationRequest.getSms())){
           response = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
       }else {
           throw new CustomException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
       }
        }else if(generateNotificationRequest.getType().equalsIgnoreCase("M")) {
            TblSmsMessage tblSmsMessage = new TblSmsMessage();
            tblSmsMessage.setMobileNo(generateNotificationRequest.getMobileNumber());
            tblSmsMessage.setSendFlag("1");
            tblSmsMessage.setCreatedate(new Date());
            tblSmsMessage.setCreateuser(userId);
            tblSmsMessage.setSmsMessageTemplateId(generateNotificationRequest.getTemplateId());
            tblSmsMessage.setMessage(generateNotificationRequest.getSms());
            tblSmsMessageRepo.saveAndFlush(tblSmsMessage);
            response = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        }
        return response;
    }
    @Override
    public boolean sendSimpleEmail(String to, String subject, String text) {
        boolean flag = false;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
