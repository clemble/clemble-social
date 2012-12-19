package com.test.utils.concurrent;

public class ThreadLocalFactory<T> extends ThreadLocal<T> {

    final private ValueFactory<T> valueFactory;

    public ThreadLocalFactory(ValueFactory<T> usedValueFactory) {
        this.valueFactory = usedValueFactory;
    }

    @Override
    public T get() {
        if (super.get() == null)
            super.set(valueFactory.create());
        return super.get();
    }
}
