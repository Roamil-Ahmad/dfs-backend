package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.BvsRequest;
import com.dfs.agentapp.dto.NidBvsRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.TblAgent;
import com.dfs.agentapp.model.TblBioverisy;
import com.dfs.agentapp.repo.TblAgentRepo;
import com.dfs.agentapp.repo.TblBioverisyRepo;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.NidService;
import com.dfs.agentapp.util.AESencryption;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.CustomDataNotFoundException;
import com.dfs.agentapp.util.GenericResponseCode;
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
    private TblAgentRepo tblAgentRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AESencryption aeSencryption;

    @Override
    public HashMap<String, Object> bioverisys(NidBvsRequest nidBvsRequest, Request request, BigDecimal userId) {
        boolean flag=true;
        TblAgent tblAgent= tblAgentRepo.findByMobile(aeSencryption.encryptwith256(nidBvsRequest.getMobileNumber()));
        if(tblAgent==null){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
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
            tblBioverisy.setTemplateBase64(bvsRequest.getBase64Template());
            tblBioverisy.setFingerIndex(new BigDecimal(bvsRequest.getFingerIndex()));
            tblBioverisy.setIsActive("Y");
            tblBioverisy.setNidNo(nidBvsRequest.getNidNumber());
            tblBioverisy.setTblAgent(tblAgent);
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
