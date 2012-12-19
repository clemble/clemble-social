package com.socialone.data.user.contact;

import java.util.Collection;

import org.springframework.social.connect.ConnectionKey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.socialone.data.social.SocialPersonProfile;

public class ProfileUtils {

    public static Collection<ConnectionKey> extract(Profile profile) {
        // Step 1. Sanity check
        if (profile == null)
            return ImmutableList.of();
        // Step 2. Preparing Collection of ConnectionKey's
        Collection<ConnectionKey> connectionKeys = Lists.newArrayList();
        for (SocialPersonProfile socialPersonProfile : profile.getSocialProfiles()) {
            if (socialPersonProfile == null || socialPersonProfile.getSocialConnection() == null
                    || socialPersonProfile.getSocialConnection().getConnectionKey() == null)
                continue;
            connectionKeys.addAll(socialPersonProfile.getSocialConnection().getConnectionKey());

        }
        // Step 3. Returning accumulated result
        return connectionKeys;
    }

}