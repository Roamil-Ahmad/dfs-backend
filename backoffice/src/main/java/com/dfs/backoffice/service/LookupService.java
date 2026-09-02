package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.LovResponse;
import com.dfs.backoffice.model.TblAppUser;

import java.util.List;

public interface LookupService {
    List<LovResponse> lovParentMenu();

    List<TblAppUser> getAppUsers();

    List<LovResponse> lovGlChildtAccount();

    List<LovResponse> lovRoles();

    List<LovResponse> getMenus();

    List<LovResponse> getUsers();

    List<LovResponse> getAccountStatus();

    List<LovResponse> getTransDocs();

    List<LovResponse> lovGlChildAccountLiability();

    List<LovResponse> lovTaxRegime();

    List<LovResponse> lovGlAccountType();

    List<LovResponse> lovParentAgent();

    int getAgentLevelById(String id);

    List<LovResponse> lovDistrict(Long provinceId);

    List<LovResponse> lovCityWithDistrict(Long districtId);

    List<LovResponse> lovAgentClass();

    List<LovResponse> lovProvince();

    List<LovResponse> lovGlParentAccount();

    List<LovResponse> lovAllDistrict();

    List<LovResponse> lovAccountLevel();
}
