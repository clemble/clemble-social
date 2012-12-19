package com.socialone.event.autodiscovery;

import org.springframework.social.connect.ConnectionKey;

public class ContactDiscoveredEvent extends AbstractAutodiscoveryEvent {

    /**
     * Generated 10/09/2012
     */
    private static final long serialVersionUID = 5174748360800914223L;

    public ContactDiscoveredEvent(String user, String profile, ConnectionKey providerConnection) {
        super(user, profile, providerConnection);
    }

}
