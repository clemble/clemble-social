package com.clemble.social.provider.data.callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.Builder;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;
import com.clemble.social.provider.data.callback.delivery.DeliveryConfiguration;
import com.google.common.collect.ImmutableSet;

public abstract class CallbackConfiguration extends CallbackConfigurationGeneric implements Serializable, Mutable<CallbackConfiguration> {
    final static private long serialVersionUID = 1L;

    abstract public Set<? extends DeliveryConfiguration> getDeliveryConfigurations();

    @Override
    public CallbackConfiguration freeze() {
        return new ImmutableCallbackConfiguration(getDeliveryConfigurations());
    }

    @Override
    public CallbackConfiguration merge(CallbackConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public CallbackConfiguration safeMerge(CallbackConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public CallbackConfiguration safeMerge(Collection<? extends CallbackConfiguration> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getDeliveryConfigurations() == null ? 0 : getDeliveryConfigurations().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        CallbackConfiguration other = (CallbackConfiguration) obj;
        if (getDeliveryConfigurations() == null) {
            if (getDeliveryConfigurations() != null)
                return false;
        } else if (!getDeliveryConfigurations().containsAll(other.getDeliveryConfigurations()) || !other.getDeliveryConfigurations().containsAll(getDeliveryConfigurations()))
            return false;
        return true;
    }

    public static abstract class CallbackConfigurationBuilder extends CallbackConfiguration implements Builder<CallbackConfiguration> {

        final static private long serialVersionUID = 1L;

        public CallbackConfigurationBuilder() {
        }

        public CallbackConfigurationBuilder(CallbackConfiguration other) {
            merge(other);
        }

        final public CallbackConfigurationBuilder setDeliveryConfigurations(Collection<? extends DeliveryConfiguration> additional) {
            getDeliveryConfigurations().clear();
            if (additional != null && !additional.isEmpty())
                for (DeliveryConfiguration addition : additional)
                    addDeliveryConfigurations(addition);
            return this;
        }

        abstract public CallbackConfigurationBuilder addDeliveryConfigurations(DeliveryConfiguration additional);

        @Override
        public CallbackConfigurationBuilder merge(CallbackConfiguration other) {
            if (other != null) {
                setDeliveryConfigurations(other.getDeliveryConfigurations());
            }
            return this;
        }

        @Override
        public CallbackConfigurationBuilder safeMerge(CallbackConfiguration other) {
            if (other != null) {
                setDeliveryConfigurations(other.getDeliveryConfigurations());
            }
            return this;
        }

        @Override
        public CallbackConfigurationBuilder safeMerge(Collection<? extends CallbackConfiguration> others) {
            if (others != null && !others.isEmpty()) {
                for (CallbackConfiguration other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public CallbackConfiguration build() {
            return freeze();
        }
    }

    final public static class ImmutableCallbackConfiguration extends CallbackConfiguration implements Immutable {
        final static private long serialVersionUID = 1L;

        final private Set<? extends DeliveryConfiguration> deliveryConfigurations;

        public ImmutableCallbackConfiguration(CallbackConfiguration other) {
            this((other != null ? other.getDeliveryConfigurations() : ImmutableSet.<com.clemble.social.provider.data.callback.delivery.DeliveryConfiguration> of()));
        }

        public ImmutableCallbackConfiguration(Collection<? extends DeliveryConfiguration> deliveryConfigurations) {
            if (deliveryConfigurations != null) {
                ArrayList<DeliveryConfiguration> tmpDeliveryConfigurations = new ArrayList<DeliveryConfiguration>();
                for (DeliveryConfiguration value : deliveryConfigurations) {
                    if (value != null) {
                        tmpDeliveryConfigurations.add(value.freeze());
                    }
                }
                this.deliveryConfigurations = ImmutableSet.<DeliveryConfiguration> copyOf(tmpDeliveryConfigurations);
            } else {
                this.deliveryConfigurations = ImmutableSet.<DeliveryConfiguration> of();
            }
        }

        @Override
        public Set<? extends DeliveryConfiguration> getDeliveryConfigurations() {
            return this.deliveryConfigurations;
        }

        @Override
        public CallbackConfiguration freeze() {
            return this;
        }
    }

    public static class SimpleCallbackConfigurationBuilder extends CallbackConfigurationBuilder {
        final static private long serialVersionUID = 1L;

        private Set<? extends DeliveryConfiguration> deliveryConfigurations = new HashSet<DeliveryConfiguration>();

        public SimpleCallbackConfigurationBuilder() {
        }

        public SimpleCallbackConfigurationBuilder(CallbackConfiguration other) {
            super(other);
        }

        public SimpleCallbackConfigurationBuilder(Collection<? extends DeliveryConfiguration> deliveryConfigurations) {
            this.deliveryConfigurations = new HashSet<DeliveryConfiguration>(deliveryConfigurations);
        }

        @Override
        public Set<? extends DeliveryConfiguration> getDeliveryConfigurations() {
            return this.deliveryConfigurations;
        }

        @Override
        public CallbackConfigurationBuilder addDeliveryConfigurations(DeliveryConfiguration additional) {
            if (additional != null)
                ((Collection<DeliveryConfiguration>) (Collection<?>) this.deliveryConfigurations).add(additional);
            return this;
        }
    }
}