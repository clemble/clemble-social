package com.clemble.social.event.connection;

import org.springframework.integration.annotation.Publisher;
import org.springframework.social.connect.Connection;

public interface ConnectionPublisher {

    @Publisher
    public ConnectionAddedEvent publish(String providerId, Connection<?> connection);

}
