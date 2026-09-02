package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpCurrencyRepo extends JpaRepository<LkpCurrency,Long> {

    List<LkpCurrency> findAllByIsActive(String yes);
}
