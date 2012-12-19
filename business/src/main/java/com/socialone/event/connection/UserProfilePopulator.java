package com.socialone.event.connection;

import javax.inject.Inject;

import org.springframework.integration.annotation.Publisher;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.socialone.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.socialone.data.user.contact.Profile;
import com.socialone.event.connection.UserConnectionAddedEvent;
import com.socialone.event.connection.UserConnectionAddedEventListener;
import com.socialone.event.user.UserProfileUpdatedEvent;
import com.socialone.provider.data.ProviderConfiguration;
import com.socialone.provider.service.ProviderConfigurationRepository;
import com.socialone.service.connection.ConnectionApiFactory;
import com.socialone.service.connection.UserConnectionApiAdapter;
import com.socialone.service.merge.MergeService;
import com.socialone.service.social.SocialPersonProfileRepository;
import com.socialone.service.user.contact.ProfileRepository;

@Component
public class UserProfilePopulator implements UserConnectionAddedEventListener<UserProfileUpdatedEvent> {

    @Inject
    private SocialPersonProfileRepository socialPersonProfileRepository;

    @Inject
    private ProviderConfigurationRepository configurationRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private ConnectionApiFactory connectionApiFactory;

    @Inject
    private MergeService mergeService;

    @Override
    @Publisher
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @ServiceActivator(inputChannel = "userConnectionAddedEventPipe")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public UserProfileUpdatedEvent userConnectionAddedEvent(UserConnectionAddedEvent connectionAddedEvent) {
        // Step 1. Extracting provider configurations
        final ProviderConfiguration providerConfiguration = configurationRepository.getProviderConfiguration(connectionAddedEvent.getProviderIdentifier());
        if (providerConfiguration == null)
            return null;
        // Step 2. Extract appropriate profile from the system
        final String userIdentifier = connectionAddedEvent.getUserID();
        final Connection<?> connection = connectionAddedEvent.getConnection();
        final String providerID = connection.createData().getProviderId();
        final UserConnectionApiAdapter apiAdapter = connectionApiFactory.get(providerID);
        // Step 3. Retrieve list of already associated services to the queue
        final SocialPersonProfile userSocialProfile = apiAdapter.getContact(connection.getApi(), connection.getKey().getProviderUserId());
        // Step 4. Retrieving List of Provider associated services
        socialPersonProfileRepository.updateSocialPersonProfile(userSocialProfile);
        profileRepository.addConnection(userIdentifier, userSocialProfile);
        // Step 5. Generating SocialPersonProfile for provider based on existing user profile
        Profile oldUserProfile = profileRepository.getProfile(userIdentifier);
        // Step 6. This is a new profile need to generate appropriate SocialPersonProfile
        SocialPersonProfileBuilder providerProfile = mergeService.merge(oldUserProfile, providerConfiguration.getMergeConfiguration());
        ConnectionKey userConnectionKey = new ConnectionKey(connectionAddedEvent.getProviderIdentifier(), userIdentifier);
        providerProfile.setSocialConnection(new ImmutableSocialConnection(userConnectionKey, ImmutableList.<ConnectionKey>of(userConnectionKey)));
        socialPersonProfileRepository.updateSocialPersonProfile(providerProfile);
        profileRepository.addConnection(userIdentifier, providerProfile);
        // Step 7. Extracting the most recent generated Profile
        Profile newUserProfile = profileRepository.getProfile(userIdentifier);
        return new UserProfileUpdatedEvent(connectionAddedEvent.getProviderIdentifier(), oldUserProfile, newUserProfile);
    }

}
