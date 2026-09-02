package com.wallet.transaction.switching.common.repo;

import com.wallet.transaction.switching.common.model.TblTransactionMock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Read-only access to TBL_TRANSACTION_MOCK.
 *
 * There is no write method on purpose: the table is reference data owned by the database team.
 */
@Repository
public interface TblTransactionMockRepo extends JpaRepository<TblTransactionMock, java.math.BigDecimal> {

    /**
     * Looks up the mock record for one flow and one beneficiary account.
     *
     * The account is matched after trimming so a padded IBAN and the same IBAN typed by the user
     * resolve to the same row.
     */
    @Query(value = "SELECT * \n"
            + "  FROM TBL_TRANSACTION_MOCK M\n"
            + " WHERE M.TRANSACTION_TYPE = :transactionType\n"
            + "   AND TRIM(M.BENEFICIARY_ACCOUNT_NO) = TRIM(:beneficiaryAccountNo)",
            nativeQuery = true)
    TblTransactionMock findByTransactionTypeAndBeneficiaryAccountNo(
            @Param("transactionType") String transactionType,
            @Param("beneficiaryAccountNo") String beneficiaryAccountNo);

    /**
     * Looks up the mock bill for one utility company and one consumer number.
     *
     * The same table serves bill payment through its utility columns: UTILITY_COMPANY_CODE,
     * UTILITY_CONSUMER_ID, UTILITY_COMPANY_NAME, BILL_AMOUNT, BILL_DUE_DATE and BILL_PAID.
     * Trimmed on both sides so a padded consumer number resolves to the same row.
     */
    @Query(value = "SELECT * \n"
            + "  FROM TBL_TRANSACTION_MOCK M\n"
            + " WHERE M.TRANSACTION_TYPE = :transactionType\n"
            + "   AND TRIM(M.UTILITY_COMPANY_CODE) = TRIM(:utilityCompanyCode)\n"
            + "   AND TRIM(M.UTILITY_CONSUMER_ID) = TRIM(:utilityConsumerId)",
            nativeQuery = true)
    TblTransactionMock findBillByCompanyCodeAndConsumerId(
            @Param("transactionType") String transactionType,
            @Param("utilityCompanyCode") String utilityCompanyCode,
            @Param("utilityConsumerId") String utilityConsumerId);
}
