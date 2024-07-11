package vn.edu.hust.ehustclassregistrationjavabackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableCaching
@EnableScheduling
@Configuration
public class CacheService {
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    @Scheduled(fixedDelay = 1000 * 60 * 10, initialDelay = 1000 * 60 * 10)
    public void evictAllCachedUser() {
//        log.info("{} evicted all cached users: {}", this.getClass().getSimpleName(), new Date());
    }
}
