package com.socialone.test.utils;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

public interface ValueGenerator<T> {

    final public static RandomGenerator RANDOM_UTILS = new JDKRandomGenerator();

    T generate();

}
