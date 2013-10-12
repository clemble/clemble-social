 package com.clemble.social.service.deduplication;

import java.util.Collection;
import java.util.Map;

import com.clemble.social.data.user.contact.Profile;
import com.clemble.social.service.match.MatchScore;

public interface DeduplicationCriteria {

    Map<Profile, MatchScore> match(Profile contact, Collection<Profile> allContacts);

}
