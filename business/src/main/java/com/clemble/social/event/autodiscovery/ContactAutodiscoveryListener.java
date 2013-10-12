package com.clemble.social.event.autodiscovery;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Component;

import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.data.user.contact.ProfileUtils;
import com.clemble.social.event.BaseEvent;
import com.clemble.social.event.autodiscovery.ContactDiscoveredEvent;
import com.clemble.social.event.connection.UserConnectionAddedEvent;
import com.clemble.social.event.connection.UserConnectionAddedEventListener;
import com.clemble.social.event.contact.UserContactAddedEvent;
import com.clemble.social.event.contact.UserContactAddedEventListener;
import com.clemble.social.event.contact.UserContactUpdatedEvent;
import com.clemble.social.event.contact.UserContactUpdatedEventListener;
import com.clemble.social.service.social.SocialPersonProfileRepository;
import com.clemble.social.service.user.UserRepository;
import com.clemble.social.service.user.contact.ProfileRepository;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

@Component
public class ContactAutodiscoveryListener implements UserContactAddedEventListener<Collection<BaseEvent>>,
        UserContactUpdatedEventListener<Collection<BaseEvent>>, UserConnectionAddedEventListener<Collection<BaseEvent>> {

    @Inject
    private UserRepository userRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private SocialPersonProfileRepository personProfileRepository;

    @Override
    @Subscribe
    public Collection<BaseEvent> contactAdded(UserContactAddedEvent contactAddedEvent) {
        // Step 1. Sanity check
        if (contactAddedEvent == null || isNullOrEmpty(contactAddedEvent.getUserID()) || contactAddedEvent.getContacts() == null
                || contactAddedEvent.getContacts().size() == 0)
            return ImmutableList.<BaseEvent> of();
        // Step 2. Searching for appropriate users
        Collection<? extends SocialPersonProfile> userConnections = profileRepository.getConnections(contactAddedEvent.getUserID());
        if (userConnections == null || userConnections.size() == 0)
            return ImmutableList.<BaseEvent> of();
        Collection<BaseEvent> resultEvents = Lists.newArrayList();
        // Step 3. Going through each connection in the map and trying to find appropriate service match
        final Collection<String> relevantProviders = Collections2.transform(userConnections, new Function<SocialPersonProfile, String>() {
            public String apply(SocialPersonProfile personProfile) {
                return personProfile.getPrimaryConnection().getProviderId();
            }
        });
        // Step 4. Going Contact by contact looking for relevant connections
        for(Profile contact: contactAddedEvent.getContacts()) {
            final Collection<ConnectionKey> existingConnections = ProfileUtils.extract(contact);
            // Looking for all primary connections
            Collection<ConnectionKey> primaryConnections = personProfileRepository.getPrimaryConnections(existingConnections);
            // Existing connections
            Collection<String> possibleUsers = userRepository.getUsersWithConnections(primaryConnections);
            if(possibleUsers == null || possibleUsers.size() == 0)
                continue;
            // Going through each possible User one by one
            for(String user: possibleUsers) {
                Collection<? extends SocialPersonProfile> socialPersonProfile = profileRepository.getConnections(user);
                // Step 5.1. Filtering SocialPersonProfiles relevant for the User
                socialPersonProfile = Collections2.filter(socialPersonProfile, new Predicate<SocialPersonProfile>() {
                    public boolean apply(SocialPersonProfile socialPersonProfile) {
                        return relevantProviders.contains(socialPersonProfile.getPrimaryConnection().getProviderId());
                    }
                });
                // Step 5.2. Removing already connected presences
                socialPersonProfile = Collections2.filter(socialPersonProfile, new Predicate<SocialPersonProfile>() {
                    public boolean apply(SocialPersonProfile socialPersonProfile) {
                        return !existingConnections.contains(socialPersonProfile.getPrimaryConnection());
                    }
                });
                // Step 5.3. Adding appropriate Connections to the User
                for(SocialPersonProfile personProfile: socialPersonProfile) {
                    profileRepository.addConnection(contact.getProfileId(), personProfile);
                    resultEvents.add(new ContactDiscoveredEvent(contactAddedEvent.getUserID(), contact.getProfileId(), personProfile.getPrimaryConnection()));
                }
            }
        }
        return ImmutableList.<BaseEvent>of();
    }

    @Override
    @Subscribe
    public Collection<BaseEvent> contactUpdated(UserContactUpdatedEvent contactUpdatedEvent) {
        // Step 1. Sanity check
        if (contactUpdatedEvent == null)
            return ImmutableList.<BaseEvent> of();
        // Step 2. Emittating Contact added events
        return contactAdded(new UserContactAddedEvent(contactUpdatedEvent.getUserID(), contactUpdatedEvent.getContacts()));
    }

    @Override
    @Subscribe
    public Collection<BaseEvent> userConnectionAddedEvent(UserConnectionAddedEvent connectionAddedEvent) {
        // Step 1. Sanity check
        if (connectionAddedEvent == null || connectionAddedEvent.getUserID() == null)
            return ImmutableList.<BaseEvent> of();
        // Step 2. Emitating UserContactAddedEvent
        Collection<? extends Profile> contacts = userRepository.getConnections(connectionAddedEvent.getUserID());
        return contactAdded(new UserContactAddedEvent(connectionAddedEvent.getUserID(), contacts));
    }

}
