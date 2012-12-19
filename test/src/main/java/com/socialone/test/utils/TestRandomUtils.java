package com.socialone.test.utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class TestRandomUtils {

    final public static RandomGenerator RANDOM_UTILS = new JDKRandomGenerator();

    final private static Collection<?> excluded = Sets.newHashSet();

    final private static LoadingCache<Class<? extends Enum<?>>, List<?>> ENUM_OPTIONS = CacheBuilder.newBuilder().build(
            new CacheLoader<Class<? extends Enum<?>>, List<?>>() {
                @Override
                public List<?> load(Class<? extends Enum<?>> key) throws Exception {
                    List<?> options = Lists.newArrayList(key.getEnumConstants());
                    options.removeAll(excluded);
                    return options;
                }
            });

    private TestRandomUtils() {
        throw new IllegalAccessError();
    }

    public static <T> T randomElement(Collection<T> sourceCollection) {
        List<T> list = Lists.newArrayList(sourceCollection);
        return list.get(RANDOM_UTILS.nextInt(sourceCollection.size()));
    }

    public static <T> Collection<T> randomSelection(Collection<T> sourceCollection, int selectionSize) {
        if (sourceCollection == null || selectionSize <= 0)
            return ImmutableList.of();
        if (selectionSize >= sourceCollection.size())
            return sourceCollection;

        Collection<T> resultList = Lists.newArrayList();

        List<T> sourceList = Lists.newArrayList(sourceCollection);

        Set<Integer> selection = Sets.newHashSet();
        while (selection.size() != selectionSize) {
            selection.add(RANDOM_UTILS.nextInt(sourceCollection.size()));
        }

        for (Integer selected : selection) {
            resultList.add(sourceList.get(selected));
        }

        return resultList;
    }

    public static String randomString(int num) {
        return RandomStringUtils.randomAlphabetic(num);
    }

    public static String randomCase(CharSequence originalString) {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < originalString.length(); i++) {
            // Step 1. Extracting data String
            String testCharacter = String.valueOf(originalString.charAt(i));
            // Step 2. Checking data
            if (nextInt(Integer.MAX_VALUE) > (Integer.MAX_VALUE >> 1)) {
                randomString.append(testCharacter.toUpperCase());
            } else {
                randomString.append(testCharacter.toLowerCase());
            }
        }
        return randomString.toString();
    }

    public static long nextLong() {
        return (long) ((long) RANDOM_UTILS.nextInt() << 32 | RANDOM_UTILS.nextInt());
    }
    
    public static int nextInt(int limit) {
        return RANDOM_UTILS.nextInt(limit);
    }
    
    public static char nextChar() {
        return randomString(1).charAt(0);
    }
    
    public static short nextShort() {
        return (short) RANDOM_UTILS.nextInt();
    }
    
    public static byte nextByte() {
        return (byte) RANDOM_UTILS.nextInt();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T randomElement(Class<T> enumType) {
        try {
            List<T> options = (List<T>) ENUM_OPTIONS.get(enumType);
            return options.get(RANDOM_UTILS.nextInt(options.size()));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Enum<T>> String randomName(Class<T> enumType) {
        return randomElement(enumType).name();
    }
}
