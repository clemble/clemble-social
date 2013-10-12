package com.clemble.social.utils.soundmatch;

import com.clemble.social.service.match.MatchScore;

public interface SoundMatch {

    MatchScore match(String source, String compared);

    SoundMatchAlgorithm getSoundMatchAlgorithm();

}
