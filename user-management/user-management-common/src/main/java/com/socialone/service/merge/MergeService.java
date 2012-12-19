package com.socialone.service.merge;

import com.socialone.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.socialone.data.user.contact.Profile;
import com.socialone.provider.data.merge.MergeConfiguration;

public interface MergeService {

    public SocialPersonProfileBuilder merge(Profile profile, MergeConfiguration configurations);

}
