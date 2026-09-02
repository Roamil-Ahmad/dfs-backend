package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpSegmentRepo extends JpaRepository<LkpSegment, Long> {

    List<LkpSegment> findAllByIsActive(String isActive);
}
