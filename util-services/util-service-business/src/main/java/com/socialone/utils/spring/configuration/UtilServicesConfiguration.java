package com.socialone.utils.spring.configuration;

import javax.inject.Singleton;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.socialone.service.delivery.spring.DeliveryServiceConfiguration;
import com.socialone.service.match.word.SimpleWordMatchService;
import com.socialone.service.match.word.WordMatchService;
import com.socialone.service.translation.spring.TranslationServiceConfiguration;
import com.socialone.service.translit.spring.TranslitServiceConfiguration;
import com.socialone.utils.soundmatch.spring.configuration.SoundmatchServiceConfiguration;
import com.socialone.utils.synonyms.spring.configuration.SynonymServiceConfiguration;

@Configuration
@ComponentScan(basePackages = "com.socialone", excludeFilters = { @Filter(Configuration.class) })
@Import(value = {DeliveryServiceConfiguration.class, SynonymServiceConfiguration.class, TranslationServiceConfiguration.class, TranslitServiceConfiguration.class, SoundmatchServiceConfiguration.class })
public class UtilServicesConfiguration {
    
    @Bean
    @Singleton
    public WordMatchService wordMatchService() {
        return new SimpleWordMatchService();
    }
}
