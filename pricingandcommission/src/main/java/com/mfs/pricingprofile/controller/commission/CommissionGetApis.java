/*
Author Name:muhammad.kashif

Project Name: limits

Package Name:com.mfs.limits.Controller

Class Name: LimitsGetApis

Date and Time:5/14/2023 2:55 PM

Version:1.0

*/
package com.mfs.pricingprofile.controller.commission;

import com.mfs.pricingprofile.controller.AbstractApi;
import com.mfs.pricingprofile.controller.pricingprofile.PricingProfileGetApis;
import com.mfs.pricingprofile.dto.CommissionProfileData;
import com.mfs.pricingprofile.dto.Response;
import com.mfs.pricingprofile.model.TblCommissionDoc;
import com.mfs.pricingprofile.model.TblCommissionProfile;
import com.mfs.pricingprofile.model.TblCommissionSlab;
import com.mfs.pricingprofile.service.CommissionService;
import com.mfs.pricingprofile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = Constants.S_1, allowedHeaders = Constants.S_1)
@RestController
@RestControllerAdvice
public class CommissionGetApis extends AbstractApi {

    @Autowired
    private CommissionService commissionService;
    @Autowired
    private PricingProfileGetApis pricingProfileGetApis;

    @GetMapping(value = Constants.GET_COMMISSION_BY_ID)
    public ResponseEntity<Response> getCommissionById(@PathVariable String commissionProfileId, HttpServletRequest request) {
        TblCommissionProfile tblCommissionProfile = commissionService.getCommissionProfileById(Long.valueOf(commissionProfileId));
        if (tblCommissionProfile != null) {
            List<TblCommissionDoc> tblCommissionDocs = commissionService.getTblCommissionDocs(tblCommissionProfile.getCommissionProfileId());
            if (!tblCommissionDocs.isEmpty()) {
                tblCommissionProfile.setTblCommissionDocs(tblCommissionDocs);
            }
            List<TblCommissionSlab> commissionSlab = commissionService.getCommissionSlab(tblCommissionProfile.getCommissionProfileId());
            if (!commissionSlab.isEmpty()) {
                tblCommissionProfile.setTblCommissionSlabs(commissionSlab);
            }
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", tblCommissionProfile);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", tblCommissionProfile);
        }
    }

    @GetMapping(value = Constants.GETCOMMISSIONPROFILEDATA)
    public ResponseEntity<Response> getcommissionprofiledata() {

        CommissionProfileData pricingProfileData = new CommissionProfileData();
        ResponseEntity<Response> agentClassResponse = pricingProfileGetApis.getAgentClass();
        ResponseEntity<Response> glAccountResponse = pricingProfileGetApis.getGlAccounts();
        ResponseEntity<Response> transDocsResponse = pricingProfileGetApis.getTransDocs();

        pricingProfileData.setAgentClass(agentClassResponse.getBody().getPayload());
        pricingProfileData.setGlAccounts(glAccountResponse.getBody().getPayload());
        pricingProfileData.setTblTransDocs(transDocsResponse.getBody().getPayload());
        if (pricingProfileData.getAgentClass() != null || pricingProfileData.getGlAccounts() != null) {
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", pricingProfileData);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", pricingProfileData);
        }
    }
}