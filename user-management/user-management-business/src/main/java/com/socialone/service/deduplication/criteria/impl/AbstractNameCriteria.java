package com.socialone.service.deduplication.criteria.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.user.contact.Profile;
import com.socialone.service.deduplication.DeduplicationCriteria;
import com.socialone.service.match.MatchScore;
import com.socialone.service.match.word.WordMatchService;

abstract public class AbstractNameCriteria implements DeduplicationCriteria {

    final private static Predicate<String> NAME_FILTER = new Predicate<String>() {
        @Override
        public boolean apply(String name) {
            return !isNullOrEmpty(name);
        }
    };

    final private Function<SocialPersonProfile, String> NAME_EXTRACTORS;
    
    @Inject
    private WordMatchService wordMatchService;

    protected AbstractNameCriteria(Function<SocialPersonProfile, String> nameExtractor) {
        this.NAME_EXTRACTORS = checkNotNull(nameExtractor);
    }

    @Override
    public Map<Profile, MatchScore> match(Profile contact, Collection<Profile> possibleMatch) {
        Map<Profile, MatchScore> result = Maps.newHashMap();
        // Step 1. Extracting firstName from the ContactProfiles
        Collection<String> firstNames = Collections2.transform(contact.getSocialProfiles(), NAME_EXTRACTORS);
        firstNames = Collections2.filter(firstNames, NAME_FILTER);
        // Step 2. Checking each contact firstName
        for (Profile comparedContact : possibleMatch) {
            if (firstNames.size() == 0) {
                result.put(comparedContact, MatchScore.INCONCLUSIVE);
                continue;
            }
            // Step 3. Extracting first names
            Collection<String> comparedFirstNames = Collections2.transform(comparedContact.getSocialProfiles(), NAME_EXTRACTORS);
            comparedFirstNames = Collections2.filter(comparedFirstNames, NAME_FILTER);
            if (comparedFirstNames.size() == 0) {
                result.put(comparedContact, MatchScore.INCONCLUSIVE);
                continue;
            }
            MatchScore matchScore = wordMatchService.getMatch(firstNames, comparedFirstNames);
            result.put(comparedContact, matchScore);
        }
        return result;
    }
}
