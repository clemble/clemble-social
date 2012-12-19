package com.socialone.event.connection;

import org.springframework.social.connect.Connection;

public class ConnectionAddedEvent extends AbstractConnectionEvent {

    /**
     * Generated 10/09/2012
     */
    private static final long serialVersionUID = -2083490395769460605L;

    public ConnectionAddedEvent(String providerIdentifier, Connection<?> connection) {
        super(providerIdentifier, connection);
    }

}
