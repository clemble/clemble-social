package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;


public class LongValueGenerator implements ValueGenerator<Long> {

    @Override
    public Long generate() {
        return RANDOM_UTILS.nextLong();
    }

}
