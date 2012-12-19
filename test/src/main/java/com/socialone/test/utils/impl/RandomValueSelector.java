package com.socialone.test.utils.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.socialone.test.utils.ValueGenerator;

public class RandomValueSelector<T> implements ValueGenerator<T> {

    final private List<T> values;

    public RandomValueSelector(T[] objectValues) {
        this(Arrays.asList(objectValues));
    }
    
    public RandomValueSelector(Collection<T> values) {
        this.values = new ArrayList<T>(values);
    }

    @Override
    public T generate() {
        return values.size() == 0 ? null : values.get(RANDOM_UTILS.nextInt(values.size()));
    }

}
