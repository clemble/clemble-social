package com.socialone.data;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.Lists;

public enum Gender {
    M("M", "MALE", "MAN"), W("F", "FEMALE", "WOMAN", "W");
    
    final private Collection<String> possiblePresentations = Lists.newArrayList();
    
    private Gender(String ... names) {
        possiblePresentations.addAll(Arrays.asList(names));
    }
    
    public static Gender parse(String gender) {
        // Step 1. Sanity check
        if(isNullOrEmpty(gender))
            return null;
        // Step 2. Checking for male
        gender = gender.toUpperCase();
        if(M.possiblePresentations.contains(gender)) {
            return Gender.M;
        } else if(W.possiblePresentations.contains(gender)) {
            return Gender.W;
        } else {
            return null;
        }
    }
}
