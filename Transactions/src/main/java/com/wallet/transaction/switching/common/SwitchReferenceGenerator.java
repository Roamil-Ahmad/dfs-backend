package com.wallet.transaction.switching.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates the two identifiers every 1LINK financial message needs.
 *
 * STAN (DE-11) must be unique for a business date, and the RRN (DE-37) is the 12-character acquirer
 * reference. Together with the PAN and the transmission date/time they form the 1LINK transaction
 * key used for duplicate detection and for matching a response to its request (spec section 6).
 *
 * The RRN follows the layout 1LINK prescribes for acquirers in spec section 9.28:
 * {@code y} (last digit of the year) + {@code ddd} (day of year) + {@code hh} + the 6-digit STAN.
 */
@Component
public class SwitchReferenceGenerator {

    private static final DateTimeFormatter DAY_OF_YEAR = DateTimeFormatter.ofPattern("DDD");
    private static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH");

    private final AtomicInteger counter = new AtomicInteger(0);

    /** Six-digit rolling trace number. */
    public String nextStan() {
        int value = counter.updateAndGet(current -> current >= 999999 ? 1 : current + 1);
        return String.format("%06d", value);
    }

    /** Twelve-character retrieval reference number derived from the supplied STAN. */
    public String rrnFor(String stan) {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear() % 10);
        return year + now.format(DAY_OF_YEAR) + now.format(HOUR) + stan;
    }
}
