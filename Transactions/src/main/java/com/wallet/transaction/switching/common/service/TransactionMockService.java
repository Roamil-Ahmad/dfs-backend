package com.wallet.transaction.switching.common.service;

import com.wallet.transaction.switching.common.SwitchTransactionType;
import com.wallet.transaction.switching.common.model.TblTransactionMock;
import com.wallet.transaction.switching.common.repo.TblTransactionMockRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Resolves beneficiary details for a switched transaction from TBL_TRANSACTION_MOCK.
 *
 * Nothing about a beneficiary is ever hardcoded or invented: if the table has no row for the
 * account, the caller is told and the flow stops.
 */
@Service
public class TransactionMockService {

    private static final Logger log = LoggerFactory.getLogger(TransactionMockService.class);

    @Autowired
    private TblTransactionMockRepo tblTransactionMockRepo;

    /**
     * @return the mock record, or {@code null} when the beneficiary account is not registered for
     *         this transaction type
     */
    public TblTransactionMock find(SwitchTransactionType type, String beneficiaryAccountNo) {
        if (beneficiaryAccountNo == null || beneficiaryAccountNo.isBlank()) {
            log.warn("Mock lookup skipped: no beneficiary account supplied | type:{}", type.getValue());
            return null;
        }
        TblTransactionMock record = tblTransactionMockRepo
                .findByTransactionTypeAndBeneficiaryAccountNo(type.getValue(), beneficiaryAccountNo.trim());

        if (record == null) {
            log.warn("No TBL_TRANSACTION_MOCK row | TRANSACTION_TYPE:{} | BENEFICIARY_ACCOUNT_NO:{}",
                    type.getValue(), beneficiaryAccountNo);
        } else {
            log.info("Beneficiary resolved from TBL_TRANSACTION_MOCK | TRANSACTION_TYPE:{} | id:{} | bank:{} | imd:{}",
                    type.getValue(), record.getTransactionMockId(), record.getBeneficiaryBankName(), record.getImdNo());
        }
        return record;
    }

    /**
     * Resolves a bill from the utility columns of TBL_TRANSACTION_MOCK.
     *
     * As with a beneficiary, nothing about a bill is invented: no row means the consumer number is
     * not registered with that company, and the flow stops there.
     *
     * @return the mock bill, or {@code null} when there is no such bill
     */
    public TblTransactionMock findBill(SwitchTransactionType type, String utilityCompanyCode,
                                       String utilityConsumerId) {
        if (utilityCompanyCode == null || utilityCompanyCode.isBlank()
                || utilityConsumerId == null || utilityConsumerId.isBlank()) {
            log.warn("Bill lookup skipped: company code or consumer number missing | type:{}", type.getValue());
            return null;
        }
        TblTransactionMock record = tblTransactionMockRepo.findBillByCompanyCodeAndConsumerId(
                type.getValue(), utilityCompanyCode.trim(), utilityConsumerId.trim());

        if (record == null) {
            log.warn("No TBL_TRANSACTION_MOCK bill | TRANSACTION_TYPE:{} | UTILITY_COMPANY_CODE:{} | consumer:{}",
                    type.getValue(), utilityCompanyCode, utilityConsumerId);
        } else {
            log.info("Bill resolved from TBL_TRANSACTION_MOCK | TRANSACTION_TYPE:{} | id:{} | company:{} | paid:{}",
                    type.getValue(), record.getTransactionMockId(), record.getUtilityCompanyName(),
                    record.getBillPaid());
        }
        return record;
    }
}
