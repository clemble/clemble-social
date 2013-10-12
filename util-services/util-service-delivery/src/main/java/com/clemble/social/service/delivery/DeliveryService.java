package com.clemble.social.service.delivery;

import com.clemble.social.provider.data.callback.delivery.DeliveryConfiguration;

public interface DeliveryService {

    public void publish(Object event, DeliveryConfiguration deliveryConfiguration);

}
