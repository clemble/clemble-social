package com.socialone.service.delivery;

import com.socialone.provider.data.callback.delivery.DeliveryConfiguration;

public interface DeliveryService {

    public void publish(Object event, DeliveryConfiguration deliveryConfiguration);

}
