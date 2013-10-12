package com.clemble.social.event.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.social.data.user.contact.Profile;

public class UserProfileUpdatedEvent {

    final private Profile oldUserProfile;

    final private Profile newUserProfile;

    final private String providerIdentifier;

    final private String userIdentifier;

    public UserProfileUpdatedEvent(String providerIdentifier, Profile oldUserProfile, Profile newUserProfile) {
        this.providerIdentifier = checkNotNull(providerIdentifier);
        this.userIdentifier = checkNotNull(oldUserProfile.getProfileId());
        this.oldUserProfile = checkNotNull(oldUserProfile);
        this.newUserProfile = checkNotNull(newUserProfile);

        if (!oldUserProfile.getProfileId().equals(newUserProfile.getProfileId()))
            throw new IllegalArgumentException("Profile identifiers must match");
    }

    public Profile getOldUserProfile() {
        return oldUserProfile;
    }

    public Profile getNewUserProfile() {
        return newUserProfile;
    }

    public String getProviderIdentifier() {
        return providerIdentifier;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }
}
