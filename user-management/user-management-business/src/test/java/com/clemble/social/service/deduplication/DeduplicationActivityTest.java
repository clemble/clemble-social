package com.clemble.social.service.deduplication;

import org.junit.Test;

public class DeduplicationActivityTest extends AbstractBusinessTierTest{

    final private String USER_IDENTIFIER = "1";

    @Test
    public void initialized() {
        
    }

/*
    @Inject // Need to do it this way, since real implementation will be covered in proxy
    private UserContactAddedEventListener<ContactMatchResultEvent> matchContactAddedEventListener;

    @Inject
    private UserRepository userRepository;

    @Test
    public void testDeduplicationActivity() {
        User user = userRepository.getUserProfile(USER_IDENTIFIER);

        Map<Integer, Collection<Entry<Profile, Profile>>> scoreMap = Maps.newTreeMap();
        
        for (Profile contact : user.getConnections()) {
            Map<Profile, Map<Profile, Integer>> score = matchContactAddedEventListener.contactAdded(new UserContactAddedEvent(USER_IDENTIFIER, contact)).getMatchScore();
            if(score.size() == 0)
                continue;
            Map<Profile, Integer> scores = score.get(contact);
            if (scores.size() > 0) {
                for (Profile scored : scores.keySet()) {
                    if(scoreMap.get(scores.get(scored)) == null) {
                        Collection<Entry<Profile, Profile>> entries = Lists.newArrayList();
                        scoreMap.put(scores.get(scored), entries);
                    }
                    scoreMap.get(scores.get(scored)).add(new ImmutablePair<Profile, Profile>(contact, scored));
                }
            }
        }
        
        for(Integer score : scoreMap.keySet()) {
            System.out.println("Score " + score);
            Collection<Entry<Profile, Profile>> scores = scoreMap.get(score);
            for(Entry<Profile, Profile> entry: scores) {
                System.out.println("   Compared " + entry.getKey() + " : " + entry.getValue());
            }
        }
    }
*/
}
