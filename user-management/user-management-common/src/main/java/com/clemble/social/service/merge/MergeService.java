package com.clemble.social.service.merge;

import com.clemble.social.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.provider.data.merge.MergeConfiguration;

public interface MergeService {

    public SocialPersonProfileBuilder merge(Profile profile, MergeConfiguration configurations);

}
