package com.dfs.switchgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Switch gateway: the thin integration layer between the DFS transaction layer and the payment
 * switch (1LINK, simulated by switch-simulator).
 *
 * It holds no transaction state and no business rules. It receives a request, turns it into an
 * ISO 8583 message, sends it to the switch, waits for the correlated answer and maps it back.
 * There is deliberately no JPA, no message queue, no store-and-forward and no retry here.
 */
@SpringBootApplication
public class SwitchGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwitchGatewayApplication.class, args);
    }
}
