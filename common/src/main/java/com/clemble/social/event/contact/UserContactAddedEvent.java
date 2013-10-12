package com.clemble.social.event.contact;

import java.util.Collection;

import com.clemble.social.data.user.contact.Profile;
import com.google.common.collect.ImmutableList;

public class UserContactAddedEvent extends UserContactUpdatedEvent {

    /**
     * Generated 17/07/2012
     */
    private static final long serialVersionUID = -4487386887699867703L;

    public UserContactAddedEvent(String userID, Profile contact) {
        this(userID, ImmutableList.of(contact));
    }
    
    public UserContactAddedEvent(String userID, Collection<? extends Profile> contacts) {
        super(userID, contacts);
    }

}
