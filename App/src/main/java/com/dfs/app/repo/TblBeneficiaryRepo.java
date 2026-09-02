package com.dfs.app.repo;

import com.dfs.app.model.TblBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblBeneficiaryRepo extends JpaRepository<TblBeneficiary,Long> {

    @Query(value = "SELECT i.IMD_LIST_ID FROM TBL_IMD_LIST i WHERE i.IMD_NO =:beneficaryBankImd",nativeQuery = true)
    BigDecimal findBankImdIdByImd(String beneficaryBankImd);
    List<TblBeneficiary> findAllByTblCustomerCustomerId(long customerId);
    TblBeneficiary findByTblCustomerCustomerIdAndBeneficiaryAccountNoAndBeneficiaryType(long customerId, String beneficaryAccountNo, String beneficaryType);

    List<TblBeneficiary> findAllByTblCustomerCustomerIdAndIsActive(long customerId, String yes);
}
