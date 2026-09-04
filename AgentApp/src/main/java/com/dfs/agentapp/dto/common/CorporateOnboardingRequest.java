package com.dfs.agentapp.dto.common;

import com.dfs.agentapp.dto.Partner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Payload for the combined agent onboarding endpoint.
 *
 * <p>Carries everything the three step-by-step calls needed between them:</p>
 * <ul>
 *   <li>the device fields {@code agentDeviceRegistration} took - added here</li>
 *   <li>every KYC field {@code agentkyc} took - inherited unchanged from
 *       {@link AgentKycRequest}, so the two stay in step automatically</li>
 *   <li>the partners list, which only this endpoint accepts</li>
 * </ul>
 *
 * <p>Partners live here rather than on {@link AgentKycRequest} deliberately: {@code agentkyc}
 * keeps exactly the contract and behaviour it had, and only onboarding creates an app user per
 * partner.</p>
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CorporateOnboardingRequest extends AgentKycRequest {

    // ---- device registration fields, from the agentDeviceRegistration payload ----

    private String appVersion;
    private String deviceModel;
    private String imeiNo;
    private String ipAddressP;
    private String ipAddressA;

    /**
     * Partners on a corporate agent account. Optional and unbounded: none means the agent keeps a
     * single app user, and any number given produces one app user each.
     */
    private List<Partner> partners;

    /**
     * Commission percentage the parent agent earns on this agent, written to
     * TBL_AGENT_COMMISSION_DISTRIBUTION.
     *
     * <p>Only meaningful alongside {@code parentAgentId} - a standalone agent has no parent to
     * pay. Left out, no distribution row is written and onboarding behaves exactly as before.</p>
     */
    private String parentCommission;
}
