package com.socialone.event.autodiscovery;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.ConnectionKey;

import com.socialone.event.BaseEvent;
import com.socialone.event.UserEvent;

public class AbstractAutodiscoveryEvent implements BaseEvent, UserEvent {

    /**
     * Generated 10/09/2012
     */
    private static final long serialVersionUID = -3093624636908000764L;

    final private String user;
    final private String profile;
    final private ConnectionKey newConnection;

    protected AbstractAutodiscoveryEvent(String user, String profile, String providerId, String providerUserId) {
        this(user, profile, new ConnectionKey(checkNotNull(providerId), checkNotNull(providerUserId)));
    }
    
    protected AbstractAutodiscoveryEvent(String user, String profile, ConnectionKey providerConnection) {
        this.user = checkNotNull(user);
        this.profile = checkNotNull(profile);
        this.newConnection = checkNotNull(providerConnection);
        checkNotNull(providerConnection.getProviderId());
        checkNotNull(providerConnection.getProviderUserId());
    }

    @Override
    public String getUserID() {
        return user;
    }

    public String getProfile() {
        return profile;
    }

    public ConnectionKey getNewConnection() {
        return newConnection;
    }

}
