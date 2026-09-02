package com.dfs.agentapp.controller.document;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
