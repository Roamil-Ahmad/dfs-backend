package com.dfs.app.controller.common;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.UploadDocumentRequest;
import com.dfs.app.service.CommonService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommonController  extends HelperClass {
    @Autowired
    private CommonService commonService;
    @GetMapping(value = "/v1/getappscreendata/{name}/{languageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getAppScreenData(@PathVariable("name")String name, @PathVariable("languageId")String languageId){
        RequestValidator.validateGetAppScreenDataRequest(name,languageId);
        HashMap<String, Object> response=commonService.getAppScreenData(name,languageId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping(value = "/v1/getfaqs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getAppScreenData(){
        HashMap<String, Object> response=commonService.getFaqs();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }


    @GetMapping(value = "/v1/gettutorials", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getTutorials(){
        HashMap<String, Object> response=commonService.getTutorials();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping(value = "/v1/contactus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> contactUs(){
        HashMap<String, Object> response=commonService.contactUs();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping(value = "/v1/taxCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> taxCertificate(){
        HashMap<String, Object> response=commonService.taxCertificate();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping(value = "/v1/notification/{appUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> notification(@PathVariable("appUserId") String appUserId){
        HashMap<String, Object> response=commonService.notification(appUserId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping(value = "/v1/spending/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> spending(@PathVariable("accountId") String accountId){
        HashMap<String, Object> response=commonService.spending(accountId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping(value = "/v1/appMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> appMenu(){
        HashMap<String, Object> response=commonService.appMenu();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }
}
