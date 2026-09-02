package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    LoginResponse userLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    TblAppUserLoginHistory userLogout(HttpServletRequest request);

    Response processCreateUser(CreateUserRequest createUserRequest, HttpServletRequest httpServletRequest);

    TblUser getUserByCnic(BigDecimal cnic);

    TblAppUser getAppUserByUserName(String userName);

    Response processUpdateUser(CreateUserRequest createUserRequest, HttpServletRequest httpServletRequest);

    TblUser getUserById(long userId);

    Response processInactiveUser(CreateUserRequest createUserRequest, HttpServletRequest request);

    Response checkerActionUser(HttpServletRequest request, Response response, EntityActionRequest entityActionRequest, McActionResponse mcResponse);

    Response processChangePassword(ChangePasswordRequest data, HttpServletRequest request);

    Response processChangeUserPassword(ChangeUserPasswordRequest changeUserPasswordRequest, HttpServletRequest request);

    Response saveMenu(CreateMenuRequest createMenuRequest, HttpServletRequest request);

    Response updateMenu(CreateMenuRequest createMenuRequest, HttpServletRequest request);

    Response processInactiveMenu(CreateMenuRequest createMenuRequest, HttpServletRequest request);

    Response saveRole(CreateRoleRequest createRoleRequest, HttpServletRequest request);

    Response updateRole(CreateRoleRequest createRoleRequest, HttpServletRequest request);

    Response inactiveRole(CreateRoleRequest createRoleRequest, HttpServletRequest request);

    Response saveRoleRights(CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request);

    Response updateRoleRights(CreateRoleRightsRequest createRoleRequest, HttpServletRequest request);

    Response inactiveRoleRights(CreateRoleRightsRequest createRoleRightsRequest, HttpServletRequest request);

    Response saveAccountLevel(AccountLevelRequest accountLevelRequest, HttpServletRequest request);

    Response updateAccountLevel(AccountLevelRequest accountLevelRequest, HttpServletRequest request);

    Response createTransactionWiseLimit(TblTransLimitRequest tblTransLimitRequest, HttpServletRequest request);

    Response updateTransactionWiseLimit(TblTransLimitRequest tblTransLimitRequest, HttpServletRequest request);

    List<TblUser> getAllUsers(UserSearch userSearch);

    List<TblMenu> getAllMenu();

    List<TblRole> getAllRoles(SearchRole searchRole);

    List<TblRoleRight> getAllRoleRights();

    TblUser getUserByUserid(String userId);

    TblAppUser getAppUserByUserId(long userId);

    List<TblAccountLevel> getAllAccountLevel(SearchAccountLevel searchAccountLevel);

    TblRoleRight getRoleRightById(String roleRightId);

    TblRole getRoleById(String roleId);

    TblAccountLevel getAccountLevelById(String accountLevelId);

    Response twoFactorAuth(TwoFactorAuthRequest twoFactorAuthRequest, HttpServletRequest httpServletRequest);

    Response auditLogs(AuditLogRequest auditLogRequest, HttpServletRequest httpServletRequest);

    Response forgetPassword(ForgetPasswordRequest forgetPasswordRequest) throws JsonProcessingException;

    Response getTwoFactorAuth(HttpServletRequest httpServletRequest);

    TblAppUser getAppUserById(long longValue);
}
