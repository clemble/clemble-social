package com.socialone.service.translation.spring;

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

import com.memetix.mst.MicrosoftTranslatorAPI;
import com.socialone.service.translation.SimpleTranslationService;
import com.socialone.service.translation.TranslationDataRepository;
import com.socialone.service.translation.TranslationService;
import com.socialone.service.translation.jdbc.JdbcTranslationDataRepository;

@Configuration
@ComponentScan(basePackages = "com.socialone.translation.adapter", excludeFilters = { @Filter(Configuration.class) })
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
