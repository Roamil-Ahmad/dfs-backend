package com.dfs.backoffice.controller.lookups;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.LovResponse;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblAppUser;
import com.dfs.backoffice.service.LookupService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LookupsGetApi extends HelperClass {

    @Autowired
    private LookupService lookupService;

    @GetMapping("/lovParentMenu")
    public ResponseEntity<Response> lovParentMenu(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovParentMenu = lookupService.lovParentMenu();
        if (!lovParentMenu.isEmpty()) {
            setResponse(response, Constants.ONE, lovParentMenu, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovParentMenu, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping(value = "/getAppUsers")
    public ResponseEntity<Response> getAppUsers(HttpServletRequest request) {
        Response response = new Response();
        List<TblAppUser> tblAppUsers = lookupService.getAppUsers();
        if (!tblAppUsers.isEmpty()) {
            setResponse(response, Constants.ONE, tblAppUsers, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAppUsers, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/lovGlChildAccount")
    public ResponseEntity<Response> lovGlChildAccount(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> glChildtAccount = lookupService.lovGlChildtAccount();
        if (!glChildtAccount.isEmpty()) {
            setResponse(response, Constants.ONE, glChildtAccount, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, glChildtAccount, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/getRoles")
    public ResponseEntity<Response> getRoles(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> roles = lookupService.lovRoles();
        if (!roles.isEmpty()) {
            setResponse(response, Constants.ONE, roles, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roles, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/getMenus")
    public ResponseEntity<Response> getMenus(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> roles = lookupService.getMenus();
        if (!roles.isEmpty()) {
            setResponse(response, Constants.ONE, roles, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roles, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/getUsers")
    public ResponseEntity<Response> getUsers(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> roles = lookupService.getUsers();
        if (!roles.isEmpty()) {
            setResponse(response, Constants.ONE, roles, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roles, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/getAccountStatus")
    public ResponseEntity<Response> getAccountStatus(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> roles = lookupService.getAccountStatus();
        if (!roles.isEmpty()) {
            setResponse(response, Constants.ONE, roles, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roles, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/getTransDocs")
    public ResponseEntity<Response> getTransDocs(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> roles = lookupService.getTransDocs();
        if (!roles.isEmpty()) {
            setResponse(response, Constants.ONE, roles, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, roles, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/lovGlChildAccountLiability")
    public ResponseEntity<Response> lovGlChildAccountLiability(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> glChildtAccount = lookupService.lovGlChildAccountLiability();
        if (!glChildtAccount.isEmpty()) {
            setResponse(response, Constants.ONE, glChildtAccount, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, glChildtAccount, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/lovTaxRegime")
    public ResponseEntity<Response> lovTaxRegime(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovTaxRegime = lookupService.lovTaxRegime();
        if (!lovTaxRegime.isEmpty()) {
            setResponse(response, Constants.ONE, lovTaxRegime, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovTaxRegime, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/lovGlAccountType")
    public ResponseEntity<Response> lovGlAccountType(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovGlAccountType = lookupService.lovGlAccountType();
        if (!lovGlAccountType.isEmpty()) {
            setResponse(response, Constants.ONE, lovGlAccountType, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovGlAccountType, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/lovParentAgent")
    public ResponseEntity<Response> lovParentAgent(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovParentAgent = lookupService.lovParentAgent();
        if (!lovParentAgent.isEmpty()) {
            setResponse(response, Constants.ONE, lovParentAgent, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovParentAgent, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping(value = "/getAgentLevel/{id}")
    public ResponseEntity<Response> getAgentLevel(@PathVariable String id, HttpServletRequest request) {
        Response response = new Response();
        int getAgentLevel = lookupService.getAgentLevelById(id);
        setResponse(response, Constants.ONE, getAgentLevel, GenericResponseCode.RECORD_FOUND.getResponseCode());
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/lovDistrict/{proviceId}")
    public ResponseEntity<Response> lovDistrict(@PathVariable String proviceId,
                                                HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovDistrict(Long.valueOf(proviceId));
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/lovCityWithDistrict/{districtId}")
    public ResponseEntity<Response> lovCityWithDistrict(@PathVariable String districtId,
                                                        HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovCityWithDistrict(Long.valueOf(districtId));
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping(value = "/lovAgentClass")
    public ResponseEntity<Response> lovAgentClass(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovAgentClass();
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/lovProvince")
    public ResponseEntity<Response> lovProvince(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovProvince();
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/lovGlParentAccount")
    public ResponseEntity<Response> lovGlParentAccount(HttpServletRequest request) {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovGlParentAccount();
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/lovDistrict")
    public ResponseEntity<Response> lovAllDistrict() {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovAllDistrict();
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

    @GetMapping("/lovAccountLevel")
    public ResponseEntity<Response> lovAccountLevel() {
        Response response = new Response();
        List<LovResponse> lovResults = lookupService.lovAccountLevel();
        if (!lovResults.isEmpty()) {
            setResponse(response, Constants.ONE, lovResults, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lovResults, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }
}
