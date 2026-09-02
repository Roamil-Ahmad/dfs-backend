package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.dto.PricingProfileSearch;
import com.mfs.pricingprofile.model.TblTransCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblTransChargeRepo extends JpaRepository<TblTransCharge, Long> {

    @Query(value = "SELECT * FROM TBL_TRANS_CHARGES T\n" +
            "    INNER JOIN TBL_TRANS_CHARGES_DOCS D ON D.TRANS_CHARGES_ID = T.TRANS_CHARGES_ID\n" +
            "    WHERE T.CHARGES_PROFILE_NAME = NVL(:#{#pricingProfileSearch.chargesProfileName}, T.CHARGES_PROFILE_NAME)\n" +
            "    AND D.TRANS_DOCS_ID = NVL(:#{#pricingProfileSearch.transTypeId}, D.TRANS_DOCS_ID)\n" +
            "    AND CAST(T.CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(T.CREATEDATE AS DATE))\n" +
            "                                                  AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(T.CREATEDATE AS DATE))", nativeQuery = true)
    List<TblTransCharge> getAllTblTransCharge(PricingProfileSearch pricingProfileSearch, String dateFromInput, String dateToInput);
}
