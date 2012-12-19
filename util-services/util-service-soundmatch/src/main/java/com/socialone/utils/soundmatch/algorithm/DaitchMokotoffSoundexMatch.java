package com.socialone.utils.soundmatch.algorithm;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.socialone.service.match.MatchScore;
import com.socialone.utils.soundmatch.SoundMatchAlgorithm;
import com.socialone.utils.soundmatch.encoder.DMSoundex;

@Component
public class DaitchMokotoffSoundexMatch extends AbstractSoundMatch {

    final private static class DaitchMokotoffSoundexPresentationComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.equals(o2))
                return MatchScore.SOLID_MATCH.getScore();
            return MatchScore.INCONCLUSIVE.getScore();
        }

    }

    protected DaitchMokotoffSoundexMatch() {
        super(new DMSoundex(), new DaitchMokotoffSoundexPresentationComparator(), SoundMatchAlgorithm.DaitchMokotoffSoundex);
    }

}
