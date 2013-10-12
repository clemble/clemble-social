package com.clemble.social.service.translit;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Component
public class SimpleTranslitService implements TranslitService, ApplicationContextAware {

    final private Map<String, Collection<LanguageTranslit>> languageTranslitMap = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Retrieving all registered translit services
        Map<String, LanguageTranslit> translitServices = applicationContext.getBeansOfType(LanguageTranslit.class);
        // Step 2. Populating map with the Translit values
        for (LanguageTranslit languageTranslit : translitServices.values()) {
            if (languageTranslitMap.get(languageTranslit.getLocale()) != null) {
                languageTranslitMap.get(languageTranslit.getLocale()).add(languageTranslit);
            } else {
                languageTranslitMap.put(languageTranslit.getLocale(), Lists.newArrayList(languageTranslit));
            }
        }

    }

    @Override
    public Collection<String> translit(String sourceString) {
        // Step 1. Detecting locale of the laguage
        String locale = detectLocale(sourceString);
        // Step 2. Returning appropriate Translit presentations
        return translit(sourceString, locale);
    }

    @Override
    public Collection<String> translit(String sourceString, String sourceLocale) {
        // Step 1. Returning translit values
        return translit(sourceString, languageTranslitMap.get(sourceLocale));
    }
    
    private Collection<String> translit(String sourceString, Collection<LanguageTranslit> languageTranslits) {
        // Step 1. Sanity check
        if(languageTranslits == null || languageTranslits.size() == 0)
            return Collections.emptyList();
        // Step 2. Gather all possible translit values in a set
        Collection<String> resultSet = Sets.newHashSet(sourceString);
        for(LanguageTranslit languageTranslit: languageTranslits){
            resultSet.add(languageTranslit.toTranslit(sourceString));
        }
        // Step 3. Returning result set of translit values.
        return resultSet;
    }
    
    private String detectLocale(String sourceString) {
        return "ru";
    }

}
