package com.dfs.app.repo;

import com.dfs.app.model.TblBulkAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * TBL_BULK_ACCOUNTS - accounts submitted in bulk by the Corporate Portal.
 *
 * <p>Write only from this service; the rows are read by whatever picks the batch up.</p>
 */
public interface TblBulkAccountRepo extends JpaRepository<TblBulkAccount, Long> {

    /**
     * The bulk rows submitted for a mobile number and identity number.
     *
     * <p>Values are stored as the portal sent them, so both are compared trimmed. Nothing stops the
     * same pair being submitted twice, so this returns a list, newest first - the most recent
     * submission is the one that describes the customer being onboarded now.</p>
     */
    @Query("SELECT b FROM TblBulkAccount b WHERE TRIM(b.mobileNo) = :mobileNo "
            + "AND TRIM(b.nidNo) = :nidNo ORDER BY b.bulkAccountId DESC")
    List<TblBulkAccount> findByMobileNoAndNidNo(@Param("mobileNo") String mobileNo,
                                                @Param("nidNo") String nidNo);
}
