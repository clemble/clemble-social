package com.clemble.social.event.social;

import com.clemble.social.data.social.SocialPersonProfile;

public class SocialPersonProfileUpdatedEvent extends AbstractSocialPersonProfileEvent {

    /**
     * Generated 03/09/2012
     */
    private static final long serialVersionUID = 3132678143808761366L;

    final private SocialPersonProfile oldSocialPersonProfile;
    
    public SocialPersonProfileUpdatedEvent(SocialPersonProfile socialPersonProfile, SocialPersonProfile oldSocialPersonProfile) {
        super(socialPersonProfile);
        this.oldSocialPersonProfile = oldSocialPersonProfile;
    }

    public SocialPersonProfile getOldSocialPersonProfile() {
        return oldSocialPersonProfile;
    }
}
