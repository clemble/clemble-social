package com.socialone.service.match.word;

import java.util.Collection;

import com.socialone.service.match.MatchScore;

public interface WordMatchService {

    MatchScore getMatch(String sourceWord, String comparedWord);

    MatchScore getMatch(Collection<String> sourceWords, Collection<String> comparedWords);

}
