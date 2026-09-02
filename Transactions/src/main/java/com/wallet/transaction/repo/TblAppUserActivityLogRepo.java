package com.wallet.transaction.repo;


import com.wallet.transaction.model.TblAppUserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAppUserActivityLogRepo extends JpaRepository<TblAppUserActivityLog,Long> {
}
