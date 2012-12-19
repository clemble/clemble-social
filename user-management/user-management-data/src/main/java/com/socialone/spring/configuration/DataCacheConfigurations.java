package com.socialone.spring.configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.socialone.service.cache.NullableConcurrentCacheMap;

@Configuration
@EnableCaching
public class DataCacheConfigurations implements CachingConfigurer{

    @Bean
    @Singleton
    @Override
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        Collection<Cache> caches = Lists.newArrayList();
        caches.add(new NullableConcurrentCacheMap("synonymsCache", false));
        caches.add(new NullableConcurrentCacheMap("translationCache", false));
        caches.add(new NullableConcurrentCacheMap("soundMatchCache", false));
        simpleCacheManager.setCaches(caches);
        return simpleCacheManager;
    }

    @Bean
    @Singleton
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return StringUtils.collectionToCommaDelimitedString(Arrays.asList(params));
            }
        };
    }
    
    @Bean
    @Singleton
    public Cache synonymsCache() {
        ConcurrentMapCache concurrentMapCache = new ConcurrentMapCache("synonymsCache", false);
        return concurrentMapCache;
    }

}
