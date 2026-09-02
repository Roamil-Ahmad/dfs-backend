package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.CashInInquiryRequest;
import com.dfs.agentapp.dto.CashInInquiryResponse;
import com.dfs.agentapp.dto.MpinVerificationRequest;
import com.dfs.agentapp.dto.ViewLimitResponse;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.TblAccount;
import com.dfs.agentapp.model.TblCustomer;
import com.dfs.agentapp.repo.TblAccountRepo;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.CustomerAccountDetailService;
import com.dfs.agentapp.util.AESencryption;
import com.dfs.agentapp.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;

@Service
public class CustomerAccountDetailServiceImpl implements CustomerAccountDetailService {

  @Autowired
  private TblAccountRepo tblAccountRepo;

  @Autowired
  private CommonService commonService;

  @Autowired
  private AESencryption aeSencryption;

  /** LKP_ACCOUNT_TYPE code for an individual wallet; an agent account must not match here. */
  @Value("${account.type.individual.code}")
  private String accountTypeIndividual;

  @Override
  public HashMap<String, Object> accountDetail(MpinVerificationRequest mpinVerificationRequest, Request request) {


    Object object = tblAccountRepo.accountDetail(mpinVerificationRequest.getMobileNumber());
    if (object == null) {
      return commonService.getResponse(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode(), null);
    }
    Object[] result = (Object[]) object;
    HashMap<String, Object> map = new HashMap<>();
    map.put("accountNumber", result[0]);
    map.put("accountTitle", aeSencryption.decrypt(String.valueOf(result[1])));
    map.put("accountLevel", result[2]);

    return map;
  }

  /**
   * Depositor lookup for agent cash-in.
   *
   * <p>The account has to be a customer account - an agent account, or an account of any other
   * type, is reported as not found rather than returned. The CNIC has to belong to that same
   * account: without that check the endpoint would let an agent read back the name and date of
   * birth of any account holder from the account number alone.</p>
   *
   * <p>Nothing is created or updated here; it is a read used to fill the cash-in request.</p>
   */
  @Override
  public HashMap<String, Object> cashInInquiry(CashInInquiryRequest cashInInquiryRequest, Request request) {

    TblAccount tblAccount = tblAccountRepo.findByAccountNoAndAccountTypeCode(
            cashInInquiryRequest.getAccountNo(), accountTypeIndividual);
    if (tblAccount == null || tblAccount.getTblCustomer() == null) {
      return commonService.getResponse(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode(), null);
    }

    TblCustomer tblCustomer = tblAccount.getTblCustomer();

    // Stored encrypted, so the supplied CNIC is encrypted for the comparison rather than the
    // stored one being decrypted.
    String suppliedNid = aeSencryption.encryptwith256(cashInInquiryRequest.getDepositorNid());
    if (tblCustomer.getNidNo() == null || !tblCustomer.getNidNo().equals(suppliedNid)) {
      return commonService.getResponse(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode(), null);
    }

    CashInInquiryResponse response = new CashInInquiryResponse();
    response.setAccountNo(tblAccount.getAccountNo());
    response.setName(aeSencryption.decrypt(tblCustomer.getFullName()));
    response.setDob(tblCustomer.getDob() == null
            ? null
            : new SimpleDateFormat("yyyy-MM-dd").format(tblCustomer.getDob()));

    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
  }
}
