package com.socialone.event.contact;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.socialone.data.user.contact.Profile;

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
