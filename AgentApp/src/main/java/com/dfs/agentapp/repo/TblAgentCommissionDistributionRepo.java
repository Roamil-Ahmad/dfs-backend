package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblAgentCommissionDistribution;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TBL_AGENT_COMMISSION_DISTRIBUTION - how a sub-agent's commission is shared with its parent.
 *
 * <p>Only corporate onboarding writes here, and only when the request supplies a parent agent and
 * a commission for it. Nothing in this service reads the table; the pricing service owns that.</p>
 */
public interface TblAgentCommissionDistributionRepo
        extends JpaRepository<TblAgentCommissionDistribution, Long> {
}
