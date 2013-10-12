package com.clemble.social.event.contact;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Collection;

import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.event.UserEvent;
import com.google.common.collect.ImmutableList;

public class UserContactUpdatedEvent implements UserEvent, Serializable {

    /**
     * Generated 16/06/2012
     */
    private static final long serialVersionUID = -1379690040250191875L;

    final private String userID;

    final private Collection<? extends Profile> contacts;

    public UserContactUpdatedEvent(String userID, Profile contact) {
        this(userID, ImmutableList.of(contact));
    }
    
    public UserContactUpdatedEvent(String userID, Collection<? extends Profile> contact) {
        this.userID = checkNotNull(userID);
        this.contacts = checkNotNull(contact);
    }

    public String getUserID() {
        return userID;
    }

    public Collection<? extends Profile> getContacts() {
        return contacts;
    }
}
