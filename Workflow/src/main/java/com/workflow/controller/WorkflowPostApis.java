/*Author Name:muhammad.kashif
Project Name: makerchecker
Package Name:com.workflow.makerchecker.controller
Class Name: makerCheckerPostApis
Date and Time:2/17/2023 11:00 AM
Version:1.0*/
package com.workflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.dto.*;
import com.workflow.service.WorkflowService;
import com.workflow.utils.Constants;
import com.workflow.utils.JwtConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = Constants.S_1, allowedHeaders = Constants.S_1)
@RestController
@RestControllerAdvice
public class WorkflowPostApis extends AbstractApi {

    @Autowired
    private WorkflowService workflowService;

    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.CKECK_MC_APPLICABLE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> ckeckMcApplicable(@Valid @RequestBody Request data, HttpServletRequest request) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            McApplicable checkerApplicable = objectMapper.readValue(convertObjecttoJson(data.getPayLoad()), McApplicable.class);
            ProcedureResponse checkMcApplicability = workflowService.checkMcApplicability(checkerApplicable.getTableName(), checkerApplicable.getFormName(), checkerApplicable.getRequestType());
            if (checkMcApplicability != null) {
                return getResponseFormat(HttpStatus.OK, checkMcApplicability.getStatusDescr(), checkMcApplicability);
            } else {
                return getResponseFormat(HttpStatus.BAD_REQUEST, "No Response From Workflow", checkMcApplicability);
            }
        } catch (Exception e) {
            LOG.error("\n CLASS == WorkflowPostApis \n METHOD == ckeckMcApplicable();  ERROR ----- "
                    + e.getLocalizedMessage());
            LOG.info("EXITING THIS METHOD == ckeckMcApplicable(); \n\n\n");
            return getResponseFormat(HttpStatus.NOT_FOUND, "General Processing Error", null);
        }
    }

    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.MC_REQUEST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> mcRequest(@Valid @RequestBody Request data, HttpServletRequest request) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            McRequestDto mcRequestDto = objectMapper.readValue(convertObjecttoJson(data.getPayLoad()), McRequestDto.class);
            McResponse mcRequestResponse = workflowService.parkRequestToChecker(mcRequestDto.getFormName(), mcRequestDto.getMakerId(), mcRequestDto.getMakerComments(), mcRequestDto.getFtFlag(), mcRequestDto.getTableName(), mcRequestDto.getRequestType(), mcRequestDto.getUpdateType(), mcRequestDto.getRefTableId(), mcRequestDto.getUpdateJson(), new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)), mcRequestDto.getOldJson());
            if (mcRequestResponse != null) {
                if (mcRequestResponse.getStatus() == 1) {
                    return getResponseFormat(HttpStatus.OK, mcRequestResponse.getStatusDecsr(), mcRequestResponse);
                } else {
                    return getResponseFormat(HttpStatus.OK, mcRequestResponse.getStatusDecsr(), mcRequestResponse);
                }
            } else {
                return getResponseFormat(HttpStatus.BAD_REQUEST, "No Response From Workflow", mcRequestResponse);
            }
        } catch (Exception e) {
            LOG.error("\n CLASS == WorkflowPostApis \n METHOD == mcRequest();  ERROR ----- "
                    + e.getLocalizedMessage());
            LOG.info("EXITING THIS METHOD == mcRequest(); \n\n\n");
            return getResponseFormat(HttpStatus.NOT_FOUND, "General Processing Error", null);
        }
    }

    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.MC_ACTION, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> mcAction(@Valid @RequestBody Request data, HttpServletRequest request) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            McActionRequest actionRequest = objectMapper.readValue(convertObjecttoJson(data.getPayLoad()), McActionRequest.class);
            McActionResponse mcActionResponse = workflowService.mcAction(actionRequest);
            if (mcActionResponse != null) {
                if (mcActionResponse.getStatus() == 1) {
                    return getResponseFormat(HttpStatus.OK, mcActionResponse.getStatusDecsr(), mcActionResponse);
                } else {
                    return getResponseFormat(HttpStatus.OK, mcActionResponse.getStatusDecsr(), mcActionResponse);
                }
            } else {
                return getResponseFormat(HttpStatus.BAD_REQUEST, "No Response From Workflow", mcActionResponse);
            }
        } catch (Exception e) {
            LOG.error("\n CLASS == WorkflowPostApis \n METHOD == mcAction();  ERROR ----- "
                    + e.getLocalizedMessage());
            LOG.info("EXITING THIS METHOD == mcAction(); \n\n\n");
            return getResponseFormat(HttpStatus.NOT_FOUND, "General Processing Error", null);
        }
    }

    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.GETALLMYAUTHORIZATIONSREQUESTS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getallmyauthorizationsrequests(@Valid @RequestBody Request data, HttpServletRequest request) throws JsonProcessingException, SQLException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CheckerSearch checkerSearch = objectMapper.readValue(convertObjecttoJson(data.getPayLoad()), CheckerSearch.class);
            List<CheckerSearchResponse> checkerSearchResponses = workflowService.getallmyauthorizationsrequests(checkerSearch, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            if (checkerSearchResponses != null && !checkerSearchResponses.isEmpty()) {
                return getResponseFormat(HttpStatus.OK, "Record Found", checkerSearchResponses);
            } else {
                return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", checkerSearchResponses);
            }
        } catch (Exception e) {
            LOG.error("\n CLASS == WorkflowPostApis \n METHOD == mcAction();  ERROR ----- "
                    + e.getLocalizedMessage());
            LOG.info("EXITING THIS METHOD == mcAction(); \n\n\n");
            return getResponseFormat(HttpStatus.NOT_FOUND, "General Processing Error", null);
        }
    }

}