package com.socialone.utils.synonyms;

import java.util.Collection;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface SynonymWordDataRepository {

    @Cacheable(value = "synonymsCache", key = "#word")
    Collection<String> getSynonyms(String word);

    @CacheEvict(value = "synonymsCache", allEntries = true)
    void addSynonyms(String word, String synonym);

}
