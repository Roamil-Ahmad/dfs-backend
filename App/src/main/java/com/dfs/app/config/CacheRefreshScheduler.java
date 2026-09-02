package com.dfs.app.config;

import com.dfs.app.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Smart, automatic cache invalidation for the lookup/reference caches defined in {@link CacheConfig}.
 *
 * Every {@code cache.refresh.interval-ms} it runs a cheap {@code COUNT(*)} per reference table. If a
 * table's row count changed since the last check (e.g. the back-office added a new value), ONLY the
 * cache(s) that depend on that table are evicted, so the next request reloads fresh data.
 *
 * No external trigger is needed; the cache stays warm otherwise. The configured TTLs remain a
 * fallback (e.g. for in-place edits that do not change the row count).
 */
@Component
public class CacheRefreshScheduler {

    private static final Logger log = LoggerFactory.getLogger(CacheRefreshScheduler.class);

    /** Reference entity -> the cache name(s) that depend on it. */
    private static final Map<Class<?>, List<String>> ENTITY_CACHES = new LinkedHashMap<>();
    static {
        ENTITY_CACHES.put(LkpProvince.class,          List.of("provinces", "provinceByName", "accountUpgradeLovs"));
        ENTITY_CACHES.put(LkpDistrict.class,          List.of("districtsByProvince"));
        ENTITY_CACHES.put(LkpIssueType.class,         List.of("issueTypes"));
        ENTITY_CACHES.put(LkpTransDocsCategory.class, List.of("categories"));
        ENTITY_CACHES.put(LkpOccupation.class,        List.of("accountUpgradeLovs", "deviceRegistrationLovs"));
        ENTITY_CACHES.put(LkpAccountPurpose.class,    List.of("accountUpgradeLovs", "deviceRegistrationLovs"));
        // sources behind the deviceRegistration lookup lists
        ENTITY_CACHES.put(LkpCity.class,                  List.of("deviceRegistrationLovs"));
        ENTITY_CACHES.put(LkpExpectedMonthlyVolume.class, List.of("deviceRegistrationLovs"));
        ENTITY_CACHES.put(LkpRecoveryQuestion.class,      List.of("deviceRegistrationLovs"));
        ENTITY_CACHES.put(LkpBusinessType.class,          List.of("deviceRegistrationLovs"));
        ENTITY_CACHES.put(LkpSourceOfIncome.class,    List.of("accountUpgradeLovs"));
        ENTITY_CACHES.put(TblAccountLevel.class,      List.of("accountUpgradeLovs"));
        ENTITY_CACHES.put(TblMultilanguage.class,     List.of("appScreenData"));
        ENTITY_CACHES.put(LkpLanguage.class,          List.of("appScreenData"));
        ENTITY_CACHES.put(LkpFaqCategory.class,       List.of("faqs"));
        ENTITY_CACHES.put(TblFaq.class,               List.of("faqs"));
        ENTITY_CACHES.put(TblTutorial.class,          List.of("tutorials"));
        ENTITY_CACHES.put(TblContactus.class,         List.of("contactUs"));
        ENTITY_CACHES.put(LkpCmsCategory.class,       List.of("appMenu"));
    }

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CacheManager cacheManager;

    /** Last observed row count per reference table; first cycle just records the baseline. */
    private final Map<Class<?>, Long> lastCounts = new ConcurrentHashMap<>();

    @Scheduled(fixedDelayString = "${cache.refresh.interval-ms:120000}",
               initialDelayString = "${cache.refresh.interval-ms:120000}")
    @Transactional(readOnly = true)
    public void evictChangedCaches() {
        for (Map.Entry<Class<?>, List<String>> entry : ENTITY_CACHES.entrySet()) {
            Class<?> entity = entry.getKey();
            try {
                long count = entityManager
                        .createQuery("select count(e) from " + entity.getSimpleName() + " e", Long.class)
                        .getSingleResult();
                Long previous = lastCounts.put(entity, count);
                if (previous != null && previous.longValue() != count) {
                    entry.getValue().forEach(this::evict);
                    log.info("Reference table {} changed ({} -> {}); evicted caches {}",
                            entity.getSimpleName(), previous, count, entry.getValue());
                }
            } catch (Exception ex) {
                // A bad mapping/entity name must not stop the other checks.
                log.warn("Cache change-check failed for {}: {}", entity.getSimpleName(), ex.getMessage());
            }
        }
    }

    private void evict(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
