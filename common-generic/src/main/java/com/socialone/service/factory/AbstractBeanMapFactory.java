package com.socialone.service.factory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class AbstractBeanMapFactory<K, T> implements BeanMapFactory<K, T>, ApplicationContextAware {

    final private Map<K, T> connectionApiAdapters = Maps.newHashMap();

    final private Function<T, K> keyExtractor;

    final private Class<T> targetClass;

    public AbstractBeanMapFactory(Class<T> targetBean, Function<T, K> keyExtractor) {
        this.targetClass = checkNotNull(targetBean);
        this.keyExtractor = checkNotNull(keyExtractor);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Extracting all application contexts
        Map<String, T> adapters = applicationContext.getBeansOfType(targetClass);
        // Step 2. Populating all contexts to the HashMap for processing
        for (T connectionApiAdapter : adapters.values()) {
            K key = keyExtractor.apply(connectionApiAdapter);
            if (connectionApiAdapters.get(key) != null)
                throw new IllegalArgumentException("Can't have few adapters for the same provider");
            connectionApiAdapters.put(key, connectionApiAdapter);
        }
    }

    @Override
    public T get(K key) {
        return connectionApiAdapters.get(key);
    }

    @Override
    public Collection<T> getAll() {
        return connectionApiAdapters.values();
    }

}
