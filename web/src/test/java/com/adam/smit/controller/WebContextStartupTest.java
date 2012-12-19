package com.adam.smit.controller;

import junit.framework.Assert;

import org.junit.Test;

public class WebContextStartupTest extends AbstractWebTierTest {

    @Test
    public void initialized() {
        Assert.assertNotNull(getMockMvcBuilder());
    }

}
