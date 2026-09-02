package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.LevelLimitResponse;
import com.dfs.app.dto.common.LovResponse;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LovService;
import com.dfs.app.model.TblNidData;
import com.dfs.app.repo.TblNidDataRepo;
import com.dfs.app.util.BirthPlacesEnum;
import com.dfs.app.util.Constants;
import com.dfs.app.util.MotherNamesEnum;
import com.dfs.app.util.CustomDataNotFoundException;
import com.dfs.app.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LovServiceImpl extends HelperClass implements LovService {
    @Autowired
    private LkpCityRepo lkpCityRepo;
    @Autowired
    private LkpExpectedMonthlyVolumeRepo lkpExpectedMonthlyVolumeRepo;
    @Autowired
    private LkpRecoveryQuestionRepo lkpRecoveryQuestionRepo;
    @Autowired
    private LkpBusinessTypeRepo lkpBusinessTypeRepo;

    @Autowired
    private LkpProvinceRepo lkpProvinceRepo;
    @Autowired
    private LKpAccountPurposeRepo lKpAccountPurposeRepo;
    @Autowired
    private LkpOccupationRepo lkpOccupationRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private CommonService commonService;
    @Value("${account.level.two}")
    private String accountLevelTwo;
    @Autowired
    private LkpDistrictRepo lkpDistrictRepo;
    @Autowired
    private LkpIssueTypeRepo lkpIssueTypeRepo;
    @Autowired
    private LkpTransDocsCategoryRepo lkpTransDocsCategoryRepo;
    @Autowired
    private TblNidDataRepo tblNidDataRepo;

    /**
     * This bean through its Spring proxy, so the internal call to the {@code @Cacheable} static-list
     * method still goes through the cache interceptor. {@code @Lazy} breaks the startup cycle.
     */
    @Autowired
    @Lazy
    private LovService self;

    @Override
    @Cacheable("provinces")
    public HashMap<String, Object> getAllProvince() {
        List<LovResponse> provinces;
        List<LkpProvince> lkpProvinces = lkpProvinceRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpProvinces)) {
            provinces = lkpProvinces.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getProvinceId());
                lovResponse.setCode(p.getProvinceCode());
                lovResponse.setName(p.getProvinceDescr());
                return lovResponse;
            }).collect(Collectors.toList());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), provinces);
        }
        return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    @Override
    @Cacheable("accountUpgradeLovs")
    public HashMap<String, Object> getAccountUpgradeLovs() {
        List<LovResponse> occupations = null;
        List<LovResponse> purposes = null;
        List<LovResponse> incomeSources = null;
        List<LovResponse> recoveryQuestions = null;
        List<LovResponse> businessType = null;
        LevelLimitResponse levelLimitResponse = null;
        HashMap<String, Object> response = new HashMap<>();
        List<LkpOccupation> lkpOccupations = lkpOccupationRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpOccupations)) {
            occupations = lkpOccupations.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getOccupationId());
                lovResponse.setCode(p.getOccupationCode());
                lovResponse.setName(p.getOccupationName());
                return lovResponse;
            }).collect(Collectors.toList());
        }
        List<LkpAccountPurpose> lkpAccountPurposes = lKpAccountPurposeRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpOccupations)) {
            purposes = lkpAccountPurposes.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getAccountPurposeId());
                lovResponse.setCode(p.getAccountPurposeCode());
                lovResponse.setName(p.getAccountPurposeDescr());
                return lovResponse;
            }).collect(Collectors.toList());
        }
        List<LkpExpectedMonthlyVolume> sourceOfIncomes = lkpExpectedMonthlyVolumeRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpOccupations)) {
            incomeSources = sourceOfIncomes.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getExpectedMonthlyVolumeId());
                lovResponse.setCode(p.getExpectedMonthlyVolumeCode());
                lovResponse.setName(p.getExpectedMonthlyVolumeDescr());
                return lovResponse;
            }).collect(Collectors.toList());
        }
        List<LkpRecoveryQuestion> lkpRecoveryQuestions = lkpRecoveryQuestionRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpRecoveryQuestions)) {
            recoveryQuestions = lkpRecoveryQuestions.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getRecoveryQuestionId());
                lovResponse.setCode(p.getRecoveryQuestionCode());
                lovResponse.setName(p.getRecoveryQuestionDescr());
                return lovResponse;
            }).collect(Collectors.toList());
        }
        List<LkpBusinessType> lkpBusinessTypes = lkpBusinessTypeRepo.findAllByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpBusinessTypes)) {
            businessType = lkpBusinessTypes.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getBusinessTypeId());
                lovResponse.setCode(p.getBusinessTypeCode());
                lovResponse.setName(p.getBusinessTypeDescr());
                return lovResponse;
            }).collect(Collectors.toList());
        }
        TblAccountLevel tblAccountLevel = tblAccountLevelRepo.findByAccountLevelCode(accountLevelTwo);
        if (tblAccountLevel != null) {
            levelLimitResponse = new LevelLimitResponse();
            levelLimitResponse.setAccountLevelCode(tblAccountLevel.getAccountLevelCode());
            levelLimitResponse.setAccountLevelName(tblAccountLevel.getAccountLevelDescr());
            levelLimitResponse.setAccountLevelLimit(tblAccountLevel.getMonthlyAmtLimitDr().toString());

        }
        response.put("levelLimit", levelLimitResponse);
        response.put("occupations", occupations);
        response.put("incomeSources", incomeSources);
        response.put("recoveryQuestions", recoveryQuestions);
        response.put("businessType", businessType);
        response.put("purposes", purposes);

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);

    }

    @Override
    @Cacheable(value = "provinceByName", key = "#province")
    public List<LovResponse> getProviceByName(String province) {
        List<LovResponse> provinces=null;
        List<LkpProvince> lkpProvinces = lkpProvinceRepo.findByProvinceDescrAndIsActive(province,Constants.YES);
        if (!isNullOrEmpty(lkpProvinces)) {
            provinces = lkpProvinces.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getProvinceId());
                lovResponse.setCode(p.getProvinceCode());
                lovResponse.setName(p.getProvinceDescr());
                return lovResponse;
            }).collect(Collectors.toList());

        }
        return provinces;

    }

    @Override
    @Cacheable(value = "districtsByProvince", key = "#id")
    public HashMap<String, Object> getAllDistrictByProviceId(Long id) {
        if (isNullOrEmpty(id)) {
            throw new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        List<LovResponse> lovResponses = null;
        List<LkpDistrict> lkpDistricts = lkpDistrictRepo.findAllByProvinceId(id);
        if (!isNullOrEmpty(lkpDistricts)) {
            lovResponses = lkpDistricts.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getDistrictId());
                lovResponse.setCode(p.getDistrictCode());
                lovResponse.setName(p.getDistrictDescr());
                return lovResponse;
            }).collect(Collectors.toList());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
        } else {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }


    }

    @Cacheable("issueTypes")
    public HashMap<String, Object> getIssueTypeLov() {
        List<LovResponse> provinces;
        List<LkpIssueType> lkpIssueTypes = lkpIssueTypeRepo.findByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpIssueTypes)) {
            provinces = lkpIssueTypes.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getIssueTypeId());
                lovResponse.setCode(p.getIssueTypeCode());
                lovResponse.setName(p.getIssueTypeDescr());
                return lovResponse;
            }).collect(Collectors.toList());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), provinces);
        }
        return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    @Override
    @Cacheable("categories")
    public HashMap<String, Object> getCategories() {
        List<LovResponse> provinces;
        List<LkpTransDocsCategory> lkpIssueTypes = lkpTransDocsCategoryRepo.findByIsActive(Constants.YES);
        if (!isNullOrEmpty(lkpIssueTypes)) {
            provinces = lkpIssueTypes.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getTransDocsCategoryId());
                lovResponse.setCode(p.getCategoryCode());
                lovResponse.setName(p.getCategoryDescr());
                return lovResponse;
            }).collect(Collectors.toList());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), provinces);
        }
        return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);

    }

    /**
     * Lookup lists returned by deviceRegistration so the client can populate the
     * signup screens. Six come from the DB; noTinOptions is a fixed regulatory list.
     */
    private static final List<LovResponse> NO_TIN_OPTIONS = Collections.unmodifiableList(Arrays.asList(
            new LovResponse("A)  The country Jurisdiction where Account Holder's resident does not provide TIN's.", "A", 1L,
                    "A)  The country Jurisdiction where Account Holder's resident does not provide TIN's."),
            new LovResponse("B)  The Account holder is otherwise unable to obtain a TIN or equivalent number.", "B", 2L,
                    "B)  The Account holder is otherwise unable to obtain a TIN or equivalent number."),
            new LovResponse("C)  No TIN is required (only select this option if relevant jurisdiction law does not require the collection of the TIN issued by such jurisdiction).", "C", 3L,
                    "C)  No TIN is required (only select this option if relevant jurisdiction law does not require the collection of the TIN issued by such jurisdiction).")));

    private LovResponse lov(long id, String code, String name, String description) {
        LovResponse lovResponse = new LovResponse();
        lovResponse.setId(id);
        lovResponse.setCode(code);
        lovResponse.setName(name);
        lovResponse.setDescription(description);
        return lovResponse;
    }

    /** How many options each verification challenge presents, the real one included. */
    private static final int CHALLENGE_OPTIONS = 8;

    /**
     * The static lists plus the two per-customer verification challenges.
     *
     * <p>The static lists come from the cached method; this one is deliberately NOT cached, because
     * the mother name and birth place options are specific to one CNIC. The cached map is copied
     * rather than added to - mutating it would put one customer's challenge into every other
     * customer's response.</p>
     */
    @Override
    public HashMap<String, Object> getDeviceRegistrationLovs(String nidNo) {
        HashMap<String, Object> response = new HashMap<>(self.getDeviceRegistrationLovs());

        TblNidData nidData = findNidData(nidNo);
        response.put("motherName", motherNameOptions(nidData));
        response.put("birthPlace", birthPlaceOptions(nidData));
        return response;
    }

    /** The CNIC record the nadra service stored, or null when there is nothing to match against. */
    private TblNidData findNidData(String nidNo) {
        if (nidNo == null || nidNo.trim().isEmpty()) {
            return null;
        }
        try {
            return tblNidDataRepo.findLatestByNidNo(Long.valueOf(nidNo.trim()));
        } catch (NumberFormatException e) {
            // TBL_NID_DATA.NID_NO is numeric; anything else simply has no record.
            return null;
        }
    }

    /**
     * Eight mother-name options: the real one from the CNIC record and seven decoys.
     *
     * <p>Every option is upper-cased so the real value cannot be spotted by casing, the decoys
     * exclude the real name so it cannot appear twice, and the list is shuffled so its position
     * carries no information.</p>
     */
    private List<LovResponse> motherNameOptions(TblNidData nidData) {
        String actual = nidData == null ? null : nidData.getMotherNameEn();
        return buildChallenge(actual, MotherNamesEnum.getRandomNames(
                actual == null || actual.trim().isEmpty() ? CHALLENGE_OPTIONS : CHALLENGE_OPTIONS - 1,
                Collections.singletonList(actual)), "Mother name from CNIC");
    }

    /** Eight birth-place options, built the same way as the mother-name challenge. */
    private List<LovResponse> birthPlaceOptions(TblNidData nidData) {
        String actual = nidData == null ? null : nidData.getBirthPlaceEn();
        return buildChallenge(actual, BirthPlacesEnum.getRandomPlaces(
                actual == null || actual.trim().isEmpty() ? CHALLENGE_OPTIONS : CHALLENGE_OPTIONS - 1,
                Collections.singletonList(actual)), "Birth place from CNIC");
    }

    private List<LovResponse> buildChallenge(String actual, List<String> decoys, String actualDescription) {
        List<LovResponse> options = new ArrayList<>();
        boolean hasActual = actual != null && !actual.trim().isEmpty();

        if (hasActual) {
            options.add(lov(1L, "ACTUAL", actual.trim().toUpperCase(Locale.ROOT), actualDescription));
        }
        for (int i = 0; i < decoys.size(); i++) {
            long id = hasActual ? i + 2 : i + 1;
            options.add(lov(id, "OPTION_" + (i + 1), decoys.get(i).toUpperCase(Locale.ROOT), ""));
        }
        // Position must not give the answer away.
        Collections.shuffle(options, new SecureRandom());
        return options;
    }

    @Override
    @Cacheable("deviceRegistrationLovs")
    public HashMap<String, Object> getDeviceRegistrationLovs() {
        HashMap<String, Object> response = new HashMap<>();

        // LKP_CITY has no _NAME column, so name and description both come from CITY_DESCR
        response.put("city", lkpCityRepo.findAllByIsActive(Constants.YES).stream()
                .map(c -> lov(c.getCityId(), c.getCityCode(), c.getCityDescr(), c.getCityDescr()))
                .collect(Collectors.toList()));

        response.put("occupation", lkpOccupationRepo.findAllByIsActive(Constants.YES).stream()
                .map(o -> lov(o.getOccupationId(), o.getOccupationCode(), o.getOccupationName(), o.getOccupationDescr()))
                .collect(Collectors.toList()));

        response.put("expectedMonthlyVolume", lkpExpectedMonthlyVolumeRepo.findAllByIsActive(Constants.YES).stream()
                .map(v -> lov(v.getExpectedMonthlyVolumeId(), v.getExpectedMonthlyVolumeCode(),
                        v.getExpectedMonthlyVolumeName(), v.getExpectedMonthlyVolumeDescr()))
                .collect(Collectors.toList()));

        response.put("recoveryQuestion", lkpRecoveryQuestionRepo.findAllByIsActive(Constants.YES).stream()
                .map(q -> lov(q.getRecoveryQuestionId(), q.getRecoveryQuestionCode(),
                        q.getRecoveryQuestionName(), q.getRecoveryQuestionDescr()))
                .collect(Collectors.toList()));

        response.put("businessType", lkpBusinessTypeRepo.findAllByIsActive(Constants.YES).stream()
                .map(b -> lov(b.getBusinessTypeId(), b.getBusinessTypeCode(),
                        b.getBusinessTypeName(), b.getBusinessTypeDescr()))
                .collect(Collectors.toList()));

        // LKP_ACCOUNT_PURPOSE has no _NAME column, so name and description both come from _DESCR
        response.put("accountPurpose", lKpAccountPurposeRepo.findAllByIsActive(Constants.YES).stream()
                .map(p -> lov(p.getAccountPurposeId(), p.getAccountPurposeCode(),
                        p.getAccountPurposeDescr(), p.getAccountPurposeDescr()))
                .collect(Collectors.toList()));

        response.put("noTinOptions", NO_TIN_OPTIONS);
        return response;
    }

}
