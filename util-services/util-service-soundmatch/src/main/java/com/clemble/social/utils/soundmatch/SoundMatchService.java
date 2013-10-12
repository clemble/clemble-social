package com.clemble.social.utils.soundmatch;

import com.clemble.social.service.match.MatchScore;

public interface SoundMatchService {

    MatchScore bestMatch(String sourceString, String comparedStrings);

}
