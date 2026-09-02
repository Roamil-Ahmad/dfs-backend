package com.dfs.app.controller.googleAuthentication;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.ValidateGoogleCode;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.Response;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class GoogleAuthenticationController extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(GoogleAuthenticationController.class);
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private CommonService commonService;

    @Value("${google.auth.url}")
    private String googleAuthUrl;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private JWTSecurity jwtSecurity;
    @Autowired
    private TblLoginTokenRepo tblLoginTokenRepo;

    @GetMapping("/v1/generategoogleqrcode/{accountno}/{flag}")
    public ResponseEntity<HashMap<String, Object>> generateQr(@PathVariable String accountno, @PathVariable String flag) {
        Object result = null;
        if (flag.equals(Constants.YES)) {
            String url = googleAuthUrl + "/code/generateQr/" + accountno;
            result = thirdPartyService.generateQr(url);
        }
        TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(accountno);
        tblAppUser.setTwoFAEnabled(flag);
        tblAppUserRepo.save(tblAppUser);
        return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.SUCCESS.getResponseCode(), GenericResponseCode.SUCCESS.getResponseMessage(), result, "/v1/generateQr/{accountno}");

    }

    @PostMapping("/v1/validate/googleqrcode")
    public ResponseEntity<HashMap<String, Object>> validateGooleCode(@RequestBody Request request) throws JsonProcessingException {
        String url = googleAuthUrl + "/code/validateQrCode/key";
        ValidateGoogleCode validateGoogleCode = fromJson(convertObjecttoJson(request.getPayload()), ValidateGoogleCode.class);
        Response response = thirdPartyService.validateGooleCode(validateGoogleCode, request, url);
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(validateGoogleCode.getUsername()));
        if (isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
        if (isNullOrEmpty(tblCustomerAll.getImeiNo()) || !request.getImieNo().equals(tblCustomerAll.getImeiNo())) {
            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_INVALID.getResponseCode());
        }
        TblAppUser tblAppUser = tblAppUserRepo.findAppUserByUserName(aeSencryption.encryptwith256(validateGoogleCode.getUsername()));
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_USERNAME.getResponseCode());
        }
        TblCustomer tblCustomer = tblCustomerRepo.findByIdAndIsActive(tblAppUser.getCustomerId().longValue(), Constants.YES);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblAccount tblAccount = tblAccountRepo.findByCustomerAllId(tblCustomer.getTblCustomerAll().getCustomerAllId());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        HashMap<String, Object> hmClaims = new HashMap<>();
        hmClaims.put(JwtConstants.IMIE_NUMBER, tblCustomerAll.getImeiNo());
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblAccount.getAccountNo());
        hmClaims.put(JwtConstants.UUID, tblCustomerAll.getUuid());
        hmClaims.put(JwtConstants.APP_USER_ID, tblAppUser.getAppUserId());
        String autoLoginToken =null;
        if("1".equals(response.getResponsecode())){
            autoLoginToken = jwtSecurity.createJWTWithClaimsForDays(tblAccount.getAccountNo(), hmClaims, 7);
            TblLoginToken tblLoginToken = tblLoginTokenRepo.findByAppUserIdAndIsActive(tblAppUser.getAppUserId(), Constants.YES);
            if (tblLoginToken != null) {
                tblLoginToken.setIsActive(Constants.N);
                tblLoginToken.setLastupdateuser(tblAppUser.getUserId());
                tblLoginToken.setLastupdatedate(new Date());
                tblLoginToken.setUpdateindex(tblLoginToken.getUpdateindex() != null ? tblLoginToken.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblLoginTokenRepo.saveAndFlush(tblLoginToken);
            }
            tblLoginToken = new TblLoginToken();
            tblLoginToken.setCreatedate(new Date());
            tblLoginToken.setCreateuser(BigDecimal.valueOf(tblAppUser.getAppUserId()));
            tblLoginToken.setLoginToken(autoLoginToken);
            Calendar calendar = Calendar.getInstance();
            // Set current date-time as token effective from
            tblLoginToken.setIsActive(Constants.YES);
            tblLoginToken.setEffectiveFrom(calendar.getTime());
            // Add 7 days to current time for token expiration
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            tblLoginToken.setEffectiveTo(calendar.getTime());
            tblLoginToken.setTblAppUser(tblAppUser);
            tblLoginTokenRepo.saveAndFlush(tblLoginToken);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("autoLoginToken", autoLoginToken);
        HttpStatus status = HttpStatus.OK;
        boolean isSuccess = "1".equals(response.getResponsecode());
        String code = isSuccess
                ? GenericResponseCode.SUCCESS.getResponseCode()
                : GenericResponseCode.BAD_REQUEST.getResponseCode();
        return getCustomizedResponseFormat(status, code, response.getMessages(), map, "/v1/generateQr/{accountno}");
    }
}

