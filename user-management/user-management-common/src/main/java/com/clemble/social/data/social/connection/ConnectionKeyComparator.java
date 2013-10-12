package com.clemble.social.data.social.connection;

import java.util.Comparator;

import org.springframework.social.connect.ConnectionKey;

public class ConnectionKeyComparator implements Comparator<ConnectionKey> {

    @Override
    public int compare(ConnectionKey firstKey, ConnectionKey secondKey) {
        int comparison = firstKey.getProviderId().compareTo(secondKey.getProviderId());
        comparison = comparison != 0 ? comparison : firstKey.getProviderUserId().compareTo(secondKey.getProviderUserId());
        return comparison;
    }

}
