package com.dfs.app.repo;


import com.dfs.app.model.TblCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TblCustomerRepo extends JpaRepository<TblCustomer, Long> {



	@Query(value = " SELECT C.* FROM TBL_ACCOUNT A\n" +
			"            INNER JOIN TBL_CUSTOMER C ON A.CUSTOMER_ID=C.CUSTOMER_ID \n" +
			"            WHERE A.ACCOUNT_NO=:accountNo OR C.NID_NO=:nidNumber AND C.IS_ACTIVE='Y'",nativeQuery = true)
	TblCustomer findByAccountNoOrNidNo(String accountNo, String nidNumber);
	@Query(value = "SELECT * FROM TBL_CUSTOMER WHERE CUSTOMER_ID=:customerId AND IS_ACTIVE=:isActive",nativeQuery = true)
	TblCustomer findByIdAndIsActive(Long customerId,String isActive);
	@Query(value = "SELECT C.* FROM TBL_CUSTOMER C INNER JOIN TBL_CUSTOMER_ALL A " +
			"on A.CUSTOMER_ALL_ID=C.CUSTOMER_ALL_ID" +
			" WHERE C.NID_NO=:nidNumber OR A.MOBILE_NO=:mobileNumber AND C.IS_ACTIVE='Y'",nativeQuery = true)
	TblCustomer findByMobileNumberOrNidNo(String mobileNumber, String nidNumber);

	TblCustomer findByCustomerId(Long customerid);
	@Query(value = "SELECT * FROM TBL_CUSTOMER WHERE EMAIL=:email",nativeQuery = true)
	List<TblCustomer> findByEmail(String email);
}
