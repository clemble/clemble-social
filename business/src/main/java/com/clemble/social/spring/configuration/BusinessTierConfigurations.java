package com.clemble.social.spring.configuration;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.clemble.social.provider.spring.configuration.ProviderBusinessTierConfigurations;
import com.clemble.social.service.merge.MergeService;
import com.clemble.social.service.merge.SimpleMergeService;
import com.clemble.social.spring.configuration.CommonConfiguration;
import com.clemble.social.spring.configuration.UserManagementServiceConfiguration;
import com.clemble.social.utils.spring.configuration.UtilServicesConfiguration;

@Configuration
@ComponentScan(basePackages = "com.clemble.social", excludeFilters = { @Filter(Configuration.class) })
@Import(value = { CommonConfiguration.class, UserManagementServiceConfiguration.class, UtilServicesConfiguration.class, ProviderBusinessTierConfigurations.class,
        IntegrationTierConfigurations.class, SocialTierConfigurations.class, BusinessTierConfigurations.CloudFoundryConfigurations.class,
        BusinessTierConfigurations.DefaultConfigurations.class })
public class BusinessTierConfigurations {

    @Bean
    @Singleton
    public MergeService mergeService() {
        return new SimpleMergeService();
    }

    @Configuration
    @Profile("cloud")
    @PropertySource("classpath:socialNetworkAccess.properties")
    static public class CloudFoundryConfigurations {

    }

    @Configuration
    @Profile(value = { "default", "test" })
    @PropertySource("classpath:localhostSocialNetworkAccess.properties")
    static class DefaultConfigurations {
    }
}
