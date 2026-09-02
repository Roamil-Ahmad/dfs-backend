package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblComplaintRepo extends JpaRepository<TblComplaint,Long> {
}
