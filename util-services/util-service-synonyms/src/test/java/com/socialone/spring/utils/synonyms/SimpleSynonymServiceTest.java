package com.socialone.spring.utils.synonyms;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.RunInParallel;
import org.springframework.test.context.SpringJUnit4FrequentClassRunner;

import com.socialone.utils.synonyms.SynonymWordDataRepository;

@RunWith(SpringJUnit4FrequentClassRunner.class)
public class SimpleSynonymServiceTest extends AbstractSynonymsTest {

    @Inject
    private SynonymWordDataRepository synonymWordDataRepository;

    @Test
    public void testAddNew() {
        String randomWord = RandomStringUtils.random(10);
        String firstSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(randomWord, firstSynonym);

        Collection<String> synonyms = synonymWordDataRepository.getSynonyms(randomWord);
        assertEquals(synonyms.size(), 2);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));

        synonyms = synonymWordDataRepository.getSynonyms(firstSynonym);
        assertEquals(synonyms.size(), 2);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
    }

    @Test
    public void testAddToOriginal() {
        String randomWord = RandomStringUtils.random(10);
        String firstSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(randomWord, firstSynonym);

        String anotherSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(randomWord, anotherSynonym);

        Collection<String> synonyms = synonymWordDataRepository.getSynonyms(randomWord);
        assertEquals(synonyms.size(), 3);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
        assertTrue(synonyms.contains(anotherSynonym));

        synonyms = synonymWordDataRepository.getSynonyms(firstSynonym);
        assertEquals(synonyms.size(), 3);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
        assertTrue(synonyms.contains(anotherSynonym));

        synonyms = synonymWordDataRepository.getSynonyms(anotherSynonym);
        assertEquals(synonyms.size(), 3);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
        assertTrue(synonyms.contains(anotherSynonym));
    }

    @Test
    public void testAddToSynonym() {
        String randomWord = RandomStringUtils.random(10);
        String firstSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(randomWord, firstSynonym);

        String anotherSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(anotherSynonym, firstSynonym);

        Collection<String> synonyms = synonymWordDataRepository.getSynonyms(randomWord);
        assertEquals(synonyms.size(), 3);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
        assertTrue(synonyms.contains(anotherSynonym));

        synonyms = synonymWordDataRepository.getSynonyms(firstSynonym);
        assertEquals(synonyms.size(), 3);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
        assertTrue(synonyms.contains(anotherSynonym));

        synonyms = synonymWordDataRepository.getSynonyms(anotherSynonym);
        assertEquals(synonyms.size(), 3);
        assertTrue(synonyms.contains(randomWord));
        assertTrue(synonyms.contains(firstSynonym));
        assertTrue(synonyms.contains(anotherSynonym));
    }

    @Test
    public void testCombineBranches() {
        String firstBranch = RandomStringUtils.random(10);
        String firstSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(firstBranch, firstSynonym);

        String secondBranch = RandomStringUtils.random(10);
        String secondSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(secondBranch, secondSynonym);

        synonymWordDataRepository.addSynonyms(firstBranch, secondBranch);

        Collection<String> expectedString = Arrays.asList(new String[] { firstBranch, firstSynonym, secondBranch, secondSynonym });

        Collection<String> synonyms = synonymWordDataRepository.getSynonyms(firstBranch);
        assertEquals(synonyms.size(), expectedString.size());
        assertTrue(synonyms.containsAll(expectedString));

        synonyms = synonymWordDataRepository.getSynonyms(firstSynonym);
        assertEquals(synonyms.size(), expectedString.size());
        assertTrue(synonyms.containsAll(expectedString));

        synonyms = synonymWordDataRepository.getSynonyms(secondBranch);
        assertEquals(synonyms.size(), expectedString.size());
        assertTrue(synonyms.containsAll(expectedString));

        synonyms = synonymWordDataRepository.getSynonyms(secondSynonym);
        assertEquals(synonyms.size(), expectedString.size());
        assertTrue(synonyms.containsAll(expectedString));
    }

    @Test
    public void testNonExistent() {
        String randomWord = RandomStringUtils.random(10);

        Collection<String> synonyms = synonymWordDataRepository.getSynonyms(randomWord);
        assertTrue(synonyms.isEmpty());
    }
    
    @Test
    @RunInParallel
    public void testAddTheSameInParallel() {
        String fixedWord = "fixedWord";
        String firstSynonym = RandomStringUtils.random(10);
        synonymWordDataRepository.addSynonyms(fixedWord, firstSynonym);

        Collection<String> synonyms = synonymWordDataRepository.getSynonyms(fixedWord);
        assertTrue(synonyms.contains(fixedWord));
        assertTrue(synonyms.contains(firstSynonym));

        
    }
}
