package com.dfs.agentapp.controller.accountupgrade;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.service.AccountUpgradeService;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.service.LoginService;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.GenericResponseCode;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountUpgradeController extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private AccountUpgradeService accountUpgradeService;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/v1/upgradeAccount")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest,request, Constants.AFTER_LOGIN);
        UpdateAccountLevelRequest updateAccountLevelRequest = fromJson(convertObjecttoJson(request.getPayload()), UpdateAccountLevelRequest.class);
        RequestValidator.validateUpdateAccountLevelRequest(updateAccountLevelRequest,request);
        UploadDocumentRequest uploadDocumentRequest=new UploadDocumentRequest();
        uploadDocumentRequest.setAccountLevelCode("L1");
        uploadDocumentRequest.setMobileNumber(updateAccountLevelRequest.getMobileNumber());
        List<Document> documentList=new ArrayList<>();
        Document tf=new Document();
        tf.setBase64("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");
        tf.setContentType("image/png");
        tf.setDocFileName("TF.png");
        Document tb=new Document();
        tb.setBase64("iVBORw0KGgoAAAANSUhEUgAAAS8AAADTCAYAAADQ+ZUOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAcUSURBVHhe7d1JqI/fH8Bx85C5FIpkHhMiM2XeSGyMZVgqiaRsDBnCliWJBbEUlsqwoWxEkSJzpszzcP6d07134cf//vp971Wfvq9XndX3fJ/v7t3znOc8z7dJAghIvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8gJDECwhJvICQxAsISbyAkMQLCEm8qNfBgwfT1KlT08iRI//zWLVqVbpx40Y53rdv39KpU6fS3Llzfzs3j8mTJ6elS5emXbt2pQsXLqSXL1+mnz9/lu9DJl7Ua926dalDhw6pSZMm/3mMHTs2Xbp0qRzvw4cPaffu3alTp06/nZtHs2bNUps2bVLnzp3T8OHD09atW9Pdu3fL9yETL+p17NixtHz58rRw4cJ/jDFjxpTI5OB07949zZkz57fzNm/enG7fvl2O9/79+7R9+/bUvn378r0pU6aktWvXpo0bN5axfv36tGjRotSvX78SsTy6detWzsLevn1bjgHiRb3evXuXHj9+nB4+fPiPceDAgRKWHKGZM2emy5cv/3be8+fP09evX8vxfo3Xli1b0oMHD9KbN2/KeP36dfm9ixcvpvnz56e2bduWef37909Xr14txwDxoiLHjx9PPXr0KHGZN29eun//fs0nf/ZrvPIlZA7Wr75//56uXLmShg0bVs6+mjdvno4cOZI+ffpUM4NqJl5UpDHjleXF/QULFqTWrVuXudu2bUuvXr2q+ZRqJl5UpLHjleX1ttpLx507d/7fuVQP8aIijR2vvE6WbwK0bNmyXDoePnw4ffz4seZTqpl4UZHGXvM6c+ZM6tOnT2ratGkaOnRoWbC334tMvKhIY8QrR+vRo0fpxIkTZRtFXu/q2rVr2rt3b7nzCZl4UZGGiNfAgQPTjBkzyo77PGbPnp0mTpyYevXqVe4wDh48OO3fvz89efKk5gggXlSoIeJV3+jSpUtauXJl2ff15cuXmqNQ7cSLijREvEaPHl2eY1yxYkXdWLJkSTkDy7vsW7VqVeaOHz8+nTx5snwfxIuKNES8anfY53Wv2vHs2bN08+bNdPbs2bR48eLUrl27cgk5ZMiQdO7cubrd+lQv8aIijbFg/6t8h3HatGmpRYsWZf7q1avT06dPaz6lWokXFfkb8crzd+zYUTd/1KhR6fr16+nHjx81M6hG4kVF/ka88r6uQ4cOldfj5PkDBgwoi/d5SwXVS7yoyN+IV5Z31tfGK79d4vz58+W5R6qXeFGRvxGv/DjQnj176l6ImO9O3rp1y077KideVORvxCu/Bnr69Onl+cY8f8OGDenFixc1n1KtxIuKNES8Nm3aVBbg7927Vzfu3LlT3uWV358/a9asurmDBg0ql4y2SiBeVKQh4tW7d+80bty4NGnSpLoxYcKENGLEiPJMY97flef17ds3HT161PONFOJFRfKO9549e5a3PuR31edXPtcn/wFHXsOqXYD/08iXiR07dizPOOYXEp4+fbqEz1oXmXhRkWvXrpU/zcjPHubtDP/mDzLyFoe81SH/K9GyZcv+ONasWZP27dtXopUvJT9//lxzBBAvKpS3K+Rg5Vcz57uC//asKK9Z5T/byN/708if57O0PNfZFr8SLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyAk8QJCEi8gJPECQhIvICTxAkISLyCglP4HCtC33gfjoScAAAAASUVORK5CYII=");
        tb.setContentType("image/png");
        tb.setDocFileName("TB.png");
        documentList.add(tf);
        documentList.add(tb);
        uploadDocumentRequest.setDocuments(documentList);
        documentService.uploadDocument(uploadDocumentRequest,request,userId);
        HashMap<String, Object> response=accountUpgradeService.upgradeToL2(updateAccountLevelRequest,request,userId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @PostMapping(value = "/v1/updateDevice", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> updateDevice(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        UpdateDeviceRequest updateDeviceRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), UpdateDeviceRequest.class);
        RequestValidator.validateUpdateDeviceRequest(updateDeviceRequest);
        HashMap<String, Object> response = loginService.updateDeviceRegistration(updateDeviceRequest, apiRequest, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }

    @PostMapping(value = "/v1/verifyUpdateDevice", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> verifyUpdateDevice(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        VerifyDeviceRequest verifyDeviceRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), VerifyDeviceRequest.class);
        RequestValidator.validateVerifyDeviceRequest(verifyDeviceRequest);
        HashMap<String, Object> response = loginService.verifyUpdateDeviceRegistration(verifyDeviceRequest, apiRequest, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }

    @PostMapping(value = "/v1/resetpin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> resetPin(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        ResetPasswordRequest resetPasswordRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), ResetPasswordRequest.class);
        RequestValidator.validateResetPasswordRequestRequest(resetPasswordRequest);
        HashMap<String, Object> response = null;
        if (resetPasswordRequest.getStep().equals("1")) {
            response = loginService.generateOtpToResetPassword(resetPasswordRequest, apiRequest, httpServletRequest);
        } else if (resetPasswordRequest.getStep().equals("2")) {
            commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
            response = loginService.verifyOtp(resetPasswordRequest, apiRequest, httpServletRequest);

        } else if (resetPasswordRequest.getStep().equals("3")) {
            commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
            response = loginService.resetPassword(resetPasswordRequest, apiRequest, httpServletRequest);
        } else {
            response = new HashMap<>();
            response.put("responsecode", GenericResponseCode.INVALID_STEP.getResponseCode());
            response.put("messages", GenericResponseCode.INVALID_STEP.getResponseMessage());
            response.put("data", null);
        }
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }

}
