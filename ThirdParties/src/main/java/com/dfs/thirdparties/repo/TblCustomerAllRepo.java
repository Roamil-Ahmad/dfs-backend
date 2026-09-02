package com.dfs.thirdparties.repo;




import com.dfs.thirdparties.model.TblCustomerAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TblCustomerAllRepo extends JpaRepository<TblCustomerAll, Long> {

	TblCustomerAll findByMobileNo(String mobileno);
	


	@Modifying
	@Query(value = "UPDATE TBL_CUSTOMER_ALL SET VERIFIED = 'I' WHERE MOBILE_NO = :mobileNo", nativeQuery = true)
	int updateCustomerAllMobileNo(@Param("mobileNo") String mobileNo);

//	@Query(value = "select a.* from tbl_customer_all a inner join tbl_customer c on a.customer_all_id = c.customer_all_id where a.tmsverified = 'N' and c.createuser = 51", nativeQuery = true)
//	List<TblCustomerAll> findTmsNonVerifiedCustomerAll();

}
