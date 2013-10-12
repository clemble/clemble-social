package com.clemble.social.utils.soundmatch.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import com.clemble.social.utils.soundmatch.SimpleSoundMatchService;
import com.clemble.social.utils.soundmatch.SoundMatchService;

@Configuration
@ComponentScan(basePackages = "com.clemble.social.utils.soundmatch", excludeFilters = { @Filter(Configuration.class) })
// Do not use CACHE it is not effective 
// @Import(value = { DataSourceSoundmatchServiceConfiguration.class })
public class SoundmatchServiceConfiguration {

    @Bean
    public SoundMatchService soundMatchService() {
        return new SimpleSoundMatchService();
    }

}
