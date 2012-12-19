package com.socialone.spring.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.socialone.provider.spring.configuration.ProviderBusinessTierConfigurations;
import com.socialone.service.merge.MergeService;
import com.socialone.service.merge.SimpleMergeService;
import com.socialone.spring.configuration.CommonConfiguration;
import com.socialone.spring.configuration.UserManagementServiceConfiguration;
import com.socialone.utils.spring.configuration.UtilServicesConfiguration;

@Configuration
@ComponentScan(basePackages = "com.socialone", excludeFilters = { @Filter(Configuration.class) })
@Import(value = { CommonConfiguration.class, UserManagementServiceConfiguration.class, UtilServicesConfiguration.class, ProviderBusinessTierConfigurations.class,
        IntegrationTierConfigurations.class, SocialTierConfigurations.class, BusinessTierConfigurations.CloudFoundryConfigurations.class,
        BusinessTierConfigurations.DefaultConfigurations.class })
public class BusinessTierConfigurations {

    @Inject
    private Environment environment;

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
