package com.socialone.provider.data.callback;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.socialone.provider.data.callback.delivery.DeliveryChannel;
import com.socialone.provider.data.callback.delivery.DeliveryConfiguration;
import com.socialone.provider.data.callback.delivery.DeliveryFormat;
import com.socialone.provider.data.callback.delivery.DeliveryConfiguration.DeliveryConfigurationBuilder;

@Embeddable
@AttributeOverrides(value = { @AttributeOverride(name = "deliveryChannel", column = @Column(name = "DELIVERY_CHANNEL")),
        @AttributeOverride(name = "deliveryFormat", column = @Column(name = "DELIVERY_FORMAT")) })
public class DeliveryConfigurationEntity extends DeliveryConfigurationBuilder {

    /**
     * Generated 07/09/2012
     */
    private static final long serialVersionUID = 4738282090327736860L;

    @Column(name = "EVENT")
    private String eventType;
    @Column(name = "DELIVERY_CHANNEL")
    @Enumerated(EnumType.STRING)
    private DeliveryChannel deliveryChannel;
    @Column(name = "DELIVERY_FORMAT")
    @Enumerated(EnumType.STRING)
    private DeliveryFormat deliveryFormat;

    public DeliveryConfigurationEntity() {
    }

    public DeliveryConfigurationEntity(DeliveryConfiguration deliveryConfiguration) {
        super(deliveryConfiguration);
    }

    @Override
    public DeliveryConfigurationBuilder setDeliveryChannel(DeliveryChannel newDeliveryChannel) {
        this.deliveryChannel = newDeliveryChannel;
        return this;
    }

    @Override
    public DeliveryConfigurationBuilder setDeliveryFormat(DeliveryFormat newDeliveryFormat) {
        this.deliveryFormat = newDeliveryFormat;
        return this;
    }

    @Override
    public DeliveryConfigurationBuilder setEventType(String newEventType) {
        this.eventType = newEventType;
        return this;
    }

    @Override
    public DeliveryChannel getDeliveryChannel() {
        return deliveryChannel;
    }

    @Override
    public DeliveryFormat getDeliveryFormat() {
        return deliveryFormat;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

}
