/*
Author Name:muhammad.kashif

Project Name: limits

Package Name:com.mfs.limits.Controller

Class Name: LimitsPostApis

Date and Time:5/14/2023 2:22 PM

Version:1.0

*/
package com.mfs.pricingprofile.controller.pricingprofile;

import com.mfs.pricingprofile.controller.AbstractApi;
import com.mfs.pricingprofile.dto.PricingProfileSearch;
import com.mfs.pricingprofile.dto.Response;
import com.mfs.pricingprofile.dto.TransChargeRequest;
import com.mfs.pricingprofile.model.LkpStatus;
import com.mfs.pricingprofile.model.TblTransCharge;
import com.mfs.pricingprofile.service.PricingProfileService;
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
public class PricingProfilePostApis extends AbstractApi {
    @Autowired
    private PricingProfileService pricingProfileService;

    //Get All Pricing Profile
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.GET_ALL_PRICING_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllPricingProfile(@Valid @RequestBody PricingProfileSearch pricingProfileSearch, HttpServletRequest request) {
        RequestValidator.getAllPricingProfileJsonValidate(pricingProfileSearch);
        List<TblTransCharge> tblTransCharges = pricingProfileService.getAllTblTransCharge(pricingProfileSearch);
        if (!tblTransCharges.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found Successfully", tblTransCharges);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", null);
        }
    }

    //Save Pricing Profile
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.CREATE_PRICING_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createpricingprofile(@Valid @RequestBody TblTransCharge tblTransCharge, HttpServletRequest request) throws ParseException {
        TblTransCharge saveTblTransCharge = new TblTransCharge();
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2L);
        tblTransCharge.setLkpStatus(lkpStatus);
        tblTransCharge.setIsActive(tblTransCharge.getIsActive());
        saveTblTransCharge = pricingProfileService.saveTransCharge(tblTransCharge, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (saveTblTransCharge != null) {
            return getResponseFormat(HttpStatus.OK, "Record Saved Successfully", saveTblTransCharge);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Saved", saveTblTransCharge);
        }
    }

    //Update Pricing Profile
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.UPDATEPRICINGPROFILE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updatepricingprofile(@Valid @RequestBody TblTransCharge
                                                                 transChargeRequest, HttpServletRequest request) throws ParseException {
        TblTransCharge tblTransCharge = new TblTransCharge();
        tblTransCharge = pricingProfileService.updatepricingprofile(transChargeRequest, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (tblTransCharge != null) {
            return getResponseFormat(HttpStatus.OK, "Record Updated Sucessfully", tblTransCharge);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Updated", tblTransCharge);
        }
    }

    //Update Pricing Profile
    @SecurityRequirement(name = Constants.BEARER_AUTHENTICATION)
    @PostMapping(value = Constants.INACTIVEPRICINGPROFILE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> inactivepricingprofile(@Valid @RequestBody TransChargeRequest
                                                                   transChargeRequest, HttpServletRequest request) {
        TblTransCharge tblTransCharge = pricingProfileService.inactivepricingprofile(transChargeRequest, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (tblTransCharge != null) {
            return getResponseFormat(HttpStatus.OK, "Record Updated Sucessfully", tblTransCharge);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Updated", tblTransCharge);
        }
    }

}