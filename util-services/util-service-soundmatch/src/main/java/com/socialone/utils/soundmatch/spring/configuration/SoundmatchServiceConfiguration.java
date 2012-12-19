package com.socialone.utils.soundmatch.spring.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.socialone.utils.soundmatch.SimpleSoundMatchService;
import com.socialone.utils.soundmatch.SoundMatchService;

@Configuration
@ComponentScan(basePackages = "com.socialone.utils.soundmatch", excludeFilters = { @Filter(Configuration.class) })
// Do not use CACHE it is not effective 
// @Import(value = { DataSourceSoundmatchServiceConfiguration.class })
public class SoundmatchServiceConfiguration {

    @Inject
    private Environment environment;

    @Bean
    @Singleton
    public SoundMatchService soundMatchService() {
        return new SimpleSoundMatchService();
    }

}
