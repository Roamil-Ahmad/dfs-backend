package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpDebitCardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpDebitCardTypeRepo extends JpaRepository<LkpDebitCardType,Long> {
    LkpDebitCardType findByDebitCardTypeCode(String cardType);
}
