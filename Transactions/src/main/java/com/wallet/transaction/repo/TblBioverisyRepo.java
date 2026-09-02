package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblBioverisy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblBioverisyRepo extends JpaRepository<TblBioverisy,Long> {

}
