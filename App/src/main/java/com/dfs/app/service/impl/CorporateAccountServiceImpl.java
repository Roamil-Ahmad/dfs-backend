package com.dfs.app.service.impl;

import com.dfs.app.dto.AccountDetailsRequest;
import com.dfs.app.dto.AccountDetailsResponse;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.TblAccount;
import com.dfs.app.model.TblCustomer;
import com.dfs.app.repo.TblAccountRepo;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.CorporateAccountService;
import com.dfs.app.util.AESencryption;
import com.dfs.app.util.CustomDataNotFoundException;
import com.dfs.app.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Describes one wallet for the Corporate Portal.
 *
 * <p>Looked up on ACCOUNT_NO, which carries the mobile number and is unique, so a mobile resolves
 * to at most one account.</p>
 */
@Service
public class CorporateAccountServiceImpl implements CorporateAccountService {

    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AESencryption aeSencryption;

    @Override
    public HashMap<String, Object> accountDetails(AccountDetailsRequest accountDetailsRequest, Request request) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(accountDetailsRequest.getMobileNumber().trim());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }

        AccountDetailsResponse response = new AccountDetailsResponse();
        response.setAccountNo(tblAccount.getAccountNo());
        response.setMobileNo(tblAccount.getMobileNo());
        response.setIban(tblAccount.getIban());
        response.setQrCode(tblAccount.getQrCode());
        response.setCurrentBalance(tblAccount.getCurrentBalance());
        // Held encrypted at rest, and the portal has no key of its own.
        response.setAccountTitle(decryptOrNull(tblAccount.getAccountTitle()));
        if (tblAccount.getLkpAccountStatus() != null) {
            response.setAccountStatusDescr(tblAccount.getLkpAccountStatus().getAccountStatusDescr());
        }

        // An agent account carries no customer, so the customer-held fields stay null rather than
        // failing the lookup.
        TblCustomer tblCustomer = tblAccount.getTblCustomer();
        if (tblCustomer != null) {
            response.setNidNo(decryptOrNull(tblCustomer.getNidNo()));
            response.setGender(tblCustomer.getGender());
            if (tblCustomer.getLkpSegment() != null) {
                response.setSegmentDescr(tblCustomer.getLkpSegment().getSegmentDescr());
            }
        }

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
    }

    /**
     * Decrypts a stored value, leaving null alone.
     *
     * <p>A row written before the column was encrypted would otherwise fail the whole lookup for
     * one unreadable field; it is returned as stored instead.</p>
     */
    private String decryptOrNull(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return aeSencryption.decrypt(value);
        } catch (Exception e) {
            return value;
        }
    }
}
