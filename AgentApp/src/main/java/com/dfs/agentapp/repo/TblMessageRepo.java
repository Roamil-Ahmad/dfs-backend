package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblMessageRepo extends JpaRepository<TblMessage,Long>
{
    @Query(value = "SELECT * FROM TBL_MESSAGE WHERE MESSAGE_CODE=:messageCode",nativeQuery = true)
    TblMessage findTblMessageByMessageCode(String messageCode);
}
