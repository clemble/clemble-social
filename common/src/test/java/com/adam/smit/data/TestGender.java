package com.adam.smit.data;

import junit.framework.Assert;

import org.junit.Test;

import com.socialone.data.Gender;

public class TestGender {

    final private static String[] male = { "m", "male", "man" };
    final private static String[] female = { "F", "FEMALE", "WOMAN", "W" };

    @Test
    public void testMaleParsing() {
        for (String identifier : male) {
            Gender expected = Gender.parse(identifier.toLowerCase());
            Assert.assertEquals(expected, Gender.M);
            expected = Gender.parse(identifier.toUpperCase());
            Assert.assertEquals(expected, Gender.M);
        }
    }

    @Test
    public void testFemaleParsing() {
        for (String identifier : female) {
            Gender expected = Gender.parse(identifier.toUpperCase());
            Assert.assertEquals(expected, Gender.W);
            expected = Gender.parse(identifier.toLowerCase());
            Assert.assertEquals(expected, Gender.W);
        }
    }
}
