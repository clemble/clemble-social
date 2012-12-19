package com.socialone.event.autodiscovery;

import java.util.Collection;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.socialone.data.social.connection.ConnectionKeyFactory;
import com.socialone.data.user.contact.ProfileUtils;
import com.socialone.event.BaseEvent;
import com.socialone.event.autodiscovery.UserJoinedProviderEvent;
import com.socialone.event.user.UserProfileUpdatedEvent;
import com.socialone.event.user.UserProfileUpdatedEventListener;
import com.socialone.service.social.SocialPersonProfileRepository;
import com.socialone.service.user.contact.ProfileRepository;

@Component
public class UserAutodiscoveryListener implements UserProfileUpdatedEventListener<Collection<BaseEvent>> {

    @Inject
    private SocialPersonProfileRepository personProfileRepository;

    @Inject
    private ProfileRepository profileRepository;
    
    @Override
    public Collection<BaseEvent> userProfileUpdated(UserProfileUpdatedEvent userProfileUpdatedEvent) {
        Collection<BaseEvent> resultEvents = Lists.newArrayList();
        ConnectionKey discoveredConnection = ConnectionKeyFactory.get(userProfileUpdatedEvent.getProviderIdentifier(), userProfileUpdatedEvent.getUserIdentifier());
        // Step 1. Checking if changed number of connections before and after
        Collection<ConnectionKey> oldConnections = ProfileUtils.extract(userProfileUpdatedEvent.getOldUserProfile());
        Collection<ConnectionKey> newConnections = ProfileUtils.extract(userProfileUpdatedEvent.getNewUserProfile());
        // Step 2. Checking if there are any new Connections
        Collection<ConnectionKey> connectionIntersection = CollectionUtils.intersection(oldConnections, newConnections);
        if(connectionIntersection.size() == newConnections.size())
            return resultEvents;
        // Step 3. Checking for unrealized connections
        Collection<ConnectionKey> existingPrimaryConnections = personProfileRepository.getPrimaryConnections(newConnections);
        Collection<ConnectionKey> excludeConnections = ImmutableList.<ConnectionKey>of(discoveredConnection);
        // Step 4. Retrieving existing profiles that do not have connection to this profile
        Collection<Entry<String, String>> profileIdentifiers = profileRepository.getProfileIdentifiers(existingPrimaryConnections, excludeConnections);
        if(profileIdentifiers == null || profileIdentifiers.size() == 0)
            return resultEvents;
        // Step 5. Processing retrieved List
        for(Entry<String, String> retrievedConnection: profileIdentifiers) {
            resultEvents.add(new UserJoinedProviderEvent(retrievedConnection.getKey(), retrievedConnection.getValue(), discoveredConnection));
        }
        return resultEvents;
    }
}
