package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblBeneficiaryRepo extends JpaRepository<TblBeneficiary,Long> {
    @Query(value = "SELECT * FROM TBL_BENEFICIARY WHERE CUSTOMER_ID = :customerId AND BENEFICIARY_ACCT_TYPE = 'B'",nativeQuery = true)
    List<TblBeneficiary> getBeneficiaries(long customerId);

    TblBeneficiary findByBeneficiaryAccountNoAndBeneficiaryType(String consumerNo, String bp);
}
