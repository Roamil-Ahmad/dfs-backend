package com.dfs.app.repo;

import com.dfs.app.model.TblTutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblTutorialRepo extends JpaRepository<TblTutorial,Long> {

    List<TblTutorial> findByIsActive(String status);
}
