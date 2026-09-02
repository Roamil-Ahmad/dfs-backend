package com.dfs.app.controller.document;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.controller.login.LoginController;
import com.dfs.app.dto.UploadDocumentRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.DocumentService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController extends HelperClass {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private CommonService commonService;


    @PostMapping(value ="/v1/uploadDocument", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> mpinVerification(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest,request, Constants.AFTER_LOGIN);
        UploadDocumentRequest uploadDocumentRequest = fromJson(convertObjecttoJson(request.getPayload()), UploadDocumentRequest.class);
        RequestValidator.validateUploadDocumentRequest(uploadDocumentRequest,request);
        HashMap<String, Object> response=documentService.uploadDocument(uploadDocumentRequest,request,userId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }
}
