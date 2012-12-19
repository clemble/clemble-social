package com.socialone.event.autodiscovery;

import org.springframework.social.connect.ConnectionKey;

public class UserJoinedProviderEvent extends AbstractAutodiscoveryEvent {

    /**
     * Generated 10/09/2012
     */
    private static final long serialVersionUID = -3432117208653654941L;

    public UserJoinedProviderEvent(String user, String profile, String providerId, String providerUserId) {
        super(user, profile, providerId, providerUserId);
    }

    public UserJoinedProviderEvent(String user, String profile, ConnectionKey newConnection) {
        super(user, profile, newConnection);
    }

}
