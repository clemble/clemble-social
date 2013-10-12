package com.clemble.social.service.delivery.format.impl;

import org.springframework.stereotype.Component;

import com.clemble.social.provider.data.callback.delivery.DeliveryFormat;
import com.clemble.social.service.delivery.format.DeliveryFormatAdapter;
import com.clemble.social.service.delivery.format.DeliveryFormatAdapterFactory;
import com.clemble.social.service.factory.AbstractBeanMapFactory;
import com.google.common.base.Function;

@Component
public class SimpleDeliveryFormatAdapterFactory extends AbstractBeanMapFactory<DeliveryFormat, DeliveryFormatAdapter> implements DeliveryFormatAdapterFactory {

    public SimpleDeliveryFormatAdapterFactory() {
        super(DeliveryFormatAdapter.class, new Function<DeliveryFormatAdapter, DeliveryFormat>(){
            @Override
            public DeliveryFormat apply(DeliveryFormatAdapter input) {
                return input != null ? input.getDeliveryFormat() : null;
            }});
    }

}
