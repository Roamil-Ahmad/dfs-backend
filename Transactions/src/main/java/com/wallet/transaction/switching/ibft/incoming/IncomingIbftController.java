package com.wallet.transaction.switching.ibft.incoming;

import com.wallet.transaction.controller.HelperClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Incoming IBFT API - funds arriving from another bank for a DFS customer.
 *
 * Returns the same envelope as the rest of the transaction layer (see {@code FTPostApiController}):
 * the service produces the response map and {@code getCustomizedResponseFormat} sends it.
 *
 * The caller is the switch gateway, not the DFS mobile application, so the request body is the
 * ISO 8583 message rather than the {@code Request} envelope, and the message the gateway has to
 * send back down the link is the {@code data} member of the response. Header and device
 * authentication is not applied for the same reason: the gateway is an internal service on the
 * same host and the switch has no DFS session.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IncomingIbftController extends HelperClass {

    @Autowired
    private IncomingIbftService incomingIbftService;

    /** 0200 with processing code 620000 - the remitting bank is verifying a DFS beneficiary. */
    @PostMapping("/v1/ibft/incoming/titleFetch")
    public ResponseEntity<HashMap<String, Object>> ibftIncomingTitleFetch(@RequestBody IncomingSwitchMessage message) {
        HashMap<String, Object> response = incomingIbftService.titleFetch(message);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/ibftIncomingTitleFetch");

    }

    /** 0220 with processing code 480000 - funds are being credited to a DFS customer. */
    @PostMapping("/v1/ibft/incoming/advice")
    public ResponseEntity<HashMap<String, Object>> ibftIncomingAdvice(@RequestBody IncomingSwitchMessage message) {
        HashMap<String, Object> response = incomingIbftService.advice(message);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/ibftIncomingAdvice");

    }
}
