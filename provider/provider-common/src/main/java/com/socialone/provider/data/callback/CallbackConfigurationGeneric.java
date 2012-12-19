package com.socialone.provider.data.callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.socialone.provider.data.callback.delivery.DeliveryConfiguration;

abstract class CallbackConfigurationGeneric {

    private Map<Class<?>, Collection<DeliveryConfiguration>> deliveryConfigurations;

    abstract public Collection<? extends DeliveryConfiguration> getDeliveryConfigurations();

    public Map<Class<?>, Collection<DeliveryConfiguration>> getMessageCallbacks() {
        if (deliveryConfigurations == null) {
            Map<Class<?>, Collection<DeliveryConfiguration>> tmpDeliveryConfigurations = new HashMap<Class<?>, Collection<DeliveryConfiguration>>();
            for (DeliveryConfiguration deliveryConfiguration : getDeliveryConfigurations()) {
                try {
                    Class<?> key = Class.forName(deliveryConfiguration.getEventType());
                    if (tmpDeliveryConfigurations.get(key) == null)
                        tmpDeliveryConfigurations.put(key, new ArrayList<DeliveryConfiguration>());
                    tmpDeliveryConfigurations.get(key).add(deliveryConfiguration);
                } catch (ClassNotFoundException classNotFoundException) {
                    throw new RuntimeException(classNotFoundException);
                }
            }
            deliveryConfigurations = ImmutableMap.<Class<?>, Collection<DeliveryConfiguration>>copyOf(tmpDeliveryConfigurations);
        }
        return deliveryConfigurations;
    }
}
