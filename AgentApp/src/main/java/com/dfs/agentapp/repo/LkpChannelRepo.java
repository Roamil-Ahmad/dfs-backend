package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpChannelRepo extends JpaRepository<LkpChannel,Long> {
    LkpChannel findByChannelCodeAndIsActive(String channelCode,String isActive);
}
