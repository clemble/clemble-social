package com.clemble.social.service.translit;


public interface LanguageTranslit {

    String toTranslit(String sourceString);

    String getLocale();
}
