package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;


public class IntegerValueGenerator implements ValueGenerator<Integer> {

    @Override
    public Integer generate() {
        return RANDOM_UTILS.nextInt();
    }

}
