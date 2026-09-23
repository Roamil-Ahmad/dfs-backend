package com.dfs.app.service.impl;

import com.dfs.app.dto.BulkAccount;
import com.dfs.app.dto.BulkAccountRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.TblBulkAccount;
import com.dfs.app.repo.TblBulkAccountRepo;
import com.dfs.app.service.BulkAccountService;
import com.dfs.app.service.CommonService;
import com.dfs.app.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores accounts the Corporate Portal submits in bulk.
 *
 * <p>Every row of a submission is written or none of them is: the whole batch is one transaction,
 * so a row that fails half way through does not leave the earlier ones behind for the portal to
 * reconcile against. Resubmitting a corrected batch is then a clean retry.</p>
 */
@Service
public class BulkAccountServiceImpl implements BulkAccountService {

    @Autowired
    private TblBulkAccountRepo tblBulkAccountRepo;
    @Autowired
    private CommonService commonService;

    @Override
    @Transactional
    public HashMap<String, Object> saveBulkAccounts(BulkAccountRequest bulkAccountRequest, Request request) {
        List<TblBulkAccount> rows = new ArrayList<>();
        Date now = new Date();
        for (BulkAccount account : bulkAccountRequest.getAccounts()) {
            TblBulkAccount row = new TblBulkAccount();
            row.setMobileNo(trimToNull(account.getMobileNo()));
            row.setAccountTitle(trimToNull(account.getAccountTitle()));
            row.setNidNo(trimToNull(account.getNidNo()));
            row.setSegmentDescr(trimToNull(account.getSegmentDescr()));
            // BULK_ACCOUNT_ID comes from TBL_BULK_ACCOUNTS_SEQ through the entity mapping.
            row.setCreateuser(BigDecimal.ONE);
            row.setCreatedate(now);
            rows.add(row);
        }
        List<TblBulkAccount> saved = tblBulkAccountRepo.saveAll(rows);

        HashMap<String, Object> data = new HashMap<>();
        data.put("savedCount", saved.size());
        data.put("bulkAccountIds", saved.stream().map(TblBulkAccount::getBulkAccountId).collect(Collectors.toList()));
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), data);
    }

    /**
     * Surrounding spaces are never part of a mobile number or an identity number, and an entry that
     * is only spaces is not a value at all - it is stored as null rather than as blanks.
     */
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
