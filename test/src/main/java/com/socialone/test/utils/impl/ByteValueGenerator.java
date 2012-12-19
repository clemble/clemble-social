package com.socialone.test.utils.impl;

import com.socialone.test.utils.ValueGenerator;


public class ByteValueGenerator implements ValueGenerator<Byte> {

    @Override
    public Byte generate() {
        return (byte) RANDOM_UTILS.nextInt();
    }

}
