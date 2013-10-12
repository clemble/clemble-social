package com.clemble.social.service.translation;

import java.util.Collection;

public interface TranslationService {

    /**
     * Translates data to English.
     * 
     * @param value - value to translate
     * @return translated value
     */
    Collection<Translation> translate(String word);

}
