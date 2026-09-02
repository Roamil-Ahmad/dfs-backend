package com.dfs.app.repo;

import com.dfs.app.model.TblTransDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblTransDocRepo extends JpaRepository<TblTransDoc,Long> {
@Query(value = "SELECT * FROM TBL_TRANS_DOCS WHERE END_POINT =:endPoint AND IS_ACTIVE ='Y'",nativeQuery = true)
    TblTransDoc getTblTransDocByEndPoint(String endPoint);
}
