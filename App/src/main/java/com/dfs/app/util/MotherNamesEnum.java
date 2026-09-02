package com.dfs.app.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Decoy mother names for the signup verification challenge.
 *
 * The customer is shown their real mother's name from the CNIC record alongside several of these,
 * and has to pick the right one. That only works if the decoys are indistinguishable from the real
 * value, so they are common Pakistani female names and every option is upper-cased before it
 * leaves the service.
 *
 * SecureRandom rather than Random: the position and the choice of decoys should not be predictable
 * from a previous response.
 */
public enum MotherNamesEnum {

    FATIMA("Fatima"),
    AYESHA("Ayesha"),
    KHADIJA("Khadija"),
    ZAINAB("Zainab"),
    MARIAM("Mariam"),
    RUKHSANA("Rukhsana"),
    NASREEN("Nasreen"),
    SHAHNAZ("Shahnaz"),
    PARVEEN("Parveen"),
    SAIMA("Saima"),
    NAZIA("Nazia"),
    ROBINA("Robina"),
    SHAZIA("Shazia"),
    TAHIRA("Tahira"),
    YASMEEN("Yasmeen"),
    ZUBAIDA("Zubaida"),
    SUGHRA("Sughra"),
    HAJRA("Hajra"),
    AMNA("Amna"),
    BUSHRA("Bushra"),
    FARZANA("Farzana"),
    GHAZALA("Ghazala"),
    KAUSAR("Kausar"),
    NAHEED("Naheed"),
    SAKINA("Sakina"),
    SURAYYA("Surayya"),
    UZMA("Uzma"),
    ZAKIA("Zakia");

    private final String name;

    MotherNamesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param count how many distinct decoys to draw
     * @return up to {@code count} distinct names, upper-cased; fewer only if the pool is smaller
     */
    public static List<String> getRandomNames(int count) {
        return getRandomNames(count, Collections.emptyList());
    }

    /**
     * Draws decoys while avoiding anything in {@code exclude}, so the real mother's name can never
     * appear twice in the list - a duplicate would give the answer away.
     */
    public static List<String> getRandomNames(int count, List<String> exclude) {
        List<String> excluded = exclude == null
                ? Collections.emptyList()
                : exclude.stream()
                        .filter(value -> value != null)
                        .map(value -> value.trim().toUpperCase(Locale.ROOT))
                        .collect(Collectors.toList());

        List<String> pool = Arrays.stream(values())
                .map(entry -> entry.getName().toUpperCase(Locale.ROOT))
                .filter(name -> !excluded.contains(name))
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(pool, new SecureRandom());
        return pool.subList(0, Math.min(count, pool.size()));
    }
}
