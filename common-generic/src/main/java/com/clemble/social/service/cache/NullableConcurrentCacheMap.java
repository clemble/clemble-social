package com.clemble.social.service.cache;

import org.springframework.cache.concurrent.ConcurrentMapCache;

public class NullableConcurrentCacheMap extends ConcurrentMapCache {

    public NullableConcurrentCacheMap(String name) {
        super(name);
    }
    
    public NullableConcurrentCacheMap(String name, boolean allowNullValues) {
        super(name, allowNullValues);
    }

    public void put(Object key, Object value) {
        if(!isAllowNullValues() && value == null)
            return;
        super.put(key, value);
    }
}
