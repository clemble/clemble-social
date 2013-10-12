package com.clemble.social.utils.soundmatch;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface SoundMatchDataRepository {

    @Cacheable(value = "soundMatchCache", key="#word")
    String get(String word, SoundMatchAlgorithm matchAlgorithm);

    @CacheEvict(value = "soundMatchCache", key="#word")
    void put(String word, String presentation, SoundMatchAlgorithm matchAlgorithm);

}
