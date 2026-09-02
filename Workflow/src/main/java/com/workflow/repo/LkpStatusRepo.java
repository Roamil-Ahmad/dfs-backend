package com.workflow.repo;

import com.workflow.modal.LkpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpStatusRepo extends JpaRepository<LkpStatus, Long> {
    List<LkpStatus> findByIsActive(String y);
}
