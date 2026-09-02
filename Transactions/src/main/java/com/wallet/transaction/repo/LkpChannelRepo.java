package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * LKP_CHANNEL lookup. The channel arrives on the request envelope as a code (AGNT, MOB, WEB, ...)
 * and the stored procedures want the numeric id, so the translation happens here rather than the
 * caller sending an id it would have to know.
 */
@Repository
public interface LkpChannelRepo extends JpaRepository<LkpChannel, Long> {

    LkpChannel findByChannelCodeAndIsActive(String channelCode, String isActive);
}
