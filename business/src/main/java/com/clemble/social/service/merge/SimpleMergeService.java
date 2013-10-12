package com.clemble.social.service.merge;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.clemble.social.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.provider.data.merge.MergeConfiguration;
import com.clemble.social.service.merge.MergeService;
import com.google.common.collect.Lists;

public class SimpleMergeService implements MergeService {

    @Override
    public SocialPersonProfileBuilder merge(final Profile profile, final MergeConfiguration configurations) {
        // Step 1. Sanity check
        if (profile == null)
            throw new IllegalArgumentException("Profile can't be NULL");
        if (configurations == null)
            throw new IllegalArgumentException("Merge configurations can't be NULL");
        // Step 2. Generating SocialPersonProfile based on existing profiles
        final List<SocialPersonProfile> socialPersonProfiles = Lists.newArrayList(profile.getSocialProfiles());
        Collections.sort(socialPersonProfiles, new Comparator<SocialPersonProfile>() {
            @Override
            public int compare(SocialPersonProfile socialPersonProfile, SocialPersonProfile comparedSocialPersonProfile) {
                // Step 1. Extracting priority of socialPersonProfile
                Integer socialPersonProfilePriority = configurations.getProvidersOrder().get(socialPersonProfile.getPrimaryConnection().getProviderId());
                socialPersonProfilePriority = socialPersonProfilePriority == null ? Integer.valueOf(0) : socialPersonProfilePriority;
                // Step 2. Extracting priority of socialPersonProfile
                Integer comparedPersonProfilePriority = configurations.getProvidersOrder().get(comparedSocialPersonProfile.getPrimaryConnection().getProviderId());
                comparedPersonProfilePriority = comparedPersonProfilePriority == null ? Integer.valueOf(0) : comparedPersonProfilePriority;
                // Step 3. Comparing priorities
                return socialPersonProfilePriority.compareTo(comparedPersonProfilePriority);
            }
        });
        // Step 3. Generating appropriate 
        return new SimpleSocialPersonProfileBuilder().safeMerge(socialPersonProfiles);
    }

}
