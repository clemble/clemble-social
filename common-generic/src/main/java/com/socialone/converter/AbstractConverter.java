package com.socialone.converter;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;

public class AbstractConverter extends AbstractConverterRegistry {

    @Override
    @SuppressWarnings("unchecked")
    protected Collection<Converter<?, ?>> getRegister() {
        return (Collection<Converter<?, ?>>) (Collection<?>) Arrays.asList(this);
    }

}
