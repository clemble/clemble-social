package com.socialone.service.translit;


public interface LanguageTranslit {

    String toTranslit(String sourceString);

    String getLocale();
}
