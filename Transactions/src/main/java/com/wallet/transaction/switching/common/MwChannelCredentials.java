package com.wallet.transaction.switching.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The credentials PKG_MW checks before it will do anything.
 *
 * <p>{@code PKG_MW.VALIDATE_TOKEN} opens a cursor on TBL_MW_CHANNEL and requires all three of
 * CLIENT_SECRET, CHANNEL_CODE and USER_ID to match one row. Miss any one and the procedure
 * returns 1046 - Invalid Token and does nothing else.</p>
 *
 * <p>Two things about that triple are easy to get wrong, so they are spelled out here:</p>
 * <ul>
 *   <li>TBL_MW_CHANNEL.CHANNEL_CODE is numeric - 0108 is mobile banking. The alphabetic codes
 *       (MOB, POS, AGNT) belong to a different, older token table and never match here.</li>
 *   <li>USER_ID is the MW service account that owns the channel row, not the logged-in customer.
 *       PKG_MW.OUTGOING_IBFT references P_USER_ID only inside VALIDATE_TOKEN - the customer is
 *       identified by P_RELATIONSHIPID - so sending the customer's id only breaks the token.</li>
 * </ul>
 *
 * <p>The secret has no committed default: it comes from the MW_CLIENT_SECRET environment
 * variable so it stays out of the repository and the image.</p>
 */
@Component
public class MwChannelCredentials {

    @Value("${switching.mw.clientSecret:}")
    private String clientSecret;

    @Value("${switching.mw.userId:}")
    private Long userId;

    public String getClientSecret() {
        return clientSecret;
    }

    public Long getUserId() {
        return userId;
    }

    /**
     * True when all three parts are present. Worth checking before the call: an unset secret
     * produces the same opaque 1046 as a wrong one, and the procedure cannot say which it was.
     */
    public boolean isComplete() {
        return clientSecret != null && !clientSecret.trim().isEmpty()
                && userId != null;
    }
}
