package com.clemble.social.provider.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = "com.clemble.social", excludeFilters = { @Filter(Configuration.class) })
@Import(value = {ProviderDataTierConfigurations.class, ProviderBusinessTierConfigurations.CloudFoundryConfigurations.class,
        ProviderBusinessTierConfigurations.DefaultConfigurations.class })
public class ProviderBusinessTierConfigurations {

    @Configuration
    @Profile("cloud")
    static public class CloudFoundryConfigurations {

    }

    @Configuration
    @Profile(value = { "default", "test" })
    static class DefaultConfigurations {
    }
}
