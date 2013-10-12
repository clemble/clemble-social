package com.clemble.social.utils.soundmatch.algorithm;

import java.util.Comparator;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.springframework.stereotype.Component;

import com.clemble.social.service.match.MatchScore;
import com.clemble.social.utils.soundmatch.SoundMatchAlgorithm;

@Component
public class DoubleMetaphoneMatch extends AbstractSoundMatch {

    final private static class DoubleMetaphonePresentationComparator implements Comparator<String> {
        @Override
        public int compare(String sourcePresentaion, String comparedPresentation) {
            MatchScore matchScore = null;
            if (sourcePresentaion.startsWith(comparedPresentation) || comparedPresentation.startsWith(sourcePresentaion)) {
                matchScore =  Math.min(sourcePresentaion.length(), comparedPresentation.length()) > 2 ? MatchScore.FULL_MATCH : MatchScore.WEAK_MATCH;
            } else if (sourcePresentaion.contains(comparedPresentation) || comparedPresentation.contains(sourcePresentaion)) {
                matchScore = Math.min(sourcePresentaion.length(), comparedPresentation.length()) > 2 ? MatchScore.SOLID_MATCH : MatchScore.WEAK_MATCH;
            }
            return matchScore == null ? MatchScore.NO_MATCH.getScore() : matchScore.getScore();
        }
    }

    protected DoubleMetaphoneMatch() {
        super(new DoubleMetaphone(), new DoubleMetaphonePresentationComparator(), SoundMatchAlgorithm.DoubleMetaphone);
        ((DoubleMetaphone) getStringEncoder()).setMaxCodeLen(8);
    }

}
