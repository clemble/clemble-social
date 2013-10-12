package com.clemble.social.provider.data;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.social.provider.spring.configuration.ProviderDataTierConfigurations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProviderDataTierConfigurations.class)
@ActiveProfiles(value = "providerTest")
abstract public class AbstractProviderDataTierTest {

}
