package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;

public class DoubleValueGenerator implements ValueGenerator<Double>{

    @Override
    public Double generate() {
        return RANDOM_UTILS.nextDouble();
    }

}
