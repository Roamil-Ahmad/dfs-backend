package com.mfs.pricingprofile.service;

import com.mfs.pricingprofile.dto.PricingProfileSearch;
import com.mfs.pricingprofile.dto.TransChargeRequest;
import com.mfs.pricingprofile.model.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface PricingProfileService {
    TblTransCharge saveTransCharge(TblTransCharge transChargeRequest, BigDecimal userId) throws ParseException;

    TblTransCharge save(TblTransCharge model);

    TblTransCharge updatepricingprofile(TblTransCharge transChargeRequest, BigDecimal userId) throws ParseException;

    List<LkpChannel> getchannels();

    List<TblAgentClass> getAgentClass();

    List<TblGlAccount> getGlAccounts();

    List<TblTransDoc> getTransDocs();

    TblTransCharge getTransChargesById(Long transChargeId);

    List<TblTransChargesDoc> getTblTransChargesDocs(long transChargesId);

    List<TblTransChargesSlab> getTransChargesSlab(long transChargesId);

    List<TblTransCharge> getAllTblTransCharge(PricingProfileSearch pricingProfileSearch);

    List<LkpSegment> getSegments();

    TblTransCharge inactivepricingprofile(TransChargeRequest transChargeRequest, BigDecimal userId);
}
