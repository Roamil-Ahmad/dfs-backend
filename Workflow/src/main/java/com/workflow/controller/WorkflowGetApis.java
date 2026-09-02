/*
Author Name:muhammad.kashif

Project Name: Workflow

Package Name:com.workflow.workflow.Controller

Class Name: WorkflowGetApis

Date and Time:2/24/2023 3:22 PM

Version:1.0

*/
package com.workflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.dto.*;
import com.workflow.modal.LkpStatus;
import com.workflow.modal.TblMcConfig;
import com.workflow.modal.TblMcRequest;
import com.workflow.service.WorkflowService;
import com.workflow.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = Constants.S_1, allowedHeaders = Constants.S_1)
@RestController
@RestControllerAdvice
public class WorkflowGetApis extends AbstractApi {

    @Autowired
    private WorkflowService workflowService;

    @GetMapping(value = Constants.GET_ALL_MC_REQUEST_AGAINST_USER_ID_USER_ID)
    public ResponseEntity<Response> getAllMcRequestAgainstUserId(@PathVariable String userId) {

        List<McRequestResponse> mcRequests = workflowService.getAllMcRequestAgainstUserId(userId);
        if (mcRequests != null && !mcRequests.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", mcRequests);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", mcRequests);
        }
    }


    @GetMapping(value = Constants.GET_PENDIG_MC_REQUEST_AGAINST_USER_ID_USER_ID)
    public ResponseEntity<Response> getPendigMcRequestAgainstUserId(@PathVariable String userId) {

        List<McPendingRequestResponse> mcRequests = workflowService.getPendigMcRequestAgainstUserId(userId);
        if (mcRequests != null && !mcRequests.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", mcRequests);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", mcRequests);
        }
    }

    @GetMapping(value = Constants.GET_APPROVED_MC_REQUEST_AGAINST_USER_ID)
    public ResponseEntity<Response> getApprovedMcRequestAgainstUserId(@PathVariable String userId) {

        List<McApprovedRequestResponse> mcRequests = workflowService.getApprovedMcRequestAgainstUserId(userId);
        if (mcRequests != null && !mcRequests.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", mcRequests);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", mcRequests);
        }
    }

    @GetMapping(value = Constants.GET_REJECTED_MC_REQUEST_AGAINST_USER_ID)
    public ResponseEntity<Response> getRejectedMcRequestAgainstUserId(@PathVariable String userId) {

        List<McRejectRequestResponse> mcRequests = workflowService.getRejectedMcRequestAgainstUserId(userId);
        if (mcRequests != null && !mcRequests.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", mcRequests);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", mcRequests);
        }
    }

    @GetMapping(value = Constants.GET_ACTION_AUTHORIZATION_HISTORY)
    public ResponseEntity<Response> getActionAuthorizationHistory(@PathVariable String refTableId, @PathVariable String tableName) {

        List<AuthorizationHistoryResponse> actionAuthorizationHistory = workflowService.getActionAuthorizationHistory(refTableId, tableName);
        if (actionAuthorizationHistory != null && !actionAuthorizationHistory.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", actionAuthorizationHistory);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", actionAuthorizationHistory);
        }
    }

    @GetMapping(value = Constants.COMPARE_VALUES_MC_REQUEST_ID)
    public ResponseEntity<Response> compareValues(@PathVariable String mcRequestId) {

        TblMcRequest mcRequests = workflowService.getMcRequestByMcRequestId(mcRequestId);
        if (mcRequests != null) {
            String oldJsonStr = mcRequests.getOldJson();
            String newJsonStr = mcRequests.getUpdateJson();
            List<CompareJson> compareJson = compareJsons(oldJsonStr, newJsonStr);
            if (compareJson != null && !compareJson.isEmpty()) {
                return getResponseFormat(HttpStatus.OK, "Record Found", compareJson);
            } else {
                return getResponseFormat(HttpStatus.OK, "Record Not Found", compareJson);
            }
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", null);
        }
    }

    public static List<CompareJson> compareJsons(String oldJsonStr, String newJsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode oldJsonNode = mapper.readTree(oldJsonStr);
            JsonNode newJsonNode = mapper.readTree(newJsonStr);

            List<CompareJson> differences = new ArrayList<>();
            compareJsonNodes(oldJsonNode, newJsonNode, differences);

            return differences;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void compareJsonNodes(JsonNode oldNode, JsonNode newNode, List<CompareJson> differences) {
        if (oldNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = oldNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                String key = field.getKey();
                if ("isActive".equals(key) || "statusId".equals(key) || "createdate".equals(key) || "createuser".equals(key) || "createUser".equals(key) || "lastupdatedate".equals(key) || "lastupdateuser".equals(key) || "updateindex".equals(key) || "lkpStatus".equals(key) || "createdBy".equals(key) || "updatedBy".equals(key)) {
                    continue; // Skip "isActive" and "statusId" fields
                }
                JsonNode oldValueNode = field.getValue();
                JsonNode newValueNode = newNode.get(key);
                if (newValueNode != null) {
                    if (!oldValueNode.equals(newValueNode)) {
                        differences.add(new CompareJson(key, oldValueNode.asText(), newValueNode.asText()));
                    }
                    compareJsonNodes(oldValueNode, newValueNode, differences); // Recursively compare nested objects
                }
            }
        } else if (oldNode.isArray()) {
            Iterator<JsonNode> oldElements = oldNode.elements();
            Iterator<JsonNode> newElements = newNode.elements();

            while (oldElements.hasNext() && newElements.hasNext()) {
                JsonNode oldElement = oldElements.next();
                JsonNode newElement = newElements.next();
                compareJsonNodes(oldElement, newElement, differences);
            }
        }
    }

    @GetMapping(value = "/getMcRequestById/{mcRequestId}")
    public ResponseEntity<Response> getMcRequestById(@PathVariable String mcRequestId, HttpServletRequest request) {
        String methodName = getCurrentMethodName();
        try {
            TblMcRequest mcRequest = workflowService.getMcRequestByMcRequestId(mcRequestId);
            if (mcRequest != null && mcRequest.getUpdateJson() != null) {
                LOG.info("\n\n\n EXITING THIS METHOD == " + methodName + " \nOF CLASS = " + this.getClass().getSimpleName() + " \n\n\n");
                return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", mcRequest.getUpdateJson());
            } else {
                LOG.info("\n\n\n EXITING THIS METHOD == " + methodName + " \nOF CLASS = " + this.getClass().getSimpleName() + " \n\n\n");
                return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", null);
            }
        } catch (Exception e) {
            LOG.error("\n CLASS == UserGetApi \n METHOD == " + methodName + "();  ERROR ----- "
                    + e.getLocalizedMessage());
            LOG.info("\n EXITING THIS METHOD == " + methodName + "(); \n\n\n");
            return getResponseFormat(HttpStatus.NOT_FOUND, "General Processing Error", null);
        }
    }


    @RequestMapping(value = "/getRequestTypes", method = RequestMethod.GET)
    public ResponseEntity<Response> getRequestTypes() {

        String methodName = getCurrentMethodName();
        List<TblMcConfig> tblMcConfigs = workflowService.getAllRequestTypes();
        if (tblMcConfigs != null && !tblMcConfigs.isEmpty()) {
            LOG.info("\n\n\n EXITING THIS METHOD == " + methodName + " \nOF CLASS = " + this.getClass().getSimpleName() + " \n\n\n");
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", tblMcConfigs);
        } else {
            LOG.info("\n\n\n EXITING THIS METHOD == " + methodName + " \nOF CLASS = " + this.getClass().getSimpleName() + " \n\n\n");
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", null);
        }
    }

    @RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    public ResponseEntity<Response> getStatus() throws JsonProcessingException {
        String methodName = getCurrentMethodName();
        List<LkpStatus> lkpStatuses = workflowService.getAllStatus();
        if (lkpStatuses != null && !lkpStatuses.isEmpty()) {
            LOG.info("\n\n\n EXITING THIS METHOD == " + methodName + " \nOF CLASS = " + this.getClass().getSimpleName() + " \n\n\n");
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", lkpStatuses);
        } else {
            LOG.info("\n\n\n EXITING THIS METHOD == " + methodName + " \nOF CLASS = " + this.getClass().getSimpleName() + " \n\n\n");
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", null);
        }
    }
}