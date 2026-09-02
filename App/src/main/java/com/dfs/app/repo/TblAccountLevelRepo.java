package com.dfs.app.repo;


import com.dfs.app.model.TblAccount;
import com.dfs.app.model.TblAccountLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAccountLevelRepo extends JpaRepository<TblAccountLevel, Long> {

	TblAccountLevel findByAccountLevelCode(String code);

}
