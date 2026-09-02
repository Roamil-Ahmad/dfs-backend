package com.mfs.pricingprofile.service.impl;

import com.mfs.pricingprofile.dto.CommissionProfileRequest;
import com.mfs.pricingprofile.dto.CommissionProfileSearch;
import com.mfs.pricingprofile.dto.CommissionSlabRequest;
import com.mfs.pricingprofile.model.*;
import com.mfs.pricingprofile.repo.TblCommissionDocRepo;
import com.mfs.pricingprofile.repo.TblCommissionProfileRepo;
import com.mfs.pricingprofile.repo.TblCommissionSlabRepo;
import com.mfs.pricingprofile.service.CommissionService;
import com.mfs.pricingprofile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommissionImpl implements CommissionService {
    @PersistenceContext
    EntityManager em;
    @Autowired
    private TblCommissionDocRepo tblCommissionDocRepo;
    @Autowired
    private TblCommissionProfileRepo tblCommissionProfileRepo;
    @Autowired
    private TblCommissionSlabRepo tblCommissionSlabRepo;

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public TblCommissionProfile saveCommissionProfile(CommissionProfileRequest commissionProfileRequest, BigDecimal userId) throws ParseException {
        TblCommissionProfile tblCommissionProfile = new TblCommissionProfile();

        tblCommissionProfile.setCommissionProfileName(commissionProfileRequest.getCommissionProfileName());
        TblGlAccount tblAccount = new TblGlAccount();
        tblAccount.setGlAccountId(commissionProfileRequest.getGlAccountId());
        tblCommissionProfile.setTblGlAccount(tblAccount);
        tblCommissionProfile.setIsActive(commissionProfileRequest.getIsActive());
        tblCommissionProfile.setStatusId(commissionProfileRequest.getStatusId());
        tblCommissionProfile.setEffectiveFrom(commissionProfileRequest.getEffectiveFrom());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(commissionProfileRequest.getEffectiveTo());
        String dateTimeString = dateString + " 23:59:59";
        try {
            Date updatedEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeString);
            tblCommissionProfile.setEffectiveTo(updatedEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tblCommissionProfile.setCreateuser(userId);
        tblCommissionProfile.setCreatedate(new Date());

        tblCommissionProfile = tblCommissionProfileRepo.save(tblCommissionProfile);
        if (tblCommissionProfile != null) {
            for (Long doc : commissionProfileRequest.getTblTransDoc()) {
                TblCommissionDoc tblCommissionDoc = new TblCommissionDoc();
                tblCommissionDoc.setTblCommissionProfile(tblCommissionProfile);
                TblTransDoc tblDoc = new TblTransDoc();
                tblDoc.setTransDocsId(doc);
                tblCommissionDoc.setTblTransDoc(tblDoc);
                tblCommissionDoc.setCreatedate(new Date());
                tblCommissionDoc.setCreateuser(userId);
                tblCommissionDoc.setIsActive(Constants.Y);
                tblCommissionDoc = tblCommissionDocRepo.save(tblCommissionDoc);
            }

            for (CommissionSlabRequest commissionSlabRequest : commissionProfileRequest.getCommissionSlabRequests()) {
                TblCommissionSlab tblCommissionSlab = new TblCommissionSlab();
                tblCommissionSlab.setCommissionType(commissionSlabRequest.getCommissionTypeCode());
                if (commissionSlabRequest.getCommissionTypeCode().equalsIgnoreCase("F")) {
                    tblCommissionSlab.setCommissionAmount(commissionSlabRequest.getCommissionAmount());
                } else if (commissionSlabRequest.getCommissionTypeCode().equalsIgnoreCase("P")) {
                    tblCommissionSlab.setCommissionPercentage(commissionSlabRequest.getCommissionPercentage());
                }
                tblCommissionSlab.setCreatedate(new Date());
                tblCommissionSlab.setCreateuser(userId);
                tblCommissionSlab.setIsActive(Constants.Y);
                tblCommissionSlab.setFromAmount(commissionSlabRequest.getFromAmount());
                tblCommissionSlab.setToAmount(commissionSlabRequest.getToAmount());
                tblCommissionSlab.setTblCommissionProfile(tblCommissionProfile);
                tblCommissionSlab = tblCommissionSlabRepo.save(tblCommissionSlab);
            }
        }
        return tblCommissionProfile;
    }

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public TblCommissionProfile updatecommissionprofile(CommissionProfileRequest commissionProfileRequest, BigDecimal userId) throws ParseException {
        TblCommissionProfile tblCommissionProfile = tblCommissionProfileRepo.findById(commissionProfileRequest.getCommissionProfileId()).orElse(null);
        if (tblCommissionProfile != null) {
            tblCommissionProfile.setCommissionProfileName(commissionProfileRequest.getCommissionProfileName());
            tblCommissionProfile.setCommissionProfileName(commissionProfileRequest.getCommissionProfileName());
            TblGlAccount tblAccount = new TblGlAccount();
            tblAccount.setGlAccountId(commissionProfileRequest.getGlAccountId());
            tblCommissionProfile.setTblGlAccount(tblAccount);
            tblCommissionProfile.setStatusId(commissionProfileRequest.getStatusId());
            tblCommissionProfile.setIsActive(commissionProfileRequest.getIsActive());
            tblCommissionProfile.setEffectiveFrom(commissionProfileRequest.getEffectiveFrom());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(commissionProfileRequest.getEffectiveTo());
            String dateTimeString = dateString + " 23:59:59";
            try {
                Date updatedEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeString);
                tblCommissionProfile.setEffectiveTo(updatedEndDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tblCommissionProfile.setLastupdateuser(userId);
            tblCommissionProfile.setLastupdatedate(new Date());
            tblCommissionProfile.setUpdateindex(tblCommissionProfile.getUpdateindex() == null ? new BigDecimal(1)
                    : new BigDecimal(tblCommissionProfile.getUpdateindex().intValue() + 1));
            tblCommissionProfile = tblCommissionProfileRepo.save(tblCommissionProfile);
            if (tblCommissionProfile != null && commissionProfileRequest.getTblTransDoc().size() > 0) {
                List<Long> oldProductIds = tblCommissionDocRepo.findByTblCommissionProfileCommissionProfileId(tblCommissionProfile.getCommissionProfileId())
                        .stream()
                        .map(detail -> detail.getTblTransDoc().getTransDocsId())
                        .collect(Collectors.toList());

                List<Long> filteredNewList = commissionProfileRequest.getTblTransDoc().stream()
                        .map(detail -> detail.longValue())
                        .collect(Collectors.toList());

                List<Long> newProductIds = filteredNewList.stream()
                        .filter(key -> !oldProductIds.contains(key))
                        .collect(Collectors.toList());

                List<Long> removedProductIds = oldProductIds.stream()
                        .filter(key -> !filteredNewList.contains(key))
                        .collect(Collectors.toList());

                for (Long productId : removedProductIds) {
                    TblCommissionDoc tblCommissionProduct = tblCommissionDocRepo
                            .findByTblCommissionProfileCommissionProfileIdAndTblTransDocTransDocsId(tblCommissionProfile.getCommissionProfileId(), productId.longValue());
                    if (tblCommissionProduct != null && !tblCommissionProduct.getIsActive().equals(Constants.N)) {
                        tblCommissionProduct.setIsActive(Constants.N);
                        tblCommissionProduct.getTblCommissionProfile().setCommissionProfileId(tblCommissionProfile.getCommissionProfileId());
                        tblCommissionProduct.setLastupdateuser(userId);
                        tblCommissionProduct.setLastupdatedate(new Date());
                        tblCommissionProduct.setUpdateindex(tblCommissionProduct.getUpdateindex() == null ? new BigDecimal(1)
                                : new BigDecimal(tblCommissionProduct.getUpdateindex().intValue() + 1));
                        tblCommissionProduct = tblCommissionDocRepo.save(tblCommissionProduct);
                    }
                }

                Set<Long> oldProductIdsSet = new HashSet<>(oldProductIds);
                for (Long productId : filteredNewList) {
                    if (oldProductIdsSet.contains(productId)) {
                        TblCommissionDoc tblCommissionProduct = tblCommissionDocRepo
                                .findByTblCommissionProfileCommissionProfileIdAndTblTransDocTransDocsId(tblCommissionProfile.getCommissionProfileId(), productId.longValue());
                        if (tblCommissionProduct != null && tblCommissionProduct.getIsActive().equals(Constants.N)) {
                            tblCommissionProduct.setIsActive(Constants.Y);
                            tblCommissionProduct.getTblCommissionProfile().setCommissionProfileId(tblCommissionProfile.getCommissionProfileId());
                            tblCommissionProduct.setLastupdateuser(userId);
                            tblCommissionProduct.setLastupdatedate(new Date());
                            tblCommissionProduct.setUpdateindex(tblCommissionProduct.getUpdateindex() == null ? new BigDecimal(1)
                                    : new BigDecimal(tblCommissionProduct.getUpdateindex().intValue() + 1));
                            tblCommissionProduct = tblCommissionDocRepo.save(tblCommissionProduct);
                        }
                    }
                }

                for (Long productId : newProductIds) {
                    TblCommissionDoc tblCommissionProduct = new TblCommissionDoc();
                    tblCommissionProduct.setTblCommissionProfile(tblCommissionProfile);
                    TblTransDoc tblTransDoc = new TblTransDoc();
                    tblTransDoc.setTransDocsId(productId);
                    tblCommissionProduct.setTblTransDoc(tblTransDoc);
                    tblCommissionProduct.setCreatedate(new Date());
                    tblCommissionProduct.setCreateuser(userId);
                    tblCommissionProduct.setIsActive(Constants.Y);
                    tblCommissionProduct = tblCommissionDocRepo.save(tblCommissionProduct);
                }

                if (commissionProfileRequest.getCommissionSlabRequests() != null && !commissionProfileRequest.getCommissionSlabRequests().isEmpty()) {

                    List<Long> oldCommissionIds = tblCommissionSlabRepo.findByTblCommissionProfileCommissionProfileId(tblCommissionProfile.getCommissionProfileId())
                            .stream()
                            .map(detail -> detail.getCommissionSlabId())
                            .collect(Collectors.toList());

                    List<Long> filteredNewCommissionList = commissionProfileRequest.getCommissionSlabRequests().stream()
                            .map(detail -> detail.getCommissionSlabId())
                            .collect(Collectors.toList());

                    List<Long> newCommissionIds = filteredNewCommissionList.stream()
                            .filter(key -> !oldCommissionIds.contains(key))
                            .collect(Collectors.toList());

                    List<Long> removedCommissionIds = oldCommissionIds.stream()
                            .filter(key -> !filteredNewCommissionList.contains(key))
                            .collect(Collectors.toList());

                    /////REMOVED RECORD
                    for (Long id : removedCommissionIds) {
                        TblCommissionSlab tblCommissionSlab = tblCommissionSlabRepo
                                .findById(id).orElse(null);
                        if (tblCommissionSlab != null) {
                            tblCommissionSlab.setIsActive(Constants.N);
                            tblCommissionSlab.setTblCommissionProfile(tblCommissionProfile);
                            tblCommissionSlab.setLastupdateuser(userId);
                            tblCommissionSlab.setLastupdatedate(new Date());
                            tblCommissionSlab.setUpdateindex(tblCommissionSlab.getUpdateindex() == null ? new BigDecimal(1)
                                    : new BigDecimal(tblCommissionSlab.getUpdateindex().intValue() + 1));
                            tblCommissionSlab = tblCommissionSlabRepo.save(tblCommissionSlab);
                        }
                    }

                    ////NEW RECORD
                    for (CommissionSlabRequest commissionSlabRequest : commissionProfileRequest.getCommissionSlabRequests()) {
                        if (commissionSlabRequest.getCommissionSlabId() <= 0) {
                            TblCommissionSlab tblCommissionSlab = new TblCommissionSlab();
                            if (commissionSlabRequest.getCommissionTypeCode().equalsIgnoreCase("F")) {
                                tblCommissionSlab.setCommissionAmount(commissionSlabRequest.getCommissionAmount());
                            } else if (commissionSlabRequest.getCommissionTypeCode().equalsIgnoreCase("P")) {
                                tblCommissionSlab.setCommissionPercentage(commissionSlabRequest.getCommissionPercentage());
                            }
                            tblCommissionSlab.setCommissionType(commissionSlabRequest.getCommissionTypeCode());
                            tblCommissionSlab.setFromAmount(commissionSlabRequest.getFromAmount());
                            tblCommissionSlab.setToAmount(commissionSlabRequest.getToAmount());
                            tblCommissionSlab.setIsActive(Constants.Y);
                            tblCommissionSlab.setCreatedate(new Date());
                            tblCommissionSlab.setCreateuser(userId);
                            tblCommissionSlab.setTblCommissionProfile(tblCommissionProfile);
                            tblCommissionSlab = tblCommissionSlabRepo.save(tblCommissionSlab);
                        }
                    }

                    /////UPDATE RECORD
                    for (CommissionSlabRequest commissionSlabRequest : commissionProfileRequest.getCommissionSlabRequests()) {
                        TblCommissionSlab tblCommissionSlab = tblCommissionSlabRepo
                                .findById(commissionSlabRequest.getCommissionSlabId()).orElse(null);
                        if (tblCommissionSlab != null) {
                            if (commissionSlabRequest.getCommissionTypeCode().equalsIgnoreCase("F")) {
                                tblCommissionSlab.setCommissionAmount(commissionSlabRequest.getCommissionAmount());
                            } else if (commissionSlabRequest.getCommissionTypeCode().equalsIgnoreCase("P")) {
                                tblCommissionSlab.setCommissionPercentage(commissionSlabRequest.getCommissionPercentage());
                            }
                            tblCommissionSlab.setCommissionType(commissionSlabRequest.getCommissionTypeCode());
                            tblCommissionSlab.setFromAmount(commissionSlabRequest.getFromAmount());
                            tblCommissionSlab.setToAmount(commissionSlabRequest.getToAmount());
                            tblCommissionSlab.setIsActive(Constants.Y);
                            tblCommissionSlab.setLastupdateuser(userId);
                            tblCommissionSlab.setLastupdatedate(new Date());
                            tblCommissionSlab.setUpdateindex(tblCommissionSlab.getUpdateindex() == null ? new BigDecimal(1)
                                    : new BigDecimal(tblCommissionSlab.getUpdateindex().intValue() + 1));
                            tblCommissionSlab = tblCommissionSlabRepo.save(tblCommissionSlab);
                        }
                    }
                }
            }

        } else {
            tblCommissionProfile = new TblCommissionProfile();
        }


        return tblCommissionProfile;
    }

    @Override
    public TblCommissionProfile getCommissionProfileById(Long commissionProfileId) {
        return tblCommissionProfileRepo.findById(commissionProfileId).orElse(null);
    }


    @Override
    public BigDecimal checkCommissionProfileExistance(CommissionProfileRequest commissionProfileRequest) {
        String countSql = "SELECT COUNT(*) FROM TBL_COMMISSION_PROFILE C\n" +
                "                                    WHERE UPPER(C.COMMISSION_PROFILE_NAME) = UPPER(?)";
        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter(1, commissionProfileRequest.getCommissionProfileName());
        BigDecimal consentCount = (BigDecimal) countQuery.getSingleResult();
        if (consentCount.longValue() > 0) {
            return new BigDecimal(1);
        } else {
            return new BigDecimal(0);
        }
    }

    @Override
    public BigDecimal checkCommissionProfileExistanceUpdate(CommissionProfileRequest commissionProfileRequest) {
        String countSql = "SELECT COUNT(*) FROM TBL_COMMISSION_PROFILE C\n" +
                "                    WHERE UPPER(C.COMMISSION_PROFILE_NAME) = UPPER(?) AND COMMISSION_PROFILE_ID<>?";
        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter(1, commissionProfileRequest.getCommissionProfileName());
        countQuery.setParameter(2, commissionProfileRequest.getCommissionProfileId());
        BigDecimal consentCount = (BigDecimal) countQuery.getSingleResult();
        if (consentCount.longValue() > 0) {
            return new BigDecimal(1);
        } else {
            return new BigDecimal(0);
        }
    }

    @Override
    public List<TblCommissionProfile> getAllCommissionProfiles(CommissionProfileSearch commissionProfileSearch) {
        String dateFromInput = null;
        String dateToInput = null;
        if (commissionProfileSearch.getFromDate() != null && !(commissionProfileSearch.getFromDate().equals(""))) {
            dateFromInput = commissionProfileSearch.getFromDate() + " 00:00:00";
        }
        if (commissionProfileSearch.getToDate() != null && !(commissionProfileSearch.getToDate().equals(""))) {
            dateToInput = commissionProfileSearch.getToDate() + " 23:59:59";
        }
        return tblCommissionProfileRepo.getAllCommissionProfiles(commissionProfileSearch, dateFromInput, dateToInput);
    }

    @Override
    public List<TblCommissionDoc> getTblCommissionDocs(long commissionProfileId) {
        return tblCommissionDocRepo.findByTblCommissionProfileCommissionProfileIdAndIsActive(commissionProfileId, Constants.Y);
    }

    @Override
    public List<TblCommissionSlab> getCommissionSlab(long commissionProfileId) {
        return tblCommissionSlabRepo.findByTblCommissionProfileCommissionProfileIdAndIsActive(commissionProfileId, Constants.Y);
    }
}
