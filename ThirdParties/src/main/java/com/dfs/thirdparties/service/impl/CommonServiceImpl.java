package com.dfs.thirdparties.service.impl;

import com.dfs.thirdparties.dto.common.Request;
import com.dfs.thirdparties.model.TblDeviceInfo;
import com.dfs.thirdparties.model.TblMessage;
import com.dfs.thirdparties.repo.TblAppUserRepo;
import com.dfs.thirdparties.repo.TblDeviceInfoRepo;
import com.dfs.thirdparties.repo.TblMessageRepo;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.util.CustomDataNotFoundException;
import com.dfs.thirdparties.util.GenericResponseCode;
import com.dfs.thirdparties.util.JWTSecurity;
import com.dfs.thirdparties.util.JwtConstants;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private JWTSecurity jwtSecurity;
    @Autowired
    private TblMessageRepo tblMessageRepo;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;


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
    public boolean validatePreRegToken(String token) {
        Claims claims = jwtSecurity.parseJWT(token);
        // Extract user details from claims
        String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
        String mobileNumber = (String) claims.get(JwtConstants.MOBILE_NUMBER);
        Long appUserId = tblAppUserRepo.validateDeviceToken(mobileNumber, imie);
        if (appUserId != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public BigDecimal authenticateHeaderAndDevice(HttpServletRequest httpServletRequest, Request request) {
        // Get the request URI
        String requestURI = httpServletRequest.getRequestURI();

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtSecurity.parseJWT(token);

                // Extract user details from claims
                String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
                String mobileNumber = (String) claims.get(JwtConstants.MOBILE_NUMBER);
                String uuid = (String) claims.get(JwtConstants.UUID);


                if (imie != null && mobileNumber != null && uuid != null) {


                    if (request != null) {
                        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(mobileNumber);
                        if (tblDeviceInfo == null) {
                            tblDeviceInfo = tblDeviceInfoRepo.findByAccountNo(mobileNumber);
                        }
                        if (tblDeviceInfo == null) {
                            tblDeviceInfo = tblDeviceInfoRepo.findByAccountNoAgent(mobileNumber);
                        }
                        if (tblDeviceInfo != null && tblDeviceInfo.getImeiNo().equalsIgnoreCase(request.getImieNo()) && tblDeviceInfo.getUuid().equalsIgnoreCase(uuid)) {
                            if (claims.containsKey(JwtConstants.APP_USER_ID)) {
                                return new BigDecimal((Integer) claims.get(JwtConstants.APP_USER_ID));
                            }
                            return BigDecimal.ONE;
                        } else {
                            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_AUTHENTICATION_FAILED.getResponseCode());
                        }
                    } else {
                        throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
                    }
                } else {
                    throw new CustomDataNotFoundException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
                }


            } catch (Exception e) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
            }
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
    }

    @Override
    public BigDecimal authenticateHeaderAndDeviceLoggedIn(HttpServletRequest httpServletRequest, Request request) {
        String requestURI = httpServletRequest.getRequestURI();

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtSecurity.parseJWT(token);

                // Extract user details from claims
                String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
                String mobileNumber = (String) claims.get(JwtConstants.MOBILE_NUMBER);
                String uuid = (String) claims.get(JwtConstants.UUID);


                if (imie != null && mobileNumber != null && uuid != null) {


                    if (request != null) {
                        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(mobileNumber);
                        if (tblDeviceInfo == null) {
                            tblDeviceInfo = tblDeviceInfoRepo.findByAccountNo(mobileNumber);
                        }
                        if (tblDeviceInfo != null && tblDeviceInfo.getImeiNo().equalsIgnoreCase(request.getImieNo()) && tblDeviceInfo.getUuid().equalsIgnoreCase(uuid)) {
                            if (claims.containsKey(JwtConstants.APP_USER_ID)) {
                                return new BigDecimal((Integer) claims.get(JwtConstants.APP_USER_ID));
                            }
                            return BigDecimal.ONE;
                        } else {
                            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_AUTHENTICATION_FAILED.getResponseCode());
                        }
                    } else {
                        throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
                    }
                } else {
                    throw new CustomDataNotFoundException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
                }


            } catch (Exception e) {
                throw new CustomDataNotFoundException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
            }
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }

    }
}
