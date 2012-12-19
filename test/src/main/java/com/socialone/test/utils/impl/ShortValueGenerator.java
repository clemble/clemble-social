package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;


public class ShortValueGenerator implements ValueGenerator<Short> {

    @Override
    public Short generate() {
        return (short) RANDOM_UTILS.nextInt();
    }

}
