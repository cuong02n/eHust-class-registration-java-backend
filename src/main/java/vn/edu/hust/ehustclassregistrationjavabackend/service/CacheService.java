package vn.edu.hust.ehustclassregistrationjavabackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableCaching
@EnableScheduling
public class CacheService {
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("users");
//    }

    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60)
    public void evictAllCachedUser() {
        log.info("{} evicted all cached users: {}", this.getClass().getSimpleName(), new Date());
    }
}
