package com.clemble.social.service.translation.spring;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.clemble.social.service.translation.SimpleTranslationService;
import com.clemble.social.service.translation.TranslationDataRepository;
import com.clemble.social.service.translation.TranslationService;
import com.clemble.social.service.translation.jdbc.JdbcTranslationDataRepository;
import com.memetix.mst.MicrosoftTranslatorAPI;

@Configuration
@ComponentScan(basePackages = "com.clemble.social.translation.adapter", excludeFilters = { @Filter(Configuration.class) })
@Import(value = { DataSourceTranslationServiceConfiguration.class, TranslationServiceConfiguration.CloudFoundryConfigurations.class,
        TranslationServiceConfiguration.DefaultConfigurations.class })
public class TranslationServiceConfiguration {

    @Inject
    private Environment environment;

    @Inject
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        // Step 1. Initializing Microsoft Bing translator access
        MicrosoftTranslatorAPI.setClientId(environment.getProperty("microsoft.client.identifier"));
        MicrosoftTranslatorAPI.setClientSecret(environment.getProperty("microsoft.client.secret"));
    }

    @Bean
    @Singleton
    public TranslationService translationService() {
        return new SimpleTranslationService();
    }

    @Bean
    @Singleton
    public TranslationDataRepository translationDataRepository() {
        return new JdbcTranslationDataRepository(dataSource);
    }

    @Configuration
    @Profile("cloud")
    @PropertySource("classpath:translationServiceCriteria.properties")
    static public class CloudFoundryConfigurations {

    }

    @Configuration
    @Profile(value = { "default", "test" })
    @PropertySource("classpath:localhostTranslationServiceCriteria.properties")
    static class DefaultConfigurations {
    }
}
