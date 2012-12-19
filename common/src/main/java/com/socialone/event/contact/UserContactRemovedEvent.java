package com.socialone.event.contact;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.socialone.data.user.contact.Profile;

public class UserContactRemovedEvent extends UserContactUpdatedEvent {

    /**
     * Generated 17/06/2012
     */
    private static final long serialVersionUID = -2624755821400753478L;

    public UserContactRemovedEvent(String userID, Profile contact) {
        this(userID, ImmutableList.of(contact));
    }

    public UserContactRemovedEvent(String userID, Collection<Profile> contacts) {
        super(userID, contacts);
    }

}
