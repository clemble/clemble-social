package com.socialone.utils.soundmatch.algorithm;

import static com.google.common.base.Strings.isNullOrEmpty;

public class StringTrimUtils {

    public static String removeEnding(String sourceString, String character) {
        // Step 1. Sanity check
        if(isNullOrEmpty(sourceString) || isNullOrEmpty(character))
            return sourceString;
        // Step 2. Removing character from the end
        while(sourceString.endsWith(character)) {
            sourceString = sourceString.substring(0, sourceString.length() - character.length());
        }
        // Step 3. Returning appropriate result
        return sourceString;
    }
}
