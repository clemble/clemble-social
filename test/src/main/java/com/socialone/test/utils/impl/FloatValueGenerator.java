package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;

public class FloatValueGenerator implements ValueGenerator<Float> {

    @Override
    public Float generate() {
        return RANDOM_UTILS.nextFloat();
    }

}
