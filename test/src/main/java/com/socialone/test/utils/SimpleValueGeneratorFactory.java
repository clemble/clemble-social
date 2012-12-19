package com.socialone.test.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.reflections.Reflections;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.socialone.test.utils.impl.BooleanValueGenerator;
import com.socialone.test.utils.impl.ByteValueGenerator;
import com.socialone.test.utils.impl.CharValueGenerator;
import com.socialone.test.utils.impl.ClassValueGenerator;
import com.socialone.test.utils.impl.CollectionValueGenerator;
import com.socialone.test.utils.impl.DequeValueGenerator;
import com.socialone.test.utils.impl.DoubleValueGenerator;
import com.socialone.test.utils.impl.FloatValueGenerator;
import com.socialone.test.utils.impl.IntegerValueGenerator;
import com.socialone.test.utils.impl.ListValueGenerator;
import com.socialone.test.utils.impl.LongValueGenerator;
import com.socialone.test.utils.impl.MapValueGenerator;
import com.socialone.test.utils.impl.QueueValueGenerator;
import com.socialone.test.utils.impl.RandomValueSelector;
import com.socialone.test.utils.impl.SetValueGenerator;
import com.socialone.test.utils.impl.ShortValueGenerator;
import com.socialone.test.utils.impl.StringValueGenerator;

public class SimpleValueGeneratorFactory implements ValueGeneratorFactory {

    final private Map<Class, ValueGenerator> standardValueGenerators = new HashMap<Class, ValueGenerator>();
    {
        standardValueGenerators.put(String.class, new StringValueGenerator());

        standardValueGenerators.put(Boolean.class, new BooleanValueGenerator());
        standardValueGenerators.put(boolean.class, new BooleanValueGenerator());

        standardValueGenerators.put(Byte.class, new ByteValueGenerator());
        standardValueGenerators.put(byte.class, new ByteValueGenerator());
        standardValueGenerators.put(Character.class, new CharValueGenerator());
        standardValueGenerators.put(char.class, new CharValueGenerator());
        standardValueGenerators.put(Short.class, new ShortValueGenerator());
        standardValueGenerators.put(short.class, new ShortValueGenerator());
        standardValueGenerators.put(Integer.class, new IntegerValueGenerator());
        standardValueGenerators.put(int.class, new IntegerValueGenerator());
        standardValueGenerators.put(Long.class, new LongValueGenerator());
        standardValueGenerators.put(long.class, new LongValueGenerator());

        standardValueGenerators.put(Float.class, new FloatValueGenerator());
        standardValueGenerators.put(float.class, new FloatValueGenerator());
        standardValueGenerators.put(Double.class, new DoubleValueGenerator());
        standardValueGenerators.put(double.class, new DoubleValueGenerator());

        standardValueGenerators.put(Collection.class, new CollectionValueGenerator());
        standardValueGenerators.put(List.class, new ListValueGenerator());
        standardValueGenerators.put(Set.class, new SetValueGenerator());
        standardValueGenerators.put(Map.class, new MapValueGenerator());
        standardValueGenerators.put(Queue.class, new QueueValueGenerator());
        standardValueGenerators.put(Deque.class, new DequeValueGenerator());
    }

    final private Predicate<Class<?>> ABSTRACT = new Predicate<Class<?>>() {
        @Override
        public boolean apply(Class<?> inputClass) {
            return (inputClass.getModifiers() & (Modifier.INTERFACE | Modifier.ABSTRACT)) != 0;
        }
    };

    final private Predicate<Field> FIELD_FILTER = new Predicate<Field>() {
        @Override
        public boolean apply(Field input) {
            if (input == null)
                return false;
            return ((input.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) == 0) && !Collection.class.isAssignableFrom(input.getType())
                    && !Map.class.isAssignableFrom(input.getType());
        }
    };

    final private Predicate<Method> METHOD_FILTER = new Predicate<Method>() {
        @Override
        public boolean apply(Method method) {
            return method.getName().startsWith("add") && method.getParameterTypes().length == 1;
        }
    };

