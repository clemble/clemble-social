package com.socialone.service.deduplication.impl;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.socialone.data.user.contact.Profile;
import com.socialone.service.deduplication.DeduplicationCriteria;
import com.socialone.service.deduplication.DeduplicationCriteriaFactory;
import com.socialone.service.deduplication.DeduplicationService;
import com.socialone.service.match.MatchScore;
import com.socialone.service.user.UserRepository;

public class SimpleDeduplicationService implements DeduplicationService {

    final private static Predicate<MatchScore> CONTINUE_MATCHING_CRITERIA = new Predicate<MatchScore>() {
        @Override
        public boolean apply(MatchScore score) {
            return !MatchScore.NO_MATCH.equals(score);
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
    private DeduplicationCriteriaFactory deduplicationCriteriaFactory;

    @Override
    public void checkForDuplicates(String userID, String providerID) {
        Collection<? extends Profile> allContacts = userRepository.getUserProfile(userID).getConnections();
        for (Profile contactToCheck : allContacts) {
            Map<Profile, Map<Profile, Integer>> match = checkForDuplicates(allContacts, contactToCheck);
        }
    }

    @Override
    public void checkForDuplicates(final String userID, final String providerID, final String contactID) {
        Collection<? extends Profile> allContacts = userRepository.getUserProfile(userID).getConnections();
        Collection<? extends Profile> checkedContacts = Collections2.filter(allContacts, new Predicate<Profile>() {
            @Override
            public boolean apply(Profile contact) {
                return contact.getProfileId().equals(contactID);
            }
        });
        if (checkedContacts.isEmpty() || checkedContacts.size() > 1)
            return;

        checkForDuplicates(allContacts, allContacts.iterator().next());
    }

    private Map<Profile, Map<Profile, Integer>> checkForDuplicates(final Collection<? extends Profile> contacts, final Profile contactToCheck) {
        Collection<? extends Profile> allContacts = Collections2.filter(contacts, new Predicate<Profile>() {
            @Override
            public boolean apply(Profile contact) {
                return !contact.getProfileId().equals(contactToCheck.getProfileId());
            }
        });
        // Step 4. Generating contact match result event
        Map<Profile, Map<Profile, Integer>> contactMatchMap = Maps.newHashMap();
        // Step 4. Preparing score board
        Map<Profile, Integer> scoreBoard = Maps.newHashMap();
        for (Profile contact : allContacts) {
            scoreBoard.put(contact, Integer.valueOf(0));
        }
        // Step 5. Going through all the DeduplicationCriterias registered
        for (DeduplicationCriteria deduplicationCriteria : deduplicationCriteriaFactory.getRegisteredCriterias()) {
            // If there is no more match candidates leave
            if (scoreBoard.size() == 0)
                break;
            // Step 1. Get Matched values and filter out NO_MATCH values
            final Map<Profile, MatchScore> matched = deduplicationCriteria.match(contactToCheck, scoreBoard.keySet());
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
            contactMatchMap.put(contactToCheck, scoreBoard);
        }
        return contactMatchMap;
    }
}
