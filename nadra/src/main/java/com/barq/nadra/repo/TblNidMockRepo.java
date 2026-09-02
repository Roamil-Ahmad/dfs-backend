package com.barq.nadra.repo;

import com.barq.nadra.model.TblNidMock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TblNidMockRepo extends JpaRepository<TblNidMock, String> {

    @Query(value = "SELECT * \n" +
            "   FROM TBL_NID_MOCK M\n" +
            "   WHERE M.NID_NO = :nidNo",nativeQuery = true)
    TblNidMock findByNidNo(@Param("nidNo") String nidNo);
}
