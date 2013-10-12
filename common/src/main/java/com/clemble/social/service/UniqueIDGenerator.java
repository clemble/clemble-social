package com.clemble.social.service;

import java.util.UUID;

public class UniqueIDGenerator {

    private UniqueIDGenerator() {
        throw new IllegalArgumentException();
    }
    
    public static String next() {
        return UUID.randomUUID().toString();
    }
}
