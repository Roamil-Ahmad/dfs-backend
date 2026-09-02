package com.wallet.transaction.switching.common;

import com.wallet.transaction.model.TblGlobalConfig;
import com.wallet.transaction.repo.TblGlobalConfigRepo;
import com.wallet.transaction.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Whether the switch gateway should be called at all, read from TBL_GLOBAL_CONFIG.
 *
 * <p>The row is KEY_NAME = 'SWITCH'. 'Y' means talk to 1LINK through the gateway; anything else
 * means the gateway is not there and every flow runs its database side only. That is the state
 * while the gateway service is not deployed.</p>
 *
 * <p>Read per call rather than cached at startup, so the flag can be flipped without restarting
 * the service - which is the reason for keeping it in the database instead of a property.</p>
 *
 * <p>A missing row counts as off. Defaulting the other way would have a misconfigured service
 * firing live payment traffic at a host that may not exist.</p>
 */
@Component
public class SwitchGatewayToggle {

    private static final Logger log = LoggerFactory.getLogger(SwitchGatewayToggle.class);

    /** TBL_GLOBAL_CONFIG.KEY_NAME holding the flag. */
    public static final String KEY_SWITCH = "SWITCH";

    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;

    public boolean isEnabled() {
        TblGlobalConfig config = tblGlobalConfigRepo.findByKeyName(KEY_SWITCH);
        if (config == null || config.getKeyValue() == null) {
            log.warn("No {} row in TBL_GLOBAL_CONFIG; treating the switch gateway as unavailable",
                    KEY_SWITCH);
            return false;
        }
        return Constants.YES.equalsIgnoreCase(config.getKeyValue().trim());
    }
}
