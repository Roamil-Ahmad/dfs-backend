package com.dfs.agentapp.repo;


import com.dfs.agentapp.model.TblCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblCustomerRepo extends JpaRepository<TblCustomer, Long> {



	@Query(value = "SELECT C.* FROM TBL_CUSTOMER C INNER JOIN TBL_CUSTOMER_ALL A on A.CUSTOMER_ALL_ID=C.CUSTOMER_ALL_ID WHERE C.NID_NO=:nidNumber OR A.MOBILE_NO=:mobileNumber",nativeQuery = true)
	TblCustomer findByMobileNumberOrNidNo(String mobileNumber, String nidNumber);
	@Query(value = "SELECT * FROM TBL_CUSTOMER WHERE CUSTOMER_ID=:customerId AND IS_ACTIVE=:isActive",nativeQuery = true)
	TblCustomer findByIdAndIsActive(Long customerId, String isActive);

	TblCustomer findByCustomerId(Long customerid);
}
