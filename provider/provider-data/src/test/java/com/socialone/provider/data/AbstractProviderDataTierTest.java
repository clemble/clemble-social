package com.socialone.provider.data;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.socialone.provider.spring.configuration.ProviderDataTierConfigurations;
import com.stresstest.runners.spring.SpringJUnit4FrequentClassRunner;

@RunWith(SpringJUnit4FrequentClassRunner.class)
@ContextConfiguration(classes = ProviderDataTierConfigurations.class)
@ActiveProfiles(value = "providerTest")
abstract public class AbstractProviderDataTierTest {

}
