package org.springframework.test.context.transaction;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.test.utils.concurrent.ThreadLocalFactory;
import com.test.utils.concurrent.ValueFactory;

public class ThreadLocalMap<K, V> implements Map<K, V> {
    
    final private ThreadLocalFactory<Map<K, V>> realMap;
    
    public ThreadLocalMap(ValueFactory<Map<K,V>> valueFactory) {
        realMap = new ThreadLocalFactory<Map<K,V>>(valueFactory);
    }

    @Override
    public void clear() {
        realMap.get().clear();
    }

    @Override
    public boolean containsKey(Object arg0) {
        return realMap.get().containsKey(arg0);
    }

    @Override
    public boolean containsValue(Object arg0) {
        return realMap.get().containsValue(arg0);
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return realMap.get().entrySet();
    }

    @Override
    public V get(Object arg0) {
        return realMap.get().get(arg0);
    }

    @Override
    public boolean isEmpty() {
        return realMap.get().isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return realMap.get().keySet();
    }

    @Override
    public V put(K arg0, V arg1) {
        return realMap.get().put(arg0, arg1);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> arg0) {
        realMap.get().putAll(arg0);
    }

    @Override
    public V remove(Object arg0) {
        return realMap.get().remove(arg0);
    }

    @Override
    public int size() {
        return realMap.get().size();
    }

    @Override
    public Collection<V> values() {
        return realMap.get().values();
    }

}
