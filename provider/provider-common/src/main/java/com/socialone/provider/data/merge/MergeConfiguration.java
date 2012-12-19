package com.socialone.provider.data.merge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.Builder;

import com.google.common.collect.ImmutableSet;
import com.socialone.data.Immutable;
import com.socialone.data.markups.Mutable;

public abstract class MergeConfiguration extends MergeConfigurationGeneric implements Serializable, Mutable<MergeConfiguration> {
    final static private long serialVersionUID = 1L;

    abstract public Collection<? extends ProviderPriority> getProviderPriority();

    @Override
    public MergeConfiguration freeze() {
        return new ImmutableMergeConfiguration(getProviderPriority());
    }

    @Override
    public MergeConfiguration merge(MergeConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public MergeConfiguration safeMerge(MergeConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public MergeConfiguration safeMerge(Collection<? extends MergeConfiguration> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getProviderPriority() == null ? 0 : getProviderPriority().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        MergeConfiguration other = (MergeConfiguration) obj;
        if (getProviderPriority() == null) {
            if (getProviderPriority() != null)
                return false;
        } else if (!getProviderPriority().equals(other.getProviderPriority()))
            return false;
        return true;
    }

    public static abstract class MergeConfigurationBuilder extends MergeConfiguration implements Builder<MergeConfiguration> {

        final static private long serialVersionUID = 1L;

        public MergeConfigurationBuilder() {
        }

        public MergeConfigurationBuilder(MergeConfiguration other) {
            merge(other);
        }

        final public MergeConfigurationBuilder setProviderPriority(Collection<? extends ProviderPriority> additional) {
            getProviderPriority().clear();
            if (additional != null && !additional.isEmpty())
                for (ProviderPriority addition : additional)
                    addProviderPriority(addition);
            return this;
        }

        abstract public MergeConfigurationBuilder addProviderPriority(ProviderPriority additional);

        @Override
        public MergeConfigurationBuilder merge(MergeConfiguration other) {
            if (other != null) {
                setProviderPriority(other.getProviderPriority());
            }
            return this;
        }

        @Override
        public MergeConfigurationBuilder safeMerge(MergeConfiguration other) {
            if (other != null) {
                if (other.getProviderPriority() != null && !other.getProviderPriority().isEmpty()) {
                    for (ProviderPriority otherProviderPriority : other.getProviderPriority())
                        addProviderPriority(otherProviderPriority);
                }

            }
            return this;
        }

        @Override
        public MergeConfigurationBuilder safeMerge(Collection<? extends MergeConfiguration> others) {
            if (others != null && !others.isEmpty()) {
                for (MergeConfiguration other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public MergeConfiguration build() {
            return freeze();
        }
    }

    final public static class ImmutableMergeConfiguration extends MergeConfiguration implements Immutable {
        final static private long serialVersionUID = 1L;

        final private Set<? extends ProviderPriority> providerPriority;

        public ImmutableMergeConfiguration(MergeConfiguration other) {
            this((other != null ? other.getProviderPriority() : ImmutableSet.<ProviderPriority> of()));
        }

        public ImmutableMergeConfiguration(Collection<? extends ProviderPriority> providerPriority) {
            if (providerPriority != null) {
                ArrayList<ProviderPriority> tmpProviderPriority = new ArrayList<ProviderPriority>();
                for (ProviderPriority value : providerPriority) {
                    if (value != null) {
                        tmpProviderPriority.add(value.freeze());
                    }
                }
                this.providerPriority = ImmutableSet.<ProviderPriority> copyOf(tmpProviderPriority);
            } else {
                this.providerPriority = ImmutableSet.<ProviderPriority> of();
            }
        }

        @Override
        public Collection<? extends ProviderPriority> getProviderPriority() {
            return this.providerPriority;
        }

        @Override
        public MergeConfiguration freeze() {
            return this;
        }
    }

    public static class SimpleMergeConfigurationBuilder extends MergeConfigurationBuilder {
        final static private long serialVersionUID = 1L;

        private Set<? extends ProviderPriority> providerPriority = new HashSet<ProviderPriority>();

        public SimpleMergeConfigurationBuilder() {
        }

        public SimpleMergeConfigurationBuilder(MergeConfiguration other) {
            merge(other);
        }

        public SimpleMergeConfigurationBuilder(Collection<? extends ProviderPriority> providerPriority) {
            this.providerPriority = new HashSet<ProviderPriority>(providerPriority);
        }

        @Override
        public Collection<? extends ProviderPriority> getProviderPriority() {
            return this.providerPriority;
        }

        @Override
        public MergeConfigurationBuilder addProviderPriority(ProviderPriority additional) {
            if (additional != null)
                ((Collection<ProviderPriority>) (Collection<?>) this.providerPriority).add(additional);
            return this;
        }
    }
}