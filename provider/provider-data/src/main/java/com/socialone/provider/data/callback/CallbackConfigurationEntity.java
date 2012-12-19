package com.socialone.provider.data.callback;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.socialone.provider.data.callback.CallbackConfiguration;
import com.socialone.provider.data.callback.CallbackConfiguration.CallbackConfigurationBuilder;
import com.socialone.provider.data.callback.delivery.DeliveryConfiguration;

@Embeddable
public class CallbackConfigurationEntity extends CallbackConfigurationBuilder {

    /**
     * Generated 07/09/2012
     */
    private static final long serialVersionUID = 368916290828505084L;

    @ElementCollection
    @CollectionTable(name = "PROVIDER_CALLBACK_CONFIGURATIONS", joinColumns = @JoinColumn(name = "PROVIDER_NAME"))
    private Set<DeliveryConfigurationEntity> deliveryConfigurations = Sets.newHashSet();

    public CallbackConfigurationEntity() {
    }

    public CallbackConfigurationEntity(CallbackConfiguration callbackConfiguration) {
        merge(callbackConfiguration);
    }

    @Override
    public Set<? extends DeliveryConfiguration> getDeliveryConfigurations() {
        return deliveryConfigurations;
    }

    @Override
    public CallbackConfigurationBuilder addDeliveryConfigurations(DeliveryConfiguration newDeliveryConfigurations) {
        deliveryConfigurations.add(new DeliveryConfigurationEntity(newDeliveryConfigurations));
        return this;
    }

}
