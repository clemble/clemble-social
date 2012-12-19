package com.socialone.service.deduplication.criteria.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.user.contact.Profile;
import com.socialone.service.deduplication.DeduplicationCriteria;
import com.socialone.service.match.MatchScore;

@Component
public class SocialConnectionCriteria implements DeduplicationCriteria {

    @Override
    public Map<Profile, MatchScore> match(Profile contact, Collection<Profile> allContacts) {
        Map<Profile, MatchScore> resultMap = Maps.newHashMap();
        // Step 1. Generating initial ConnectionKey list
        final Collection<ConnectionKey> contactConnections = extractConnections(contact);
        // Step 2. Checking all possible candidates on connection key overlapp
        for(Profile candidate: allContacts) {
            // Step 3. Fetch all the Connection Key's from the candidate Contact
            Collection<ConnectionKey> candidateConnections = extractConnections(candidate);
            candidateConnections = Collections2.filter(candidateConnections, new Predicate<ConnectionKey>() {
                @Override
                public boolean apply(ConnectionKey connection) {
                    return contactConnections.contains(connection);
                }
            });
            // Step 4. Checking connections overlapp
            if (candidateConnections.size() > 0) {
                resultMap.put(candidate, MatchScore.FULL_MATCH);
            } else {
                resultMap.put(candidate, MatchScore.INCONCLUSIVE);
            }
            
        }
        // Step 5. Returning result map
        return resultMap;
    }
    
    private Collection<ConnectionKey> extractConnections(Profile contact) {
        // Step 0. Preparing responce for Connection keys request
        Collection<ConnectionKey> connectionKeys = Lists.newArrayList();
        // Step 1. Going through all the connections to find possible connection Key
        for(SocialPersonProfile contactProfile: contact.getSocialProfiles()) {
            connectionKeys.addAll(contactProfile.getSocialConnection().getConnectionKey());
        }
        // Step 2. Returning accumulated keys
        return connectionKeys;
    }

}
