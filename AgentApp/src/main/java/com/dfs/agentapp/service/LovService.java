package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.common.LovResponse;

import java.util.HashMap;
import java.util.List;

public interface LovService {
    HashMap<String, Object> getAllProvince();

    HashMap<String, Object> getAccountUpgradeLovs();

    List<LovResponse> getProviceByName(String province);

    HashMap<String, Object> getAllDistrictByProviceId(Long id);

    HashMap<String, Object> getAllBusinessTypes();

    HashMap<String, Object> getAllSegments();

    /**
     * Every lookup list the app needs, in one call, so a screen does not have to make a dozen
     * round trips to populate its pickers.
     */
    HashMap<String, Object> getAllLovs();

    HashMap<String, Object> getIssueTypeLov();

    HashMap<String, Object> getCategories();

    HashMap<String, Object> getDeviceRegistrationLovs();

    /**
     * The same lists plus the two per-customer verification challenges, built from the CNIC record
     * the nadra service stored. Not cached: the options are specific to one CNIC.
     */
    HashMap<String, Object> getDeviceRegistrationLovs(String nidNo);
}
