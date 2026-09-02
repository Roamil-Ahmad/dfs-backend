package com.mfs.pricingprofile.service;

import com.mfs.pricingprofile.dto.CommissionProfileRequest;
import com.mfs.pricingprofile.dto.CommissionProfileSearch;
import com.mfs.pricingprofile.model.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface CommissionService {

    TblCommissionProfile saveCommissionProfile(CommissionProfileRequest commissionProfileRequest, BigDecimal userId) throws ParseException;

    TblCommissionProfile updatecommissionprofile(CommissionProfileRequest commissionProfileRequest, BigDecimal userId) throws ParseException;

    TblCommissionProfile getCommissionProfileById(Long commissionProfileId);

    BigDecimal checkCommissionProfileExistance(CommissionProfileRequest commissionProfileRequest);

    BigDecimal checkCommissionProfileExistanceUpdate(CommissionProfileRequest commissionProfileRequest);

    List<TblCommissionProfile> getAllCommissionProfiles(CommissionProfileSearch commissionProfileSearch);

    List<TblCommissionDoc> getTblCommissionDocs(long commissionProfileId);

    List<TblCommissionSlab> getCommissionSlab(long commissionProfileId);
}
