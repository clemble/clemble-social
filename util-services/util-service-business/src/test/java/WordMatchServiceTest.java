

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.socialone.service.match.MatchScore;
import com.socialone.service.match.word.WordMatchService;
import com.socialone.utils.soundmatch.AbstractBusinessTierTest;

public class WordMatchServiceTest extends AbstractBusinessTierTest {

    private String[] FULL_MATCH = { "Andrei", "Andrey", "Anna", "Anna", "Anton", "Anton", "Petrov", "Petrova", "Inokentii",
            "Inokenty", "Евгений", "Evgenii" };
    private String[] SOLID_MATCH = { "Шевчик", "Shevch", "Aleksandra", "Aleks" };
    private String[] WEAK_MATCH = { "Aleksandra", "A", "Eugene", "E", "Inokentii", "I", "Shevchik", "Sh"};

    @Autowired
    private WordMatchService soundMatchService;

    @Test
    public void testFullMatch() {
        genericCheck(FULL_MATCH, MatchScore.FULL_MATCH);
    }

    @Test
    public void testSolidMatch() {
        genericCheck(SOLID_MATCH, MatchScore.SOLID_MATCH);
    }

    @Test
    public void testWeakMatch() {
        genericCheck(WEAK_MATCH, MatchScore.WEAK_MATCH);
    }

    public void genericCheck(final String[] collection, final MatchScore matchScore) {
        // Step 1. transforming list to contacts with the only meaningful value
        // in last name
        List<String> contacts = Lists.newArrayList(collection);
        // Step 2. Processing each 2 element, as it must match with the nearest
        // one
        Assert.assertEquals(contacts.size() % 2, 0);
        for (int i = 0; i < (contacts.size() >> 1); i++) {
            // Step 3. Generating list of candidates without searched contact
            String testedString = contacts.get(i << 1);
            for (int j = 0; j < contacts.size(); j++) {
                if (j != (i << 1)) {
                    MatchScore resultScore = soundMatchService.getMatch(testedString, contacts.get(j));
                    if (j == ((i << 1) + 1)) {
                        Assert.assertEquals("Compared " + contacts.get(j) + " with " + testedString, resultScore, matchScore);
                    } else {
                        if (resultScore != MatchScore.INCONCLUSIVE && resultScore != MatchScore.WEAK_MATCH)
                            Assert.fail("Compared " + contacts.get(j) + " with " + testedString);
                    }
                }
            }
        }
    }

}
