package com.dfs.app.repo;

import com.dfs.app.model.TblBulkAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TBL_BULK_ACCOUNTS - accounts submitted in bulk by the Corporate Portal.
 *
 * <p>Write only from this service; the rows are read by whatever picks the batch up.</p>
 */
public interface TblBulkAccountRepo extends JpaRepository<TblBulkAccount, Long> {
}
