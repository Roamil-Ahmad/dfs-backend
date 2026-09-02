package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpExpectedMonthlyVolume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpExpectedMonthlyVolumeRepo extends JpaRepository<LkpExpectedMonthlyVolume,Long> {
    List<LkpExpectedMonthlyVolume> findAllByIsActive(String isActive);
}
