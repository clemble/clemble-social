package com.socialone.test.utils;

public interface ValueGeneratorFactory {

    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass);

}
