package com.socialone.service.delivery.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.socialone.provider.data.callback.delivery.DeliveryConfiguration;
import com.socialone.service.delivery.DeliveryService;
import com.socialone.service.delivery.channel.DeliveryChannelAdapter;
import com.socialone.service.delivery.channel.DeliveryChannelAdapterFactory;
import com.socialone.service.delivery.format.DeliveryFormatAdapter;
import com.socialone.service.delivery.format.DeliveryFormatAdapterFactory;

@Component
public class SimpleDeliveryService implements DeliveryService {

    @Inject
    private DeliveryFormatAdapterFactory deliveryFormatAdapterFactory;
    
    @Inject
    private DeliveryChannelAdapterFactory deliveryChannelAdapterFactory;
    
    @Override
    public void publish(Object event, DeliveryConfiguration deliveryConfiguration) {
        // Step 1. Sanity check
        if(event == null || deliveryConfiguration == null)
            return;
        // Step 2. Adapting message for delivery
        DeliveryFormatAdapter deliveryFormatAdapter = deliveryFormatAdapterFactory.get(deliveryConfiguration.getDeliveryFormat());
        if(deliveryFormatAdapter == null)
            return;
        byte[] data = deliveryFormatAdapter.format(event);
        // Step 3. Making actual delivery
        DeliveryChannelAdapter deliveryChannelAdapter = deliveryChannelAdapterFactory.get(deliveryConfiguration.getDeliveryChannel());
        if(deliveryChannelAdapter == null)
            return;
        deliveryChannelAdapter.deliver(data, deliveryConfiguration);
    }

}
