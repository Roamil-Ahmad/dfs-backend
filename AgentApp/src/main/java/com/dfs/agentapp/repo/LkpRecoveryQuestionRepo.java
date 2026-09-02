package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpRecoveryQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpRecoveryQuestionRepo extends JpaRepository<LkpRecoveryQuestion,Long> {
    List<LkpRecoveryQuestion> findAllByIsActive(String isActive);
}
