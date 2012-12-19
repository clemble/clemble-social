package com.socialone.event.connection;

import org.springframework.social.connect.Connection;

import com.socialone.event.BaseEvent;

public interface ConnectionEvent extends BaseEvent {

    String getProviderIdentifier();

    Connection<?> getConnection();

}
