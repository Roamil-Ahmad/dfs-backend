/*
Author Name:muhammad.kashif

Project Name: limits

Package Name:com.mfs.limits.Controller

Class Name: LimitsGetApis

Date and Time:5/14/2023 2:55 PM

Version:1.0

*/
package com.mfs.pricingprofile.controller.pricingprofile;

import com.mfs.pricingprofile.controller.AbstractApi;
import com.mfs.pricingprofile.dto.LovDto;
import com.mfs.pricingprofile.dto.PricingProfileData;
import com.mfs.pricingprofile.dto.Response;
import com.mfs.pricingprofile.model.*;
import com.mfs.pricingprofile.service.PricingProfileService;
import com.mfs.pricingprofile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = Constants.S_1, allowedHeaders = Constants.S_1)
@RestController
public class PricingProfileGetApis extends AbstractApi {

    @Autowired
    private PricingProfileService pricingProfileService;

    //Get Transaction Limits By TransLimitId
    @GetMapping(value = Constants.GET_PRICING_PROFILE_BY_ID)
    public ResponseEntity<Response> getPricingProfileById(@PathVariable String pricingProfileId, HttpServletRequest request) {
        TblTransCharge tblTransCharge = pricingProfileService.getTransChargesById(Long.valueOf(pricingProfileId));
        if (tblTransCharge != null) {
            List<TblTransChargesDoc> tblTransChargesDocs = pricingProfileService.getTblTransChargesDocs(tblTransCharge.getTransChargesId());
            if (!tblTransChargesDocs.isEmpty()) {
                tblTransCharge.setTblTransChargesDocs(tblTransChargesDocs);
            }
            List<TblTransChargesSlab> tblTransChargesSlabs = pricingProfileService.getTransChargesSlab(tblTransCharge.getTransChargesId());
            if (!tblTransChargesSlabs.isEmpty()) {
                tblTransCharge.setTblTransChargesSlabs(tblTransChargesSlabs);
            }
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", tblTransCharge);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", null);
        }
    }

    @GetMapping(value = Constants.GETFEEPROFILEDATA)
    public ResponseEntity<Response> getfeeprofiledata() {
        PricingProfileData pricingProfileData = new PricingProfileData();
        ResponseEntity<Response> channelsResponse = getchannels();
        ResponseEntity<Response> glAccountResponse = getGlAccounts();
        ResponseEntity<Response> transDocsResponse = getTransDocs();
        ResponseEntity<Response> segmentsResponse = getSegments();

        pricingProfileData.setChannel(channelsResponse.getBody().getPayload());
        pricingProfileData.setGlAccounts(glAccountResponse.getBody().getPayload());
        pricingProfileData.setTblTransDocs(transDocsResponse.getBody().getPayload());
        pricingProfileData.setSegments(segmentsResponse.getBody().getPayload());
        if (pricingProfileData != null && (pricingProfileData.getChannel() != null || pricingProfileData.getAgentClass() != null || pricingProfileData.getGlAccounts() != null || pricingProfileData.getGlAccounts() != null)) {
            return getResponseFormat(HttpStatus.OK, "Record Found Sucessfully", pricingProfileData);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not Found", pricingProfileData);
        }
    }

    @GetMapping(value = Constants.GETCHANNELS)
    public ResponseEntity<Response> getchannels() {
        List<LovDto> lovDtos = new ArrayList<>();
        List<LkpChannel> lkpChannels = pricingProfileService.getchannels();
        for (LkpChannel lkpChannel : lkpChannels) {
            LovDto lovDto = new LovDto();
            lovDto.setIsActive(lkpChannel.getIsActive());
            lovDto.setLovId(String.valueOf(lkpChannel.getChannelId()));
            lovDto.setName(lkpChannel.getChannelDescr());
            lovDto.setCode(lkpChannel.getChannelCode());
            lovDtos.add(lovDto);
        }
        if (!lovDtos.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", lovDtos);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not found", lovDtos);
        }
    }

    @GetMapping(value = Constants.GET_AGENT_CLASS)
    public ResponseEntity<Response> getAgentClass() {
        List<LovDto> lovDtos = new ArrayList<>();
        List<TblAgentClass> tblAgentClasses = pricingProfileService.getAgentClass();
        for (TblAgentClass tblAgentClass : tblAgentClasses) {
            LovDto lovDto = new LovDto();
            lovDto.setIsActive(tblAgentClass.getIsActive());
            lovDto.setLovId(String.valueOf(tblAgentClass.getAgentClassId()));
            lovDto.setName(tblAgentClass.getAgentClassDescr());
            lovDto.setCode(tblAgentClass.getAgentClassCode());
            lovDtos.add(lovDto);
        }
        if (!lovDtos.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", lovDtos);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not found", lovDtos);
        }
    }

    @GetMapping(value = Constants.GET_SEGMENTS)
    public ResponseEntity<Response> getSegments() {
        List<LovDto> lovDtos = new ArrayList<>();
        List<LkpSegment> lkpSegments = pricingProfileService.getSegments();
        for (LkpSegment lkpSegment : lkpSegments) {
            LovDto lovDto = new LovDto();
            lovDto.setIsActive(lkpSegment.getIsActive());
            lovDto.setLovId(String.valueOf(lkpSegment.getSegmentId()));
            lovDto.setName(lkpSegment.getSegmentDescr());
            lovDto.setCode(lkpSegment.getSegmentCode());
            lovDtos.add(lovDto);
        }
        if (!lovDtos.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", lovDtos);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not found", lovDtos);
        }
    }

    @GetMapping(value = Constants.GET_GL_ACCOUNTS)
    public ResponseEntity<Response> getGlAccounts() {
        List<LovDto> lovDtos = new ArrayList<>();
        List<TblGlAccount> tblGlAccounts = pricingProfileService.getGlAccounts();
        for (TblGlAccount tblGlAccount : tblGlAccounts) {
            LovDto lovDto = new LovDto();
            lovDto.setIsActive(tblGlAccount.getIsActive());
            lovDto.setLovId(String.valueOf(tblGlAccount.getGlAccountId()));
            lovDto.setName(tblGlAccount.getGlAccountDescr());
            lovDto.setCode(tblGlAccount.getGlAccountCode());
            lovDtos.add(lovDto);
        }
        if (!lovDtos.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", lovDtos);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not found", lovDtos);
        }
    }

    @GetMapping(value = Constants.GET_TRANS_DOCS)
    public ResponseEntity<Response> getTransDocs() {
        List<LovDto> lovDtos = new ArrayList<>();
        List<TblTransDoc> tblTransDocs = pricingProfileService.getTransDocs();
        for (TblTransDoc tblTransDoc : tblTransDocs) {
            LovDto lovDto = new LovDto();
            lovDto.setIsActive(tblTransDoc.getIsActive());
            lovDto.setLovId(String.valueOf(tblTransDoc.getTransDocsId()));
            lovDto.setName(tblTransDoc.getTransDocsDescr());
            lovDto.setCode(tblTransDoc.getTransDocsCode());
            lovDtos.add(lovDto);
        }
        if (!lovDtos.isEmpty()) {
            return getResponseFormat(HttpStatus.OK, "Record Found", lovDtos);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, "Record Not found", lovDtos);
        }
    }
}