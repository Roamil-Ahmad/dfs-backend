package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.dto.CommissionProfileSearch;
import com.mfs.pricingprofile.model.TblCommissionProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblCommissionProfileRepo extends JpaRepository<TblCommissionProfile, Long> {

  @Query(value = "SELECT * FROM TBL_COMMISSION_PROFILE C\n" +
          "    INNER JOIN TBL_COMMISSION_DOCS D ON D.COMMISSION_PROFILE_ID = C.COMMISSION_PROFILE_ID\n" +
          "    WHERE C.COMMISSION_PROFILE_NAME = NVL(:#{#commissionProfileSearch.chargesProfileName}, C.COMMISSION_PROFILE_NAME)\n" +
          "        AND D.TRANS_DOCS_ID = NVL(:#{#commissionProfileSearch.transTypeId}, D.TRANS_DOCS_ID)\n" +
          "        AND CAST(C.CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(C.CREATEDATE AS DATE))\n" +
          "        AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(C.CREATEDATE AS DATE))", nativeQuery = true)
  List<TblCommissionProfile> getAllCommissionProfiles(CommissionProfileSearch commissionProfileSearch, String dateFromInput, String dateToInput);
}
