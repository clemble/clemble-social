package com.clemble.social.service.translation;


import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

@Ignore
public class TranslatorTest extends AbstractBusinessTierTest {

    @Test
    public void testTranslationWorks() throws Exception {
        String anotherResult = Translate.execute("Привет", Language.RUSSIAN, Language.ENGLISH);
        Assert.assertEquals("Hello", anotherResult);
        
        String result = Translate.execute("Hello", Language.ENGLISH, Language.RUSSIAN);
        Assert.assertEquals("Привет", result);
    }
}
