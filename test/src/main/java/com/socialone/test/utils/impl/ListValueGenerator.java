package com.socialone.test.utils.impl;

import java.util.ArrayList;
import java.util.List;

import com.socialone.test.utils.ValueGenerator;

public class ListValueGenerator<T> implements ValueGenerator<List<T>>{

    @Override
    public List<T> generate() {
        return new ArrayList<T>();
    }

}
