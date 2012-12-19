package com.socialone.utils.soundmatch;

import com.socialone.service.match.MatchScore;

public interface SoundMatch {

    MatchScore match(String source, String compared);

    SoundMatchAlgorithm getSoundMatchAlgorithm();

}
