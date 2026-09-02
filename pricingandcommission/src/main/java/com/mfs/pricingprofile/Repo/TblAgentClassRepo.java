package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblAgentClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblAgentClassRepo extends JpaRepository<TblAgentClass,Long> {
    List<TblAgentClass> findByIsActive(String y);
}
