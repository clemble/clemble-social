package com.clemble.social.spring.configuration;

import javax.inject.Singleton;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.FormattingConversionService;

import com.google.common.eventbus.EventBus;

@Configuration
@Import(value = CommonConfiguration.CloudFoundryConfigurations.class)
public class CommonConfiguration {
    
    @Bean
    @Singleton
    public GenericConversionService mvcConversionService() {
        // This is minimum class requirement to be able to use it in Web Context 
        return new FormattingConversionService();
    }
    
    @Bean
    @Singleton
    public EventBus eventBus() {
        // For now single event bus will be used
        return new EventBus();
    }

    @Configuration
    @Profile(value = "cloud")
    static class CloudFoundryConfigurations {

        @Bean
        public CloudEnvironment cloudEnvironment() {
            return new CloudEnvironment();
        }

    }

}
