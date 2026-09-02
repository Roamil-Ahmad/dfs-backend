package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.LovResponse;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.repo.*;
import com.dfs.backoffice.service.LookupService;
import com.dfs.backoffice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LookupServiceImpl extends HelperClass implements LookupService {
    @Autowired
    private TblMenuRepo tblMenuRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblGlAccountRepo tblGlAccountRepo;
    @Autowired
    private TblRoleRepo tblRoleRepo;
    @Autowired
    private TblUserRepo tblUserRepo;
    @Autowired
    private LkpAccountStatusRepo lkpAccountStatusRepo;
    @Autowired
    private TblTransDocRepo tblTransDocRepo;
    @Autowired
    private TblTaxRegimeRepo tblTaxRegimeRepo;
    @Autowired
    private LkpAccountTypeRepo lkpAccountTypeRepo;
    @Autowired
    private TblAgentRepo tblAgentRepo;
    @Autowired
    private LkpDistrictRepo lkpDistrictRepo;
    @Autowired
    private LkpCityRepo lkpCityRepo;
    @Autowired
    private TblAgentClassRepo tblAgentClassRepo;
    @Autowired
    private LkpProvinceRepo lkpProvinceRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;

    @Override
    public List<LovResponse> lovParentMenu() {
        List<TblMenu> tblMenus = tblMenuRepo.getAllParentMenus();
        List<LovResponse> lovResponses = new ArrayList<>();
        LovResponse lovResponse = null;
        for (TblMenu tblMenu : tblMenus) {
            lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblMenu.getMenuId()));
            lovResponse.setDescr(tblMenu.getMenuDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<TblAppUser> getAppUsers() {
        return tblAppUserRepo.getAppUsers();
    }

    @Override
    public List<LovResponse> lovGlChildtAccount() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblGlAccount> tblGlAccounts = tblGlAccountRepo.lovGlChildtAccount();
        for (TblGlAccount tblGlAccount : tblGlAccounts) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblGlAccount.getGlAccountId()));
            lovResponse.setDescr(tblGlAccount.getGlAccountDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovRoles() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblRole> tblRoles = tblRoleRepo.findByIsActive(Constants.YES);
        for (TblRole tblRole : tblRoles) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblRole.getRoleId()));
            lovResponse.setDescr(tblRole.getRoleDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> getMenus() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblMenu> tblMenus = tblMenuRepo.findByIsActive(Constants.YES);
        for (TblMenu tblMenu : tblMenus) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblMenu.getMenuId()));
            lovResponse.setDescr(tblMenu.getMenuDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> getUsers() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblUser> tblUsers = tblUserRepo.findByIsActive(Constants.YES);
        for (TblUser tblUser : tblUsers) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblUser.getUserId()));
            lovResponse.setDescr(tblUser.getEmployeeName());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> getAccountStatus() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<LkpAccountStatus> lkpAccountStatuses = lkpAccountStatusRepo.findByIsActive(Constants.YES);
        for (LkpAccountStatus lkpAccountStatus : lkpAccountStatuses) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(lkpAccountStatus.getAccountStatusId()));
            lovResponse.setDescr(lkpAccountStatus.getAccountStatusDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> getTransDocs() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblTransDoc> tblTransDocs = tblTransDocRepo.findByTransTypeIdAndIsActive(new BigDecimal(1),Constants.YES);
        for (TblTransDoc transDoc : tblTransDocs) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(transDoc.getTransDocsId()));
            lovResponse.setDescr(transDoc.getTransDocsDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovGlChildAccountLiability() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblGlAccount> tblGlAccounts = tblGlAccountRepo.lovGlAccountLiability();
        for (TblGlAccount tblGlAccount : tblGlAccounts) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblGlAccount.getGlAccountId()));
            lovResponse.setDescr(tblGlAccount.getGlAccountDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovTaxRegime() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<TblTaxRegime> tblTaxRegimes = tblTaxRegimeRepo.findByIsActive(Constants.YES);
        for (TblTaxRegime tblTaxRegime : tblTaxRegimes) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblTaxRegime.getTaxRegimeId()));
            lovResponse.setDescr(tblTaxRegime.getTaxRegimeDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovGlAccountType() {
        List<LovResponse> lovResponses = new ArrayList<>();
        List<LkpAccountType> lkpAccountTypes = lkpAccountTypeRepo.findByAccountType("G");
        for (LkpAccountType lkpAccountType : lkpAccountTypes) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(lkpAccountType.getAccountTypeId()));
            lovResponse.setDescr(lkpAccountType.getAccountTypeDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovParentAgent() {
        List<TblAgent> parentAgents = tblAgentRepo.getParentAgents();
        List<LovResponse> lovResponses = new ArrayList<>();
        LovResponse lovResponse = null;
        for (TblAgent tblAgent : parentAgents) {
            lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblAgent.getAgentId()));
            lovResponse.setDescr(decrypttWithAes(tblAgent.getName()));
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public int getAgentLevelById(String id) {
        return tblAgentRepo.getAgentLevel(id);
    }

    @Override
    public List<LovResponse> lovDistrict(Long provinceId) {
        List<LkpDistrict> lkpDistricts = lkpDistrictRepo.lovDistrictWithProvince(provinceId);
        List<LovResponse> lovResponses = new ArrayList<>();
        LovResponse lovResponse = null;
        for (LkpDistrict lkpDistrict : lkpDistricts) {
            lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(lkpDistrict.getDistrictId()));
            lovResponse.setDescr(lkpDistrict.getDistrictDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovCityWithDistrict(Long districtId) {
        List<LkpCity> lkpCities = lkpCityRepo.lovCityWithDistrict(districtId);
        List<LovResponse> lovResponses = new ArrayList<>();
        LovResponse lovResponse = null;
        for (LkpCity lkpCity : lkpCities) {
            lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(lkpCity.getCityId()));
            lovResponse.setDescr(lkpCity.getCityDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovAgentClass() {
        List<TblAgentClass> agentClasses = tblAgentClassRepo.findByIsActive(Constants.YES);
        List<LovResponse> lovResponses = new ArrayList<>();
        LovResponse lovResponse = null;
        for (TblAgentClass agentClass : agentClasses) {
            lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(agentClass.getAgentClassId()));
            lovResponse.setDescr(agentClass.getAgentClassDescr());

            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovProvince() {
        List<LkpProvince> lkprsults = lkpProvinceRepo.findByIsActive(Constants.YES);
        List<LovResponse> lovResponses = new ArrayList<>();
        LovResponse lovResponse = null;
        for (LkpProvince result : lkprsults) {
            lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(result.getProvinceId()));
            lovResponse.setDescr(result.getProvinceDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovGlParentAccount() {
        List<LovResponse> lovResponses = new ArrayList<LovResponse>();
        List<TblGlAccount> tblGlAccounts = tblGlAccountRepo.getAllParentGeneralLedgers();
        for (TblGlAccount tblGlAccount : tblGlAccounts) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(tblGlAccount.getGlAccountId()));
            lovResponse.setDescr(tblGlAccount.getGlAccountDescr());
            lovResponses.add(lovResponse);

        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovAllDistrict() {
        List<LkpDistrict> lkpDistricts = lkpDistrictRepo.findAll();
        List<LovResponse> lovResponses = new ArrayList<>();
        for (LkpDistrict lkpDistrict : lkpDistricts) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(lkpDistrict.getDistrictId()));
            lovResponse.setDescr(lkpDistrict.getDistrictDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }

    @Override
    public List<LovResponse> lovAccountLevel() {
        List<TblAccountLevel> res = tblAccountLevelRepo.findAll();
        List<LovResponse> lovResponses = new ArrayList<>();
        for (TblAccountLevel r : res) {
            LovResponse lovResponse = new LovResponse();
            lovResponse.setCode(String.valueOf(r.getAccountLevelId()));
            lovResponse.setDescr(r.getAccountLevelDescr());
            lovResponses.add(lovResponse);
        }
        return lovResponses;
    }
}
