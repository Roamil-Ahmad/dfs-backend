package com.dfs.backoffice.controller.user;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.service.UserService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import com.dfs.backoffice.utils.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserPostApi extends HelperClass {

    @Autowired
    private UserService userService;
    private Gson gson = new Gson();

    @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        RequestValidator.userLoginValidator(loginRequest);
        LoginResponse loginResponse = userService.userLogin(loginRequest, httpServletRequest);
        if (loginResponse != null) {
            if (loginResponse.getErrorResponse() != null) {
                return getResponseFormat(HttpStatus.BAD_REQUEST, loginResponse.getErrorResponse(), null, true);
            } else {
                return getResponseFormat(HttpStatus.OK, Constants.LOGIN_SUCCESSFULL, loginResponse, true);
            }
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, Constants.LOGIN_UN_SUCCESSFULL, null, true);
        }
    }

    @PostMapping(value = "/user/logout", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> userLogout(HttpServletRequest request) {
        TblAppUserLoginHistory tblUserLoginHistory = userService.userLogout(request);
        if (tblUserLoginHistory != null) {
            return getResponseFormat(HttpStatus.OK, Constants.LOGOUT_SUCCESSFULL, null, true);
        } else {
            return getResponseFormat(HttpStatus.BAD_REQUEST, Constants.LOGOUT_UN_SUCCESSFULL, null, true);
        }
    }

    @PostMapping(value = "/getAllUsers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllUsers(@RequestBody UserSearch userSearch, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.searchUserJsonValidate(userSearch);
        List<TblUser> tblUsers = userService.getAllUsers(userSearch);
        for (TblUser tblUser : tblUsers) {
            TblAppUser tblAppUser = userService.getAppUserByUserId(tblUser.getUserId());
            tblUser.setUsername(tblAppUser.getUsername());
        }
        if (!tblUsers.isEmpty()) {
            setResponse(response, Constants.ONE, tblUsers, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblUsers, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.createUserJsonValidate(createUserRequest);
        Response response = userService.processCreateUser(createUserRequest, httpServletRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateUser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.updateUserJsonValidate(createUserRequest);
        Response response = userService.processUpdateUser(createUserRequest, httpServletRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/inactiveUser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> inactiveUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
        Response response = userService.processInactiveUser(createUserRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/userCheckerAction", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> userCheckerAction(@RequestBody EntityActionRequest entityActionRequest, HttpServletRequest request) throws JsonProcessingException {
        Request jsonRequest = new Request();
        Response response = new Response();
        ObjectMapper objectMapper = new ObjectMapper();
        TblAppUser tblAppUser = userService.getAppUserById(new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)).longValue());
        String mcAction = mcAction(jsonRequest, request.getHeader(Constants.AUTHORIZATION), entityActionRequest.getMcActionRequest(), tblAppUser.getUserId());
        if (mcAction != null) {
            Response response2 = objectMapper.readValue(mcAction, Response.class);
            McActionResponse mcResponse = objectMapper.readValue(Objects.requireNonNull(convertObjecttoJson(response2.getPayload())), McActionResponse.class);
            if (mcResponse != null && entityActionRequest.getMcActionRequest().getAction().equals("2") && Constants.A.equals(mcResponse.getRequestStatus()) && mcResponse.getStatus() == 1) {
                response = userService.checkerActionUser(request, response, entityActionRequest, mcResponse);
            } else {
                setResponse(response, Constants.ONE, mcResponse, GenericResponseCode.SUCCESS.getResponseCode(), mcResponse != null ? mcResponse.getStatusDecsr() : GenericResponseCode.GENERAL_PROCESSING_ERROR.getResponseMessage());
            }
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_UPDATED.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                   HttpServletRequest request) {
        RequestValidator.changePasswordJsonValidate(changePasswordRequest);
        Response response = userService.processChangePassword(changePasswordRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/changeUserPassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> changeUserPassword(@RequestBody ChangeUserPasswordRequest changeUserPasswordRequest,
                                                       HttpServletRequest request) {
        RequestValidator.changeUserPasswordJsonValidate(changeUserPasswordRequest);
        Response response = userService.processChangeUserPassword(changeUserPasswordRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/createMenu", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createMenu(@RequestBody CreateMenuRequest createMenuRequest, HttpServletRequest request) {
        RequestValidator.createMenuJsonValidate(createMenuRequest);
        Response response = userService.saveMenu(createMenuRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateMenu", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateMenu(@RequestBody CreateMenuRequest createMenuRequest, HttpServletRequest request) {
        RequestValidator.updateMenuJsonValidate(createMenuRequest);
        Response response = userService.updateMenu(createMenuRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/inactiveMenu", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> inactiveMenu(@RequestBody CreateMenuRequest createMenuRequest, HttpServletRequest request) {
        Response response = userService.processInactiveMenu(createMenuRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/createRole", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createRole(@RequestBody CreateRoleRequest createRoleRequest, HttpServletRequest request) {
        RequestValidator.createRoleJsonValidate(createRoleRequest);
        Response response = userService.saveRole(createRoleRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateRole", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateRole(@RequestBody CreateRoleRequest createRoleRequest, HttpServletRequest request) {
        RequestValidator.updateRoleJsonValidate(createRoleRequest);
        Response response = userService.updateRole(createRoleRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/inactiveRole", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> inactiveRole(@RequestBody CreateRoleRequest createRoleRequest, HttpServletRequest request) {
        RequestValidator.inactiveRoleJsonValidate(createRoleRequest);
        Response response = userService.inactiveRole(createRoleRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getAllRoles", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllRoles(@RequestBody SearchRole searchRole, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.searchRoleJsonValidate(searchRole);
        List<TblRole> tblRoles = userService.getAllRoles(searchRole);
        if (!tblRoles.isEmpty()) {
            setResponse(response, Constants.ONE, tblRoles, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRoles, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/createRoleRights", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createRoleRights(@RequestBody CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request) {
        RequestValidator.createRoleRightsJsonValidate(createRoleRightsRequest);
        Response response = userService.saveRoleRights(createRoleRightsRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateRoleRights", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateRoleRights(@RequestBody CreateRoleRightsRequest createRoleRequest, HttpServletRequest request) {
        RequestValidator.updateRoleRightsJsonValidate(createRoleRequest);
        Response response = userService.updateRoleRights(createRoleRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/inactiveRoleRights", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> inactiveRoleRights(@RequestBody CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request) {
        RequestValidator.inactiveRoleRightsJsonValidate(createRoleRightsRequest);
        Response response = userService.inactiveRoleRights(createRoleRightsRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getAllAccountLevel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllAccountLevel(@RequestBody SearchAccountLevel searchAccountLevel, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.searchAccountLevelJsonValidate(searchAccountLevel);
        List<TblAccountLevel> tblAccountLevels = userService.getAllAccountLevel(searchAccountLevel);
        if (!tblAccountLevels.isEmpty()) {
            setResponse(response, Constants.ONE, tblAccountLevels, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAccountLevels, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/createAccountLevel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createAccountLevel(@RequestBody AccountLevelRequest accountLevelRequest, HttpServletRequest request) {
        RequestValidator.createAccountLevelJsonValidate(accountLevelRequest);
        Response response = userService.saveAccountLevel(accountLevelRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateAccountLevel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateAccountLevel(@RequestBody AccountLevelRequest accountLevelRequest, HttpServletRequest request) {
        RequestValidator.updateAccountLevelJsonValidate(accountLevelRequest);
        Response response = userService.updateAccountLevel(accountLevelRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/createTransactionWiseLimit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createTransactionWiseLimit(@RequestBody TblTransLimitRequest tblTransLimitRequest, HttpServletRequest request) {
        RequestValidator.createTransactionWiseLimitJsonValidate(tblTransLimitRequest);
        Response response = userService.createTransactionWiseLimit(tblTransLimitRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateTransactionWiseLimit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateTransactionWiseLimit(@RequestBody TblTransLimitRequest tblTransLimitRequest, HttpServletRequest request) {
        RequestValidator.updateTransactionWiseLimitJsonValidate(tblTransLimitRequest);
        Response response = userService.updateTransactionWiseLimit(tblTransLimitRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/twoFactorAuth", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> twoFactorAuth(@RequestBody TwoFactorAuthRequest twoFactorAuthRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.twoFactorAuthJsonValidate(twoFactorAuthRequest);
        Response response = userService.twoFactorAuth(twoFactorAuthRequest, httpServletRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/auditLogs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> auditLogs(@RequestBody AuditLogRequest auditLogRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.auditLogsJsonValidate(auditLogRequest);
        Response response = userService.auditLogs(auditLogRequest, httpServletRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/forgetPassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        RequestValidator.forgetPasswordJsonValidate(forgetPasswordRequest);
        Response response = userService.forgetPassword(forgetPasswordRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }


}
