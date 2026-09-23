package com.dfs.app.service.impl;

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
import java.util.Date;
import java.util.HashMap;

/**
 * Stores an account the Corporate Portal submits, as one row of TBL_BULK_ACCOUNTS.
 *
 * <p>One call, one row. The segment comes from the request envelope rather than the payload,
 * because {@code segment} is the field the platform already carries for it.</p>
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
        TblBulkAccount row = new TblBulkAccount();
        row.setMobileNo(trimToNull(bulkAccountRequest.getMobileNo()));
        row.setAccountTitle(trimToNull(bulkAccountRequest.getAccountTitle()));
        row.setNidNo(trimToNull(bulkAccountRequest.getNidNo()));
        // SEGMENT_DESCR comes from the envelope's segment, not from the payload.
        row.setSegmentDescr(trimToNull(request.getSegment()));
        // BULK_ACCOUNT_ID comes from TBL_BULK_ACCOUNTS_SEQ through the entity mapping.
        row.setCreateuser(BigDecimal.ONE);
        row.setCreatedate(new Date());

        TblBulkAccount saved = tblBulkAccountRepo.save(row);

        HashMap<String, Object> data = new HashMap<>();
        data.put("bulkAccountId", saved.getBulkAccountId());
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
