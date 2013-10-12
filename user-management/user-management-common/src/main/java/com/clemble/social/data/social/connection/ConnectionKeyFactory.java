package com.clemble.social.data.social.connection;

import java.util.Collection;

import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.social.SocialNetworkType;
import com.google.common.collect.ImmutableList;

public class ConnectionKeyFactory {
    final static public ConnectionKey get(String socialNetworkType, String providerUserId) {
        return new ConnectionKey(socialNetworkType, providerUserId);
    }

    final static public ConnectionKey get(SocialNetworkType socialNetworkType, String providerUserId) {
        assert socialNetworkType != null;
        assert providerUserId != null;
        return new ConnectionKey(socialNetworkType.name(), providerUserId);
    }

    final static public Collection<ConnectionKey> normalize(SocialNetworkType connectionType, String providerUserId) {
        return normalize(connectionType.name(), providerUserId);
    }

    final static public Collection<ConnectionKey> normalize(String providerID, String providerUserId) {
        return ImmutableList.<ConnectionKey> of(new ConnectionKey(providerID, providerUserId));
    }

    final static public Collection<ConnectionKey> normalize(SocialNetworkType connectionType, String providerUserId, String location) {
        return normalize(connectionType.name(), providerUserId, location);
    }

    final static public Collection<ConnectionKey> normalize(String providerID, String providerUserId, String location) {
        return ImmutableList.<ConnectionKey> of(new ConnectionKey(providerID, providerUserId));
    }
}
