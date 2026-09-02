package com.dfs.app.repo;

import com.dfs.app.model.TblComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblComplaintRepo extends JpaRepository<TblComplaint,Long> {
}
