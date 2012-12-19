package com.socialone.provider.data.callback.delivery;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.socialone.data.Immutable;
import com.socialone.data.markups.Mutable;

public abstract class DeliveryConfiguration implements Serializable, Mutable<DeliveryConfiguration> {
    final static private long serialVersionUID = 1L;

    abstract public DeliveryChannel getDeliveryChannel();

    abstract public DeliveryFormat getDeliveryFormat();

    abstract public String getEventType();

    @Override
    public DeliveryConfiguration freeze() {
        return new ImmutableDeliveryConfiguration(getDeliveryChannel(), getDeliveryFormat(), getEventType());
    }

    @Override
    public DeliveryConfiguration merge(DeliveryConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public DeliveryConfiguration safeMerge(DeliveryConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public DeliveryConfiguration safeMerge(Collection<? extends DeliveryConfiguration> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getDeliveryChannel() == null ? 0 : getDeliveryChannel().hashCode());
        result = 31 * result + (getDeliveryFormat() == null ? 0 : getDeliveryFormat().hashCode());
        result = 31 * result + (getEventType() == null ? 0 : getEventType().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        DeliveryConfiguration other = (DeliveryConfiguration) obj;
        if (getDeliveryChannel() == null) {
            if (getDeliveryChannel() != null)
                return false;
        } else if (!getDeliveryChannel().equals(other.getDeliveryChannel()))
            return false;
        if (getDeliveryFormat() == null) {
            if (getDeliveryFormat() != null)
                return false;
        } else if (!getDeliveryFormat().equals(other.getDeliveryFormat()))
            return false;
        if (getEventType() == null) {
            if (getEventType() != null)
                return false;
        } else if (!getEventType().equals(other.getEventType()))
            return false;
        return true;
    }

    public static abstract class DeliveryConfigurationBuilder extends DeliveryConfiguration implements Builder<DeliveryConfiguration> {

        final static private long serialVersionUID = 1L;

        public DeliveryConfigurationBuilder() {
        }

        public DeliveryConfigurationBuilder(DeliveryConfiguration other) {
            merge(other);
        }

        abstract public DeliveryConfigurationBuilder setDeliveryChannel(DeliveryChannel newDeliveryChannel);

        abstract public DeliveryConfigurationBuilder setDeliveryFormat(DeliveryFormat newDeliveryFormat);

        abstract public DeliveryConfigurationBuilder setEventType(String newEventType);

        @Override
        public DeliveryConfigurationBuilder merge(DeliveryConfiguration other) {
            if (other != null) {
                setDeliveryChannel(other.getDeliveryChannel()).setDeliveryFormat(other.getDeliveryFormat()).setEventType(other.getEventType());
            }
            return this;
        }

        @Override
        public DeliveryConfigurationBuilder safeMerge(DeliveryConfiguration other) {
            if (other != null) {
                if (getDeliveryChannel() == null)
                    setDeliveryChannel(other.getDeliveryChannel());
                if (getDeliveryFormat() == null)
                    setDeliveryFormat(other.getDeliveryFormat());
                if (getEventType() == null)
                    setEventType(other.getEventType());
            }
            return this;
        }

        @Override
        public DeliveryConfigurationBuilder safeMerge(Collection<? extends DeliveryConfiguration> others) {
            if (others != null && !others.isEmpty()) {
                for (DeliveryConfiguration other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public DeliveryConfiguration build() {
            return freeze();
        }
    }

    final public static class ImmutableDeliveryConfiguration extends DeliveryConfiguration implements Immutable {
        final static private long serialVersionUID = 1L;

        final private DeliveryChannel deliveryChannel;
        final private DeliveryFormat deliveryFormat;
        final private String eventType;

        public ImmutableDeliveryConfiguration(DeliveryConfiguration other) {
            this((other != null ? other.getDeliveryChannel() : null), (other != null ? other.getDeliveryFormat() : null), (other != null ? other.getEventType()
                    : null));
        }

        public ImmutableDeliveryConfiguration(DeliveryChannel deliveryChannel, DeliveryFormat deliveryFormat, String eventType) {
            this.deliveryChannel = deliveryChannel;
            this.deliveryFormat = deliveryFormat;
            this.eventType = eventType;
        }

        @Override
        public DeliveryChannel getDeliveryChannel() {
            return this.deliveryChannel;
        }

        @Override
        public DeliveryFormat getDeliveryFormat() {
            return this.deliveryFormat;
        }

        @Override
        public String getEventType() {
            return this.eventType;
        }

        @Override
        public DeliveryConfiguration freeze() {
            return this;
        }
    }

    public static class SimpleDeliveryConfigurationBuilder extends DeliveryConfigurationBuilder {
        final static private long serialVersionUID = 1L;

        private DeliveryChannel deliveryChannel;
        private DeliveryFormat deliveryFormat;
        private String eventType;

        public SimpleDeliveryConfigurationBuilder() {
        }

        public SimpleDeliveryConfigurationBuilder(DeliveryConfiguration other) {
            super(other);
        }

        public SimpleDeliveryConfigurationBuilder(DeliveryChannel deliveryChannel, DeliveryFormat deliveryFormat, String eventType) {
            this.deliveryChannel = deliveryChannel;
            this.deliveryFormat = deliveryFormat;
            this.eventType = eventType;
        }

        @Override
        public DeliveryChannel getDeliveryChannel() {
            return this.deliveryChannel;
        }

        @Override
        public DeliveryConfigurationBuilder setDeliveryChannel(DeliveryChannel newDeliveryChannel) {
            this.deliveryChannel = newDeliveryChannel;
            return this;
        }

        @Override
        public DeliveryFormat getDeliveryFormat() {
            return this.deliveryFormat;
        }

        @Override
        public DeliveryConfigurationBuilder setDeliveryFormat(DeliveryFormat newDeliveryFormat) {
            this.deliveryFormat = newDeliveryFormat;
            return this;
        }

        @Override
        public String getEventType() {
            return this.eventType;
        }

        @Override
        public DeliveryConfigurationBuilder setEventType(String newEventType) {
            this.eventType = newEventType;
            return this;
        }
    }
}