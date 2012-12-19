package com.socialone.test.utils.impl;

import java.util.HashMap;
import java.util.Map;

import com.socialone.test.utils.ValueGenerator;

public class MapValueGenerator<K, V> implements ValueGenerator<Map<K, V>>{

    @Override
    public Map<K, V> generate() {
        return new HashMap<K, V>();
    }

}
