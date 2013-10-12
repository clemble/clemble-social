package com.clemble.social.service.translation;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

@Component
public class SimpleTranslationService implements TranslationService, ApplicationContextAware {
    @Inject
    private TranslationDataRepository translationServiceRepository;

    private Map<TranslationProvider, TranslationServiceAdapter> TRANSLATION_MAP = Maps.newHashMap();
    
    private Function<Translation, TranslationProvider> TRANSLATION_PROVIDER_EXTRACTOR = new Function<Translation, TranslationProvider>(){
        @Override
        public TranslationProvider apply(Translation translation) {
            return translation.getProvider();
        }
    };

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Retrieving all TranslationAdapters
        Map<String, TranslationServiceAdapter> translationServiceAdapters = applicationContext.getBeansOfType(TranslationServiceAdapter.class);
        // Step 2. Populating them to the Map
        for (TranslationServiceAdapter translationAdapter : translationServiceAdapters.values()) {
            TRANSLATION_MAP.put(translationAdapter.getProvider(), translationAdapter);
        }
    }

    @Override
    public Collection<Translation> translate(String word) {
        // Step 1. Retrieving list of valid translations
        Collection<Translation> translations = translationServiceRepository.get(word);
        // Step 2. Filtering out translation already done
        Collection<TranslationProvider> translationProviders = Collections2.transform(translations, TRANSLATION_PROVIDER_EXTRACTOR);
        // Step 3. Checking that word already been translated by all the registered adapters
        if(!translationProviders.containsAll(TRANSLATION_MAP.keySet())) {
            for(TranslationProvider translationProvider: TRANSLATION_MAP.keySet()) {
                if(!translationProviders.contains(translationProvider)) {
                    // Step 4. Found not user Translator trying to translate value
                    TranslationServiceAdapter translationServiceAdapter = TRANSLATION_MAP.get(translationProvider);
                    Translation translation = translationServiceAdapter.translate(word);
                    if(translation != null) {
                        // Step 5. Saving translations
                        translationServiceRepository.put(word, translation);
                        translations.add(translation);
                    }
                }
            }
        }
        // Step 6. Returning translations Map
        return translations;
    }
}
