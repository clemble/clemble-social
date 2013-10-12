package com.clemble.social.service.social;

import java.util.Collection;

import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.social.SocialPersonProfile;

public interface SocialPersonProfileRepository {

    public SocialPersonProfile addSocialPersonProfile(SocialPersonProfile socialPersonProfile);

    public void addSocialPersonProfiles(Collection<SocialPersonProfile> socialPersonProfile);

    public SocialPersonProfile getSocialPersonProfile(ConnectionKey connectionKey);

    public Collection<ConnectionKey> getPrimaryConnections(Collection<ConnectionKey> connectionKeys);
    
    public Collection<? extends SocialPersonProfile> getSocialPersonProfiles(String providerID, Collection<String> connectionKeys);

    public SocialPersonProfile updateSocialPersonProfile(SocialPersonProfile socialPersonProfile);

    public void removeSocialPersonProfile(ConnectionKey connectionKey);

}
