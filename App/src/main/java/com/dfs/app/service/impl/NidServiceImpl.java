package com.dfs.app.service.impl;

import com.dfs.app.dto.BvsRequest;
import com.dfs.app.dto.NidBvsRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.TblBioverisy;
import com.dfs.app.model.TblCustomer;
import com.dfs.app.repo.TblBioverisyRepo;
import com.dfs.app.repo.TblCustomerRepo;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.NidService;
import com.dfs.app.util.AESencryption;
import com.dfs.app.util.Constants;
import com.dfs.app.util.CustomDataNotFoundException;
import com.dfs.app.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class NidServiceImpl implements NidService {
    @Autowired
    private TblBioverisyRepo tblBioverisyRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AESencryption aeSencryption;

    @Override
    public HashMap<String, Object> bioversys(NidBvsRequest nidBvsRequest, Request request, BigDecimal userId) {
        boolean flag=true;
        TblCustomer tblCustomer=tblCustomerRepo.findByMobileNumberOrNidNo(Constants.EMPTY,aeSencryption.encryptwith256(nidBvsRequest.getNidNumber()));
        if(tblCustomer==null){
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        try {
        for(BvsRequest bvsRequest:nidBvsRequest.getBvsRequest()){
            TblBioverisy tblBioverisy=tblBioverisyRepo.findByNidNoAndFingerIndex(nidBvsRequest.getNidNumber(),bvsRequest.getFingerIndex());
            if(tblBioverisy==null){
                tblBioverisy=new TblBioverisy();
                tblBioverisy.setCreatedate(new Date());
                tblBioverisy.setCreateuser(userId);
            }else {
                tblBioverisy.setLastupdatedate(new Date());
                tblBioverisy.setLastupdateuser(userId);
                tblBioverisy.setUpdateindex(tblBioverisy.getUpdateindex()==null?BigDecimal.ONE:tblBioverisy.getUpdateindex().add(BigDecimal.ONE));
            }
            tblBioverisy.setTblCustomer(tblCustomer);
            tblBioverisy.setTemplateBase64(bvsRequest.getBase64Template());
            tblBioverisy.setIsoTemplateBase64(bvsRequest.getIsoTemplate());
            tblBioverisy.setFingerIndex(new BigDecimal(bvsRequest.getFingerIndex()));
            tblBioverisy.setIsActive("Y");
            tblBioverisy.setNidNo(nidBvsRequest.getNidNumber());
            tblBioverisyRepo.saveAndFlush(tblBioverisy);

        }
        }catch (Exception e){
            flag=false;
        }finally {
            if(flag==true){
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),null);
            }else {
                return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),null);
            }
        }

    }
}
