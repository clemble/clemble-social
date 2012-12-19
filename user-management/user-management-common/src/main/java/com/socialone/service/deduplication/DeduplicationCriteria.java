 package com.socialone.service.deduplication;

import java.util.Collection;
import java.util.Map;

import com.socialone.data.user.contact.Profile;
import com.socialone.service.match.MatchScore;

public interface DeduplicationCriteria {

    Map<Profile, MatchScore> match(Profile contact, Collection<Profile> allContacts);

}
