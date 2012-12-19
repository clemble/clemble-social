package org.springframework.test.context;

// This move is needed in order to have access to spring application context

import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.FrequentTestRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class SpringJUnit4FrequentClassRunner extends SpringJUnit4ClassRunner {

    final private Class<?> klass;

    public SpringJUnit4FrequentClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.klass = klass;
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return FrequentTestRunner.order(super.getChildren());
    }

    @Override
    public void run(final RunNotifier notifier) {
        FrequentTestRunner.run(klass, new Runnable() {
            @Override
            public void run() {
                execute(notifier);
            }
        });
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        FrequentTestRunner.run(method.getMethod(), new Runnable() {
            @Override
            public void run() {
                executeChild(method, notifier);
            }
        });
    }

    public void execute(final RunNotifier notifier) {
        super.run(notifier);
    }

    public void executeChild(final FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }
}
