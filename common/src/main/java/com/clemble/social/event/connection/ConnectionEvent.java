package com.clemble.social.event.connection;

import org.springframework.social.connect.Connection;

import com.clemble.social.event.BaseEvent;

public interface ConnectionEvent extends BaseEvent {

    String getProviderIdentifier();

    Connection<?> getConnection();

}
