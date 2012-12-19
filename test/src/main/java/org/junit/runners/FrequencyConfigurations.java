package org.junit.runners;

import java.lang.reflect.Method;

public class FrequencyConfigurations {

    final private static int MIN_RUNS = 1;

    final private int runs;
    final private boolean multithread;
    final private int randomStartDelay;
    final private int numThreads;
    
    public FrequencyConfigurations(Class<?> klass) {
        this(ReflectionUtils.getAnnotation(klass, RunTimes.class), ReflectionUtils.getAnnotation(klass, RunInParallel.class));
    }

    public FrequencyConfigurations(Method method) {
        this(method.getAnnotation(RunTimes.class), method.getAnnotation(RunInParallel.class));
    }
    
    private FrequencyConfigurations(RunTimes annotation, RunInParallel runMultithreaded) {
        runs = annotation != null ? Math.max(annotation.value(), MIN_RUNS) : (runMultithreaded != null ? Math.max(MIN_RUNS, runMultithreaded.numThreads())
                : MIN_RUNS);

        multithread = runMultithreaded != null ? true : false;
        numThreads = runMultithreaded != null ? Math.min(getRuns(), runMultithreaded.maxThreads()) : getRuns();
        randomStartDelay = runMultithreaded != null ? Math.max(0, runMultithreaded.randomStartDelayMax()) : 0;
    }

    public boolean isMultithread() {
        return multithread;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getRuns() {
        return runs;
    }

    public int getRandomStartDelay() {
        return randomStartDelay;
    }
}
