package com.clemble.social.service.delivery.format;

import com.clemble.social.provider.data.callback.delivery.DeliveryFormat;

public interface DeliveryFormatAdapter {

    public DeliveryFormat getDeliveryFormat();

    public byte[] format(Object baseEvent);

}
