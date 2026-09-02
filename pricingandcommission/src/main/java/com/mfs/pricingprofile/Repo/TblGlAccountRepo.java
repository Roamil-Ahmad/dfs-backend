package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblGlAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblGlAccountRepo extends JpaRepository<TblGlAccount,Long> {
    List<TblGlAccount> findByIsActive(String y);
}
