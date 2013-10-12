package com.clemble.social.service.match.word;

import java.util.Collection;

import com.clemble.social.service.match.MatchScore;

public interface WordMatchService {

    MatchScore getMatch(String sourceWord, String comparedWord);

    MatchScore getMatch(Collection<String> sourceWords, Collection<String> comparedWords);

}
