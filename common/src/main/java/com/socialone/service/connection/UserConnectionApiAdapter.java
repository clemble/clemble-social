package com.socialone.service.connection;

import java.util.Collection;

import org.springframework.social.connect.ApiAdapter;

import com.socialone.data.social.SocialPersonProfile;

public interface UserConnectionApiAdapter<A> extends ApiAdapter<A> {

    Collection<String> getConnections(A api);

    Collection<SocialPersonProfile> getAllContacts(A api);

    SocialPersonProfile getContact(A api, String connectionKey);

    Collection<SocialPersonProfile> getContacts(A api, Collection<String> connectionKeys);

    String getProviderId();

}
