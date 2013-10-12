package com.clemble.social.connection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;

public class BusinessContextStartupTest extends AbstractBusinessTierTest {
    
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Test
    public void contextInitialized() {
        Assert.assertNotNull(connectionFactoryLocator);
    }
}
