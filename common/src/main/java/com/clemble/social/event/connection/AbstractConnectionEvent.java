package com.clemble.social.event.connection;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.Connection;

abstract public class AbstractConnectionEvent implements ConnectionEvent {

    /**
     * Generated 10/09/2012
     */
    private static final long serialVersionUID = 8686243841123960388L;

    final private String providerIdentifier;
    final private Connection<?> connection;

    protected AbstractConnectionEvent(String providerIdentifier, Connection<?> connection) {
        this.providerIdentifier = checkNotNull(providerIdentifier);
        this.connection = checkNotNull(connection);
    }

    @Override
    public String getProviderIdentifier() {
        return providerIdentifier;
    }

    @Override
    public Connection<?> getConnection() {
        return connection;
    }

}
