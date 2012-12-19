package org.junit.runners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.runners.model.FrameworkMethod;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;

public class FrequentTestRunner {
    final private static RandomGenerator RANDOM_UTILS = new JDKRandomGenerator();

    final private static ConcurrentHashMap<Method, String> singleMethods = new ConcurrentHashMap<Method, String>();

    final private static LoadingCache<Class, ReentrantLock> lockMap = CacheBuilder.newBuilder().build(new CacheLoader<Class, ReentrantLock>() {
        @Override
        public ReentrantLock load(Class key) throws Exception {
            return new ReentrantLock();
        }

    });

    final static class JobRunner implements Callable<Boolean> {
        final private Runnable runnable;
        final private int randomStartDelay;

        private JobRunner(final int randomStartDelay, final Runnable runnable) {
            this.runnable = runnable;
            this.randomStartDelay = randomStartDelay;
        }

        @Override
        public Boolean call() throws Exception {
            boolean passed = false;

            if (randomStartDelay > 0) {
                Thread.sleep(RANDOM_UTILS.nextInt(randomStartDelay));
            }

            try {
                runnable.run();
                passed = true;
            } catch (Throwable throwable) {
                passed = false;
            }

            return passed;
        }
    }

    final private static Comparator<FrameworkMethod> METHOD_SORTER = new Comparator<FrameworkMethod>() {
        @Override
        public int compare(FrameworkMethod method, FrameworkMethod secondMethod) {
            SingleRun singleRunMethod = method.getAnnotation(SingleRun.class);
            SingleRun secondRunMethod = secondMethod.getAnnotation(SingleRun.class);

            if (singleRunMethod != null && secondRunMethod != null)
                return 0;
            else if (singleRunMethod != null)
                return -1;
            else if (secondRunMethod != null)
                return 1;
            else
                return 0;
        }
    };

    public static List<FrameworkMethod> order(List<FrameworkMethod> frameworkMethods) {
        if (frameworkMethods == null || frameworkMethods.isEmpty())
            return frameworkMethods;
        Collections.sort(frameworkMethods, METHOD_SORTER);
        return frameworkMethods;
    }

    public static Collection<Boolean> run(final Class<?> klass, final Runnable runnable) {
        FrequencyConfigurations frequencyConfigurations = new FrequencyConfigurations(klass);
        Callable<Boolean> job = new JobRunner(frequencyConfigurations.getRandomStartDelay(), runnable);
        return execute(frequencyConfigurations, job);
    }

    public static Collection<Boolean> run(final Method method, final Runnable runnable) {
        if (method.getAnnotation(SingleRun.class) != null) {
            String identifier = UUID.randomUUID().toString();
            String result = singleMethods.putIfAbsent(method, identifier);
            if (result != null)
                return ImmutableList.<Boolean> of();

            ReentrantLock lock = null;
            try {
                lock = lockMap.get(method.getDeclaringClass());
                if (!lock.tryLock(1, TimeUnit.MILLISECONDS))
                    lock.lock();
                Callable<Boolean> job = new JobRunner(0, runnable);
                return ImmutableList.<Boolean> of(job.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (lock != null)
                    lock.unlock();
            }
        } else {
            ReentrantLock lock = null;
            try {
                lock = lockMap.get(method.getDeclaringClass());
                if (!lock.tryLock(1, TimeUnit.MILLISECONDS))
                    lock.lock();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (lock != null)
                    lock.unlock();
            }

            FrequencyConfigurations frequencyConfigurations = new FrequencyConfigurations(method);
            Callable<Boolean> job = new JobRunner(frequencyConfigurations.getRandomStartDelay(), runnable);
            return execute(frequencyConfigurations, job);
        }
    }

    public static Collection<Boolean> execute(FrequencyConfigurations frequencyConfiguration, Callable<Boolean> callable) {
        Collection<Boolean> results = new ConcurrentLinkedQueue<Boolean>();
        // Step 1. Checking what execution type would most fit this
        if (frequencyConfiguration.isMultithread()) {
            ExecutorService executorService = Executors.newFixedThreadPool(frequencyConfiguration.getNumThreads());
            try {
                Collection<Callable<Boolean>> executionJobs = new ArrayList<Callable<Boolean>>(frequencyConfiguration.getRuns());
                for (int i = 0; i < frequencyConfiguration.getRuns(); i++) {
                    executionJobs.add(callable);
                }
                // Step 2. executing duplicated jobs
                Collection<Future<Boolean>> futureResults = executorService.invokeAll(executionJobs);
                for (Future<Boolean> futureResult : futureResults) {
                    results.add(futureResult.get());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
        } else {
            for (int i = 0; i < frequencyConfiguration.getRuns(); i++) {
                try {
                    results.add(callable.call());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;
    }
}
