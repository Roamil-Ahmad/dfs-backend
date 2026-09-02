package com.dfs.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * In-process caching for lookup / static reference data (provinces, districts, LOVs, FAQs, app screens)
 * which changes rarely — removes repeated DB reads from those endpoints.
 *
 * TTL-based expiry: the source data is maintained by the back-office (a separate system), so this
 * read-API refreshes on a timer rather than on write. Tune the TTLs / sizes as needed.
 */
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    // Geo / LOV reference data — changes very rarely.
    private static final List<String> LOV_CACHES = List.of(
            "provinces", "districtsByProvince", "accountUpgradeLovs",
            "issueTypes", "categories", "provinceByName", "deviceRegistrationLovs");

    // Static content — may be updated by the back-office more often.
    private static final List<String> CONTENT_CACHES = List.of(
            "appScreenData", "faqs", "tutorials", "contactUs", "appMenu");

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        List<CaffeineCache> caches = new ArrayList<>();
        for (String name : LOV_CACHES) {
            caches.add(new CaffeineCache(name, Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofHours(24))
                    .maximumSize(1000)
                    .build()));
        }
        for (String name : CONTENT_CACHES) {
            caches.add(new CaffeineCache(name, Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofHours(1))
                    .maximumSize(500)
                    .build()));
        }
        manager.setCaches(caches);
        return manager;
    }
}
