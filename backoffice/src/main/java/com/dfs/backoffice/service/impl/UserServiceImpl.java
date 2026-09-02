package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.repo.*;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.service.NotificationService;
import com.dfs.backoffice.service.OtpService;
import com.dfs.backoffice.service.UserService;
import com.dfs.backoffice.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends HelperClass implements UserService {
    @PersistenceContext
    EntityManager em;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblUserRepo tblUsersRepo;
    @Autowired
    private TblUserRoleRepo tblUserRoleRepo;
    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;
    @Autowired
    private TblAppUserLoginHistoryRepo tblAppUserLoginHistoryRepo;
    @Autowired
    private JWTSecurity jwtSecurity;
    @Autowired
    private TblAuthAccessTokenRepo tblAuthAccessTokenRepo;
    @Autowired
    private TblMenuRepo tblMenuRepo;
    @Autowired
    private TblRoleRepo tblRoleRepo;
    @Autowired
    private TblRoleRightRepo tblRoleRightRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private TblTransLimitRepo tblTransLimitRepo;
    @Autowired
    private TblTransLimitDetailRepo tblTransLimitDetailRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.auth.url}")
    private String googleauthurl;

    @Override
    public LoginResponse userLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        AESencryption aeSencryption = new AESencryption();
        String encPassword = aeSencryption.encryptwith256(loginRequest.getPassword());
        TblAppUser tblAppUser = tblAppUserRepo.userLogin(loginRequest.getUsername().toUpperCase(), encPassword);
        LoginResponse loginResponse = new LoginResponse();
        if (tblAppUser != null) {
            TblUser tblUsers = tblUsersRepo.getUserById(tblAppUser.getUserId().longValue());
            TblUserRole tblUserRole = tblUserRoleRepo.findByTblUserUserId(tblAppUser.getUserId().longValue());
            String multiAllowed = "";
            TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName("MULTI_LOGIN");
            multiAllowed = tblGlobalConfig.getKeyValue();
            if (multiAllowed.equals("N") && !checkUserLoggedIn(tblAppUser.getUserId())) {
                loginResponse.setErrorResponse("User cannot login from multiple devices");
                return loginResponse;
            }
            if (tblUsers.getIsActive().equals("N")) {
                loginResponse.setErrorResponse("User is Currently Inactive");
                return loginResponse;
            }
            loginResponse.setTblAppUser(tblAppUser);
            String remoteAddr = null;

            if (httpServletRequest != null) {
                remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");

                if (remoteAddr != null && !remoteAddr.trim().isEmpty()) {
                    // Take first IP if multiple IPs are present
                    remoteAddr = remoteAddr.split(",")[0].trim();
                } else {
                    remoteAddr = httpServletRequest.getRemoteAddr();
                }
            }
            System.out.println("REMOTE ADDRESS " + remoteAddr);
            TblAppUserLoginHistory tblAppUserLoginHistory = new TblAppUserLoginHistory();
            tblAppUserLoginHistory.setMacAddress(new BigDecimal(0));
            tblAppUserLoginHistory.setLoginDate(new Date());
            tblAppUserLoginHistory.setTblAppUser(tblAppUser);
            tblAppUserLoginHistory.setIpAddressA(remoteAddr);
            tblAppUserLoginHistory = tblAppUserLoginHistoryRepo.save(tblAppUserLoginHistory);

            loginResponse.setTblAppUserLoginHistory(tblAppUserLoginHistory);

            HashMap<String, Object> claims = new HashMap<>();
            claims.put(JwtConstants.APP_USER_ID, String.valueOf(tblAppUser.getAppUserId()));
            claims.put(JwtConstants.PASSWORD_UPDATE_FLAG, tblAppUser.getPasswordUpdateFlag());
            claims.put(JwtConstants.USER_LOGIN_HISTORY_ID, tblAppUserLoginHistory.getAppUserLoginHistoryId());

            String token = jwtSecurity.createJWTWithClaims(loginRequest.getUsername(), claims, 60);
            if (token != null && !token.isEmpty()) {
                TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token, tblAppUserLoginHistory.getAppUserLoginHistoryId(), Constants.AFTER_LOGIN, tblAppUser.getAppUserId());

                if (tblAuthAccessToken == null) {
                    throw new CustomException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
                }
                loginResponse.setToken(tblAuthAccessToken.getAccessToken());
                loginResponse.setTwoFA(tblUsers.getTwoFaEnabled());

                List<TblMenu> tblMenus = tblMenuRepo.userMenu(tblUserRole.getTblRole().getRoleId());
                if (tblMenus != null) {

                    List<MenuResponse> menuResponses = new ArrayList<MenuResponse>();

                    for (TblMenu tblMenu : tblMenus) {
                        if (tblMenu.getIsActive().equals("Y")) {
                            if (tblMenu.getLkpStatus().getStatusId() == 2) {
                                MenuResponse menuResponse = new MenuResponse();
                                menuResponse.setMenuId(new BigDecimal(tblMenu.getMenuId()));
                                menuResponse.setBussfunc(tblMenu.getMenuCode());
                                menuResponse.setIcon("fa fa-recycle");
                                menuResponse.setName(tblMenu.getMenuDescr());
                                menuResponse.setParent_id(tblMenu.getParentMenu());
                                menuResponse.setUrl(tblMenu.getMenuPath());
                                menuResponse.setReportId(tblMenu.getReportId());
                                menuResponse.setDeleteAllowed(tblMenu.getTblRoleRights().get(0).getDeleteAllowed());
                                menuResponse.setInsertAllowed(tblMenu.getTblRoleRights().get(0).getInsertAllowed());
                                menuResponse.setUpdateAllowed(tblMenu.getTblRoleRights().get(0).getUpdateAllowed());
                                menuResponse.setViewAllowed(tblMenu.getTblRoleRights().get(0).getViewAllowed());
                                menuResponses.add(menuResponse);
                            }
                        }
                    }
                    loginResponse.setMenu(menuResponses);
                    GenerateOtpResponse generateOtpResponse = new GenerateOtpResponse();
//                    if (tblUsers.getTwoFaEnabled() != null && tblUsers.getTwoFaEnabled().equalsIgnoreCase("Y") && tblUsers.getTwoFaType().equalsIgnoreCase("E")) {
//                        //generate OTP and sendEmail
//                        GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
//                        generateOtpRequest.setMobileNumber(tblUsers.getMobileNo());
//                        generateOtpRequest.setOtpType("LGE");
//                        generateOtpRequest.setOtpIdentifier("S");
//                        generateOtpRequest.setOtpTemplateCode("LGN");
//                        Response response = otpService.generateOtp(generateOtpRequest);
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        generateOtpResponse = objectMapper.readValue(Objects.requireNonNull(convertObjecttoJson(response.getPayload())), GenerateOtpResponse.class);
//                        generateOtpResponse.setOtpType("LGE");
//                        generateOtpResponse.setEmail(tblUsers.getEmail());
//                    } else if (tblUsers.getTwoFaEnabled() != null && tblUsers.getTwoFaEnabled().equalsIgnoreCase("Y") && tblUsers.getTwoFaType().equalsIgnoreCase("S")) {
//                        //generate OTP and send message
//                        GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
//                        generateOtpRequest.setMobileNumber(tblUsers.getMobileNo());
//                        generateOtpRequest.setOtpType("LGS");
//                        generateOtpRequest.setOtpIdentifier("S");
//                        generateOtpRequest.setOtpTemplateCode("LGN");
//                        Response response = otpService.generateOtp(generateOtpRequest);
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        generateOtpResponse = objectMapper.readValue(Objects.requireNonNull(convertObjecttoJson(response.getPayload())), GenerateOtpResponse.class);
//                        generateOtpResponse.setOtpType("LGS");
//                    }
//                    generateOtpResponse.setOtpCode(null);
                    loginResponse.setOtpResponse(generateOtpResponse);
                } else {
                    loginResponse.setErrorResponse("Error while fetching menu");
                    return loginResponse;
                }
                TblGlobalConfig tblGlobalConfigUserSession = tblGlobalConfigRepo.findByKeyName(Constants.LOGOUT_TIME_KEY);
                loginResponse.setSessionExpireDuration(tblGlobalConfigUserSession.getKeyValue());
                return loginResponse;
            } else {
                loginResponse.setErrorResponse("Error while generating token");
                return loginResponse;
            }
        } else {
            loginResponse.setErrorResponse(GenericResponseCode.INVALID_USERNAME_OR_PASSWORD.getResponseMessage());
            return loginResponse;
        }
    }

    public boolean checkUserLoggedIn(BigDecimal userId) {
        String sql = "select lh.logout_date , lh.ip_address_a from TBL_APP_USER_LOGIN_HISTORY lh where lh.APP_USER_ID = " + userId + " order by lh.APP_USER_LOGIN_HISTORY_ID desc fetch first 1 rows only";
        try {
            Query query = em.createNativeQuery(sql);
            TblAppUserLoginHistory tblAppUserLoginHistory = null;
            List<Object> tblUserLoginHistory = (List<Object>) query.getResultList();

            if (tblUserLoginHistory != null && !tblUserLoginHistory.isEmpty()) {
                for (Object record : tblUserLoginHistory) {
                    Object[] row = (Object[]) record;

                    tblAppUserLoginHistory = new TblAppUserLoginHistory();
                    tblAppUserLoginHistory.setLogoutDate((Date) row[0]);
                    tblAppUserLoginHistory.setIpAddressA((String) row[1]);
                }
                if (tblAppUserLoginHistory != null && tblAppUserLoginHistory.getLogoutDate() != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public TblAppUserLoginHistory userLogout(HttpServletRequest request) {
        TblAppUserLoginHistory tblUserLoginHistory = tblAppUserLoginHistoryRepo.findById(Long.valueOf(request.getAttribute(JwtConstants.USER_LOGIN_HISTORY_ID).toString())).orElse(null);
        if (tblUserLoginHistory != null) {
            tblUserLoginHistory.setLogoutDate(new Date());
            tblAppUserLoginHistoryRepo.logoutUser(tblUserLoginHistory.getAppUserLoginHistoryId(), tblUserLoginHistory.getLogoutDate());
        }
        return tblUserLoginHistory;
    }

    @Override
    public Response processCreateUser(CreateUserRequest createUserRequest, HttpServletRequest httpServletRequest) {
        Response response = new Response();
        boolean blockedStatus = commonService.checkBlockListStatus(createUserRequest.getNidNo().longValue());
        if (blockedStatus) {
            throw new CustomException(GenericResponseCode.USER_BLACKLISTED.getResponseCode());
        }
        TblUser userCnic = getUserByCnic(createUserRequest.getNidNo());
        if (userCnic != null) {
            throw new CustomException(GenericResponseCode.USER_NID_REGISTERED.getResponseCode());
        }
        TblAppUser tblAppUser = getAppUserByUserName(createUserRequest.getUserName());
        if (tblAppUser != null) {
            throw new CustomException(GenericResponseCode.USER_NAME_EXIST.getResponseCode());
        }
        TblUser tblUser1 = tblUsersRepo.findByEmail(createUserRequest.getEmail());
        if (tblUser1 != null) {
            throw new CustomException(GenericResponseCode.EMAIL_ALREADY_EXIST.getResponseCode());
        }
        String result = checkMakerCheckerApplicability(httpServletRequest.getHeader(Constants.AUTHORIZATION), Constants.TABLE_NAME_USER, Constants.FORM_NAME_USER_ADD, Constants.REQUEST_TYPE_SAVE);
        if (result.equals(Constants.NOT_MAKER_CHECKER)) {
            response = saveUserWithoutMakerChecker(new BigDecimal((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)), createUserRequest, response);
        } else if (result.equals(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode())) {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        } else {
            return saveUserWithMakerChecker(httpServletRequest, createUserRequest, response);
        }
        return response;
    }

    private Response saveUserWithoutMakerChecker(BigDecimal appUserId, CreateUserRequest createUserRequest, Response response) {
        createUserRequest.setIsActive(Constants.YES);
        createUserRequest.setStatusId(new BigDecimal(2));
        TblUser tblUser = saveUser(createUserRequest, appUserId);
        if (tblUser != null) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    private Response saveUserWithMakerChecker(HttpServletRequest httpServletRequest, CreateUserRequest createUserRequest, Response response) {
        createUserRequest.setIsActive(Constants.NO);
        createUserRequest.setStatusId(new BigDecimal(1));
        TblUser tblUser = saveUser(createUserRequest, new BigDecimal((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)));
        if (tblUser != null) {
            return makerCheckerRequest(httpServletRequest.getHeader(Constants.AUTHORIZATION), Constants.TABLE_NAME_USER, Constants.FORM_NAME_USER_ADD, new McRequestDetail(String.valueOf(httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)), Constants.EMPTY, Constants.EMPTY, Constants.REQUEST_TYPE_SAVE, Constants.EMPTY, Constants.EMPTY, String.valueOf(tblUser.getUserId()), Constants.EMPTY));
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }


    private TblUser saveUser(CreateUserRequest createUserRequest, BigDecimal appUserId) {
        TblUser tblUser = new TblUser();
        AESencryption aeSencryption = new AESencryption();
        String password = autoGenerate("Numeric", 6);
        String encPassword = aeSencryption.encryptwith256(password);

        String msg = "Your User has been created successfully. Your login password is " + password;
        tblUser.setEmployeeNo(createUserRequest.getEmployeeNo());
        tblUser.setEmployeeName(createUserRequest.getEmployeeName());
        tblUser.setNidNo(createUserRequest.getNidNo());
        tblUser.setDob(createUserRequest.getDob());
        tblUser.setGrandfatherName(createUserRequest.getGrandFatherName());
        tblUser.setDepartment(createUserRequest.getDepartment());
        tblUser.setEmail(createUserRequest.getEmail());
        tblUser.setMobileNo(createUserRequest.getMobileNo());
        tblUser.setIsActive(createUserRequest.getIsActive());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(createUserRequest.getStatusId().longValue());
        tblUser.setLkpStatus(lkpStatus);
        tblUser.setLandline(createUserRequest.getLandline());
        tblUser.setDesignation(createUserRequest.getDesignation());
        tblUser.setCreateuser(appUserId);
        tblUser.setCreatedate(new Date());
        TblUser tblUsers = tblUsersRepo.saveAndFlush(tblUser);
        TblAppUser tblAppUser = new TblAppUser();
        tblAppUser.setCreatedate(new Date());
        tblAppUser.setCreateuser(appUserId);
        tblAppUser.setMobileNo(createUserRequest.getMobileNo());
        tblAppUser.setPassword(encPassword);
        tblAppUser.setUserId(new BigDecimal(tblUser.getUserId()));
        tblAppUser.setPasswordUpdateFlag(Constants.NO);
        tblAppUser.setUsername(createUserRequest.getUserName());
        tblAppUser.setUserSms(msg);
        tblAppUserRepo.save(tblAppUser);
        TblUserRole tblUserRole = new TblUserRole();
        tblUserRole.setTblUser(tblUser);
        tblUserRole.setCreatedate(new Date());
        tblUserRole.setCreateuser(appUserId);
        tblUserRole.setIsActive("Y");
        tblUserRole.getTblRole().setRoleId(createUserRequest.getRoleId().longValue());
        tblUserRoleRepo.save(tblUserRole);
        TblSmsMessage message = new TblSmsMessage();
        message.setMessage(msg);
        message.setMobileNo(tblUser.getMobileNo());
        message.setSendFlag("0");
        message.setTransHeadId(null);
        message.setCreateuser(appUserId);
        commonService.saveSmSMessage(message);
        GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
        generateNotificationRequest.setEmail(tblUser.getEmail());
        generateNotificationRequest.setSubject("DFS User Email");
        generateNotificationRequest.setSms(msg);
        generateNotificationRequest.setType("E");
        notificationService.notify(generateNotificationRequest, new BigDecimal(tblAppUser.getAppUserId()));
        return tblUsers;
    }

    @Override
    public TblUser getUserByCnic(BigDecimal cnic) {
        return tblUsersRepo.getUserByCnic(cnic);
    }

    @Override
    public TblAppUser getAppUserByUserName(String userName) {
        return tblAppUserRepo.findByUsername(userName);
    }

    @Override
    public Response processUpdateUser(CreateUserRequest data, HttpServletRequest request) {
        Response response = new Response();
        TblUser tblUser = tblUsersRepo.getUsersByEmail(data.getEmail(), data.getUserId());
        if (tblUser != null) {
            throw new CustomException(GenericResponseCode.EMAIL_ALREADY_EXIST.getResponseCode());
        }
        String result = checkMakerCheckerApplicability(request.getHeader(Constants.AUTHORIZATION), Constants.TABLE_NAME_USER, Constants.FORM_NAME_USER_EDIT, Constants.REQUEST_TYPE_UPDATE);
        if (result.equals(Constants.NOT_MAKER_CHECKER)) {
            return updateUserWithoutMakerChecker(request, data, response);
        } else if (result.equals(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode())) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        } else {
            return processMakerCheckerRequest(request, data);
        }
        return response;
    }


    private Response updateUserWithoutMakerChecker(HttpServletRequest httpServletRequest, CreateUserRequest createUserRequest, Response response) {
        createUserRequest.setIsActive(Constants.YES);
        createUserRequest.setStatusId(new BigDecimal(2));
        TblUser tblUser = updateUser(createUserRequest, new BigDecimal((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)));
        if (tblUser != null) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ONE, null, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    private TblUser updateUser(CreateUserRequest createUserRequest, BigDecimal appUserId) {
        TblUser tblUser = tblUsersRepo.findById(createUserRequest.getUserId()).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
        tblUser.setEmployeeNo(createUserRequest.getEmployeeNo());
        tblUser.setEmployeeName(createUserRequest.getEmployeeName());
        tblUser.setNidNo(createUserRequest.getNidNo());
        tblUser.setDob(createUserRequest.getDob());
        tblUser.setGrandfatherName(createUserRequest.getGrandFatherName());
        tblUser.setDepartment(createUserRequest.getDepartment());
        tblUser.setEmail(createUserRequest.getEmail());
        tblUser.setMobileNo(createUserRequest.getMobileNo());
        tblUser.setLandline(createUserRequest.getLandline());
        tblUser.setDesignation(createUserRequest.getDesignation());
        tblUser.setLastupdateuser(appUserId);
        tblUser.setLastupdatedate(new Date());
        tblUser.setUpdateindex(tblUser.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblUser.getUpdateindex().intValue() + 1));
        TblUser userResponse = tblUsersRepo.save(tblUser);
        TblAppUser tblAppUser = tblAppUserRepo.findByUserId(new BigDecimal(tblUser.getUserId()));
        tblAppUser.setMobileNo(createUserRequest.getMobileNo());
        tblAppUser.setUsername(createUserRequest.getUserName());
        tblAppUserRepo.save(tblAppUser);
        TblUserRole tblUserRole = tblUserRoleRepo.findByTblUserUserId(tblUser.getUserId());
        tblUserRole.setTblUser(tblUser);
        tblUserRole.setIsActive("Y");
        TblRole tblRole = new TblRole();
        tblRole.setRoleId(createUserRequest.getRoleId().longValue());
        tblUserRole.setTblRole(tblRole);
        tblUserRole.setLastupdateuser(appUserId);
        tblUserRole.setLastupdatedate(new Date());
        tblUserRole.setUpdateindex(tblUserRole.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblUserRole.getUpdateindex().intValue() + 1));
        tblUserRoleRepo.save(tblUserRole);

        return userResponse;
    }

    @Override
    public TblUser getUserById(long userId) {
        return tblUsersRepo.getUserById(userId);
    }

    private Response processMakerCheckerRequest(HttpServletRequest request, CreateUserRequest data) {
        String updateJson = convertObjecttoJson(data);
        return makerCheckerRequest(request.getHeader(Constants.AUTHORIZATION), Constants.TABLE_NAME_USER, Constants.FORM_NAME_USER_EDIT, new McRequestDetail(String.valueOf(request.getAttribute(JwtConstants.APP_USER_ID)), Constants.EMPTY, Constants.EMPTY, Constants.REQUEST_TYPE_UPDATE, Constants.REQUEST_TYPE_UPDATE, updateJson, String.valueOf(data.getUserId()), Constants.EMPTY));
    }

    @Override
    public Response processInactiveUser(CreateUserRequest createUserRequest, HttpServletRequest request) {
        Response response = new Response();
        String result = checkMakerCheckerApplicability(request.getHeader(Constants.AUTHORIZATION), Constants.TABLE_NAME_USER, Constants.FORM_NAME_USER_EDIT, Constants.REQUEST_TYPE_UPDATE);
        if (result.equals(Constants.NOT_MAKER_CHECKER)) {
            TblUser tblUser = inactiveUser(createUserRequest, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            if (!isNullOrEmpty(tblUser)) {
                setResponse(response, Constants.ONE, tblUser, GenericResponseCode.SUCCESS.getResponseCode());
            } else {
                setResponse(response, Constants.ZERO, tblUser, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
            }
        } else if (result.equals(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode())) {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        } else {
            String updateJson = convertObjecttoJson(createUserRequest);
            makerCheckerRequest(request.getHeader(Constants.AUTHORIZATION), Constants.TABLE_NAME_USER, Constants.FORM_NAME_USER_EDIT, new McRequestDetail(String.valueOf(request.getAttribute(JwtConstants.APP_USER_ID)), Constants.EMPTY, Constants.EMPTY, Constants.REQUEST_TYPE_UPDATE, Constants.UPDATE_TYPE, updateJson, String.valueOf(createUserRequest.getUserId()), Constants.EMPTY));
        }
        return response;
    }

    private TblUser inactiveUser(CreateUserRequest createUserRequest, BigDecimal appUserId) {
        TblUser tblUser = tblUsersRepo.findById(createUserRequest.getUserId()).orElse(null);
        if (tblUser != null) {
            tblUser.setIsActive(createUserRequest.getIsActive());
            tblUser.setLastupdateuser(appUserId);
            tblUser.setLastupdatedate(new Date());
            tblUser.setUpdateindex(tblUser.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblUser.getUpdateindex().intValue() + 1));
            tblUser = tblUsersRepo.save(tblUser);
        }
        return tblUser;
    }

    @Override
    public Response checkerActionUser(HttpServletRequest request, Response response, EntityActionRequest entityActionRequest, McActionResponse mcResponse) {
        entityActionRequest.getCreateUserRequest().setStatusId(new BigDecimal(2));
        if (entityActionRequest.getCreateUserRequest().getIsActive() != null && !entityActionRequest.getCreateUserRequest().getIsActive().equals(Constants.EMPTY)) {
            entityActionRequest.getCreateUserRequest().setIsActive(entityActionRequest.getCreateUserRequest().getIsActive());
        } else {
            entityActionRequest.getCreateUserRequest().setIsActive(Constants.YES);
        }
        TblUser tblUser = updateUser(entityActionRequest.getCreateUserRequest(), new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        if (!isNullOrEmpty(tblUser)) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode(), mcResponse != null ? mcResponse.getStatusDecsr() : Constants.GENERAL_PROCESSING_ERROR);
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Transactional
    @Override
    public Response processChangePassword(@RequestBody ChangePasswordRequest data, HttpServletRequest request) {
        AESencryption aeSencryption = new AESencryption();
        Response response = new Response();

        if (data.getNewPass().equals(data.getConfirmPass())) {
            String encOldPassword = aeSencryption.encryptwith256(data.getOldPass());
            TblAppUser tblAppUser = getUserByIdPassword(Long.parseLong(request.getAttribute(JwtConstants.APP_USER_ID).toString()), encOldPassword);
            if (tblAppUser != null) {
                String encPassword = data.getNewPass();
                encPassword = aeSencryption.encryptwith256(encPassword);
                long userId = Long.parseLong(request.getAttribute(JwtConstants.APP_USER_ID).toString());
                TblAppUser tblUsers = changePassword(encPassword, userId);
                if (tblUsers != null) {
                    setResponse(response, Constants.ONE, tblUsers, GenericResponseCode.SUCCESS.getResponseCode(), "Password Changed Successfully");
                } else {
                    setResponse(response, Constants.ZERO, tblUsers, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), "Password Changed Failed");
                }
            } else {
                setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), "Invalid Old Password");
            }
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), "Invalid Old Password");
        }
        return response;
    }

    public TblAppUser getUserByIdPassword(long userId, String oldPassword) {
        return tblAppUserRepo.getUserByIdPassword(userId, oldPassword);
    }


    public TblAppUser changePassword(String encPassword, long userId) {
        TblAppUser tblAppUser = tblAppUserRepo.findById(userId).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
        tblAppUser.setPassword(encPassword);
        tblAppUser.setPasswordUpdateFlag(Constants.YES);
        return tblAppUserRepo.save(tblAppUser);
    }

    @Transactional
    @Override
    public Response processChangeUserPassword(ChangeUserPasswordRequest changeUserPasswordRequest, HttpServletRequest request) {
        AESencryption aeSencryption = new AESencryption();
        Response response = new Response();
        String encPassword = autoGenerate("Numeric", 6);
        encPassword = aeSencryption.encryptwith256(encPassword);

        TblAppUser tblAppUser = getAppUserByUserName(changeUserPasswordRequest.getUserName());
        if (tblAppUser != null) {
            TblAppUser tblUsers = changePassword(encPassword, tblAppUser.getUserId().longValue());
            if (tblUsers != null) {
                //SMS API TO Send Password
                setResponse(response, Constants.ONE, tblUsers, GenericResponseCode.SUCCESS.getResponseCode(), "Password Changed SuccessFully and Sent to Users Registered mobile Number");
            } else {
                setResponse(response, Constants.ZERO, tblUsers, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), "Password Changed Failed");
            }
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), "Invalid User Information");
        }
        return response;
    }

    @Override
    public Response saveMenu(CreateMenuRequest createMenuRequest, HttpServletRequest request) {
        TblMenu tblMenu = new TblMenu();
        Response response = new Response();
        tblMenu.setMenuCode(createMenuRequest.getMenuCode());
        tblMenu.setMenuDescr(createMenuRequest.getMenuDescr());
        tblMenu.setMenuType(createMenuRequest.getMenuType());
        tblMenu.setParentMenu(createMenuRequest.getParentMenu());
        tblMenu.setMenuPath(createMenuRequest.getMenuPath());
        tblMenu.setSortSeq(createMenuRequest.getSortSeq());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2L);
        tblMenu.setLkpStatus(lkpStatus);
        tblMenu.setIsActive(createMenuRequest.getIsActive());
        tblMenu.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblMenu.setCreatedate(new Date());
        tblMenu = tblMenuRepo.save(tblMenu);
        if (tblMenu != null) {
            setResponse(response, Constants.ONE, tblMenu, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblMenu, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateMenu(CreateMenuRequest createMenuRequest, HttpServletRequest request) {
        Response response = new Response();
        TblMenu tblMenu = getMenuById(createMenuRequest.getMenuId());
        if (tblMenu != null) {
            tblMenu.setMenuCode(createMenuRequest.getMenuCode());
            tblMenu.setMenuDescr(createMenuRequest.getMenuDescr());
            tblMenu.setMenuType(createMenuRequest.getMenuType());
            tblMenu.setParentMenu(createMenuRequest.getParentMenu());
            tblMenu.setMenuPath(createMenuRequest.getMenuPath());
            tblMenu.setSortSeq(createMenuRequest.getSortSeq());
            LkpStatus lkpStatus = new LkpStatus();
            lkpStatus.setStatusId(2L);
            tblMenu.setLkpStatus(lkpStatus);
            tblMenu.setIsActive(createMenuRequest.getIsActive());
            tblMenu.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            tblMenu.setLastupdatedate(new Date());
            tblMenu.setUpdateindex(tblMenu.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblMenu.getUpdateindex().intValue() + 1));
            tblMenu = tblMenuRepo.save(tblMenu);
            if (tblMenu != null) {
                setResponse(response, Constants.ONE, tblMenu, GenericResponseCode.SUCCESS.getResponseCode());
            } else {
                setResponse(response, Constants.ONE, tblMenu, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
            }
        } else {
            setResponse(response, Constants.ZERO, tblMenu, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }

        return response;
    }

    public TblMenu getMenuById(long menuId) {
        return tblMenuRepo.getMenuById(menuId);
    }

    @Override
    public Response processInactiveMenu(CreateMenuRequest createMenuRequest, HttpServletRequest request) {
        Response response = new Response();
        TblMenu tblMenu = getMenuById(createMenuRequest.getMenuId());
        if (tblMenu != null) {
            tblMenu.setIsActive(createMenuRequest.getIsActive());
            tblMenu.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
            tblMenu.setLastupdatedate(new Date());
            tblMenu.setUpdateindex(tblMenu.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblMenu.getUpdateindex().intValue() + 1));
            tblMenu = tblMenuRepo.save(tblMenu);
            if (tblMenu != null) {
                setResponse(response, Constants.ONE, tblMenu, GenericResponseCode.RECORD_UPDATED.getResponseCode());
            } else {
                setResponse(response, Constants.ONE, tblMenu, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
            }
        } else {
            setResponse(response, Constants.ZERO, tblMenu, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    public Response saveRole(CreateRoleRequest createRoleRequest, HttpServletRequest request) {
        Response response = new Response();
        TblRole tblRole = new TblRole();
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2L);
        tblRole.setLkpStatus(lkpStatus);
        tblRole.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblRole.setRoleCode(createRoleRequest.getRoleCode());
        tblRole.setRoleDescr(createRoleRequest.getRoleDescr());
        tblRole.setIsActive(createRoleRequest.getIsActive());
        tblRole.setCreatedate(new Date());
        tblRole = tblRoleRepo.save(tblRole);
        if (tblRole != null) {
            setResponse(response, Constants.ONE, tblRole, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRole, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateRole(CreateRoleRequest createRoleRequest, HttpServletRequest request) {
        Response response = new Response();
        TblRole tblRole = tblRoleRepo.findById(createRoleRequest.getRoleId()).orElseThrow(() -> new CustomException("Role Not Found"));
        tblRole.setRoleCode(createRoleRequest.getRoleCode());
        tblRole.setRoleDescr(createRoleRequest.getRoleDescr());
        tblRole.setIsActive(createRoleRequest.getIsActive());
        tblRole.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblRole.setLastupdatedate(new Date());
        tblRole.setUpdateindex(tblRole.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblRole.getUpdateindex().intValue() + 1));
        tblRole = tblRoleRepo.save(tblRole);
        if (tblRole != null) {
            setResponse(response, Constants.ONE, tblRole, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRole, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response inactiveRole(CreateRoleRequest createRoleRequest, HttpServletRequest request) {
        Response response = new Response();
        TblRole tblRole = tblRoleRepo.findById(createRoleRequest.getRoleId()).orElseThrow(() -> new CustomException("Role Not Found"));
        tblRole.setIsActive(createRoleRequest.getIsActive());
        tblRole.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblRole.setLastupdatedate(new Date());
        tblRole.setUpdateindex(tblRole.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblRole.getUpdateindex().intValue() + 1));
        tblRole = tblRoleRepo.save(tblRole);
        if (tblRole != null) {
            setResponse(response, Constants.ONE, tblRole, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRole, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response saveRoleRights(CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request) {
        Response response = new Response();
        TblRoleRight roleRights = new TblRoleRight();

        TblRole tblRoleByRoleId = new TblRole();
        TblMenu tblMenuByMenuId = new TblMenu();

        tblMenuByMenuId.setMenuId(Long.parseLong(createRoleRightsRequest.getMenuId()));
        tblRoleByRoleId.setRoleId(Long.parseLong(createRoleRightsRequest.getRoleId()));
        roleRights.setDeleteAllowed(createRoleRightsRequest.getDelete());
        roleRights.setViewAllowed(createRoleRightsRequest.getView());
        roleRights.setInsertAllowed(createRoleRightsRequest.getInsert());
        roleRights.setUpdateAllowed(createRoleRightsRequest.getUpdate());
        roleRights.setIsActive(createRoleRightsRequest.getIsActive());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2L);
        roleRights.setLkpStatus(lkpStatus);
        roleRights.setCreatedate(new Date());
        roleRights.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        roleRights.setTblMenu(tblMenuByMenuId);
        roleRights.setTblRole(tblRoleByRoleId);
        roleRights = tblRoleRightRepo.save(roleRights);
        if (roleRights != null) {
            setResponse(response, Constants.ONE, roleRights, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roleRights, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateRoleRights(CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request) {
        Response response = new Response();
        TblRoleRight roleRights = tblRoleRightRepo.findById(createRoleRightsRequest.getRoleRightsId()).orElseThrow(() -> new CustomException("Role Rights Not Found"));
        TblRole tblRoleByRoleId = new TblRole();
        TblMenu tblMenuByMenuId = new TblMenu();

        tblMenuByMenuId.setMenuId(Long.parseLong(createRoleRightsRequest.getMenuId()));
        tblRoleByRoleId.setRoleId(Long.parseLong(createRoleRightsRequest.getRoleId()));

        roleRights.setDeleteAllowed(createRoleRightsRequest.getDelete());
        roleRights.setViewAllowed(createRoleRightsRequest.getView());
        roleRights.setInsertAllowed(createRoleRightsRequest.getInsert());
        roleRights.setUpdateAllowed(createRoleRightsRequest.getUpdate());
        roleRights.setIsActive(createRoleRightsRequest.getIsActive());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2L);
        roleRights.setLkpStatus(lkpStatus);
        roleRights.setTblMenu(tblMenuByMenuId);
        roleRights.setTblRole(tblRoleByRoleId);
        roleRights.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        roleRights.setLastupdatedate(new Date());
        roleRights.setUpdateindex(roleRights.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(roleRights.getUpdateindex().intValue() + 1));
        roleRights = tblRoleRightRepo.save(roleRights);
        if (roleRights != null) {
            setResponse(response, Constants.ONE, roleRights, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roleRights, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response inactiveRoleRights(CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request) {
        Response response = new Response();
        TblRoleRight roleRights = tblRoleRightRepo.findById(createRoleRightsRequest.getRoleRightsId()).orElseThrow(() -> new CustomException("Role Rights Not Found"));
        roleRights.setIsActive(createRoleRightsRequest.getIsActive());
        roleRights.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        roleRights.setLastupdatedate(new Date());
        roleRights.setUpdateindex(roleRights.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(roleRights.getUpdateindex().intValue() + 1));
        roleRights = tblRoleRightRepo.save(roleRights);
        if (roleRights != null) {
            setResponse(response, Constants.ONE, roleRights, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roleRights, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response saveAccountLevel(AccountLevelRequest accountLevelRequest, HttpServletRequest request) {
        TblAccountLevel tblAccountLevelCodeCheck = tblAccountLevelRepo.findByAccountLevelCode(accountLevelRequest.getAccountLevelCode());
        if (tblAccountLevelCodeCheck != null) {
            throw new CustomException(GenericResponseCode.RECORD_ALREADY_EXIST.getResponseCode());
        }
        Response response = new Response();
        TblAccountLevel tblAccountLevel = new TblAccountLevel();
        tblAccountLevel.setAccountLevelCode(accountLevelRequest.getAccountLevelCode());
        tblAccountLevel.setAccountLevelDescr(accountLevelRequest.getAccountLevelDescr());
        tblAccountLevel.setDailyAmtLimitCr(accountLevelRequest.getDailyAmtLimitCr());
        tblAccountLevel.setMonthlyAmtLimitCr(accountLevelRequest.getMonthlyAmtLimitCr());
        tblAccountLevel.setYearlyAmtLimitCr(accountLevelRequest.getYearlyAmtLimitCr());
        tblAccountLevel.setDailyAmtLimitDr(accountLevelRequest.getDailyAmtLimitDr());
        tblAccountLevel.setMonthlyAmtLimitDr(accountLevelRequest.getMonthlyAmtLimitDr());
        tblAccountLevel.setYearlyAmtLimitDr(accountLevelRequest.getYearlyAmtLimitDr());
        tblAccountLevel.setDailyTransLimitCr(accountLevelRequest.getDailyTransLimitCr());
        tblAccountLevel.setMonthlyTransLimitCr(accountLevelRequest.getMonthlyTransLimitCr());
        tblAccountLevel.setYearlyTransLimitCr(accountLevelRequest.getYearlyTransLimitCr());
        tblAccountLevel.setDailyTransLimitDr(accountLevelRequest.getDailyTransLimitDr());
        tblAccountLevel.setMonthlyTransLimitDr(accountLevelRequest.getMonthlyTransLimitDr());
        tblAccountLevel.setYearlyTransLimitDr(accountLevelRequest.getYearlyTransLimitDr());
        tblAccountLevel.setMaxAmtLimit(accountLevelRequest.getMaxAmtLimit());
        tblAccountLevel.setMaxAmtPerTxn(accountLevelRequest.getMaxAmtPerTxn());
        tblAccountLevel.setGlAccountId(accountLevelRequest.getGlAccountId());
        tblAccountLevel.setIsActive(accountLevelRequest.getIsActive());
        tblAccountLevel.setStatusId(new BigDecimal(2));
        tblAccountLevel.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblAccountLevel.setCreatedate(new Date());
        tblAccountLevel = tblAccountLevelRepo.save(tblAccountLevel);
        if (tblAccountLevel != null) {
            setResponse(response, Constants.ONE, tblAccountLevel, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAccountLevel, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateAccountLevel(AccountLevelRequest accountLevelRequest, HttpServletRequest request) {
        Response response = new Response();
        TblAccountLevel tblAccountLevel = tblAccountLevelRepo.findById(accountLevelRequest.getAccountLevelId()).orElseThrow(() -> new CustomException("Account Level Not Found"));
        tblAccountLevel.setAccountLevelCode(accountLevelRequest.getAccountLevelCode());
        tblAccountLevel.setAccountLevelDescr(accountLevelRequest.getAccountLevelDescr());
        tblAccountLevel.setDailyAmtLimitCr(accountLevelRequest.getDailyAmtLimitCr());
        tblAccountLevel.setMonthlyAmtLimitCr(accountLevelRequest.getMonthlyAmtLimitCr());
        tblAccountLevel.setYearlyAmtLimitCr(accountLevelRequest.getYearlyAmtLimitCr());
        tblAccountLevel.setDailyAmtLimitDr(accountLevelRequest.getDailyAmtLimitDr());
        tblAccountLevel.setMonthlyAmtLimitDr(accountLevelRequest.getMonthlyAmtLimitDr());
        tblAccountLevel.setYearlyAmtLimitDr(accountLevelRequest.getYearlyAmtLimitDr());
        tblAccountLevel.setDailyTransLimitCr(accountLevelRequest.getDailyTransLimitCr());
        tblAccountLevel.setMonthlyTransLimitCr(accountLevelRequest.getMonthlyTransLimitCr());
        tblAccountLevel.setYearlyTransLimitCr(accountLevelRequest.getYearlyTransLimitCr());
        tblAccountLevel.setDailyTransLimitDr(accountLevelRequest.getDailyTransLimitDr());
        tblAccountLevel.setMonthlyTransLimitDr(accountLevelRequest.getMonthlyTransLimitDr());
        tblAccountLevel.setYearlyTransLimitDr(accountLevelRequest.getYearlyTransLimitDr());
        tblAccountLevel.setMaxAmtLimit(accountLevelRequest.getMaxAmtLimit());
        tblAccountLevel.setMaxAmtPerTxn(accountLevelRequest.getMaxAmtPerTxn());
        tblAccountLevel.setGlAccountId(accountLevelRequest.getGlAccountId());
        tblAccountLevel.setIsActive(accountLevelRequest.getIsActive());
        tblAccountLevel.setStatusId(new BigDecimal(2));
        tblAccountLevel.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblAccountLevel.setLastupdatedate(new Date());
        tblAccountLevel.setUpdateindex(tblAccountLevel.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblAccountLevel.getUpdateindex().intValue() + 1));
        tblAccountLevel = tblAccountLevelRepo.save(tblAccountLevel);
        if (tblAccountLevel != null) {
            setResponse(response, Constants.ONE, tblAccountLevel, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAccountLevel, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response createTransactionWiseLimit(TblTransLimitRequest tblTransLimitRequest, HttpServletRequest request) {
        Response response = new Response();
        TblTransLimit tblTransLimit = new TblTransLimit();
        tblTransLimit.setLimitProfileName(tblTransLimitRequest.getLimitProfileName());
        tblTransLimit.setDailyAmtLimitDr(tblTransLimitRequest.getDailyAmtLimitDr());
        tblTransLimit.setMonthlyAmtLimitDr(tblTransLimitRequest.getMonthlyAmtLimitDr());
        tblTransLimit.setYearlyAmtLimitDr(tblTransLimitRequest.getYearlyAmtLimitDr());
        tblTransLimit.setDailyAmtLimitCr(tblTransLimitRequest.getDailyAmtLimitCr());
        tblTransLimit.setMonthlyAmtLimitCr(tblTransLimitRequest.getMonthlyAmtLimitCr());
        tblTransLimit.setYearlyAmtLimitCr(tblTransLimitRequest.getYearlyAmtLimitCr());
        tblTransLimit.setDailyTransLimitDr(tblTransLimitRequest.getDailyTransLimitDr());
        tblTransLimit.setMonthlyTransLimitDr(tblTransLimitRequest.getMonthlyTransLimitDr());
        tblTransLimit.setYearlyTransLimitDr(tblTransLimitRequest.getYearlyTransLimitDr());
        tblTransLimit.setDailyTransLimitCr(tblTransLimitRequest.getDailyTransLimitCr());
        tblTransLimit.setMonthlyTransLimitCr(tblTransLimitRequest.getMonthlyTransLimitCr());
        tblTransLimit.setYearlyTransLimitCr(tblTransLimitRequest.getYearlyTransLimitCr());
        tblTransLimit.setExcludeLimit(tblTransLimitRequest.getExcludeLimit());
        tblTransLimit.setIsActive(tblTransLimitRequest.getIsActive());
        tblTransLimit.setStatusId(new BigDecimal(2));
        tblTransLimit.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblTransLimit.setCreatedate(new Date());
        tblTransLimit = tblTransLimitRepo.save(tblTransLimit);
        if (!tblTransLimitRequest.getTblTransLimitDetail().isEmpty()) {
            for (TblTransLimitDetailRequest tblTransLimitDetailRequest : tblTransLimitRequest.getTblTransLimitDetail()) {
                TblTransLimitDetail tblTransLimitDetails = new TblTransLimitDetail();
                tblTransLimitDetails.getTblTransLimit().setTransLimitId(tblTransLimit.getTransLimitId());
                tblTransLimitDetails.setTransDocsId(tblTransLimitDetailRequest.getTransDocsId());
                tblTransLimitDetails.setIsActive("Y");
                tblTransLimitDetails.setCreateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
                tblTransLimitDetailRepo.save(tblTransLimitDetails);
            }
        }
        tblTransLimit = tblTransLimitRepo.save(tblTransLimit);
        if (tblTransLimit != null) {
            setResponse(response, Constants.ONE, tblTransLimit, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblTransLimit, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response updateTransactionWiseLimit(TblTransLimitRequest tblTransLimitRequest, HttpServletRequest request) {
        Response response = new Response();
        TblTransLimit tblTransLimit = tblTransLimitRepo.findById(tblTransLimitRequest.getTransLimitId()).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        tblTransLimit.setLimitProfileName(tblTransLimitRequest.getLimitProfileName());
        tblTransLimit.setDailyAmtLimitDr(tblTransLimitRequest.getDailyAmtLimitDr());
        tblTransLimit.setMonthlyAmtLimitDr(tblTransLimitRequest.getMonthlyAmtLimitDr());
        tblTransLimit.setYearlyAmtLimitDr(tblTransLimitRequest.getYearlyAmtLimitDr());
        tblTransLimit.setDailyAmtLimitCr(tblTransLimitRequest.getDailyAmtLimitCr());
        tblTransLimit.setMonthlyAmtLimitCr(tblTransLimitRequest.getMonthlyAmtLimitCr());
        tblTransLimit.setYearlyAmtLimitCr(tblTransLimitRequest.getYearlyAmtLimitCr());
        tblTransLimit.setDailyTransLimitDr(tblTransLimitRequest.getDailyTransLimitDr());
        tblTransLimit.setMonthlyTransLimitDr(tblTransLimitRequest.getMonthlyTransLimitDr());
        tblTransLimit.setYearlyTransLimitDr(tblTransLimitRequest.getYearlyTransLimitDr());
        tblTransLimit.setDailyTransLimitCr(tblTransLimitRequest.getDailyTransLimitCr());
        tblTransLimit.setMonthlyTransLimitCr(tblTransLimitRequest.getMonthlyTransLimitCr());
        tblTransLimit.setYearlyTransLimitCr(tblTransLimitRequest.getYearlyTransLimitCr());
        tblTransLimit.setExcludeLimit(tblTransLimitRequest.getExcludeLimit());
        tblTransLimit.setIsActive(tblTransLimitRequest.getIsActive());
        tblTransLimit.setStatusId(new BigDecimal(2));
        tblTransLimit.setLastupdateuser(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
        tblTransLimit.setLastupdatedate(new Date());
        tblTransLimit.setUpdateindex(tblTransLimit.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblTransLimit.getUpdateindex().intValue() + 1));
        if (!tblTransLimitRequest.getTblTransLimitDetail().isEmpty()) {
            updateTransLimitDetail(tblTransLimitRequest.getTblTransLimitDetail(), new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)), tblTransLimit);
        }
        if (tblTransLimit != null) {
            setResponse(response, Constants.ONE, tblTransLimit, GenericResponseCode.RECORD_UPDATED.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblTransLimit, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    public void updateTransLimitDetail(List<TblTransLimitDetailRequest> tblTransLimitDetailRequestList, BigDecimal userId, TblTransLimit tblTransLimit) {
        List<Long> oldProductIds = tblTransLimitDetailRepo.findByTblTransLimitTransLimitId(tblTransLimit.getTransLimitId()).stream().map(detail -> detail.getTransDocsId().longValue()).collect(Collectors.toList());

        List<Long> filteredNewList = tblTransLimitDetailRequestList.stream().map(detail -> detail.getTransDocsId().longValue()).collect(Collectors.toList());

        List<Long> newProductIds = filteredNewList.stream().filter(key -> !oldProductIds.contains(key)).collect(Collectors.toList());

        List<Long> removedProductIds = oldProductIds.stream().filter(key -> !filteredNewList.contains(key)).collect(Collectors.toList());

        transLimitProductAction(userId, tblTransLimit, oldProductIds, filteredNewList, newProductIds, removedProductIds);
    }

    public void transLimitProductAction(BigDecimal userId, TblTransLimit tblTransLimit, List<Long> oldProductIds, List<Long> filteredNewList, List<Long> newProductIds, List<Long> removedProductIds) {
        for (Long productId : removedProductIds) {
            TblTransLimitDetail tblTransLimitDetail = tblTransLimitDetailRepo.findByTblTransLimitTransLimitIdAndTransDocsId(tblTransLimit.getTransLimitId(), new BigDecimal(productId));
            if (tblTransLimitDetail != null && !tblTransLimitDetail.getIsActive().equals(Constants.NO)) {
                inActiveExistingTransLimit(userId, tblTransLimit, tblTransLimitDetail, Constants.NO);
            }
        }

        Set<Long> oldProductIdsSet = new HashSet<>(oldProductIds);
        for (Long productId : filteredNewList) {
            if (oldProductIdsSet.contains(productId)) {
                TblTransLimitDetail tblTransLimitDetail = tblTransLimitDetailRepo.findByTblTransLimitTransLimitIdAndTransDocsId(tblTransLimit.getTransLimitId(), new BigDecimal(productId));
                if (tblTransLimitDetail != null && tblTransLimitDetail.getIsActive().equals(Constants.NO)) {
                    inActiveExistingTransLimit(userId, tblTransLimit, tblTransLimitDetail, Constants.YES);
                }
            }
        }

        for (Long productId : newProductIds) {
            TblTransLimitDetail tblTransLimitDetail = new TblTransLimitDetail();
            tblTransLimitDetail.setIsActive("Y");
            tblTransLimitDetail.setTransDocsId(new BigDecimal(productId));
            tblTransLimitDetail.getTblTransLimit().setTransLimitId(tblTransLimit.getTransLimitId());
            tblTransLimitDetail.setCreateuser(userId);
            tblTransLimitDetailRepo.save(tblTransLimitDetail);
        }
    }

    public void inActiveExistingTransLimit(BigDecimal userId, TblTransLimit tblTransLimit, TblTransLimitDetail tblTransLimitDetail, String setIsActiveNo) {
        tblTransLimitDetail.setIsActive(setIsActiveNo);
        tblTransLimitDetail.getTblTransLimit().setTransLimitId(tblTransLimit.getTransLimitId());
        tblTransLimitDetail.setLastupdateuser(userId);
        tblTransLimitDetail.setLastupdatedate(new Date());
        tblTransLimitDetail.setUpdateindex(tblTransLimitDetail.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblTransLimitDetail.getUpdateindex().intValue() + 1));
        tblTransLimitDetailRepo.save(tblTransLimitDetail);
    }

    @Override
    public List<TblUser> getAllUsers(UserSearch userSearch) {
        String dateFromInput = null;
        String dateToInput = null;
        if (userSearch.getFromDate() != null && !(userSearch.getFromDate().equals(""))) {
            dateFromInput = userSearch.getFromDate() + " 00:00:00";
        }
        if (userSearch.getToDate() != null && !(userSearch.getToDate().equals(""))) {
            dateToInput = userSearch.getToDate() + " 23:59:59";
        }
        return tblUsersRepo.getAllUsers(userSearch, dateFromInput, dateToInput);
    }

    @Override
    public List<TblMenu> getAllMenu() {
        return tblMenuRepo.findAll();
    }

    @Override
    public List<TblRole> getAllRoles(SearchRole searchRole) {
        String dateFromInput = null;
        String dateToInput = null;
        if (searchRole.getFromDate() != null && !(searchRole.getFromDate().equals(""))) {
            dateFromInput = searchRole.getFromDate() + " 00:00:00";
        }
        if (searchRole.getToDate() != null && !(searchRole.getToDate().equals(""))) {
            dateToInput = searchRole.getToDate() + " 23:59:59";
        }
        return tblRoleRepo.getAllRoles(searchRole, dateFromInput, dateToInput);
    }

    @Override
    public List<TblRoleRight> getAllRoleRights() {
        return tblRoleRightRepo.findAll();
    }

    @Override
    public TblUser getUserByUserid(String userId) {
        return tblUsersRepo.getUserById(Long.parseLong(userId));
    }

    @Override
    public TblAppUser getAppUserByUserId(long userId) {
        return tblAppUserRepo.findByUserId(new BigDecimal(userId));
    }

    @Override
    public List<TblAccountLevel> getAllAccountLevel(SearchAccountLevel searchAccountLevel) {
        String dateFromInput = null;
        String dateToInput = null;
        if (searchAccountLevel.getFromDate() != null && !(searchAccountLevel.getFromDate().equals(""))) {
            dateFromInput = searchAccountLevel.getFromDate() + " 00:00:00";
        }
        if (searchAccountLevel.getToDate() != null && !(searchAccountLevel.getToDate().equals(""))) {
            dateToInput = searchAccountLevel.getToDate() + " 23:59:59";
        }
        return tblAccountLevelRepo.getAllAccountLevel(searchAccountLevel, dateFromInput, dateToInput);
    }

    @Override
    public TblRoleRight getRoleRightById(String roleRightId) {
        return tblRoleRightRepo.findById(Long.parseLong(roleRightId)).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }

    @Override
    public TblRole getRoleById(String roleId) {
        return tblRoleRepo.findById(Long.parseLong(roleId)).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }

    @Override
    public TblAccountLevel getAccountLevelById(String accountLevelId) {
        return tblAccountLevelRepo.findById(Long.parseLong(accountLevelId)).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }

    @Override
    public Response twoFactorAuth(TwoFactorAuthRequest twoFactorAuthRequest, HttpServletRequest httpServletRequest) {
        Response response = new Response();
        TblAppUser tblAppUser = tblAppUserRepo.findById(Long.parseLong((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID))).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        TblUser tblUser = tblUsersRepo.findById(tblAppUser.getUserId().longValue()).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        if (twoFactorAuthRequest.getTwoFaEnabled().equalsIgnoreCase("Y")) {
            isGoogleAuthValid(tblAppUser.getUsername(), tblUser.getEmail());
        }
        tblUser.setTwoFaEnabled(twoFactorAuthRequest.getTwoFaEnabled());
        tblUser.setTwoFaType(twoFactorAuthRequest.getTwoFaType());
        if (twoFactorAuthRequest.getTwoFaType().equals("S")) {
            tblUser.setMobileNo(twoFactorAuthRequest.getMobileNo());
        } else {
            tblUser.setEmail(twoFactorAuthRequest.getEmail());
        }
        tblUser.setLastupdateuser(new BigDecimal((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)));
        tblUser.setLastupdatedate(new Date());
        tblUser.setUpdateindex(tblUser.getUpdateindex() == null ? new BigDecimal(1) : new BigDecimal(tblUser.getUpdateindex().intValue() + 1));
        TblUser userResponse = tblUsersRepo.save(tblUser);
        if (userResponse.getTwoFaEnabled() != null) {
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return response;
    }

    @Override
    public Response auditLogs(AuditLogRequest auditLogRequest, HttpServletRequest httpServletRequest) {
        Response response = new Response();
        List<Object> objectList = tblAppUserRepo.auditLogRequest(auditLogRequest.getMobileNo(), auditLogRequest.getUserName(), auditLogRequest.getFromDate(), auditLogRequest.getToDate());
        List<AuditLogResponse> auditLogs = objectList.stream().map(obj -> {
            Object[] row = (Object[]) obj; // Cast Object to Object[]
            AuditLogResponse dto = new AuditLogResponse();
            dto.setActivityDate((Date) row[0]);
            dto.setMobileNumber((String) row[1]);
            dto.setUserName((String) row[2]);
            dto.setActivityDetail((String) row[3]);
            dto.setDeviceModel((String) row[4]);
            dto.setIpAddress((String) row[5]);
            return dto;
        }).collect(Collectors.toList());

        if (auditLogs.size() > 0) {
            setResponse(response, Constants.ONE, auditLogs, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    public Response forgetPassword(ForgetPasswordRequest forgetPasswordRequest) throws JsonProcessingException {
        Response response = new Response();
        GenerateOtpResponse generateOtpResponse = new GenerateOtpResponse();
        TblUser tblUser = tblUsersRepo.findByEmail(forgetPasswordRequest.getEmail());
        if (tblUser != null) {
            TblAppUser tblAppUser = tblAppUserRepo.findByUserId(new BigDecimal(tblUser.getUserId()));
            GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
            generateOtpRequest.setMobileNumber(tblUser.getMobileNo());
            generateOtpRequest.setOtpType("EOV");
            generateOtpRequest.setOtpIdentifier("S");
            generateOtpRequest.setOtpTemplateCode("FGP");
            response = otpService.generateOtp(generateOtpRequest);
            ObjectMapper objectMapper = new ObjectMapper();
            generateOtpResponse = objectMapper.readValue(Objects.requireNonNull(convertObjecttoJson(response.getPayload())), GenerateOtpResponse.class);
            generateOtpResponse.setOtpType("EOV");
            generateOtpResponse.setEmail(tblUser.getEmail());
            GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
            generateNotificationRequest.setEmail(tblUser.getEmail());
            generateNotificationRequest.setSubject("DFS Email Verification");
            generateNotificationRequest.setSms("Your OTP for Forgot Password is " + generateOtpResponse.getOtpCode());
            generateNotificationRequest.setType("E");
            notificationService.notify(generateNotificationRequest, new BigDecimal(tblAppUser.getAppUserId()));
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    public Response getTwoFactorAuth(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        TblAppUser tblAppUser = tblAppUserRepo.findById(Long.parseLong((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID))).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        TblUser tblUser = tblUsersRepo.findById(tblAppUser.getUserId().longValue()).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
        TwoFactorAuthResponse twoFactorAuthResponse = new TwoFactorAuthResponse();
        if (tblUser.getTwoFaEnabled() != null) {
            twoFactorAuthResponse.setEmail(tblUser.getEmail());
            twoFactorAuthResponse.setMobileNo(tblUser.getMobileNo());
            twoFactorAuthResponse.setTwoFaEnabled(tblUser.getTwoFaEnabled());
            twoFactorAuthResponse.setTwoFaType(tblUser.getTwoFaType());
            setResponse(response, Constants.ONE, twoFactorAuthResponse, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return response;
    }

    @Override
    public TblAppUser getAppUserById(long appUserId) {
        return tblAppUserRepo.findById(appUserId).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
    }


    public boolean isGoogleAuthValid(String username, String email) {
        String url = googleauthurl + "/code/send/" + username + "/" + email;
        ResponseEntity<GoogleAuthResponse> response = restTemplate.getForEntity(url, GoogleAuthResponse.class, username, email);

        GoogleAuthResponse authResponse = response.getBody();
        return authResponse != null && authResponse.isValid();
    }
}
