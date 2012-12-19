package org.junit.runners;

import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class FrequentRunner extends BlockJUnit4ClassRunner {

    final private Class<?> klassToRun;

    public FrequentRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.klassToRun = klass;
    }

    @Override
    public void run(final RunNotifier notifier) {
        FrequentTestRunner.run(klassToRun, new Runnable() {
            @Override
            public void run() {
                execute(notifier);
            }
        });
    }
    
    @Override
    protected List<FrameworkMethod> getChildren() {
        return FrequentTestRunner.order(super.getChildren());
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
