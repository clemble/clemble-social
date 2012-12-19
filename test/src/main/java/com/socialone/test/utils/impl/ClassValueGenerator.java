package com.socialone.test.utils.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.ReflectionUtils;

import com.socialone.test.utils.ValueGenerator;

public class ClassValueGenerator<T> implements ValueGenerator<T> {

    final private Constructor<T> constructor;
    final private Collection<ValueGenerator<?>> constructorValueGenerators;
    final private Map<Field, ValueGenerator<?>> fieldValueSetters;
    final private Map<Method, ValueGenerator<?>> methodValueSetters;

    public ClassValueGenerator(final Constructor<T> constructor, final Collection<ValueGenerator<?>> constructorValueGenerator, final Map<Field, ValueGenerator<?>> fieldValueSetters, final Map<Method, ValueGenerator<?>> methodValueSetters) {
        this.constructor = constructor;
        this.constructorValueGenerators = constructorValueGenerator;
        this.fieldValueSetters = fieldValueSetters;
        this.methodValueSetters = methodValueSetters;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public T generate() {
        // Step 1. Generate value for Constructor
        Collection values = new ArrayList();
        for (ValueGenerator<?> valueGenerator : constructorValueGenerators)
            values.add(valueGenerator.generate());
        // Step 2. Invoke constructor, creating empty Object
        Object generatedObject = null;
        try {
            generatedObject = values.size() == 0 ? constructor.newInstance() : constructor.newInstance(values.toArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (generatedObject == null)
            throw new IllegalArgumentException();
        // Step 3. Set fields for the newly created class
        for (Entry<Field, ValueGenerator<?>> fieldSetters : fieldValueSetters.entrySet()) {
            Field field = fieldSetters.getKey();
            Object value = fieldSetters.getValue().generate();
            field.setAccessible(true);
            ReflectionUtils.setField(fieldSetters.getKey(), generatedObject, value);
        }
        // Step 4. Call set methods for collections
        for(Entry<Method, ValueGenerator<?>> methodSetter: methodValueSetters.entrySet()) {
            Method method = methodSetter.getKey();
            Object value = methodSetter.getValue().generate();
            ReflectionUtils.invokeMethod(method, generatedObject, value);
        }
        // Step 5. Generated Object can be used
        return (T) generatedObject;
    }

}
