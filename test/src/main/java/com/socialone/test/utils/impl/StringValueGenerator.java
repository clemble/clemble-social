package com.socialone.test.utils.impl;

import org.apache.commons.lang3.RandomStringUtils;

import com.socialone.test.utils.ValueGenerator;

public class StringValueGenerator implements ValueGenerator<String> {

    final private static int DEFAULT_GENERATOR_SIZE = 10;
    
    @Override
    public String generate() {
        return RandomStringUtils.randomAlphabetic(DEFAULT_GENERATOR_SIZE);
    }

}
