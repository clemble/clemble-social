package com.clemble.social.service.translation.adapter;

import org.springframework.stereotype.Component;

import com.clemble.social.service.translation.Translation;
import com.clemble.social.service.translation.TranslationProvider;
import com.clemble.social.service.translation.TranslationServiceAdapter;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

@Component
public class BingTranslationServiceAdapter implements TranslationServiceAdapter {

    @Override
    public Translation translate(String word) {
        String translation = null;
        try {
            translation = Translate.execute(word, Language.ENGLISH);
            if (translation.contains("ArgumentOutOfRangeException")) {
                translation = word;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (translation != null && translation.length() > 0) ? new Translation(translation, TranslationProvider.Bing) : null;
    }

    @Override
    public TranslationProvider getProvider() {
        return TranslationProvider.Bing;
    }

}
