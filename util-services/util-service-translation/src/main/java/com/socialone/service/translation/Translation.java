package com.socialone.service.translation;

import static com.google.common.base.Preconditions.checkNotNull;

public class Translation {

    final private String translation;

    final private TranslationProvider provider;

    public Translation(String translation, TranslationProvider provider) {
        this.translation = checkNotNull(translation);
        this.provider = checkNotNull(provider);
    }

    public String getTranslation() {
        return translation;
    }

    public TranslationProvider getProvider() {
        return provider;
    }
}
