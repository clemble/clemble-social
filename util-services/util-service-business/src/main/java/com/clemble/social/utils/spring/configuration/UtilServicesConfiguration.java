package com.clemble.social.utils.spring.configuration;

import javax.inject.Singleton;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.social.service.delivery.spring.DeliveryServiceConfiguration;
import com.clemble.social.service.match.word.SimpleWordMatchService;
import com.clemble.social.service.match.word.WordMatchService;
import com.clemble.social.service.translation.spring.TranslationServiceConfiguration;
import com.clemble.social.service.translit.spring.TranslitServiceConfiguration;
import com.clemble.social.utils.soundmatch.spring.configuration.SoundmatchServiceConfiguration;
import com.clemble.social.utils.synonyms.spring.configuration.SynonymServiceConfiguration;

@Configuration
@ComponentScan(basePackages = "com.clemble.social", excludeFilters = { @Filter(Configuration.class) })
@Import(value = {DeliveryServiceConfiguration.class, SynonymServiceConfiguration.class, TranslationServiceConfiguration.class, TranslitServiceConfiguration.class, SoundmatchServiceConfiguration.class })
public class UtilServicesConfiguration {
    
    @Bean
    @Singleton
    public WordMatchService wordMatchService() {
        return new SimpleWordMatchService();
    }
}
