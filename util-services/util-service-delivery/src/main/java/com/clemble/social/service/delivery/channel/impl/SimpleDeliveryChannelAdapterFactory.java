package com.clemble.social.service.delivery.channel.impl;

import org.springframework.stereotype.Component;

import com.clemble.social.provider.data.callback.delivery.DeliveryChannel;
import com.clemble.social.service.delivery.channel.DeliveryChannelAdapter;
import com.clemble.social.service.delivery.channel.DeliveryChannelAdapterFactory;
import com.clemble.social.service.factory.AbstractBeanMapFactory;
import com.google.common.base.Function;

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
