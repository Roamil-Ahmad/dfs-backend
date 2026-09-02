package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblAccountDebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblAccountDebitCardRepo extends JpaRepository<TblAccountDebitCard,Long> {

    List<TblAccountDebitCard> findAllByTblAccountAccountIdAndIsActive(long accountId,String isActive);
    @Query(value = "SELECT * FROM TBL_ACCOUNT_DEBIT_CARD WHERE ACCOUNT_ID=:accountId AND PAN=:pan AND ACCOUNT_NO=:accountNo AND IS_ACTIVE=:isActive",nativeQuery = true)
    TblAccountDebitCard findByAccountIdPanAccountNoIsActive(long accountId,String pan,String accountNo,String isActive);

}
