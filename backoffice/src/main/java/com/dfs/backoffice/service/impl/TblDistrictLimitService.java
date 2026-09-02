package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.DistrictLimitProjection;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.TblDistrictLimitRequest;
import com.dfs.backoffice.dto.TblDistrictLimitSearchRequest;
import com.dfs.backoffice.model.TblDistrictLimit;
import com.dfs.backoffice.repo.TblDistrictLimitRepo;
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
public class TblDistrictLimitService {

  private final TblDistrictLimitRepo repo;

  public TblDistrictLimitService(TblDistrictLimitRepo repo) {
    this.repo = repo;
  }

  public Response updateDistrictLimit(TblDistrictLimitRequest req, HttpServletRequest request) {
    String userId = (String) request.getAttribute(JwtConstants.APP_USER_ID);

    Optional<TblDistrictLimit> optional = repo.findById(req.getDistrictLimitId());
    if (!optional.isPresent()) {
      return new Response(Constants.ZERO, "District limit not found", null);
    }

    TblDistrictLimit limit = optional.get();
    setValue(req, limit);

    limit.setLastupdateuser(new BigDecimal(userId));
    limit.setLastupdatedate(new Date());
    repo.save(limit);

    return new Response(Constants.ONE, "District limit updated successfully", limit);
  }

  private void setValue(TblDistrictLimitRequest req, TblDistrictLimit limit) {
    limit.setDistrictId(req.getDistrictId());
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
    TblDistrictLimit limit = repo.findById(id)
        .orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    return new Response(Constants.ONE, "Success", limit);
  }

  public Response search(TblDistrictLimitSearchRequest req) {
    List<DistrictLimitProjection> result = repo.searchDistrictLimits(
        req.getDistrictId(),
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