    @Override
    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
        if (standardValueGenerators.containsKey(klass))
            return standardValueGenerators.get(klass);
        return initiateValueGenerator(klass);
    }

    public <T> ValueGenerator<T> initiateValueGenerator(Class key) {
        if (key.isEnum()) {
            return new RandomValueSelector(key.getEnumConstants());
        }
        try {
            return constructValueGenerator(key, ImmutableList.<Class<?>> of());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T constructValueGenerator(Class<T> classToGenerate, Collection<Class<?>> candidates) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ExecutionException {
        // Step 1. Trying to generate based on provided class
        ValueGenerator<T> instance = constructValueGenerator(classToGenerate);
        if (instance != null)
            return (T) instance;
        // Step 2. Checking all candidate implementations
        for (Class<?> candidate : candidates) {
            if ((instance = constructValueGenerator(candidate)) != null)
                return (T) instance;
        }
        // Step 3 If class is abstract substitute it with real implementation in the same package or sub packages
        Collection<Class<? extends T>> filteredClasses = getPossibleImplementations(classToGenerate);
        filteredClasses.removeAll(candidates);
        if (filteredClasses.size() > 0) {
            // Step 3.1 Checking extended list of candidates
            Collection<Class<?>> allPossibleCandidates = new ArrayList(candidates);
            allPossibleCandidates.addAll(filteredClasses);
            return constructValueGenerator(classToGenerate, (Collection<Class<?>>) (Collection<?>) allPossibleCandidates);
        } else {
            // Step 3.2 Checking extended list of candidates
            for (Class<?> candidate : candidates) {
                Collection<Class<?>> candidateSubClasses = (Collection<Class<?>>) (Collection<?>) getPossibleImplementations(candidate);
                if (candidateSubClasses.size() > 0)
                    try {
                        constructValueGenerator(candidate, candidateSubClasses);
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Ignore check next implementation
                    }
            }
        }
        throw new IllegalArgumentException();
    }

    private <T> Collection<Class<? extends T>> getPossibleImplementations(Class<T> classToGenerate) {
        Reflections reflections = new Reflections(classToGenerate.getPackage().toString().replace("package ", ""));
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(classToGenerate);
        return subTypes;
    }

    private <T> ValueGenerator<T> constructValueGenerator(Class<?> classToGenerate) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ExecutionException {
        // Step 1. Checking abstract class
        if (ABSTRACT.apply(classToGenerate))
            return null;
        // Step 2. Selecting appropriate constructor
        Constructor<T> bestCandidate = findBestConstructor(classToGenerate);
        if (bestCandidate == null)
            return null;
        // Step 3. Choosing generators for Constructor variable
        Collection<ValueGenerator<?>> constructorParameters = new ArrayList<ValueGenerator<?>>();
        for (Class parameter : bestCandidate.getParameterTypes()) {
            constructorParameters.add(getValueGenerator(parameter));
        }
        // Step 4. Generating field setters
        Map<Field, ValueGenerator<?>> fieldSetters = new HashMap<Field, ValueGenerator<?>>();
        Field[] fields = classToGenerate.getDeclaredFields();
        Collection<Field> fieldsToSet = Collections2.filter(Arrays.asList(fields), FIELD_FILTER);
        for (Field field : fieldsToSet) {
            fieldSetters.put(field, getValueGenerator(field.getType()));
        }
        // Step 5. Generating method setters
        Map<Method, ValueGenerator<?>> methodSetters = new HashMap<Method, ValueGenerator<?>>();
        Method[] methods = classToGenerate.getDeclaredMethods();
        Collection<Method> methodSet = Collections2.filter(Arrays.asList(methods), METHOD_FILTER);
        for (Method method : methodSet) {
            methodSetters.put(method, getValueGenerator(method.getParameterTypes()[0]));
        }
        // Step 6. Generating final ClassGenerator for the type
        return new ClassValueGenerator<T>(bestCandidate, constructorParameters, fieldSetters, methodSetters);
    }

    private <T> Constructor<T> findBestConstructor(final Class<?> classToGenerate) {
        // Step 1. Selecting appropriate constructor
        Constructor<?>[] values = classToGenerate.getConstructors();
        if (values.length == 0)
            return null;
        // Step 2. Searching for default Constructor or constructor with least configurations
        Constructor<?> bestCandidate = null;
        // Step 2.1 Filtering classes with parameters that can be cast to constructed class
        Collection<Constructor<?>> filteredConstructors = Collections2.filter(Arrays.asList(values), new Predicate<Constructor>() {
            @Override
            public boolean apply(final Constructor input) {
                for (Class<?> parameter : input.getParameterTypes())
                    if (classToGenerate.isAssignableFrom(parameter) || parameter.isAssignableFrom(classToGenerate))
                        return false;
                return true;
            }
        });
        // Step 3. Selecting constructor that would best fit for processing
        for (Constructor<?> constructor : filteredConstructors) {
            if (bestCandidate == null || constructor.getParameterTypes().length < bestCandidate.getParameterTypes().length) {
                bestCandidate = constructor;
            }
        }
        // Step 4. Returning selected constructor
        return (Constructor<T>) bestCandidate;
    }

}