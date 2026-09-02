package com.dfs.backoffice.controller.user;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.service.UserService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserGetApi extends HelperClass {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllMenu")
    public ResponseEntity<Response> getAllMenu(HttpServletRequest request) {
        Response response = new Response();
        List<TblMenu> tblMenus = userService.getAllMenu();
        if (!tblMenus.isEmpty()) {
            setResponse(response, Constants.ONE, tblMenus, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblMenus, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getAllRoleRights")
    public ResponseEntity<Response> getAllRoleRights(HttpServletRequest request) {
        Response response = new Response();
        List<TblRoleRight> tblRoleRights = userService.getAllRoleRights();
        if (!tblRoleRights.isEmpty()) {
            setResponse(response, Constants.ONE, tblRoleRights, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRoleRights, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/getuserbyuserid/{userId}")
    public ResponseEntity<Response> getuserbyuserid(@PathVariable String userId, HttpServletRequest request) {
        Response response = new Response();
        TblUser tblUsers = userService.getUserByUserid(userId);
        if (tblUsers !=null) {
            TblAppUser tblAppUser = userService.getAppUserByUserId(tblUsers.getUserId());
            tblUsers.setUsername(tblAppUser.getUsername());
            setResponse(response, Constants.ONE, tblUsers, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblUsers, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getRoleById/{roleId}")
    public ResponseEntity<Response> getRoleById(@PathVariable String roleId, HttpServletRequest request) {
        Response response = new Response();
        TblRole tblRole = userService.getRoleById(roleId);
        if (tblRole !=null) {
            setResponse(response, Constants.ONE, tblRole, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRole, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getRoleRightById/{roleRightId}")
    public ResponseEntity<Response> getRoleRightById(@PathVariable String roleRightId, HttpServletRequest request) {
        Response response = new Response();
        TblRoleRight tblRoleRight = userService.getRoleRightById(roleRightId);
        if (tblRoleRight !=null) {
            setResponse(response, Constants.ONE, tblRoleRight, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblRoleRight, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getAccountLevelById/{accountLevelId}")
    public ResponseEntity<Response> getAccountLevelById(@PathVariable String accountLevelId, HttpServletRequest request) {
        Response response = new Response();
        TblAccountLevel tblAccountLevel = userService.getAccountLevelById(accountLevelId);
        if (tblAccountLevel !=null) {
            setResponse(response, Constants.ONE, tblAccountLevel, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAccountLevel, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping(value = "/getTwoFactorAuth")
    public ResponseEntity<Response> getTwoFactorAuth(HttpServletRequest httpServletRequest) {
        Response response = userService.getTwoFactorAuth(httpServletRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }
}
