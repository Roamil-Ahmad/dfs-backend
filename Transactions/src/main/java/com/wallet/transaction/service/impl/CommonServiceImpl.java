package com.wallet.transaction.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.BankLovResponse;
import com.wallet.transaction.dto.LovResponse;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.*;
import com.wallet.transaction.repo.*;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.util.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl extends HelperClass implements CommonService {
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private JWTSecurity jwtSecurity;
    @Autowired
    private TblMessageRepo tblMessageRepo;
    @Autowired
    private TblRequestRepo tblRequestRepo;
    @Autowired
    private TblResponseRepo tblResponseRepo;
    @Autowired
    private TblAuthAccessTokenRepo tblAuthAccessTokenRepo;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Value("${default.session.time}")
    private String defaultSessionTime;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private LkpTransPurposeRepo lkpTransPurposeRepo;
    @Autowired
    private LkpCurrencyRepo lkpCurrencyRepo;
    @Autowired
    private LkpBankRepo lkpBankRepo;


    @Override
    public String getResponseMessageByCode(String code) {
        TblMessage tblMessage = tblMessageRepo.findTblMessageByMessageCode(code);
        return tblMessage != null ? tblMessage.getMessageDescr() : GenericResponseCode.MESSAGE_NOT_FOUND_AGAINST_CODE.getResponseMessage() + code;
    }

    @Override
    public HashMap<String, Object> getResponse(String responseCode, Object payload) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", getResponseMessageByCode(responseCode));
        map.put("data", payload);
        return map;
    }

    @Override
    public HashMap<String, Object> getResponseWithOutDB(String responseCode, String description, Object payload) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", description);
        map.put("data", payload);
        return map;
    }

    @Override
    public BigDecimal authenticateHeaderAndDevice(HttpServletRequest httpServletRequest, Request request) {
        String requestURI = httpServletRequest.getRequestURI();

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtSecurity.parseJWT(token);

                // Extract user details from claims
                String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
                String mobileNumber = (String) claims.get(JwtConstants.MOBILE_NUMBER);
                String uuid=(String) claims.get(JwtConstants.UUID);


                if (imie != null && mobileNumber != null && uuid!=null) {


                    if(request!=null){
                        // Device verification removed: the token is what authorises the call now.
                        // No handset lookup, no IMEI or UUID comparison.
                        if(claims.containsKey(JwtConstants.APP_USER_ID)){
                            return new BigDecimal(((Number) claims.get(JwtConstants.APP_USER_ID)).longValue());
                        }
                        return BigDecimal.ONE;
                    }else {
                        throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
                    }
                }else {
                    throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
                }



            } catch (Exception e) {
                throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
            }
        } else {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }

    }

    public  String extractMobileNumber(Object object) {
        try {
            if (object instanceof Map) {
                // Handle Map object
                Object mobileNumber = ((Map<?, ?>) object).get("mobileNumber");
                return mobileNumber != null ? mobileNumber.toString() : null;
            } else if (object instanceof String) {
                // Handle JSON String object
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue((String) object, Map.class);
                Object mobileNumber = jsonMap.get("mobileNumber");
                return mobileNumber != null ? mobileNumber.toString() : null;
            }
        } catch (Exception e) {
            // Log and handle exception
            throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
        }
        // Unsupported type or key not found
        return null;
    }

    @Override
    public HashMap<String, Object> getPurposeOfPayment() {
        List<LovResponse> lovResponses;
        List<LkpTransPurpose> transPurposes = lkpTransPurposeRepo.findByIsActive(Constants.YES);
        if (!isNullOrEmpty(transPurposes)) {
            lovResponses = transPurposes.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getTransPurposeId());
                lovResponse.setCode(p.getTransPurposeCode());
                lovResponse.setName(p.getTransPurposeDescr());
                return lovResponse;
            }).collect(Collectors.toList());
            return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
        }
        return getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> getAllBank() {
        List<BankLovResponse> lovResponses;
        List<LkpBank> transPurposes = lkpBankRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(transPurposes)) {
            lovResponses = transPurposes.parallelStream().map(p -> {
                BankLovResponse lovResponse = new BankLovResponse();
                lovResponse.setId(p.getBankId());
                lovResponse.setCode(p.getBankCode());
                lovResponse.setName(p.getBankName());
                lovResponse.setBin(p.getBicCode());
                lovResponse.setIbanShort(p.getIbanShort());
                lovResponse.setImd(p.getBankImd());
                lovResponse.setMinAccountLength(p.getMinAccLength().intValue());
                lovResponse.setMaxAccountLength(p.getMaxAccLength().intValue());
                return lovResponse;
            }).collect(Collectors.toList());
            return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
        }
        return getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> getAllCurrency() {
        List<LovResponse> lovResponses;
        List<LkpCurrency> lkpCurrencies = lkpCurrencyRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpCurrencies)) {
            lovResponses = lkpCurrencies.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getCurrencyId());
                lovResponse.setCode(p.getCurrencyCode());
                lovResponse.setName(p.getCurrencyDescr());
                return lovResponse;
            }).collect(Collectors.toList());
            return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
        }
        return getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> getLovs(List<String> requestedLovs) {


        Map<String, Object> responseMap = new HashMap<>();

        if (requestedLovs.contains("B")) {
            List<LkpBank> banks = lkpBankRepo.findAllByIsActive(Constants.YES);
            if (!isNullOrEmpty(banks)) {
                List<BankLovResponse> bankResponses = banks.parallelStream().map(p -> {
                    BankLovResponse lovResponse = new BankLovResponse();
                    lovResponse.setId(p.getBankId());
                    lovResponse.setCode(p.getBankCode());
                    lovResponse.setName(p.getBankName());
                    lovResponse.setBin(p.getBicCode());
                    lovResponse.setIbanShort(p.getIbanShort());
                    lovResponse.setImd(p.getBankImd());
                    lovResponse.setMinAccountLength(p.getMinAccLength().intValue());
                    lovResponse.setMaxAccountLength(p.getMaxAccLength().intValue());
                    return lovResponse;
                }).collect(Collectors.toList());
                responseMap.put("banks", bankResponses);
            }
        }

        if (requestedLovs.contains("C")) {
            List<LkpCurrency> currencies = lkpCurrencyRepo.findAllByIsActive(Constants.YES);
            if (!isNullOrEmpty(currencies)) {
                List<LovResponse> currencyResponses = currencies.parallelStream().map(p -> {
                    LovResponse lovResponse = new LovResponse();
                    lovResponse.setId(p.getCurrencyId());
                    lovResponse.setCode(p.getCurrencyCode());
                    lovResponse.setName(p.getCurrencyDescr());
                    return lovResponse;
                }).collect(Collectors.toList());
                responseMap.put("currencies", currencyResponses);
            }
        }

        if (responseMap.isEmpty()) {
            return getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }

        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), responseMap);
    }

}
