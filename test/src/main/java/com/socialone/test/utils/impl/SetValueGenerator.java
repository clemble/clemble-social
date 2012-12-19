package com.socialone.test.utils.impl;

import java.util.HashSet;
import java.util.Set;

import com.socialone.test.utils.ValueGenerator;

public class SetValueGenerator<T> implements ValueGenerator<Set<T>>{

    @Override
    public Set<T> generate() {
        return new HashSet<T>();
    }

}
