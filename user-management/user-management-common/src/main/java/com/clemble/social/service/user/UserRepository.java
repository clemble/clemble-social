package com.clemble.social.service.user;

import java.util.Collection;

import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.user.User;
import com.clemble.social.data.user.contact.Profile;

public interface UserRepository {

    public String getUserIdentifier(ConnectionKey connectionKey);

    public User getUserProfile(String userId);

    public User addUserProfile(User user);

    public User updateUserProfile(User user);

    public void removeUserProfile(String userId);

    public Collection<String> getUsersWithConnections(Collection<ConnectionKey> connections);

    public Collection<? extends Profile> getConnections(String userIdentifier);

    public void addConnection(String userIdentifier, Profile profile);

    public void removeConnection(String userIdentifier, Profile profile);

}
