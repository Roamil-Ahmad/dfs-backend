package com.wallet.transaction.repo;


import com.wallet.transaction.model.TblCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblCustomerRepo extends JpaRepository<TblCustomer, Long> {
    @Query(value = " SELECT * FROM TBL_CUSTOMER \n" +
            "            WHERE NID_NO=:nidNumber AND IS_ACTIVE='Y'",nativeQuery = true)
    TblCustomer findByNidNo(String nidNumber);

    /**
     * The customer who owns {@code accountNo} and whose national ID is {@code nidNo}, where
     * {@code nidNo} is the AES value as stored - NID_NO holds ciphertext, never the plain number.
     *
     * <p>Both must belong to the same row: that is what proves the caller supplied the account's
     * own national ID rather than someone else's.</p>
     *
     * <p>Customer status is deliberately not filtered here. PKG_MW checks it itself and has
     * distinct codes for a blocked or inactive customer; screening those out first would collapse
     * every one of them into a single "not found" and lose the reason.</p>
     */
    @Query(value = "SELECT C.* FROM TBL_CUSTOMER C "
            + "INNER JOIN TBL_ACCOUNT A ON A.CUSTOMER_ID = C.CUSTOMER_ID "
            + "WHERE TRIM(A.ACCOUNT_NO) = TRIM(:accountNo) AND C.NID_NO = :nidNo",
            nativeQuery = true)
    TblCustomer findByAccountNoAndNidNo(@Param("accountNo") String accountNo,
                                        @Param("nidNo") String nidNo);
}
