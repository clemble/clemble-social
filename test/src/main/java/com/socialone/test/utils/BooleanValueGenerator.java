package com.socialone.test.utils;

import com.socialone.test.utils.ValueGenerator;

public class BooleanValueGenerator implements ValueGenerator<Boolean> {

    @Override
    public Boolean generate() {
        return RANDOM_UTILS.nextBoolean();
    }

}
