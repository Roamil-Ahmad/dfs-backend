package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblAgent;
import com.dfs.backoffice.repo.TblAgentRepo;
import com.dfs.backoffice.service.AgentMaintenanceService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentMaintenanceServiceImpl extends HelperClass implements AgentMaintenanceService {

  @Autowired
  private TblAgentRepo tblAgentRepo;
  @Override
  public List<SearchAgentResponse> searchAgents(SearchCustomerRequest request) {
    // Prepare date filters
    String dateFrom = formatDateStart(request.getFromDate());
    String dateTo = formatDateEnd(request.getToDate());

    // Encrypt NidNo if provided
    request.setNidNo(encryptIfNotEmpty(request.getNidNo()));

    // Query repository
    List<Object[]> results = tblAgentRepo.searchAgents(
        request.getMobileNo(),
        request.getNidNo(),
        request.getFullName(),
        request.getAccountStatusId(),
        dateFrom,
        dateTo
    );

    // Map results to DTO
    return results.stream()
        .map(this::mapToSearchAgentResponse)
        .collect(Collectors.toList());
  }

  // Helper: format start of date
  private String formatDateStart(String date) {
    return isNullOrEmpty(date) ? null : date + " 00:00:00";
  }

  // Helper: format end of date
  private String formatDateEnd(String date) {
    return isNullOrEmpty(date) ? null : date + " 23:59:59";
  }

  // Helper: encrypt if not null or empty
  private String encryptIfNotEmpty(String value) {
    return !isNullOrEmpty(value) ? encryptWithAes(value) : "";
  }

  // Helper: map a result row to DTO
  private SearchAgentResponse mapToSearchAgentResponse(Object[] row) {
    SearchAgentResponse dto = new SearchAgentResponse();
    dto.setAgentId(getLong(row[0]));
    dto.setName(decryptSafe(row[1]));
    dto.setFatherHusbandName(decryptSafe(row[2]));
    dto.setGrandfatherName(decryptSafe(row[3]));
    dto.setNidNo(decryptSafe(row[4]));
    dto.setDob((Date) row[5]);
    dto.setEmail(decryptSafe(row[6]));
    dto.setResidentialAddress(decryptSafe(row[7]));
    dto.setPermanentAddress(decryptSafe(row[8]));
    dto.setCreateDate((Date) row[9]);
    dto.setGender((String) row[10]);
    dto.setIsActive((String) row[11]);
    dto.setPob(decryptSafe(row[12]));
    dto.setNidExpiryDate((Date) row[13]);
    dto.setNidIssueDate((Date) row[14]);
    dto.setAccountId(getLong(row[15]));
    dto.setAccountLevelDescr((String) row[16]);
    dto.setAccountStatusDescr((String) row[17]);
    return dto;
  }

  // Safe decryption helper
  private String decryptSafe(Object value) {
    return value != null ? decrypttWithAes((String) value) : null;
  }

  // Safe Long conversion helper
  private Long getLong(Object value) {
    return value != null ? ((Number) value).longValue() : null;
  }

  // Null or empty check helper
  private boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  @Override
  public Response updateAgent(UpdateCustomerRequest requestDto, HttpServletRequest request) {

    Response response = new Response();

    // Check if customer exists
    TblAgent agent = tblAgentRepo.findAgentByNidNo(
        encryptWithAes(requestDto.getNidNo())
    );

    if (agent == null) {
      return buildResponse(response, Constants.ZERO, GenericResponseCode.CUSTOMER_NOT_FOUND);
    }

    updateAgentFields(agent, requestDto);
    updateAuditFields(agent, request);

    tblAgentRepo.save(agent);

    return buildResponse(response, Constants.ONE, GenericResponseCode.RECORD_UPDATED);
  }

  private void updateAgentFields(TblAgent agent, UpdateCustomerRequest dto) {

    agent.setName(encryptWithAes(dto.getFullName()));
    agent.setFatherHusbandName(encryptWithAes(dto.getFatherName()));
    agent.setGrandfatherName(encryptWithAes(dto.getGrandFatherName()));
    agent.setGender(dto.getGender());
    agent.setPob(encryptWithAes(dto.getPlaceOfBirth()));
    agent.setPermanentAddress(encryptWithAes(dto.getPermanentAddress()));

    agent.setNidExpiryDate(parseDate(dto.getNidExpiryDate()));
    agent.setNidIssueDate(parseDate(dto.getNidIssueDate()));
    agent.setDob(parseDate(dto.getDob()));
  }

  private Date parseDate(String date) {
    if (date == null || date.isEmpty()) return null;

    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format: " + date);
    }
  }

  private void updateAuditFields(TblAgent agent, HttpServletRequest request) {

    agent.setLastupdatedate(new Date());

    BigDecimal userId = new BigDecimal(
        String.valueOf(request.getAttribute(JwtConstants.APP_USER_ID))
    );
    agent.setLastupdateuser(userId);

    BigDecimal currentIndex = agent.getUpdateindex();
    agent.setUpdateindex(currentIndex == null ? BigDecimal.ONE
        : currentIndex.add(BigDecimal.ONE));
  }

  private Response buildResponse(Response response, String flag, GenericResponseCode code) {
    setResponse(response, flag, null, code.getResponseCode());
    return response;
  }

  @Override
  public AgentDetailResponse getAgentDetail(AgentDetailRequest request) {
    List<Object[]> results = tblAgentRepo.getAgentDetail(request.getAgentId());
    if (!results.isEmpty()) {
      return mapToAgentDetailResponse(results.get(0));
    }
    return null;
  }

  private AgentDetailResponse mapToAgentDetailResponse(Object[] row) {
    AgentDetailResponse dto = new AgentDetailResponse();
    dto.setAgentId(getLong(row[0]));
    dto.setFatherHusbandName(decryptSafe(row[1]));
    dto.setResidentialAddress(decryptSafe(row[2]));
    dto.setCityId(getLong(row[3]));
    dto.setCityDescr((String) row[4]);
    dto.setDistrictId(getLong(row[5]));
    dto.setDistrictDescr((String) row[6]);
    dto.setProvinceId(getLong(row[7]));
    dto.setProvinceDescr((String) row[8]);
    dto.setEmail(decryptSafe(row[9]));
    dto.setNok(decryptSafe(row[10]));
    dto.setName(decryptSafe(row[11]));
    dto.setOccupation((String) row[12]);
    dto.setPermanentAddress(decryptSafe(row[13]));
    dto.setNidNo(decryptSafe(row[14]));
    dto.setPhone(decryptSafe(row[15]));
    dto.setMobile(decryptSafe(row[16]));
    dto.setParentAgentId(getLong(row[17]));
    dto.setAgentCode((String) row[18]);
    dto.setAgentType((String) row[19]);
    dto.setIsActive((String) row[20]);
    dto.setStatusId(getLong(row[21]));
    dto.setCreateUser(getLong(row[22]));
    dto.setCreateDate((Date) row[23]);
    dto.setPob(decryptSafe(row[24]));
    dto.setNidIssueDate((Date) row[25]);
    dto.setNidExpiryDate((Date) row[26]);
    dto.setGrandfatherName(decryptSafe(row[27]));
    dto.setIsFiler((String) row[28]);
    dto.setParentAgent(decryptSafe(row[29]));
    dto.setAgentClassId(getLong(row[30]));
    dto.setAgentLevel(getLong(row[31]));
    dto.setIsBlacklisted((String) row[32]);
    dto.setIsDeviceRegistered((String) row[33]);
    dto.setLastName(decryptSafe(row[34]));
    dto.setGender((String) row[35]);
    dto.setDob((Date) row[36]);
    dto.setCustomerAllId(getLong(row[37]));
    dto.setMrzData((String) row[38]);
    dto.setFacematchScore(getBigDecimal(row[39]));
    dto.setLivenessScore(getBigDecimal(row[40]));
    dto.setBioverified((String) row[41]);
    dto.setUuid((String) row[42]);
    dto.setOtpverified((String) row[43]);
    dto.setSelfieverified((String) row[44]);
    dto.setDeviceModel((String) row[45]);
    dto.setChannelDescr((String) row[46]);
    dto.setAccountNo((String) row[47]);
    dto.setCurrentBalance(getBigDecimal(row[48]));
    dto.setIban((String) row[49]);
    dto.setAccountTitle(decryptSafe(row[50]));
    dto.setAccountLevelDescr((String) row[51]);
    dto.setAccountStatusDescr((String) row[52]);
    dto.setDailyAmtLimitDr(getBigDecimal(row[53]));
    dto.setMonthlyAmtLimitDr(getBigDecimal(row[54]));
    dto.setYearlyAmtLimitDr(getBigDecimal(row[55]));
    dto.setDailyAmtLimitCr(getBigDecimal(row[56]));
    dto.setMonthlyAmtLimitCr(getBigDecimal(row[57]));
    dto.setYearlyAmtLimitCr(getBigDecimal(row[58]));
    dto.setDailyTransLimitDr(getBigDecimal(row[59]));
    dto.setMonthlyTransLimitDr(getBigDecimal(row[60]));
    dto.setYearlyTransLimitDr(getBigDecimal(row[61]));
    dto.setDailyTransLimitCr(getBigDecimal(row[62]));
    dto.setMonthlyTransLimitCr(getBigDecimal(row[63]));
    dto.setYearlyTransLimitCr(getBigDecimal(row[64]));
    dto.setNidFront((String) row[65]);
    dto.setNidBack((String) row[66]);
    dto.setProofOfAddress((String) row[67]);
    dto.setSelfie((String) row[68]);
    dto.setSignature((String) row[69]);
    dto.setLicense((String) row[70]);
    dto.setShopFront((String) row[71]);
    dto.setShopBack((String) row[72]);
    dto.setLicenseTypeId(getLong(row[73]));
    dto.setLicenseNumber((String) row[74]);
    dto.setLicenseIssueDate((Date) row[75]);
    dto.setLicenseExpiryDate((Date) row[76]);
    dto.setIssuingAuthority((String) row[77]);
    dto.setLicenseType((String) row[78]);
    dto.setBusinessName((String) row[79]);
    dto.setLegalStructure((String) row[80]);
    dto.setShopName((String) row[81]);
    dto.setBusinessTypeId(getLong(row[82]));
    dto.setBusinessTypeName((String) row[83]);
    dto.setBusinessAddress((String) row[84]);
    dto.setBusinessDistrict((String) row[85]);
    dto.setBusinessProvince((String) row[86]);
    return dto;
  }

  private BigDecimal getBigDecimal(Object value) {
    return value != null ? new BigDecimal(value.toString()) : null;
  }
}
