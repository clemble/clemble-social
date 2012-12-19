package com.socialone.service.delivery.format.impl;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.socialone.provider.data.callback.delivery.DeliveryFormat;
import com.socialone.service.delivery.format.DeliveryFormatAdapter;
import com.socialone.service.delivery.format.DeliveryFormatAdapterFactory;
import com.socialone.service.factory.AbstractBeanMapFactory;

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
