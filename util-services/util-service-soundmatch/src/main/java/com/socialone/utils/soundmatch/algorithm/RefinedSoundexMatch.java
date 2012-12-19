package com.socialone.utils.soundmatch.algorithm;

import java.util.Comparator;

import org.apache.commons.codec.language.RefinedSoundex;
import org.springframework.stereotype.Component;

import com.socialone.service.match.MatchScore;
import com.socialone.utils.soundmatch.SoundMatchAlgorithm;

@Component
public class RefinedSoundexMatch extends AbstractSoundMatch {
    
    final private static class SoundexPresentationComparator implements Comparator<String> {
        @Override
        public int compare(String sourcePresentaion, String comparedPresentation) {
            sourcePresentaion = StringTrimUtils.removeEnding(sourcePresentaion, "0");
            comparedPresentation = StringTrimUtils.removeEnding(comparedPresentation, "0");
            MatchScore matchScore = null;
            if (sourcePresentaion.startsWith(comparedPresentation) || comparedPresentation.startsWith(sourcePresentaion)) {
                matchScore = Math.min(sourcePresentaion.length(), comparedPresentation.length()) > 2 ? MatchScore.FULL_MATCH : MatchScore.WEAK_MATCH;
            } else if (sourcePresentaion.contains(comparedPresentation) || comparedPresentation.contains(sourcePresentaion)) {
                matchScore = Math.min(sourcePresentaion.length(), comparedPresentation.length()) > 2 ? MatchScore.SOLID_MATCH : MatchScore.WEAK_MATCH;
            }
            return matchScore == null ? MatchScore.INCONCLUSIVE.getScore() : matchScore.getScore();
        }
    }


    protected RefinedSoundexMatch() {
        super(new RefinedSoundex(), new SoundexPresentationComparator(), SoundMatchAlgorithm.RefinedSoundex);
    }

}
