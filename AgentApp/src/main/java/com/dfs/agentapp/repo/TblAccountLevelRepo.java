package com.dfs.agentapp.repo;


import com.dfs.agentapp.model.TblAccountLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAccountLevelRepo extends JpaRepository<TblAccountLevel, Long> {

	TblAccountLevel findByAccountLevelCode(String code);

}
