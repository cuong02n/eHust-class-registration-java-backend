package vn.edu.hust.ehustclassregistrationjavabackend.config;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig  implements CachingConfigurer {
    @Override
    public CacheErrorHandler errorHandler() {
        return CachingConfigurer.super.errorHandler();
    }
}
