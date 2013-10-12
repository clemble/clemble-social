package com.clemble.social.event.match;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.event.match.ContactMatchResultEvent;
import com.clemble.social.event.match.ContactMatchResultEventListener;
import com.clemble.social.service.match.MatchScore;
import com.clemble.social.service.user.contact.ProfileRepository;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
@Transactional
public class ContactMergeListener implements ContactMatchResultEventListener<Void> {

    final private static Integer MERGE_SCORE = MatchScore.FULL_MATCH.getScore() * 2;
    
    @Inject
    private ProfileRepository profileRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Void contactMatched(ContactMatchResultEvent contactMatchResultEvent) {
        for (Profile matchedContact : contactMatchResultEvent.getMatchedContact()) {
            Map<Profile, Integer> scoreBoard = contactMatchResultEvent.getMatchScore(matchedContact);
            // Step 1. Filtering candidates which are a certain Match
            Map<Profile, Integer> certainMatchMap = Maps.filterValues(scoreBoard, new Predicate<Integer>() {
                @Override
                public boolean apply(Integer matchScore) {
                    return matchScore >= MERGE_SCORE;
                }
            });
            if (certainMatchMap.size() > 0) {
                // Step 2. Retrieving Contact identifiers
                Collection<String> contactIdentifiers = Lists.newArrayList(Collections2.transform(certainMatchMap.keySet(), new Function<Profile, String>() {
                    @Override
                    public String apply(Profile contact) {
                        return contact.getProfileId();
                    }
                }));
                // Step 3. Since the list does not contain added contact adding it
                contactIdentifiers.add(matchedContact.getProfileId());
                // Step 4. Merging contacts
                Iterator<String> contactIterator = contactIdentifiers.iterator();
                String primaryProfile = contactIterator.next();
                while(contactIterator.hasNext())
                    profileRepository.unite(primaryProfile, contactIterator.next());
            }
        }
        return null;
    }
}
