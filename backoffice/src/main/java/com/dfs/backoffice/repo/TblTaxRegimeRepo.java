package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblTaxRegime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblTaxRegimeRepo extends JpaRepository<TblTaxRegime,Long> {
    List<TblTaxRegime> findByIsActive(String yes);
}
