package com.socialone.provider.spring.configuration;

import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.socialone.provider.spring.configuration.ProviderDataTierConfigurations;

@Configuration
@ComponentScan(basePackages = "com.socialone", excludeFilters = { @Filter(Configuration.class) })
@Import(value = {ProviderDataTierConfigurations.class, ProviderBusinessTierConfigurations.CloudFoundryConfigurations.class,
        ProviderBusinessTierConfigurations.DefaultConfigurations.class })
public class ProviderBusinessTierConfigurations {

    @Inject
    private Environment environment;

    @Configuration
    @Profile("cloud")
    static public class CloudFoundryConfigurations {

    }

    @Configuration
    @Profile(value = { "default", "test" })
    static class DefaultConfigurations {
    }
}
