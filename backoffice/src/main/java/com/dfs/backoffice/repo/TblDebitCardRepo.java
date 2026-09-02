package com.dfs.backoffice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.dfs.backoffice.model.TblAccount; // Using a dummy entity as JpaRepository needs one

import java.util.List;

@Repository
public interface TblDebitCardRepo extends JpaRepository<TblAccount, Long> {

    @Query(value = "SELECT ACCOUNT_ID, DAILY_DR_TOTAL, MONTHLY_DR_TOTAL, YEARLY_DR_TOTAL, DAILY_CR_TOTAL, MONTHLY_CR_TOTAL, YEARLY_CR_TOTAL, DAILY_DR_REMAINING, MONTHLY_DR_REMAINING, YEARLY_DR_REMAINING, DAILY_CR_REMAINING, MONTHLY_CR_REMAINING, YEARLY_CR_REMAINING FROM VW_AVAILABLE_LIMITS WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    Object viewLimits(long accountId);

    @Query(value = "SELECT CA.ACCOUNT_NUM, C.PAN, C.CARD_TITLE, C.EXPIRY_DATE, CARD_STATUS_NAME, L.MAX_LIMIT, L.SINGLE_TRAN_LIMIT, CASE WHEN TO_CHAR(A.CYCLE_BEGIN_DATE, 'DD-MM-YYYY') <> TO_CHAR(SYSDATE, 'DD-MM-YYYY') THEN 0 ELSE NVL(A.AVAILABLE_LIMIT,0) END DAILY_AVAILABLE_SPENDING, CASE WHEN TO_CHAR(A.CYCLE_BEGIN_DATE, 'MM-YYYY') <> TO_CHAR(SYSDATE, 'MM-YYYY') THEN 0 ELSE NVL(A.AVAILABLE_LIMIT,0) END MONTHLY_AVAILABLE_SPENDING FROM CMS.CARD_ACCOUNT CA INNER JOIN CMS.CARD C ON CA.PAN = C.PAN INNER JOIN CMS.CARD_STATUS S ON C.CARD_STATUS_CODE = S.CARD_STATUS_CODE INNER JOIN CMS.CARD_LIMIT_PROFILE L ON C.LIMIT_PROFILE = L.PROFILE_ID LEFT JOIN CMS.CARD_LIMIT_ACTUAL A ON C.PAN = A.PAN WHERE CA.ACCOUNT_NUM = :pan OR C.PAN = :pan", nativeQuery = true)
    List<Object[]> fetchCardLimits(String pan);
}
