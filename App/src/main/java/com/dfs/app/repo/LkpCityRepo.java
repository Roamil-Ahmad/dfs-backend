package com.dfs.app.repo;

import com.dfs.app.model.LkpCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpCityRepo extends JpaRepository<LkpCity, Long> {
    List<LkpCity> findAllByIsActive(String isActive);
}
