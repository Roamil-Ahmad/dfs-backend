package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.ProvinceLimitProjection;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.TblProvinceLimitRequest;
import com.dfs.backoffice.dto.TblProvinceLimitSearchRequest;
import com.dfs.backoffice.model.TblProvinceLimit;
import com.dfs.backoffice.repo.TblProvinceLimitRepo;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.JwtConstants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TblProvinceLimitService {

  private final TblProvinceLimitRepo repo;

  public TblProvinceLimitService(TblProvinceLimitRepo repo) {
    this.repo = repo;
  }

  public Response updateProvinceLimit(TblProvinceLimitRequest req, HttpServletRequest request) {
    String userId = (String) request.getAttribute(JwtConstants.APP_USER_ID);

    Optional<TblProvinceLimit> optional = repo.findById(req.getProvinceLimitId());
    if (!optional.isPresent()) {
      return new Response(Constants.ZERO, "Province limit not found", null);
    }

    TblProvinceLimit limit = optional.get();
    setValue(req, limit);

    limit.setLastupdateuser(new BigDecimal(userId));
    limit.setLastupdatedate(new Date());
    repo.save(limit);

    return new Response(Constants.ONE, "Province limit updated successfully", limit);
  }

  private void setValue(TblProvinceLimitRequest req, TblProvinceLimit limit) {
    limit.setProvinceId(req.getProvinceId());
    limit.setAccountLevelId(req.getAccountLevelId());
    limit.setRiskProfile(req.getRiskProfile());
    limit.setDailyAmtLimitCr(req.getDailyAmtLimitCr());
    limit.setDailyAmtLimitDr(req.getDailyAmtLimitDr());
    limit.setDailyTransLimitCr(req.getDailyTransLimitCr());
    limit.setDailyTransLimitDr(req.getDailyTransLimitDr());
    limit.setMonthlyAmtLimitCr(req.getMonthlyAmtLimitCr());
    limit.setMonthlyAmtLimitDr(req.getMonthlyAmtLimitDr());
    limit.setMonthlyTransLimitCr(req.getMonthlyTransLimitCr());
    limit.setMonthlyTransLimitDr(req.getMonthlyTransLimitDr());
    limit.setYearlyAmtLimitCr(req.getYearlyAmtLimitCr());
    limit.setYearlyAmtLimitDr(req.getYearlyAmtLimitDr());
    limit.setYearlyTransLimitCr(req.getYearlyTransLimitCr());
    limit.setYearlyTransLimitDr(req.getYearlyTransLimitDr());
  }

  public Response getById(long id) {
    TblProvinceLimit limit = repo.findById(id)
        .orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    return new Response(Constants.ONE, "Success", limit);
  }

  public Response search(TblProvinceLimitSearchRequest req) {
    List<ProvinceLimitProjection> result = repo.searchProvinceLimits(
        req.getProvinceId(),
        req.getAccountLevelId(),
        req.getRiskProfile(),
        req.getDateFrom(),
        req.getDateTo()
    );

    if (result.isEmpty()) {
      return new Response(Constants.ZERO, GenericResponseCode.RECORD_NOT_FOUND.getResponseMessage(), result);
    }
    return new Response(Constants.ONE, "Success", result);
  }
}