package com.socialone.service.translit.spring;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import com.socialone.service.translit.SimpleTranslitService;
import com.socialone.service.translit.TranslitService;

@Configuration
@ComponentScan(basePackages = "com.socialone.service.translit", excludeFilters = { @Filter(Configuration.class) })
public class TranslitServiceConfiguration {

    @Bean
    @Singleton
    public TranslitService translitService() {
        return new SimpleTranslitService();
    }
}
