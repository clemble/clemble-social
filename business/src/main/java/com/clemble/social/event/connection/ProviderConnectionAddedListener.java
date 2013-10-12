package com.clemble.social.event.connection;

import javax.inject.Inject;

import org.springframework.integration.annotation.Publisher;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.social.data.user.User.SimpleUserBuilder;
import com.clemble.social.event.connection.ConnectionAddedEvent;
import com.clemble.social.event.connection.ConnectionAddedEventListener;
import com.clemble.social.event.connection.UserConnectionAddedEvent;
import com.clemble.social.service.UniqueIDGenerator;
import com.clemble.social.service.user.UserRepository;

@Component
public class ProviderConnectionAddedListener implements ConnectionAddedEventListener<UserConnectionAddedEvent> {

    @Inject
    private UserRepository userRepository;
    
    @Override
    @Publisher
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserConnectionAddedEvent connectionAddedEvent(ConnectionAddedEvent connectionAddedEvent) {
        // Step 1. Searching for connection key
        ConnectionKey connectionKey = connectionAddedEvent.getConnection().getKey();
        // Step 2. Searching for already registered user with this identifier
        String userIdentifier = userRepository.getUserIdentifier(connectionKey);
        // Step 3. This is completely new user, who never used this system before, creating new account
        if(userIdentifier == null) {
            userIdentifier = UniqueIDGenerator.next();
            userRepository.addUserProfile(new SimpleUserBuilder().setId(userIdentifier));
        }
        // Step 4. Generating appropriate UserConnectionAdded event
        return new UserConnectionAddedEvent(userIdentifier, connectionAddedEvent);
    }

}
