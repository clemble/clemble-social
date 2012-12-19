package com.socialone.service;

import java.util.UUID;

public class UniqueIDGenerator {

    private UniqueIDGenerator() {
        throw new IllegalArgumentException();
    }
    
    public static String next() {
        return UUID.randomUUID().toString();
    }
}
