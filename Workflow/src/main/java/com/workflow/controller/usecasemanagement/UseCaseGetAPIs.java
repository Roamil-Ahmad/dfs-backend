package com.workflow.controller.usecasemanagement;

import com.workflow.controller.AbstractApi;
import com.workflow.dto.Response;
import com.workflow.modal.LkpMcFormName;
import com.workflow.modal.TblMcConfig;
import com.workflow.modal.TblMcConfigDetail;
import com.workflow.service.UseCaseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UseCaseGetAPIs extends AbstractApi {
    @Autowired
    UseCaseManagementService useCaseManagementService;

    //Get Use Case By Use Case ID
    @GetMapping(value = "/getUseCaseById/{useCaseId}")
    public ResponseEntity<Response> getUseCaseById(@PathVariable long useCaseId, HttpServletRequest request) {
        List<TblMcConfigDetail> tblMcConfigDetails = new ArrayList<>();
        TblMcConfig tblMcConfigs = useCaseManagementService.getUseCaseById(useCaseId);
        List<TblMcConfigDetail> tblMcConfigDetailList = useCaseManagementService.getMcConfigDetailByMcConfigId(tblMcConfigs.getMcConfigId());
        for (TblMcConfigDetail tblMcConfigDetail : tblMcConfigDetailList) {
            if (tblMcConfigDetail.getTblUser() != null) {
                tblMcConfigDetail.setApprovalCriteria("U");
            } else {
                tblMcConfigDetail.setApprovalCriteria("R");
            }
            tblMcConfigDetails.add(tblMcConfigDetail);
        }
        tblMcConfigs.setTblMcConfigDetails(tblMcConfigDetails);

        if (!isNullOrEmpty(tblMcConfigs)) {
            return getResponseFormat(HttpStatus.OK, "Record Found", tblMcConfigs);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", tblMcConfigs);
        }
    }

    @GetMapping(value = "/getprocessname")
    public ResponseEntity<Response> getprocessname(HttpServletRequest request) {
        List<LkpMcFormName> lkpMcFormNames = useCaseManagementService.getprocessname();
        if (!isNullOrEmpty(lkpMcFormNames)) {
            return getResponseFormat(HttpStatus.OK, "Record Found", lkpMcFormNames);
        } else {
            return getResponseFormat(HttpStatus.OK, "Record Not Found", lkpMcFormNames);
        }
    }
}
