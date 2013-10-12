package com.clemble.social.service.factory;

import java.util.Collection;

public interface BeanMapFactory <K, T> {
    
    public T get(K key);
    
    public Collection<T> getAll();

}
