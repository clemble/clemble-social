package com.clemble.social.service.translation;

import java.util.Collection;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface TranslationDataRepository {

    @CacheEvict(value = "translationCache", key = "#word")
    void put(String word, Translation translation);

    @Cacheable(value = "translationCache", key = "#word")
    Collection<Translation> get(String word);

}
