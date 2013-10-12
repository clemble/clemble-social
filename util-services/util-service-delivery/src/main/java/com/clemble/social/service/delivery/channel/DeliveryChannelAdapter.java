package com.clemble.social.service.delivery.channel;

import com.clemble.social.provider.data.callback.delivery.DeliveryChannel;
import com.clemble.social.provider.data.callback.delivery.DeliveryConfiguration;

public interface DeliveryChannelAdapter {

    public DeliveryChannel getDeliveryChannel();

    public void deliver(byte[] data, DeliveryConfiguration deliveryConfiguration);

}
