package com.mfs.pricingprofile.service.impl;

import com.mfs.pricingprofile.dto.PricingProfileSearch;
import com.mfs.pricingprofile.dto.TransChargeRequest;
import com.mfs.pricingprofile.model.*;
import com.mfs.pricingprofile.repo.*;
import com.mfs.pricingprofile.service.PricingProfileService;
import com.mfs.pricingprofile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PricingProfileImpl implements PricingProfileService {
    @PersistenceContext
    EntityManager em;
    @Autowired
    private TblTransChargesDocRepo tblTransChargesDocRepo;
    @Autowired
    private TblTransChargeRepo tblTransChargeRepo;
    @Autowired
    private TblTransChargesSlabRepo tblTransChargesSlabRepo;
    @Autowired
    private LkpChannelRepo lkpChannelRepo;
    @Autowired
    private TblAgentClassRepo tblAgentClassRepo;
    @Autowired
    private TblGlAccountRepo tblGlAccountRepo;
    @Autowired
    private TblTransDocRepo tblTransDocRepo;
    @Autowired
    private LkpSegmentRepo lkpSegmentRepo;
    @Autowired
    private TblTransChargesChannelRepo tblTransChargesChannelRepo;
    @Autowired
    private TblTransChargesSegmentRepo tblTransChargesSegmentRepo;

    @Override
    public TblTransCharge saveTransCharge(TblTransCharge transChargeRequest, BigDecimal userId) throws ParseException {

        if (transChargeRequest.getEffectiveTo() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transChargeRequest.getEffectiveTo());
            // Set the time part to 23:59:59
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            transChargeRequest.setEffectiveTo(endDate);
        }
        transChargeRequest.setIsActive(transChargeRequest.getIsActive());
        transChargeRequest.setCreateuser(userId);
        transChargeRequest.setCreatedate(new Date());
        setCreationDetailsForEntities(transChargeRequest.getTblTransChargesChannels(), userId, transChargeRequest);
        setCreationDetailsForEntities(transChargeRequest.getTblTransChargesSegments(), userId, transChargeRequest);
        setCreationDetailsForEntities(transChargeRequest.getTblTransChargesDocs(), userId, transChargeRequest);
        setCreationDetailsForEntities(transChargeRequest.getTblTransChargesSlabs(), userId, transChargeRequest);

        return saveTransChargesRequest(transChargeRequest);
    }

    private TblTransCharge saveTransChargesRequest(TblTransCharge transChargeRequest) {
        return save(transChargeRequest);
    }

    @Override
    public TblTransCharge save(TblTransCharge model) {
        return tblTransChargeRepo.save(model);
    }

    private <T extends TransChargesCommonEntity> void setCreationDetailsForEntities(List<T> entities, BigDecimal userId, TblTransCharge reqDto) {
        if (entities != null) {
            Date currentDate = new Date();
            for (T entity : entities) {
                entity.setCreateuser(userId);
                entity.setCreatedate(currentDate);
                entity.setIsActive("Y");
                entity.setTblTransCharge(reqDto);
            }
        }
    }

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public TblTransCharge updatepricingprofile(TblTransCharge transChargeRequest, BigDecimal userId) throws ParseException {

        TblTransCharge tblTransCharge = tblTransChargeRepo.findById(transChargeRequest.getTransChargesId()).orElse(null);
        if (tblTransCharge != null) {

            updateEntities(transChargeRequest.getTblTransChargesChannels(), userId, transChargeRequest, tblTransCharge.getTblTransChargesChannels(), TblTransChargesChannel::getTransChargesChannelId);
            updateEntities(transChargeRequest.getTblTransChargesDocs(), userId, transChargeRequest, tblTransCharge.getTblTransChargesDocs(), TblTransChargesDoc::getTransChargesDocsId);
            updateEntities(transChargeRequest.getTblTransChargesSegments(), userId, transChargeRequest, tblTransCharge.getTblTransChargesSegments(), TblTransChargesSegment::getTransChargesSegmentId);
            updateEntities(transChargeRequest.getTblTransChargesSlabs(), userId, transChargeRequest, tblTransCharge.getTblTransChargesSlabs(), TblTransChargesSlab::getTransChargesSlabsId);

            setUpdateCommonField(transChargeRequest, userId, tblTransCharge);

        }

        return save(transChargeRequest);
    }

    private <T extends TransChargesCommonEntity, K> void updateEntities(List<T> newEntities, BigDecimal userId, TblTransCharge reqDto,
                                                                        Collection<T> existingEntities, Function<T, K> idGetter) {
        Date currentDate = new Date();

        Map<K, T> existingEntityMap = existingEntities.stream()
                .collect(Collectors.toMap(idGetter, Function.identity()));

        if (newEntities != null) {
            newEntities.forEach(entity -> {
                K primaryKeyValue = idGetter.apply(entity);
                if (primaryKeyValue == null || !existingEntityMap.containsKey(primaryKeyValue)) {
                    setEntityCreationFields(entity, userId, currentDate, reqDto);
                } else {
                    setEntityUpdateFields(entity, userId, currentDate, reqDto, existingEntityMap.get(primaryKeyValue));
                    existingEntityMap.remove(primaryKeyValue);
                }
            });
        }

        existingEntityMap.values().forEach(entity -> {
            entity.setIsActive(Constants.N);
            entity.setLastupdateuser(userId);
            entity.setLastupdatedate(currentDate);
        });
    }

    private <T extends TransChargesCommonEntity> void setEntityCreationFields(T entity, BigDecimal userId, Date currentDate, TblTransCharge reqDto) {
        entity.setCreateuser(userId);
        entity.setCreatedate(currentDate);
        entity.setIsActive("Y");
        entity.setTblTransCharge(reqDto);
    }

    private <T extends TransChargesCommonEntity> void setEntityUpdateFields(T entity, BigDecimal userId, Date currentDate, TblTransCharge reqDto, T oldEntity) {
        entity.setLastupdateuser(userId);
        entity.setLastupdatedate(currentDate);
        entity.setIsActive("Y");
        entity.setCreatedate(oldEntity.getCreatedate());
        entity.setCreateuser(oldEntity.getCreateuser());
        entity.setUpdateindex(setUpdateIndex(oldEntity.getUpdateindex()));
        entity.setTblTransCharge(reqDto);
    }

    private void setUpdateCommonField(TblTransCharge reqDto, BigDecimal userId, TblTransCharge tblTransCharge) {
        reqDto.setLastupdatedate(new Date());
        reqDto.setLastupdateuser(userId);
        reqDto.setCreatedate(tblTransCharge.getCreatedate());
        reqDto.setCreateuser(tblTransCharge.getCreateuser());
        reqDto.setUpdateindex(setUpdateIndex(tblTransCharge.getUpdateindex()));
        reqDto.setIsActive(reqDto.getIsActive());
        LkpStatus lkpStatus = new LkpStatus();
        lkpStatus.setStatusId(2L);
        reqDto.setLkpStatus(lkpStatus);
    }

    public BigDecimal setUpdateIndex(BigDecimal value) {
        return value == null ? BigDecimal.ONE : value.add(BigDecimal.ONE);
    }

    @Override
    public List<LkpChannel> getchannels() {
        return lkpChannelRepo.findByIsActive(Constants.Y);
    }

    @Override
    public List<TblAgentClass> getAgentClass() {
        return tblAgentClassRepo.findByIsActive(Constants.Y);
    }

    @Override
    public List<TblGlAccount> getGlAccounts() {
        return tblGlAccountRepo.findByIsActive(Constants.Y);
    }

    @Override
    public List<TblTransDoc> getTransDocs() {
        return tblTransDocRepo.getFinancialTransDocs();
    }

    @Override
    public TblTransCharge getTransChargesById(Long transChargeId) {
        return tblTransChargeRepo.findById(transChargeId).orElse(null);
    }

    @Override
    public List<TblTransChargesDoc> getTblTransChargesDocs(long transChargesId) {
        return tblTransChargesDocRepo.findByTblTransChargeTransChargesIdAndIsActive(transChargesId, Constants.Y);
    }

    @Override
    public List<TblTransChargesSlab> getTransChargesSlab(long transChargesId) {
        return tblTransChargesSlabRepo.findByTblTransChargeTransChargesIdAndIsActive(transChargesId, Constants.Y);
    }

    @Override
    public List<TblTransCharge> getAllTblTransCharge(PricingProfileSearch pricingProfileSearch) {
        String dateFromInput = null;
        String dateToInput = null;
        if (pricingProfileSearch.getFromDate() != null && !(pricingProfileSearch.getFromDate().equals(""))) {
            dateFromInput = pricingProfileSearch.getFromDate() + " 00:00:00";
        }
        if (pricingProfileSearch.getToDate() != null && !(pricingProfileSearch.getToDate().equals(""))) {
            dateToInput = pricingProfileSearch.getToDate() + " 23:59:59";
        }
        return tblTransChargeRepo.getAllTblTransCharge(pricingProfileSearch, dateFromInput, dateToInput);
    }

    @Override
    public List<LkpSegment> getSegments() {
        return lkpSegmentRepo.findByIsActive(Constants.Y);
    }

    @Override
    public TblTransCharge inactivepricingprofile(TransChargeRequest transChargeRequest, BigDecimal userId) {
        TblTransCharge tblTransCharge = tblTransChargeRepo.findById(transChargeRequest.getTransChargesId()).orElse(null);
        if (tblTransCharge != null) {
            tblTransCharge.setIsActive(transChargeRequest.getIsActive());
            tblTransCharge.setLastupdateuser(userId);
            tblTransCharge.setLastupdatedate(new Date());
            tblTransCharge.setUpdateindex(tblTransCharge.getUpdateindex() != null ? tblTransCharge.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
        }
        return tblTransCharge;
    }
}
