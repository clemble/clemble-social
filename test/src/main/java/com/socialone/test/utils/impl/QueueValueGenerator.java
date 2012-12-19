package com.socialone.test.utils.impl;

import java.util.ArrayDeque;
import java.util.Queue;

import com.socialone.test.utils.ValueGenerator;

public class QueueValueGenerator<T> implements ValueGenerator<Queue<T>>{

    @Override
    public Queue<T> generate() {
        return new ArrayDeque<T>();
    }

}
