package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.DistrictRequest;
import com.dfs.backoffice.dto.DistrictSearchRequest;
import com.dfs.backoffice.dto.DistrictWithProvince;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.LkpDistrict;
import com.dfs.backoffice.repo.LkpDistrictRepo;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DistrictService {

  private final LkpDistrictRepo districtRepo;

  public DistrictService(LkpDistrictRepo districtRepo) {
    this.districtRepo = districtRepo;
  }

  public Response saveDistrict(DistrictRequest req, HttpServletRequest request) {
    String userId = (String) request.getAttribute(JwtConstants.APP_USER_ID);

    if (districtRepo.findByDistrictCodeIgnoreCase(req.getDistrictCode()).isPresent()) {
      return new Response(Constants.ZERO, "District code already exists", null);
    }

    LkpDistrict d = new LkpDistrict();
    setValue(req, d);
    d.setCreateUser(new java.math.BigDecimal(userId));
    d.setCreateDate(new Date());

    districtRepo.save(d);
    return new Response(Constants.ONE, "District created successfully", d);
  }

  private void setValue(DistrictRequest req, LkpDistrict d) {
    d.setDistrictCode(req.getDistrictCode());
    d.setDistrictDescr(req.getDistrictDescr());
    d.setIsActive(req.getIsActive());
    d.setProvinceId(req.getProvinceId());
    d.setRiskProfile(req.getRiskProfile());
  }

  public Response updateDistrict(DistrictRequest req, HttpServletRequest request) {
    String userId = (String) request.getAttribute(JwtConstants.APP_USER_ID);

    Optional<LkpDistrict> optional = districtRepo.findById(req.getDistrictId());
    if (!optional.isPresent()) {
      return new Response(Constants.ZERO, "District not found", null);
    }

    Optional<LkpDistrict> duplicate = districtRepo.findByDistrictCodeIgnoreCase(req.getDistrictCode());
    if (duplicate.isPresent() && duplicate.get().getDistrictId() != req.getDistrictId()) {
      return new Response(Constants.ZERO, "District code already exists", null);
    }

    LkpDistrict d = optional.get();
    setValue(req, d);
    d.setLastUpdateUser(new java.math.BigDecimal(userId));
    d.setLastUpdateDate(new Date());

    districtRepo.save(d);
    return new Response(Constants.ONE, "District updated successfully", d);
  }

  public Response searchDistricts(DistrictSearchRequest req) {
    Timestamp dateFrom = req.getDateFrom() != null ? Timestamp.valueOf(req.getDateFrom() + " 00:00:00") : null;
    Timestamp dateTo = req.getDateTo() != null ? Timestamp.valueOf(req.getDateTo() + " 23:59:59") : null;

    List<DistrictWithProvince> result = districtRepo.searchDistricts(
        req.getDistrictCode(),
        req.getDistrictDescr(),
        req.getProvinceId(),
        dateFrom,
        dateTo
    );

    if (result.isEmpty()) {
      return new Response(Constants.ZERO, "No records found", result);
    }
    return new Response(Constants.ONE, "Success", result);
  }

  public Response getById(long id) {

    LkpDistrict result = districtRepo.findById(id).orElseThrow(()->new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    return new Response(Constants.ONE, "Success", result);
  }
}