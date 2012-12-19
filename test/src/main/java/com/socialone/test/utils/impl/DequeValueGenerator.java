package com.socialone.test.utils.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import com.socialone.test.utils.ValueGenerator;

public class DequeValueGenerator<T> implements ValueGenerator<Deque<T>>{

    @Override
    public Deque<T> generate() {
        return new ArrayDeque<T>();
    }

}
