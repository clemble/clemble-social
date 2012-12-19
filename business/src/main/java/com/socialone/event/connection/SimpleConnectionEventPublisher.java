package com.socialone.event.connection;

import org.springframework.integration.annotation.Publisher;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;

import com.socialone.event.connection.ConnectionAddedEvent;
import com.socialone.event.connection.ConnectionPublisher;

@Component
public class SimpleConnectionEventPublisher implements ConnectionPublisher {

    @Override
    @Publisher
    public ConnectionAddedEvent publish(String providerId, Connection<?> connection) {
        // Step 1. Generating ConnectionAddedEvent
        return new ConnectionAddedEvent(providerId, connection);
    }

}
