package com.clemble.social.service.deduplication.criteria.impl;

import org.springframework.stereotype.Component;

import com.clemble.social.data.social.SocialPersonProfile;
import com.google.common.base.Function;

@Component
public class LastNameCriteria extends AbstractNameCriteria {

    final private static Function<SocialPersonProfile, String> LAST_NAME_EXTRACTOR = new Function<SocialPersonProfile, String>() {
        @Override
        public String apply(SocialPersonProfile contactProfile) {
            return contactProfile.getLastName();
        }
    };

    protected LastNameCriteria() {
        super(LAST_NAME_EXTRACTOR);
    }
}
