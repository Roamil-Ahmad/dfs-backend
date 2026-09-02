package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblBeneficiaryRepo extends JpaRepository<TblBeneficiary,Long> {

    @Query(value = "SELECT i.IMD_LIST_ID FROM TBL_IMD_LIST i WHERE i.IMD_NO =:beneficaryBankImd",nativeQuery = true)
    BigDecimal findBankImdIdByImd(String beneficaryBankImd);

    List<TblBeneficiary> findAllByTblCustomerCustomerId(long customerId);
}
