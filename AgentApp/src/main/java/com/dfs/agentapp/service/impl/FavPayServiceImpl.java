package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.AddFavPayRequest;
import com.dfs.agentapp.dto.BeneficaryResponse;
import com.dfs.agentapp.dto.GetBalanceRequest;
import com.dfs.agentapp.dto.UpdateFavPayRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.TblBeneficiary;
import com.dfs.agentapp.model.TblCustomer;
import com.dfs.agentapp.repo.TblBeneficiaryRepo;
import com.dfs.agentapp.repo.TblCustomerRepo;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.FavPayService;
import com.dfs.agentapp.util.AESencryption;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.CustomDataNotFoundException;
import com.dfs.agentapp.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavPayServiceImpl implements FavPayService {
    @Autowired
    private TblBeneficiaryRepo tblBeneficiaryRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AESencryption aeSencryption;
    @Override
    public HashMap<String, Object> saveBeneficary(AddFavPayRequest favPayRequest, BigDecimal userId, Request request) {
        TblCustomer tblCustomer = tblCustomerRepo.findByMobileNumberOrNidNo(aeSencryption.encryptwith256(favPayRequest.getMobileNumber()), Constants.EMPTY);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        BigDecimal bankImdId = tblBeneficiaryRepo.findBankImdIdByImd(favPayRequest.getBeneficaryBankImd());
        TblBeneficiary tblBeneficiary = new TblBeneficiary();
        tblBeneficiary.setBeneficiaryAccountNo(aeSencryption.encryptwith256(favPayRequest.getBeneficaryAccountNo()));
        tblBeneficiary.setBeneficiaryAccountTitle(aeSencryption.encryptwith256(favPayRequest.getBeneficaryAccountTitle()));
        tblBeneficiary.setBeneficiaryAcctType(favPayRequest.getBeneficaryAccountType());
        tblBeneficiary.setBeneficiaryEmail(aeSencryption.encryptwith256(favPayRequest.getBeneficaryEmail()));
        tblBeneficiary.setBeneficiaryImd(favPayRequest.getBeneficaryBankImd());
        tblBeneficiary.setBeneficiaryImdId(bankImdId);
        tblBeneficiary.setBeneficiaryMobileNo(aeSencryption.encryptwith256(favPayRequest.getBeneficaryMobileNumber()));
        tblBeneficiary.setBeneficiaryNickName(aeSencryption.encryptwith256(favPayRequest.getBeneficaryNickName()));
        tblBeneficiary.setBeneficiaryType(favPayRequest.getBeneficaryType());
        tblBeneficiary.setIsActive(Constants.YES);
        tblBeneficiary.setCreatedate(new Date());
        tblBeneficiary.setCreateuser(userId);
        tblBeneficiary.setTblCustomer(tblCustomer);
        tblBeneficiaryRepo.saveAndFlush(tblBeneficiary);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> getAllFavpay(GetBalanceRequest getBalanceRequest, Request request) {
        List<BeneficaryResponse> beneficaryResponses = new ArrayList<>();
        TblCustomer tblCustomer = tblCustomerRepo.findByMobileNumberOrNidNo(aeSencryption.encryptwith256(getBalanceRequest.getMobileNumber()), Constants.EMPTY);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        List<TblBeneficiary> tblBeneficiaries = tblBeneficiaryRepo.findAllByTblCustomerCustomerId(tblCustomer.getCustomerId());

        if (tblBeneficiaries != null && !tblBeneficiaries.isEmpty()) {
            beneficaryResponses = tblBeneficiaries.parallelStream()
                    .map(tblBeneficiary -> {
                        BeneficaryResponse beneficaryResponse = new BeneficaryResponse();
                        beneficaryResponse.setBeneficaryAccountNo(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryAccountNo()));
                        beneficaryResponse.setBeneficaryAccountTitle(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryAccountTitle()));
                        beneficaryResponse.setBeneficaryAccountType(tblBeneficiary.getBeneficiaryAcctType());
                        beneficaryResponse.setBeneficaryBankImd(tblBeneficiary.getBeneficiaryImd());
                        beneficaryResponse.setBeneficaryEmail(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryEmail()));
                        beneficaryResponse.setBeneficaryMobileNumber(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryMobileNo()));
                        beneficaryResponse.setBeneficaryNickName(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryNickName()));
                        beneficaryResponse.setBeneficaryImdId(tblBeneficiary.getBeneficiaryImdId().longValue());
                        beneficaryResponse.setBeneficaryType(tblBeneficiary.getBeneficiaryType());
                        beneficaryResponse.setIsActive(tblBeneficiary.getIsActive());
                        beneficaryResponse.setBeneficaryId(tblBeneficiary.getBeneficiaryId());
                        return beneficaryResponse;
                    })
                    .collect(Collectors.toList());
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), beneficaryResponses);
    }

    @Override
    public HashMap<String, Object> updateFavPay(UpdateFavPayRequest updateFavPayRequest, BigDecimal userId, Request request) {
        TblBeneficiary tblBeneficiary = tblBeneficiaryRepo.findById(updateFavPayRequest.getBeneficaryId()).orElseThrow(() -> new CustomDataNotFoundException(
                GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        tblBeneficiary.setBeneficiaryAccountNo(aeSencryption.encryptwith256(updateFavPayRequest.getBeneficaryAccountNo()));
        tblBeneficiary.setBeneficiaryAccountTitle(aeSencryption.encryptwith256(updateFavPayRequest.getBeneficaryAccountTitle()));
        tblBeneficiary.setBeneficiaryAcctType(updateFavPayRequest.getBeneficaryAccountType());
        tblBeneficiary.setBeneficiaryEmail(aeSencryption.encryptwith256(updateFavPayRequest.getBeneficaryEmail()));
        tblBeneficiary.setBeneficiaryImd(updateFavPayRequest.getBeneficaryBankImd());
        BigDecimal bankImdId = tblBeneficiaryRepo.findBankImdIdByImd(updateFavPayRequest.getBeneficaryBankImd());
        tblBeneficiary.setBeneficiaryImdId(bankImdId);
        tblBeneficiary.setBeneficiaryMobileNo(aeSencryption.encryptwith256(updateFavPayRequest.getBeneficaryMobileNumber()));
        tblBeneficiary.setBeneficiaryNickName(aeSencryption.encryptwith256(updateFavPayRequest.getBeneficaryNickName()));
        tblBeneficiary.setBeneficiaryType(updateFavPayRequest.getBeneficaryType());
        tblBeneficiary.setLastupdatedate(new Date());
        tblBeneficiary.setLastupdateuser(userId);
        tblBeneficiary.setIsActive(updateFavPayRequest.getIsActive());
        tblBeneficiary.setUpdateindex(tblBeneficiary.getUpdateindex() == null ? BigDecimal.ONE : tblBeneficiary.getUpdateindex().add(BigDecimal.ONE));
        tblBeneficiaryRepo.saveAndFlush(tblBeneficiary);
        BeneficaryResponse beneficaryResponse = new BeneficaryResponse();
        beneficaryResponse.setBeneficaryAccountNo(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryAccountNo()));
        beneficaryResponse.setBeneficaryAccountTitle(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryAccountTitle()));
        beneficaryResponse.setBeneficaryAccountType(tblBeneficiary.getBeneficiaryAcctType());
        beneficaryResponse.setBeneficaryBankImd(tblBeneficiary.getBeneficiaryImd());
        beneficaryResponse.setBeneficaryEmail(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryEmail()));
        beneficaryResponse.setBeneficaryMobileNumber(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryMobileNo()));
        beneficaryResponse.setBeneficaryNickName(aeSencryption.decrypt(tblBeneficiary.getBeneficiaryNickName()));
        beneficaryResponse.setBeneficaryImdId(tblBeneficiary.getBeneficiaryImdId().longValue());
        beneficaryResponse.setBeneficaryType(tblBeneficiary.getBeneficiaryType());
        beneficaryResponse.setIsActive(tblBeneficiary.getIsActive());

        beneficaryResponse.setBeneficaryId(tblBeneficiary.getBeneficiaryId());

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), beneficaryResponse);

    }
}
