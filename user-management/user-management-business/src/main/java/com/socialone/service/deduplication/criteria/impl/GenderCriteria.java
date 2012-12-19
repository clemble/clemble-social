package com.socialone.service.deduplication.criteria.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.socialone.data.Gender;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.user.contact.Profile;
import com.socialone.service.deduplication.DeduplicationCriteria;
import com.socialone.service.match.MatchScore;

@Component
public class GenderCriteria implements DeduplicationCriteria{
    
    final private static Function<SocialPersonProfile, Gender> GENDER_EXTRACTOR = new Function<SocialPersonProfile, Gender>() {
        @Override
        public Gender apply(SocialPersonProfile profile) {
            return profile.getGender();
        }
    };

    @Override
    public Map<Profile, MatchScore> match(Profile contact, Collection<Profile> allContacts) {
        Map<Profile, MatchScore> matchMap = Maps.newHashMap();
        // Step 1. Extracting gender from original contact
        Gender gender = getGender(contact);
        for(Profile candidate: allContacts) {
            if(gender == null) {
                matchMap.put(candidate, MatchScore.INCONCLUSIVE);
            } else {
                Gender candidateGender = getGender(candidate);
                if(candidateGender == null) {
                    matchMap.put(candidate, MatchScore.INCONCLUSIVE);
                } else if(candidateGender == gender) {
                    matchMap.put(candidate, MatchScore.WEAK_MATCH);
                } else if(candidateGender != gender) {
                    matchMap.put(candidate, MatchScore.NO_MATCH);
                }
            }
        }
        return matchMap;
    }
    
    private Gender getGender(Profile contact) {
        // Step 1. Extracting associated genders from Contact
        Collection<Gender> genders = Collections2.transform(contact.getSocialProfiles(), GENDER_EXTRACTOR);
        // Step 2. Generating appropriate Gender value
        Gender resultGender = null;
        for(Gender gender: genders) {
            if(gender != null) {
                // If result gender is null assign appropriate value
                if(resultGender == null) {
                    resultGender = gender;
                } else if(resultGender != gender) {
                    // If profiles genders do not much, then gender is undefined
                    return null;
                }
            }
        }
        return resultGender;
    }

}
