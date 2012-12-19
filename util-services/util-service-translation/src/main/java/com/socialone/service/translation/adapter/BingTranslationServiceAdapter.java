package com.socialone.service.translation.adapter;

import org.springframework.stereotype.Component;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import com.socialone.service.translation.Translation;
import com.socialone.service.translation.TranslationProvider;
import com.socialone.service.translation.TranslationServiceAdapter;

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
