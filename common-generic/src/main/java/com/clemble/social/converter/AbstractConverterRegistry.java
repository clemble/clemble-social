package com.clemble.social.converter;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.GenericConversionService;

import com.google.common.collect.Maps;

abstract public class AbstractConverterRegistry {

    static private Map<GenericConverter.ConvertiblePair, Converter<?, ?>> CONVERTERS_MAP = Maps.newHashMap();

    @Autowired
    private GenericConversionService conversionService;

    /**
     * @return the conversionService
     */
    public ConversionService getConversionService() {
        return conversionService;
    }

    abstract protected Collection<Converter<?, ?>> getRegister();

    @PostConstruct
    public void register() {
        if (conversionService != null) {
            for (Converter<?, ?> converter : getRegister()) {
                Class<?>[] args = GenericTypeResolver.resolveTypeArguments(converter.getClass(), Converter.class);
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(args[0], args[1]);
                if (conversionService.canConvert(args[0], args[1])) {
                    throw new IllegalArgumentException("There is alerady converter registered for this conversion " + CONVERTERS_MAP.get(convertiblePair));
                } else {
                    CONVERTERS_MAP.put(convertiblePair, converter);
                }
                conversionService.addConverter(converter);
            }
        }
    }

}
