package com.socialone.provider.spring.configuration;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

@Configuration
public class WebHttpMessageConverters {

    @Bean
    @Singleton
    public MappingJacksonHttpMessageConverter jsonHttpMessageHandler() {
        return new MappingJacksonHttpMessageConverter();
    }
    
}
