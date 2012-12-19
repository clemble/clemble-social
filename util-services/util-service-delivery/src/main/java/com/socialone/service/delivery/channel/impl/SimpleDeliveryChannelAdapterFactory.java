package com.socialone.service.delivery.channel.impl;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.socialone.provider.data.callback.delivery.DeliveryChannel;
import com.socialone.service.delivery.channel.DeliveryChannelAdapter;
import com.socialone.service.delivery.channel.DeliveryChannelAdapterFactory;
import com.socialone.service.factory.AbstractBeanMapFactory;

@Component
public class SimpleDeliveryChannelAdapterFactory extends AbstractBeanMapFactory<DeliveryChannel, DeliveryChannelAdapter> implements DeliveryChannelAdapterFactory {

    public SimpleDeliveryChannelAdapterFactory() {
        super(DeliveryChannelAdapter.class, new Function<DeliveryChannelAdapter, DeliveryChannel>() {
            @Override
            public DeliveryChannel apply(DeliveryChannelAdapter input) {
                return input != null ? input.getDeliveryChannel() : null;
            }
            
        });
    }

}
