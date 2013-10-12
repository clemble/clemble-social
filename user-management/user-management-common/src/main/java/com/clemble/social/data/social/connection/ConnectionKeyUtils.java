package com.clemble.social.data.social.connection;

import java.util.Collection;

import org.springframework.social.connect.ConnectionKey;

public class ConnectionKeyUtils {

    public static boolean contains(Collection<ConnectionKey> connectionKeys, String providerIdentifier) {
        // Step 1. Sanity check
        if(connectionKeys == null || providerIdentifier == null || connectionKeys.size() == 0)
            return false;
        // Step 2. Check every connection key encounter
        for(ConnectionKey connectionKey: connectionKeys) {
            if(connectionKey == null || connectionKey.getProviderId().equals(providerIdentifier))
                return true;
        }
        return false;
    }
}
