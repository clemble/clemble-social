package com.clemble.social.service.translit.spring;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import com.clemble.social.service.translit.SimpleTranslitService;
import com.clemble.social.service.translit.TranslitService;

@Configuration
@ComponentScan(basePackages = "com.clemble.social.service.translit", excludeFilters = { @Filter(Configuration.class) })
public class TranslitServiceConfiguration {

    @Bean
    @Singleton
    public TranslitService translitService() {
        return new SimpleTranslitService();
    }
}
