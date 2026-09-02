package com.wallet.transaction.repo;


import com.wallet.transaction.model.TblCustomerAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TblCustomerAllRepo extends JpaRepository<TblCustomerAll, Long> {

	TblCustomerAll findByMobileNo(String mobileno);

}
