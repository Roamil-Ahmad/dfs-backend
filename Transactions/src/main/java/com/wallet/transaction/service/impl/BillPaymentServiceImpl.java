package com.wallet.transaction.service.impl;

import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.GetSavedBillsRequest;
import com.wallet.transaction.dto.LovResponse;
import com.wallet.transaction.dto.SaveTemplateRequest;
import com.wallet.transaction.dto.SavedTemplateResponse;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.TblAccount;
import com.wallet.transaction.model.TblBeneficiary;
import com.wallet.transaction.model.TblCustomer;
import com.wallet.transaction.model.TblUbpCompany;
import com.wallet.transaction.repo.*;
import com.wallet.transaction.service.BillPaymentService;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.service.TransactionsService;
import com.wallet.transaction.util.AESencryption;
import com.wallet.transaction.util.CustomDataNotFoundException;
import com.wallet.transaction.util.GenericResponseCode;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BillPaymentServiceImpl extends HelperClass implements BillPaymentService {
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblBeneficiaryRepo tblBeneficiaryRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblUbpCompanyRepo tblUbpCompanyRepo;
    @Autowired
    private TblAccountDebitCardRepo tblAccountDebitCardRepo;
    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Value("${account.type.wallet}")
    private String accountTypeWallet;

    @Override
    public HashMap<String, Object> getsavedbills(GetSavedBillsRequest getSavedBillsRequest, Request request, String authorization, BigDecimal userId) {
        TblCustomer tblCustomer = tblCustomerRepo.findByNidNo(aeSencryption.encryptwith256(getSavedBillsRequest.getNidNo()));

        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }
        List<SavedTemplateResponse> lovResponses;
        List<TblBeneficiary> tblBeneficiaryList = tblBeneficiaryRepo.getBeneficiaries(tblCustomer.getCustomerId());
        if (!isNullOrEmpty(tblBeneficiaryList)) {
            lovResponses = tblBeneficiaryList.parallelStream().map(p -> {
                SavedTemplateResponse lovResponse = new SavedTemplateResponse();
                lovResponse.setBeneficiaryId(p.getBeneficiaryId());
                TblUbpCompany tblUbpCompany = tblUbpCompanyRepo.findById(p.getBeneficiaryImdId().longValue()).orElse(new TblUbpCompany());
                lovResponse.setBillerName(tblUbpCompany.getName());
                lovResponse.setCardNo(Objects.requireNonNull(tblAccountDebitCardRepo.findById(p.getAccountDebitCardId().longValue()).orElse(null)).getPan());
                lovResponse.setTemplateName(p.getBeneficiaryNickName());
                lovResponse.setLinkCard("Link Card");
                lovResponse.setConsumerNo(p.getBeneficiaryAccountNo());
                lovResponse.setServiceId(tblUbpCompany.getCode());
                lovResponse.setExpiry(Objects.requireNonNull(tblAccountDebitCardRepo.findById(p.getAccountDebitCardId().longValue()).orElse(null)).getExpiryDate());
                lovResponse.setCvv(Objects.requireNonNull(tblAccountDebitCardRepo.findById(p.getAccountDebitCardId().longValue()).orElse(null)).getCvv());
                return lovResponse;
            }).collect(Collectors.toList());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
        }
        return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> savetemplate(SaveTemplateRequest getSavedBillsRequest, Request request, String authorization, BigDecimal userId) throws Exception {
        TblBeneficiary tblBeneficiary = tblBeneficiaryRepo.findByBeneficiaryAccountNoAndBeneficiaryType(getSavedBillsRequest.getConsumerNo(), "BP");
        if (tblBeneficiary != null) {
            throw new CustomDataNotFoundException(GenericResponseCode.RECORD_ALREADY_EXISTS.getResponseCode());
        }
        TblAccount tblAccount = tblAccountRepo.getAccountByCnicImeiAndAccountType(aeSencryption.encryptwith256(getSavedBillsRequest.getNidNo()), request.getImieNo(), accountTypeWallet);
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }
        String reultMpin = transactionsService.checkMpinValidation(tblAccount.getAccountNo(), getSavedBillsRequest.getMpin(), authorization, request.getImieNo());
        JSONObject jsonObject1 = new JSONObject(reultMpin);
        if (!jsonObject1.getString("responsecode").equalsIgnoreCase("000")) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_PIN.getResponseCode());
        }
        TblBeneficiary tblBeneficiary1 = new TblBeneficiary();
        tblBeneficiary1.setTblCustomer(tblAccount.getTblCustomer());
        tblBeneficiary1.setBeneficiaryAccountNo(getSavedBillsRequest.getConsumerNo());
        tblBeneficiary1.setBeneficiaryAccountTitle(getSavedBillsRequest.getBillerName());
        tblBeneficiary1.setBeneficiaryType("BP");
        tblBeneficiary1.setCreateuser(userId);
        tblBeneficiary1.setCreatedate(new Date());
        tblBeneficiary1.setBeneficiaryNickName(getSavedBillsRequest.getTemplateName());
        tblBeneficiary1.setBeneficiaryImdId(new BigDecimal(getSavedBillsRequest.getBillerId()));
        tblBeneficiary1.setBeneficiaryAcctType("B");
        tblBeneficiary1.setIsActive("Y");
        tblBeneficiary1.setAccountDebitCardId(new BigDecimal(getSavedBillsRequest.getAccountDebitCardId()));
        tblBeneficiaryRepo.save(tblBeneficiary1);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> getbiller() {
        List<LovResponse> lovResponses;
        List<TblUbpCompany> tblUbpCompanyList = tblUbpCompanyRepo.findByStatus("A");
        if (!isNullOrEmpty(tblUbpCompanyList)) {
            lovResponses = tblUbpCompanyList.parallelStream().map(p -> {
                LovResponse lovResponse = new LovResponse();
                lovResponse.setId(p.getUbpCompaniesId());
                lovResponse.setCode(p.getCode());
                lovResponse.setName(p.getName());
                return lovResponse;
            }).collect(Collectors.toList());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
        }
        return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

}
