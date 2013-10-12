package com.clemble.social.event.connection;

import org.springframework.social.connect.Connection;

import com.clemble.social.event.UserEvent;

public class UserConnectionAddedEvent extends AbstractConnectionEvent implements ConnectionEvent, UserEvent {

    /**
     * Generated 08/07/2012
     */
    private static final long serialVersionUID = 6447359254292672888L;

    final private String userIdentifier;
    
    public UserConnectionAddedEvent(String userIdentifier, ConnectionAddedEvent connectionAddedEvent) {
        super(connectionAddedEvent.getProviderIdentifier(), connectionAddedEvent.getConnection());
        this.userIdentifier = userIdentifier;
    }

    public UserConnectionAddedEvent(String userIdentifier, String providerIdentifier, Connection<?> connection) {
        super(providerIdentifier, connection);
        this.userIdentifier = userIdentifier;
    }

    @Override
    public String getUserID() {
        return userIdentifier;
    }

}
