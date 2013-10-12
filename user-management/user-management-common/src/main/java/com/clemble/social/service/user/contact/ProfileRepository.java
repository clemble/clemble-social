package com.clemble.social.service.user.contact;

import java.util.Collection;
import java.util.Map.Entry;

import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.user.contact.Profile;

public interface ProfileRepository {

    public Profile getProfile(String profileId);

    public void addProfile(Profile newProfile);

    public void addProfiles(Collection<Profile> newProfile);

    public void unite(String toProfile, String fromProfile);

    public Profile updateProfile(Profile newProfile);

    public void removeProfile(String profileId);

    public Collection<? extends SocialPersonProfile> getConnections(String profileIdentifier);

    public Profile addConnection(String profileId, SocialPersonProfile personProfile);

    public Profile removeConnection(String profileId, SocialPersonProfile socialPersonProfile);

    public Collection<Entry<String, String>> getProfileIdentifiers(Collection<ConnectionKey> connections, Collection<ConnectionKey> excluding);
}
