package com.workflow.controller.usecasemanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.workflow.controller.AbstractApi;
import com.workflow.dto.Response;
import com.workflow.dto.UseCaseRequest;
import com.workflow.dto.UseCaseSearch;
import com.workflow.service.UseCaseManagementService;
import com.workflow.utils.Constants;
import com.workflow.utils.JwtConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UseCasePostAPIs extends AbstractApi {
    @Autowired
    UseCaseManagementService useCaseManagementService;

    //Save Use Case
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = "/createusecase", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> saveusecase(@Valid @RequestBody UseCaseRequest data, HttpServletRequest request) throws JsonProcessingException {
        return useCaseManagementService.processSaveUseCase(request, data);
    }

    //Update Use Case
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = "/updateusecase", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateusecase(@Valid @RequestBody UseCaseRequest data, HttpServletRequest
            request) throws JsonProcessingException {
        return useCaseManagementService.processUpdateUseCase(request, data);
    }

    //Inactive Use Case
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = "/inactiveusecase", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> inactiveusecase(@Valid @RequestBody UseCaseRequest data, HttpServletRequest request) throws
            JsonProcessingException {
        return useCaseManagementService.processInactiveUseCase(request, data, new BigDecimal ((String) request.getAttribute(JwtConstants.APP_USER_ID)));

    }

    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = "/getallusecases")
    public ResponseEntity<Response> getallusecases(@Valid @RequestBody UseCaseSearch data, HttpServletRequest
            request) {
        return useCaseManagementService.getAllUseCasesByFilter(data);
    }

}
