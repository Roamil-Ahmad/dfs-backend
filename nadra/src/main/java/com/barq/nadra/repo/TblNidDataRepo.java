package com.barq.nadra.repo;


import com.barq.nadra.model.TblNidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TblNidDataRepo extends JpaRepository<TblNidData, Long> {

    @Query(value = "SELECT * \n" +
            "   FROM TBL_NID_DATA A\n" +
            "   WHERE  A.NID_NO = NVL(:nidNo,'') AND TO_CHAR(TRUNC(A.ISSUANCE_DATE), 'DD-MM-YYYY')=:cnicIssuenceDate",nativeQuery = true)
    TblNidData findByNidNoAndIssuanceDate(@Param("nidNo") String nidNo, @Param("cnicIssuenceDate") String cnicIssuenceDate);

    @Query(value = "SELECT * \n" +
            "   FROM TBL_NID_DATA A\n" +
            "   WHERE  A.NID_NO = NVL(:nidNo,'')",nativeQuery = true)
    TblNidData findByNidNo(@Param("nidNo") String nidNo);
}
