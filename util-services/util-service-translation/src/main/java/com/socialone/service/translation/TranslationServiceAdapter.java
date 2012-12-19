package com.socialone.service.translation;

public interface TranslationServiceAdapter {

    TranslationProvider getProvider();
    
    Translation translate(String word);
}
