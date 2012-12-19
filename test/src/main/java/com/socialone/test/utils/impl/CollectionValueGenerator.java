package com.socialone.test.utils.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.socialone.test.utils.ValueGenerator;

public class CollectionValueGenerator<T> implements ValueGenerator<Collection<T>>{
    
    @Override
    public Collection<T> generate() {
        return new ArrayList<T>();
    }

}
