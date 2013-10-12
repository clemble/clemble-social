package com.clemble.social.service.translation;

public interface TranslationServiceAdapter {

    TranslationProvider getProvider();
    
    Translation translate(String word);
}
