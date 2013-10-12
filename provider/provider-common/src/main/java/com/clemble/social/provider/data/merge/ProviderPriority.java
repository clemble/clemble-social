package com.clemble.social.provider.data.merge;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;

public abstract class ProviderPriority implements Serializable, Mutable<ProviderPriority> {
    final static private long serialVersionUID = 1L;

    abstract public String getProviderId();

    abstract public int getPriority();

    @Override
    public ProviderPriority freeze() {
        return new ImmutableProviderPriority(getProviderId(), getPriority());
    }

    @Override
    public ProviderPriority merge(ProviderPriority other) {
        throw new IllegalAccessError();
    }

    @Override
    public ProviderPriority safeMerge(ProviderPriority other) {
        throw new IllegalAccessError();
    }

    @Override
    public ProviderPriority safeMerge(Collection<? extends ProviderPriority> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getProviderId() == null ? 0 : getProviderId().hashCode());
        result = 31 * result + 31 * ((int) getPriority());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        ProviderPriority other = (ProviderPriority) obj;
        if (getProviderId() == null) {
            if (getProviderId() != null)
                return false;
        } else if (!getProviderId().equals(other.getProviderId()))
            return false;
        if (getPriority() != other.getPriority())
            return false;

        return true;
    }

    public static abstract class ProviderPriorityBuilder extends ProviderPriority implements Builder<ProviderPriority> {

        final static private long serialVersionUID = 1L;

        public ProviderPriorityBuilder() {
        }

        public ProviderPriorityBuilder(ProviderPriority other) {
            merge(other);
        }

        abstract public ProviderPriorityBuilder setProviderId(String newProviderId);

        abstract public ProviderPriorityBuilder setPriority(int newPriority);

        @Override
        public ProviderPriorityBuilder merge(ProviderPriority other) {
            if (other != null) {
                setProviderId(other.getProviderId()).setPriority(other.getPriority());
            }
            return this;
        }

        @Override
        public ProviderPriorityBuilder safeMerge(ProviderPriority other) {
            if (other != null) {
                if (getProviderId() == null)
                    setProviderId(other.getProviderId());
                if (getPriority() == 0)
                    setPriority(other.getPriority());
            }
            return this;
        }

        @Override
        public ProviderPriorityBuilder safeMerge(Collection<? extends ProviderPriority> others) {
            if (others != null && !others.isEmpty()) {
                for (ProviderPriority other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public ProviderPriority build() {
            return freeze();
        }
    }

    final public static class ImmutableProviderPriority extends ProviderPriority implements Immutable {
        final static private long serialVersionUID = 1L;

        final private String providerId;
        final private int priority;

        public ImmutableProviderPriority(ProviderPriority other) {
            this((other != null ? other.getProviderId() : null), (other != null ? other.getPriority() : 0));
        }

        public ImmutableProviderPriority(String providerId, int priority) {
            this.providerId = providerId;
            this.priority = priority;
        }

        @Override
        public String getProviderId() {
            return this.providerId;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public ProviderPriority freeze() {
            return this;
        }
    }

    public static class SimpleProviderPriorityBuilder extends ProviderPriorityBuilder {
        final static private long serialVersionUID = 1L;

        private String providerId;
        private int priority;

        public SimpleProviderPriorityBuilder() {
        }

        public SimpleProviderPriorityBuilder(ProviderPriority other) {
            super(other);
        }

        public SimpleProviderPriorityBuilder(String providerId, int priority) {
            this.providerId = providerId;
            this.priority = priority;
        }

        @Override
        public String getProviderId() {
            return this.providerId;
        }

        @Override
        public ProviderPriorityBuilder setProviderId(String newProviderId) {
            this.providerId = newProviderId;
            return this;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public ProviderPriorityBuilder setPriority(int newPriority) {
            this.priority = newPriority;
            return this;
        }
    }
}