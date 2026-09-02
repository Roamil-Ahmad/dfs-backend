package com.dfs.app.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Decoy birth places for the signup verification challenge, alongside {@link MotherNamesEnum}.
 *
 * Pakistani cities and districts, so a decoy is not distinguishable from the real CNIC value by
 * plausibility alone. Every option is upper-cased before it leaves the service.
 */
public enum BirthPlacesEnum {

    KARACHI("Karachi"),
    LAHORE("Lahore"),
    ISLAMABAD("Islamabad"),
    RAWALPINDI("Rawalpindi"),
    FAISALABAD("Faisalabad"),
    MULTAN("Multan"),
    PESHAWAR("Peshawar"),
    QUETTA("Quetta"),
    HYDERABAD("Hyderabad"),
    GUJRANWALA("Gujranwala"),
    SIALKOT("Sialkot"),
    SUKKUR("Sukkur"),
    BAHAWALPUR("Bahawalpur"),
    SARGODHA("Sargodha"),
    ABBOTTABAD("Abbottabad"),
    MARDAN("Mardan"),
    SAHIWAL("Sahiwal"),
    OKARA("Okara"),
    LARKANA("Larkana"),
    JHELUM("Jhelum"),
    GUJRAT("Gujrat"),
    KASUR("Kasur"),
    NAWABSHAH("Nawabshah"),
    DERA_GHAZI_KHAN("Dera Ghazi Khan"),
    RAHIM_YAR_KHAN("Rahim Yar Khan"),
    MIRPUR("Mirpur"),
    SWAT("Swat"),
    KOHAT("Kohat");

    private final String place;

    BirthPlacesEnum(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    /**
     * @param count how many distinct decoys to draw
     * @return up to {@code count} distinct places, upper-cased
     */
    public static List<String> getRandomPlaces(int count) {
        return getRandomPlaces(count, Collections.emptyList());
    }

    /**
     * Draws decoys while avoiding anything in {@code exclude}, so the real birth place cannot
     * appear twice in the list.
     */
    public static List<String> getRandomPlaces(int count, List<String> exclude) {
        List<String> excluded = exclude == null
                ? Collections.emptyList()
                : exclude.stream()
                        .filter(value -> value != null)
                        .map(value -> value.trim().toUpperCase(Locale.ROOT))
                        .collect(Collectors.toList());

        List<String> pool = Arrays.stream(values())
                .map(entry -> entry.getPlace().toUpperCase(Locale.ROOT))
                .filter(place -> !excluded.contains(place))
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(pool, new SecureRandom());
        return pool.subList(0, Math.min(count, pool.size()));
    }
}
