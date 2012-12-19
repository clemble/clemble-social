package com.socialone.event.connection;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.integration.annotation.Publisher;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.user.User;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.Profile.ProfileBuilder;
import com.socialone.data.user.contact.Profile.SimpleProfileBuilder;
import com.socialone.event.BaseEvent;
import com.socialone.event.connection.UserConnectionAddedEvent;
import com.socialone.event.connection.UserConnectionAddedEventListener;
import com.socialone.event.contact.UserContactAddedEvent;
import com.socialone.event.contact.UserContactRemovedEvent;
import com.socialone.event.contact.UserContactUpdatedEvent;
import com.socialone.event.social.SocialPersonProfileAddedEvent;
import com.socialone.service.connection.ConnectionApiFactory;
import com.socialone.service.connection.UserConnectionApiAdapter;
import com.socialone.service.social.SocialPersonProfileRepository;
import com.socialone.service.user.UserRepository;
import com.socialone.service.user.contact.ProfileRepository;

@Component
public class AddedContactsPopulator implements UserConnectionAddedEventListener<Collection<BaseEvent>> {

    @Inject
    private ConnectionApiFactory connectionApiFactory;

    @Inject
    private SocialPersonProfileRepository socialPersonProfileRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private UserRepository userRepository;

    @Override
    @Publisher
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Collection<BaseEvent> userConnectionAddedEvent(UserConnectionAddedEvent connectionAddedEvent) {
        Collection<BaseEvent> resultEvent = Lists.newArrayList();
        // Step 0. Adding missing SocialConnections to the Database
        Connection<?> connection = connectionAddedEvent.getConnection();
        final Map<String, SocialPersonProfile> existingSocialConnections = updateSocialConnections(connection, resultEvent);
        // Step 1. Checking user connections
        String userIdentifier = connectionAddedEvent.getUserID();
        User user = userRepository.getUserProfile(userIdentifier);
        // Step 2. Extracting all providers associated with the provider identifier
        final String providerID = connection.createData().getProviderId();
        Map<String, Profile> existingConnections = Maps.newHashMap();
        for (Profile profile : user.getConnections()) {
            for (SocialPersonProfile personProfile : profile.getSocialProfiles()) {
                if (personProfile.getPrimaryConnection().getProviderId().equals(providerID)) {
                    existingConnections.put(personProfile.getPrimaryConnection().getProviderUserId(), profile);
                }
            }
        }
        // Step 3. Processing added profiles
        Collection<String> addedConnections = Lists.newArrayList(existingSocialConnections.keySet());
        addedConnections.removeAll(existingConnections.keySet());
        Collection<Profile> addedProfiles = Collections2.transform(addedConnections, new Function<String, Profile>() {
            @Override
            public Profile apply(String socialPersonProfile) {
                return new SimpleProfileBuilder().addSocialProfiles(existingSocialConnections.get(socialPersonProfile));
            }
        });
        // Step 3.1. Adding new profiles
        profileRepository.addProfiles(addedProfiles);
        // Step 3.2. Adding connections from user to profiles
        for (Profile profile : addedProfiles) {
            userRepository.addConnection(userIdentifier, profile);
            resultEvent.add(new UserContactAddedEvent(userIdentifier, profile));
        }
        // Step 4. Processing removed profiles
        Collection<String> removedConnections = Lists.newArrayList(existingConnections.keySet());
        removedConnections.removeAll(existingSocialConnections.keySet());
        for(String removedConnection: removedConnections) {
            Profile profile = existingConnections.get(removedConnection);
            if(profile.getSocialProfiles().size() == 1) {
                profileRepository.removeProfile(profile.getProfileId());
                resultEvent.add(new UserContactRemovedEvent(userIdentifier, profile));
            } else {
                profileRepository.removeConnection(profile.getProfileId(), existingSocialConnections.get(removedConnection));
                resultEvent.add(new UserContactUpdatedEvent(userIdentifier, profileRepository.getProfile(profile.getProfileId())));
            }
        }
        // Step 5. Returning list of events
        return resultEvent;
    }

    private Map<String, SocialPersonProfile> updateSocialConnections(Connection<?> connection, Collection<BaseEvent> resultEvents) {
        Map<String, SocialPersonProfile> resultMap = Maps.newHashMap();
        // Step 1. Extract providerId from the connection
        final String providerID = connection.createData().getProviderId();
        UserConnectionApiAdapter apiAdapter = connectionApiFactory.get(providerID);
        // Step 2. Retrieve list of already associated services to the queue
        Collection<String> existingSocialConnections = apiAdapter.getConnections(connection.getApi());
        Collection<String> providerConnectionKeys = Lists.newArrayList(existingSocialConnections);
        // Step 3. Retrieving List of Provider associated services
        Collection<? extends SocialPersonProfile> existingProfiles = socialPersonProfileRepository.getSocialPersonProfiles(providerID, providerConnectionKeys);
        toMap(existingProfiles, resultMap); // Mapping all the connections to result Map
        providerConnectionKeys.removeAll(resultMap.keySet());
        // Step 4. Extracting missing providers from underlying connection
        Collection<SocialPersonProfile> missingPersonProfiles = apiAdapter.getContacts(connection.getApi(), providerConnectionKeys);
        socialPersonProfileRepository.addSocialPersonProfiles(missingPersonProfiles);
        toMap(missingPersonProfiles, resultMap);
        // Step 5. Generating add SocialPersonProfileEvents
        for(SocialPersonProfile socialPersonProfile: missingPersonProfiles){
            resultEvents.add(new SocialPersonProfileAddedEvent(socialPersonProfile));
        }
        return resultMap;
    }
    
    private void toMap(Collection<? extends SocialPersonProfile> profiles, Map<String, SocialPersonProfile> mapToAdd) {
        for(SocialPersonProfile socialPersonProfile: profiles) {
            mapToAdd.put(socialPersonProfile.getPrimaryConnection().getProviderUserId(), socialPersonProfile);
        }
    }

}
