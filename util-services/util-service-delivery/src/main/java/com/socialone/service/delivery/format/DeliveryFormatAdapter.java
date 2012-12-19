package com.socialone.service.delivery.format;

import com.socialone.provider.data.callback.delivery.DeliveryFormat;

public interface DeliveryFormatAdapter {

    public DeliveryFormat getDeliveryFormat();

    public byte[] format(Object baseEvent);

}
