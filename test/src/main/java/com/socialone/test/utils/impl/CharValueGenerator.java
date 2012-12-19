package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;


public class CharValueGenerator implements ValueGenerator<Character> {

    @Override
    public Character generate() {
        return (char) RANDOM_UTILS.nextInt((int) Character.MAX_VALUE);
    }

}
