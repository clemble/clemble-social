package org.junit.runners;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(value = {TYPE, METHOD})
@Retention(RUNTIME)
public @interface RunTimes {

    int value() default 1;

}
