package com.dfs.agentapp.repo;


import com.dfs.agentapp.model.TblCustomerAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TblCustomerAllRepo extends JpaRepository<TblCustomerAll, Long> {

	@Query(value = "SELECT * FROM TBL_CUSTOMER_ALL WHERE MOBILE_NO =:mobileno",nativeQuery = true)
	TblCustomerAll findByMobileNo(String mobileno);
	


	@Modifying
	@Query(value = "UPDATE TBL_CUSTOMER_ALL SET VERIFIED = 'I' WHERE MOBILE_NO = :mobileNo", nativeQuery = true)
	int updateCustomerAllMobileNo(@Param("mobileNo") String mobileNo);
	@Query(value = "SELECT CA.* FROM TBL_APP_USER A \n" +
			"INNER JOIN TBL_AGENT C ON C.AGENT_ID=A.AGENT_ID \n" +
			"INNER JOIN TBL_CUSTOMER_ALL CA ON C.CUSTOMER_ALL_ID=CA.CUSTOMER_ALL_ID\n" +
			"WHERE A.USERNAME=:username",nativeQuery = true)
	TblCustomerAll findByUsername(String username);

	TblCustomerAll findByImeiNo(String imieNo);

//	@Query(value = "select a.* from tbl_customer_all a inner join tbl_customer c on a.customer_all_id = c.customer_all_id where a.tmsverified = 'N' and c.createuser = 51", nativeQuery = true)
//	List<TblCustomerAll> findTmsNonVerifiedCustomerAll();

}
