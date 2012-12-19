package com.socialone;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataContextStartupTest extends AbstractDataTierTest {

    @Autowired
    private DataSource dataSource;
    
    @Test
    public void initialize() {
        checkNotNull(dataSource);
    }
}
