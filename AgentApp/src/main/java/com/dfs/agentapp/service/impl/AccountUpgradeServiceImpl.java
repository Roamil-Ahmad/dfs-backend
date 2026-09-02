package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.UpdateAccountLevelRequest;
import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.AccountUpgradeService;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.util.AESencryption;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.CustomDataNotFoundException;
import com.dfs.agentapp.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class AccountUpgradeServiceImpl implements AccountUpgradeService {


    @Autowired
    private TblAccountUpgradeRepo tblAccountUpgradeRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private LkpProvinceRepo lkpProvinceRepo;
    @Autowired
    private LKpAccountPurposeRepo lKpAccountPurposeRepo;
    @Autowired
    private LkpSourceOfIncomeRepo lkpSourceOfIncomeRepo;
    @Autowired
    private LkpOccupationRepo lkpOccupationRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private LkpStatusRepo lkpStatusRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Value("${account.level.two}")
    private String accountLevelTwo;
    @Value("${account.level.one}")
    private String accountLevelOne;
    @Value("${account.type.code}")
    private String accountTypeWallet;

    @Value("${doc.sourceOfIncome.code}")
    private String sourceOfIncome;
    @Value("${doc.path}")
    private String docPath;

    @Value("${doc.proofOfAddress.code}")
    private String proofOfAddress;

    @Value("${doc.nidFront.code}")
    private String nidFront;

    @Value("${doc.nidBack.code}")
    private String nidBack;

    @Value("${doc.selfie.code}")
    private String selfie;

    @Value("${status.pending.code}")
    private String pendingStatusCode;

    @Autowired
    private AESencryption aeSencryption;

    @Override
    public HashMap<String, Object> upgradeToL2(UpdateAccountLevelRequest updateAccountLevelRequest, Request request, BigDecimal userId) {
        TblAccount tblAccount=tblAccountRepo.findByMobileNumberAndAccountTypeCode(updateAccountLevelRequest.getMobileNumber(),accountTypeWallet );
        if(tblAccount==null){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        TblAccountLevel tblAccountLevel=tblAccountLevelRepo.findByAccountLevelCode(updateAccountLevelRequest.getAccountLevelCode());
        if(tblAccount==null){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_LEVEL_NOT_FOUND.getResponseCode());
        }
        if(tblAccount.getTblAccountLevel().getAccountLevelId()==tblAccountLevel.getAccountLevelId()){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALEADY_UPDATED.getResponseCode());
        }
        UploadDocumentRequest uploadDocumentRequest =new UploadDocumentRequest();
        uploadDocumentRequest.setAccountLevelCode(updateAccountLevelRequest.getAccountLevelCode());
        uploadDocumentRequest.setDocuments(updateAccountLevelRequest.getDocuments());
        uploadDocumentRequest.setMobileNumber(updateAccountLevelRequest.getMobileNumber());
        HashMap<String, Object> response=documentService.uploadDocument(uploadDocumentRequest,request,userId);
        if(response.get("responsecode").toString().equals(GenericResponseCode.SUCCESS.getResponseCode())){

            LkpProvince lkpProvince = lkpProvinceRepo.findByProvinceCodeAndIsActive(updateAccountLevelRequest.getProvinceCode(), Constants.YES);
            if (lkpProvince == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.PROVINCE_NOT_FOUND.getResponseCode());
            }

            LkpSourceOfIncome lkpSourceOfIncome = lkpSourceOfIncomeRepo.findBySourceOfIncomeCodeAndIsActive(updateAccountLevelRequest.getSourceOfIncomeCode(), Constants.YES);
            if (lkpSourceOfIncome == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.SOURCE_OF_INCOME_NOT_FOUND.getResponseCode());
            }

            LkpOccupation lkpOccupation = lkpOccupationRepo.findByOccupationCodeAndIsActive(updateAccountLevelRequest.getOccupationCode(), Constants.YES);
            if (lkpOccupation == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.OCCUPATION_NOT_FOUND.getResponseCode());
            }

            LkpAccountPurpose lkpAccountPurpose = lKpAccountPurposeRepo.findByAccountPurposeCodeAndIsActive(updateAccountLevelRequest.getPurposeOfAccountCode(), Constants.YES);
            if (lkpAccountPurpose == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.PURPOSE_OF_ACCOUNT_NOT_FOUND.getResponseCode());
            }
            TblAppUser tblAppUser=tblAppUserRepo.findByMobileNumber(aeSencryption.encryptwith256(updateAccountLevelRequest.getMobileNumber()));
            if(tblAppUser==null){
                throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());

            }
            TblDocument tf=tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(tblAppUser.getAppUserId(),nidFront,accountLevelOne);
            if(tf==null){
                throw new CustomDataNotFoundException(GenericResponseCode.NID_FRONT_NOT_FOUND.getResponseCode());
            }
            TblDocument tb=tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(tblAppUser.getAppUserId(),nidBack,accountLevelOne);
            if(tb==null){
                throw new CustomDataNotFoundException(GenericResponseCode.NID_BACK_NOT_FOUND.getResponseCode());
            }
            TblDocument poa=tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(tblAppUser.getAppUserId(),proofOfAddress,accountLevelTwo);
            if(poa==null){
                throw new CustomDataNotFoundException(GenericResponseCode.PROOF_OF_INCOME_NOT_FOUND.getResponseCode());
            }
            TblDocument sf=tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(tblAppUser.getAppUserId(),selfie,accountLevelTwo);
            if(sf==null){
                throw new CustomDataNotFoundException(GenericResponseCode.SELFIE_IMAGE_NOT_FOUND.getResponseCode());
            }
            LkpStatus lkpStatus=lkpStatusRepo.findByStatusCodeAndIsActive(pendingStatusCode,Constants.YES);
            if(lkpStatus==null){
                throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
            }
            TblAccountUpgrade tblAccountUpgrade=tblAccountUpgradeRepo.findByTblAccountAccountIdAndTblAccountLevelAccountLevelId(tblAccount.getAccountId(),tblAccountLevel.getAccountLevelId());
            if(tblAccountUpgrade==null){
                tblAccountUpgrade=new TblAccountUpgrade();
                tblAccountUpgrade.setCreatedate(new Date());
                tblAccountUpgrade.setCreateuser(userId);
            }else {
                tblAccountUpgrade.setLastupdatedate(new Date());
                tblAccountUpgrade.setLastupdateuser(userId);
                tblAccountUpgrade.setUpdateindex(tblAccountUpgrade.getUpdateindex()==null?BigDecimal.ONE:tblAccountUpgrade.getUpdateindex().add(BigDecimal.ONE));
            }
            tblAccountUpgrade.setLkpSourceOfIncome(lkpSourceOfIncome);
            tblAccountUpgrade.setTblAccountLevel(tblAccountLevel);
            tblAccountUpgrade.setTblAccount(tblAccount);
            tblAccountUpgrade.setLkpStatus(lkpStatus);
            tblAccountUpgrade.setLkpProvince(lkpProvince);
            tblAccountUpgrade.setLkpAccountPurpose(lkpAccountPurpose);
            tblAccountUpgrade.setLkpOccupation(lkpOccupation);
            tblAccountUpgrade.setTblDocument4(tf);
            tblAccountUpgrade.setTblDocument1(tb);
            tblAccountUpgrade.setTblDocument2(sf);
            tblAccountUpgrade.setTblDocument3(poa);

            tblAccountUpgradeRepo.saveAndFlush(tblAccountUpgrade);
            TblCustomer tblCustomer=tblAccount.getTblCustomer();
            tblCustomer.setEmail(aeSencryption.encryptwith256(updateAccountLevelRequest.getEmail()));
            tblCustomer.setEmailVerified(Constants.N);
            tblCustomer.setLastupdatedate(new Date());
            tblCustomer.setLastupdateuser(userId);
            tblCustomer.setUpdateindex(tblCustomer.getUpdateindex()==null?BigDecimal.ONE:tblCustomer.getUpdateindex().add(BigDecimal.ONE));
            tblCustomerRepo.saveAndFlush(tblCustomer);
            HashMap<String, Object> resp= commonService.getResponse(GenericResponseCode.ACCOUNT_PARKED_FOR_APPROVAL.getResponseCode(), null);
            resp.replace("responsecode",GenericResponseCode.ACCOUNT_PARKED_FOR_APPROVAL.getResponseCode(),GenericResponseCode.SUCCESS.getResponseCode());
            return resp;
        }



        return response;
    }
}
