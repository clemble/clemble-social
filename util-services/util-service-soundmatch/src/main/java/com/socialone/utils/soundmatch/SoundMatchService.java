package com.socialone.utils.soundmatch;

import com.socialone.service.match.MatchScore;

public interface SoundMatchService {

    MatchScore bestMatch(String sourceString, String comparedStrings);

}
