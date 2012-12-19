package com.adam.smit.data;

import junit.framework.Assert;

import org.junit.Test;

import com.socialone.data.Gender;
import com.socialone.test.utils.TestRandomUtils;

public class TestGender {

    final private static String[] male = { "m", "male", "man" };
    final private static String[] female = { "F", "FEMALE", "WOMAN", "W" };

    @Test
    public void testMaleParsing() {
        for (String identifier : male) {
            Gender expected = Gender.parse(TestRandomUtils.randomCase(identifier));
            Assert.assertEquals(expected, Gender.M);
        }
    }

    @Test
    public void testFeMaleParsing() {
        for (String identifier : female) {
            Gender expected = Gender.parse(TestRandomUtils.randomCase(identifier));
            Assert.assertEquals(expected, Gender.W);
        }
    }
}
