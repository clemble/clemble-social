package com.clemble.social.utils.soundmatch.algorithm;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.clemble.social.service.match.MatchScore;
import com.clemble.social.utils.soundmatch.SoundMatchAlgorithm;
import com.clemble.social.utils.soundmatch.encoder.DMSoundex;

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
