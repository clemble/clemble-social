package org.junit.runners;

import java.lang.annotation.Annotation;

public class ReflectionUtils {

    public static <T extends Annotation> T getAnnotation(Class<?> klass, Class<T> annotationClass) {
        if(Object.class == klass)
            return null;
        T result = klass.getAnnotation(annotationClass);
        return result != null ? result : getAnnotation(klass.getSuperclass(), annotationClass);
    }
}
