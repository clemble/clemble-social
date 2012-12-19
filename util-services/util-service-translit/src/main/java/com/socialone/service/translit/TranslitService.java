package com.socialone.service.translit;

import java.util.Collection;

public interface TranslitService {

    Collection<String> translit(String sourceString);

    Collection<String> translit(String sourceString, String sourceLocale);

}
