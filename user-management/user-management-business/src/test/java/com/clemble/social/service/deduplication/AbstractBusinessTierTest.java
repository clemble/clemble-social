package com.clemble.social.service.deduplication;


import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.social.spring.configuration.UserManagementServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserManagementServiceConfiguration.class)
@ActiveProfiles(value = "test")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
abstract public class AbstractBusinessTierTest {

}
