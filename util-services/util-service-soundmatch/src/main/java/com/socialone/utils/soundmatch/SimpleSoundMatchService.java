package com.socialone.utils.soundmatch;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.socialone.service.match.MatchScore;
import com.socialone.utils.soundmatch.SoundMatch;
import com.socialone.utils.soundmatch.SoundMatchAlgorithm;
import com.socialone.utils.soundmatch.SoundMatchService;

@Component
public class SimpleSoundMatchService implements SoundMatchService, ApplicationContextAware{
    
    private Collection<SoundMatch> matchingAlgorithms = Lists.newArrayList();
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Extracting all registered applicationContext's
        Map<String, SoundMatch> soundMatchAlgorithms = applicationContext.getBeansOfType(SoundMatch.class);
        // Step 2. Adding all registered SoundMatch algorithms to the managed list
        matchingAlgorithms.addAll(soundMatchAlgorithms.values());
    }

    @Override
    public MatchScore bestMatch(String sourceString, String comparedStrings) {
        // Step 1. Going through all the algorithms until we rich FULL match
        Map<SoundMatchAlgorithm, MatchScore> comparisonScore = Maps.newHashMap();
        for(SoundMatch soundMatchAlgorithm: matchingAlgorithms) {
            MatchScore resultScore = soundMatchAlgorithm.match(sourceString, comparedStrings);
            comparisonScore.put(soundMatchAlgorithm.getSoundMatchAlgorithm(), resultScore);
        }
        // Step 2. Calculating average Match 
        double averageScore = 0;
        for(MatchScore matchedScore: comparisonScore.values()) {
            averageScore += matchedScore.getScore();
        }
        averageScore = averageScore / matchingAlgorithms.size();
        // Step 3. Returning appropriate match score
        return MatchScore.valueOf(averageScore);
    }


}
