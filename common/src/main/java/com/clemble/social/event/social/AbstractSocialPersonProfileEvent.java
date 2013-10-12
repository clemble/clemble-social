package com.clemble.social.event.social;

import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.event.BaseEvent;

public class AbstractSocialPersonProfileEvent implements BaseEvent {

    /**
     * Generated 03/09/2012
     */
    private static final long serialVersionUID = -8284766849952143829L;
    
    final private SocialPersonProfile socialPersonProfile;
    
    protected AbstractSocialPersonProfileEvent(SocialPersonProfile socialPersonProfile) {
        this.socialPersonProfile = socialPersonProfile;
    }

    public SocialPersonProfile getSocialPersonProfile() {
        return socialPersonProfile;
    }

}
