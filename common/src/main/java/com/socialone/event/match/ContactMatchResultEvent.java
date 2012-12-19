package com.socialone.event.match;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import com.socialone.data.user.contact.Profile;

public class ContactMatchResultEvent implements MatchEvent {

    /**
     * Generated 10/07/2012
     */
    private static final long serialVersionUID = 8461847981339649211L;
    
    final private String userIdentifier;
    
    final private Map<Profile, Map<Profile, Integer>> matchScore;
    
    public ContactMatchResultEvent(String userIdentifier, Map<Profile, Map<Profile, Integer>> matchScore) {
        this.userIdentifier = checkNotNull(userIdentifier);
        this.matchScore = checkNotNull(matchScore);
    }

    @Override
    public String getUserID() {
        return userIdentifier;
    }

    public Map<Profile, Map<Profile, Integer>> getMatchScore() {
        return matchScore;
    }
    
    public Map<Profile, Integer> getMatchScore(Profile profile) {
        return matchScore.get(profile);
    }

    public Collection<Profile> getMatchedContact() {
        return matchScore.keySet();
    }

}
