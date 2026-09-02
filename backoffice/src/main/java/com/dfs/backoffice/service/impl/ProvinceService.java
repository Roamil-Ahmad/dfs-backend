package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.ProvinceRequest;
import com.dfs.backoffice.dto.ProvinceSearchRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.LkpProvince;
import com.dfs.backoffice.repo.LkpProvinceRepo;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinceService {

  private final LkpProvinceRepo provinceRepository;

  public ProvinceService(LkpProvinceRepo provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  public Response saveProvince(ProvinceRequest req, HttpServletRequest request) {
    String userId = (String) request.getAttribute(JwtConstants.APP_USER_ID);

    // Check unique province code
    if (provinceRepository.findByProvinceCodeIgnoreCase(req.getProvinceCode()).isPresent()) {
      return new Response(Constants.ZERO, "Province code already exists", null);
    }

    LkpProvince p = new LkpProvince();
    setValue(req, p);

    p.setCreateuser(new BigDecimal(userId));
    p.setCreatedate(new Date());
    provinceRepository.save(p);

    return new Response(Constants.ONE, "Province created successfully", p);
  }

  public Response updateProvince(ProvinceRequest req, HttpServletRequest request) {
    String userId = (String) request.getAttribute(JwtConstants.APP_USER_ID);

    Optional<LkpProvince> optional = provinceRepository.findById(req.getProvinceId());
    if (!optional.isPresent()) {
      return new Response(Constants.ZERO, "Province not found", null);
    }

    // Check unique province code
    Optional<LkpProvince> duplicate = provinceRepository.findByProvinceCodeIgnoreCase(req.getProvinceCode());
    if (duplicate.isPresent() && duplicate.get().getProvinceId() != req.getProvinceId()) {
      return new Response(Constants.ZERO, "Province code already exists", null);
    }

    LkpProvince p = optional.get();
    setValue(req, p);

    p.setLastupdateuser(new BigDecimal(userId));
    p.setLastupdatedate(new Date());
    provinceRepository.save(p);

    return new Response(Constants.ONE, "Province updated successfully", p);
  }

  private void setValue(ProvinceRequest req, LkpProvince p) {
    p.setProvinceCode(req.getProvinceCode());
    p.setProvinceDescr(req.getProvinceDescr());
    p.setIsActive(req.getIsActive());
    p.setCapital(req.getCapital());
    p.setIbanCode(req.getIbanCode());
    p.setRiskProfile(req.getRiskProfile());
  }

  public Response searchProvinces(ProvinceSearchRequest req) {

    Timestamp dateFrom = req.getDateFrom() != null ?
        Timestamp.valueOf(req.getDateFrom() + " 00:00:00") : null;

    Timestamp dateTo = req.getDateTo() != null ?
        Timestamp.valueOf(req.getDateTo() + " 23:59:59") : null;

    List<LkpProvince> result = provinceRepository.searchProvinces(
        req.getProvinceCode(),
        req.getProvinceDescr(),
        dateFrom,
        dateTo
    );

    if (result.isEmpty()) {
      return new Response(Constants.ZERO, GenericResponseCode.RECORD_NOT_FOUND.getResponseMessage(), result);
    }

    return new Response(Constants.ONE, "Success", result);
  }

  public Response getById(long id) {
    LkpProvince lkpProvince = provinceRepository.findById(id).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    return new Response(Constants.ONE, "Success", lkpProvince);
  }
}