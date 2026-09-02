/*
Author Name:muhammad.kashif

Project Name: limits

Package Name:com.mfs.limits.Controller

Class Name: LimitsPostApis

Date and Time:5/14/2023 2:22 PM

Version:1.0

*/
package com.mfs.pricingprofile.controller.commission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfs.pricingprofile.controller.AbstractApi;
import com.mfs.pricingprofile.dto.CommissionProfileRequest;
import com.mfs.pricingprofile.dto.CommissionProfileSearch;
import com.mfs.pricingprofile.dto.Response;
import com.mfs.pricingprofile.model.TblCommissionProfile;
import com.mfs.pricingprofile.service.CommissionService;
import com.mfs.pricingprofile.utils.Constants;
import com.mfs.pricingprofile.utils.JwtConstants;
import com.mfs.pricingprofile.utils.RequestValidator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = Constants.S_1, allowedHeaders = Constants.S_1)
@RestController
public class CommissionPostApis extends AbstractApi {

    @Autowired
    private CommissionService commissionService;

    //Get Transaction Limits By TransLimitId
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.GET_ALL_COMMISSION, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllCommission(@Valid @RequestBody CommissionProfileSearch commissionProfileSearch, HttpServletRequest request) {
        RequestValidator.getAllCommissionJsonValidate(commissionProfileSearch);
        List<TblCommissionProfile> tblCommissionProfile = commissionService.getAllCommissionProfiles(commissionProfileSearch);
        if (!tblCommissionProfile.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", tblCommissionProfile);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", tblCommissionProfile);
        }
    }

    //Save Commission
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.CREATECOMMISSION, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createcommission(@Valid @RequestBody CommissionProfileRequest commissionProfileRequest, HttpServletRequest request) throws ParseException {
        TblCommissionProfile saveCommissionProfile = new TblCommissionProfile();
        BigDecimal checkPricingProfileExistance = commissionService.checkCommissionProfileExistance(commissionProfileRequest);
        if (checkPricingProfileExistance.longValue() <= 0) {
            commissionProfileRequest.setStatusId(new BigDecimal(2));
            saveCommissionProfile = commissionService.saveCommissionProfile(commissionProfileRequest, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            if (saveCommissionProfile != null) {
                return getResponseFormat(HttpStatus.OK, "Record Saved Sucessfully", saveCommissionProfile);
            } else {
                return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Saved", saveCommissionProfile);
            }
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Already Exist", null);
        }
    }

    //Update Commission
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.UPDATECOMMISSION, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updatecommission(@Valid @RequestBody CommissionProfileRequest commissionProfileRequest, HttpServletRequest request) throws ParseException {
        TblCommissionProfile tblCommissionProfile;
        BigDecimal checkPricingProfileExistance = commissionService.checkCommissionProfileExistanceUpdate(commissionProfileRequest);
        if (checkPricingProfileExistance.longValue() <= 0) {
            commissionProfileRequest.setStatusId(new BigDecimal(2));
            tblCommissionProfile = commissionService.updatecommissionprofile(commissionProfileRequest, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            if (tblCommissionProfile != null) {
                return getResponseFormat(HttpStatus.OK, "Record Updated Sucessfully", tblCommissionProfile);
            } else {
                return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Updated", tblCommissionProfile);
            }
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Already Exist", null);
        }
    }

}