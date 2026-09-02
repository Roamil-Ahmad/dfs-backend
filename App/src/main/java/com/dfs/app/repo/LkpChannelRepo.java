package com.dfs.app.repo;

import com.dfs.app.model.LkpChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpChannelRepo extends JpaRepository<LkpChannel,Long> {
    LkpChannel findByChannelCodeAndIsActive(String channelCode,String isActive);
}
