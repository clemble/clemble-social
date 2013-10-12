package com.clemble.social.service.deduplication.criteria.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.clemble.social.data.date.SocialDate;
import com.clemble.social.data.date.SocialDate.SimpleSocialDateBuilder;
import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.service.deduplication.DeduplicationCriteria;
import com.clemble.social.service.match.MatchScore;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

@Component
public class BirthDayCriteria implements DeduplicationCriteria {
    
    final private static Function<SocialPersonProfile, SocialDate> DATE_EXTRACTOR = new Function<SocialPersonProfile, SocialDate>() {
        @Override
        public SocialDate apply(SocialPersonProfile profile) {
            return profile.getBirthDate();
        }
    };

    @Override
    public Map<Profile, MatchScore> match(Profile contact, Collection<Profile> allContacts) {
        Map<Profile, MatchScore> contactMatchMap = Maps.newHashMap();
        // Step 1. Accumulating all information on birth day in one date
        Collection<SocialDate> socialBirthDates = Collections2.transform(contact.getSocialProfiles(), DATE_EXTRACTOR);
        SocialDate birthDay = new SimpleSocialDateBuilder().safeMerge(socialBirthDates);
        // Step 2. Going through all the Candidates in the list
        for(Profile candidate: allContacts) {
            Collection<SocialDate> candidateSocialBirthDates = Collections2.transform(candidate.getSocialProfiles(), DATE_EXTRACTOR);
            SocialDate candidateBirthDay = new SimpleSocialDateBuilder().safeMerge(candidateSocialBirthDates);
            // Step 3. Checking match of date parts
            boolean yearMatch = false;
            boolean monthMatch = false;
            boolean dayMatch = false;
            // Step 3.1. Checking year match
            if(birthDay.getYear() > 0 && candidateBirthDay.getYear() > 0) {
                if(birthDay.getYear() != candidateBirthDay.getYear()) {
                    contactMatchMap.put(candidate, MatchScore.NO_MATCH);
                    continue;
                } else {
                    yearMatch = true;
                }
            }
            // Step 3.2. Checking month match
            if(birthDay.getMonth() > 0 && candidateBirthDay.getMonth() > 0) {
                if(birthDay.getMonth() != candidateBirthDay.getMonth()) {
                    contactMatchMap.put(candidate, MatchScore.NO_MATCH);
                    continue;
                } else {
                    monthMatch = true;
                }
            }
            // Step 3.3. Checking day match
            if(birthDay.getDay() > 0 && candidateBirthDay.getDay() > 0) {
                if(birthDay.getDay() != candidateBirthDay.getDay()) {
                    contactMatchMap.put(candidate, MatchScore.NO_MATCH);
                    continue;
                } else {
                    dayMatch = true;
                }
            }
            // Step 4. Calculating matching score
            if(yearMatch && monthMatch && dayMatch) {
                contactMatchMap.put(candidate, MatchScore.FULL_MATCH);
            } else if ((yearMatch && monthMatch) || (yearMatch && dayMatch) || (monthMatch && dayMatch)) {
                contactMatchMap.put(candidate, MatchScore.SOLID_MATCH);
            } else if (yearMatch || monthMatch || dayMatch) {
                contactMatchMap.put(candidate, MatchScore.WEAK_MATCH);
            } else {
                contactMatchMap.put(candidate, MatchScore.INCONCLUSIVE);
            }
        }
        return contactMatchMap;
    }

}
