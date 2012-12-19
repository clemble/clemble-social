package com.socialone.service.delivery.channel;

import com.socialone.provider.data.callback.delivery.DeliveryChannel;
import com.socialone.provider.data.callback.delivery.DeliveryConfiguration;

public interface DeliveryChannelAdapter {

    public DeliveryChannel getDeliveryChannel();

    public void deliver(byte[] data, DeliveryConfiguration deliveryConfiguration);

}
