package com.workflow.repo;

import com.workflow.modal.LkpMcFormName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpMcFormNameRepo extends JpaRepository<LkpMcFormName,Long> {
    List<LkpMcFormName> findByIsActive(String y);
}
