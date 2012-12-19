package com.socialone.service.deduplication.criteria.impl;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.socialone.data.social.SocialPersonProfile;

@Component
public class FirstNameCriteria extends AbstractNameCriteria {

    final private static Function<SocialPersonProfile, String> FIRST_NAME_EXTRACTOR = new Function<SocialPersonProfile, String>() {
        @Override
        public String apply(SocialPersonProfile contactProfile) {
            return contactProfile.getFirstName();
        }
    };

    protected FirstNameCriteria() {
        super(FIRST_NAME_EXTRACTOR);
    }
}
