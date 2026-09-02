package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblTransDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblTransDocRepo extends JpaRepository<TblTransDoc,Long> {

    @Query(value = "SELECT * FROM TBL_TRANS_DOCS WHERE TRANS_TYPE_ID =1 AND IS_ACTIVE= 'Y'",nativeQuery = true)
    List<TblTransDoc> getFinancialTransDocs();

    @Query(value = "SELECT * FROM TBL_TRANS_DOCS WHERE END_POINT =:endPoint AND IS_ACTIVE ='Y'",nativeQuery = true)
    TblTransDoc getTblTransDocByEndPoint(String endPoint);
}
