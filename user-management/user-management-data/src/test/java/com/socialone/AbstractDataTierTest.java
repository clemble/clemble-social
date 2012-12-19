package com.socialone;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.socialone.spring.configuration.DataTierConfigurations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataTierConfigurations.class)
@ActiveProfiles(value = "test")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
abstract public class AbstractDataTierTest {

}
