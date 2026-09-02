package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblUbpCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblUbpCompanyRepo extends JpaRepository<TblUbpCompany, Long> {

    List<TblUbpCompany> findByStatus(String y);
}
