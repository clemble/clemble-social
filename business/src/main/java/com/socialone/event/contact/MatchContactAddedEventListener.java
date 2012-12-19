package com.socialone.event.contact;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.socialone.data.user.contact.Profile;
import com.socialone.event.contact.UserContactAddedEvent;
import com.socialone.event.contact.UserContactAddedEventListener;
import com.socialone.event.match.ContactMatchResultEvent;
import com.socialone.service.deduplication.DeduplicationCriteria;
import com.socialone.service.deduplication.DeduplicationCriteriaFactory;
import com.socialone.service.match.MatchScore;
import com.socialone.service.user.UserRepository;

@Component
@Transactional
public class MatchContactAddedEventListener implements UserContactAddedEventListener<ContactMatchResultEvent>{

    final private static Predicate<MatchScore> CONTINUE_MATCHING_CRITERIA = new Predicate<MatchScore>() {
        @Override
        public boolean apply(MatchScore score) {
            return !MatchScore.NO_MATCH.equals(score);
        }
    };

    final private static Function<Profile, String> PROFILE_ID_EXTRACTOR = new Function<Profile, String>() {
        @Override
        public String apply(Profile input) {
            return input.getProfileId();
        }
    };
    
    final private static Predicate<Integer> SCORE_POSITIVE_PREDICATE = new Predicate<Integer>() {
        @Override
        public boolean apply(Integer input) {
            return input != null && input > 0;
        }
    };

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private DeduplicationCriteriaFactory criteriaFactory;

    @Publisher
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ContactMatchResultEvent contactAdded(UserContactAddedEvent contactAddedEvent) {
        // Step 1. Sanity Check for null
        if (contactAddedEvent == null)
            return null;
        // Step 2. Checking possible candidates
        final Collection<? extends Profile> addedContacts = contactAddedEvent.getContacts();
        final Collection<String> addedContactIdentifier = Collections2.transform(addedContacts, PROFILE_ID_EXTRACTOR);
        // Step 3. Getting all contacts without searched contact
        Collection<? extends Profile> allContacts = userRepository.getUserProfile(contactAddedEvent.getUserID()).getConnections();
        allContacts = Collections2.filter(allContacts, new Predicate<Profile>() {
            @Override
            public boolean apply(Profile contact) {
                return !addedContactIdentifier.contains(contact.getProfileId());
            }
        });
        // Step 4. Generating contact match result event
        Map<Profile, Map<Profile, Integer>> contactMatchMap = Maps.newHashMap();
        for (Profile addedContact : addedContacts) {
            // Step 4. Preparing score board
            Map<Profile, Integer> scoreBoard = Maps.newHashMap();
            for (Profile contact : allContacts) {
                scoreBoard.put(contact, Integer.valueOf(0));
            }
            // Step 5. Going through all the DeduplicationCriterias registered
            for (DeduplicationCriteria deduplicationCriteria : criteriaFactory.getRegisteredCriterias()) {
                // If there is no more match candidates leave
                if (scoreBoard.size() == 0)
                    break;
                // Step 1. Get Matched values and filter out NO_MATCH values
                final Map<Profile, MatchScore> matched = deduplicationCriteria.match(addedContact, scoreBoard.keySet());
                // Step 2. Add new scores and remove unapplicable
                for (Profile contact : matched.keySet()) {
                    if (CONTINUE_MATCHING_CRITERIA.apply(matched.get(contact))) {
                        scoreBoard.put(contact, scoreBoard.get(contact) + matched.get(contact).getScore());
                    } else {
                        scoreBoard.remove(contact);
                    }
                }
            }
            scoreBoard = Maps.filterValues(scoreBoard, SCORE_POSITIVE_PREDICATE);
            // Step 6. Merging candidates that pass the merge limit
            if (scoreBoard.size() > 0) {
                contactMatchMap.put(addedContact, scoreBoard);
            }
        }
        return new ContactMatchResultEvent(contactAddedEvent.getUserID(), contactMatchMap);
    }

}
