package com.clemble.social.provider.data.autodiscovery;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;

public abstract class AutodiscoveryConfiguration implements Serializable, Mutable<AutodiscoveryConfiguration> {
    final static private long serialVersionUID = 1L;

    abstract public boolean isMultidiscoveryEnabled();

    @Override
    public AutodiscoveryConfiguration freeze() {
        return new ImmutableAutodiscoveryConfiguration(isMultidiscoveryEnabled());
    }

    @Override
    public AutodiscoveryConfiguration merge(AutodiscoveryConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public AutodiscoveryConfiguration safeMerge(AutodiscoveryConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public AutodiscoveryConfiguration safeMerge(Collection<? extends AutodiscoveryConfiguration> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (isMultidiscoveryEnabled() ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        AutodiscoveryConfiguration other = (AutodiscoveryConfiguration) obj;
        if (isMultidiscoveryEnabled() != other.isMultidiscoveryEnabled())
            return false;
        return true;
    }

    public static abstract class AutodiscoveryConfigurationBuilder extends AutodiscoveryConfiguration implements Builder<AutodiscoveryConfiguration> {

        final static private long serialVersionUID = 1L;

        public AutodiscoveryConfigurationBuilder() {
        }

        public AutodiscoveryConfigurationBuilder(AutodiscoveryConfiguration other) {
            merge(other);
        }

        abstract public AutodiscoveryConfigurationBuilder setMultidiscoveryEnabled(boolean newMultidiscoveryEnabled);

        @Override
        public AutodiscoveryConfigurationBuilder merge(AutodiscoveryConfiguration other) {
            if (other != null) {
                setMultidiscoveryEnabled(other.isMultidiscoveryEnabled());
            }
            return this;
        }

        @Override
        public AutodiscoveryConfigurationBuilder safeMerge(AutodiscoveryConfiguration other) {
            if (other != null) {
                setMultidiscoveryEnabled(other.isMultidiscoveryEnabled());
            }
            return this;
        }

        @Override
        public AutodiscoveryConfigurationBuilder safeMerge(Collection<? extends AutodiscoveryConfiguration> others) {
            if (others != null && !others.isEmpty()) {
                for (AutodiscoveryConfiguration other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public AutodiscoveryConfiguration build() {
            return freeze();
        }
    }

    final public static class ImmutableAutodiscoveryConfiguration extends AutodiscoveryConfiguration implements Immutable {
        final static private long serialVersionUID = 1L;

        final private boolean multidiscoveryEnabled;

        public ImmutableAutodiscoveryConfiguration(AutodiscoveryConfiguration other) {
            this((other != null ? other.isMultidiscoveryEnabled() : null));
        }

        public ImmutableAutodiscoveryConfiguration(boolean multidiscoveryEnabled) {
            this.multidiscoveryEnabled = multidiscoveryEnabled;
        }

        @Override
        public boolean isMultidiscoveryEnabled() {
            return this.multidiscoveryEnabled;
        }

        @Override
        public AutodiscoveryConfiguration freeze() {
            return this;
        }
    }

    public static class SimpleAutodiscoveryConfigurationBuilder extends AutodiscoveryConfigurationBuilder {
        final static private long serialVersionUID = 1L;

        private boolean multidiscoveryEnabled;

        public SimpleAutodiscoveryConfigurationBuilder() {
        }

        public SimpleAutodiscoveryConfigurationBuilder(AutodiscoveryConfiguration other) {
            super(other);
        }

        public SimpleAutodiscoveryConfigurationBuilder(boolean multidiscoveryEnabled) {
            this.multidiscoveryEnabled = multidiscoveryEnabled;
        }

        @Override
        public boolean isMultidiscoveryEnabled() {
            return this.multidiscoveryEnabled;
        }

        @Override
        public AutodiscoveryConfigurationBuilder setMultidiscoveryEnabled(boolean newMultidiscoveryEnabled) {
            this.multidiscoveryEnabled = newMultidiscoveryEnabled;
            return this;
        }
    }
}