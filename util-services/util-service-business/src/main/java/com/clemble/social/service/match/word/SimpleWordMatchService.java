package com.clemble.social.service.match.word;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import com.clemble.social.service.match.MatchScore;
import com.clemble.social.service.translation.Translation;
import com.clemble.social.service.translation.TranslationService;
import com.clemble.social.service.translit.TranslitService;
import com.clemble.social.utils.soundmatch.SoundMatchService;
import com.clemble.social.utils.synonyms.SynonymWordDataRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

public class SimpleWordMatchService implements WordMatchService {

    @Inject
    private TranslationService translationService;

    @Inject
    private TranslitService translitService;

    @Inject
    private SoundMatchService soundMatchService;
    
    @Inject
    private SynonymWordDataRepository synonymWordDataRepository;

    @Override
    public MatchScore getMatch(String sourceWord, String comparedWord) {
        return getMatch(ImmutableList.of(sourceWord), ImmutableList.of(comparedWord));
    }

    @Override
    public MatchScore getMatch(Collection<String> sourceWord, Collection<String> comparedWord) {
        Set<String> sourceNameSet = getAllCombinations(sourceWord);
        Set<String> comparedNameSet = getAllCombinations(comparedWord);
        // Step 4. Searching for name intersection
        @SuppressWarnings("unchecked")
        Collection<String> intersection = CollectionUtils.intersection(sourceNameSet, comparedNameSet);
        if (intersection.size() > 0)
            return MatchScore.FULL_MATCH;
        MatchScore highestScore = MatchScore.NO_MATCH;
        for (String name : sourceNameSet) {
            for (String comparedName : comparedNameSet) {
                MatchScore match = soundMatchService.bestMatch(name, comparedName);
                highestScore = match.getScore() > highestScore.getScore() ? match : highestScore;
            }
        }
        return highestScore;
    }

    private Set<String> getAllCombinations(Collection<String> sourceNames) {
        Set<String> sourceNameSet = Sets.newHashSet(sourceNames);
        for (String sourceName : sourceNames) {
            // Step 1. Adding all translations
            Collection<Translation> translations = translationService.translate(sourceName);
            for (Translation translation : translations) {
                sourceNameSet.add(translation.getTranslation());
            }
            // Step 2. Adding all translit values
            Collection<String> translitStrings = translitService.translit(sourceName);
            sourceNameSet.addAll(translitStrings);
            // Step 3. Adding all synonyms values
            Collection<String> synonyms = synonymWordDataRepository.getSynonyms(sourceName);
            sourceNameSet.addAll(synonyms);
        }
        return normalize(sourceNameSet);
    }
    
    private Set<String> normalize(Collection<String> words) {
        Set<String> normalizedCollection = Sets.newHashSet();
        for(String word: words) {
            normalizedCollection.add(normalize(word));
        }
        return normalizedCollection;
    }
    
    private String normalize(String word) {
        return isNullOrEmpty(word) ? "" : word.toUpperCase();
    }
}