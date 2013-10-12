package com.clemble.social.utils.synonyms.spring.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.social.utils.synonyms.SynonymWordDataRepository;
import com.clemble.social.utils.synonyms.jdbc.JdbcSynonymWordDataRepository;

@Configuration
@Import(value = { DataSourceSynonymServiceConfiguration.class })
public class SynonymServiceConfiguration {

    @Inject
    private DataSource dataSource;

    @Bean
    @Singleton
    public SynonymWordDataRepository synonymWordDataRepository() {
        return new JdbcSynonymWordDataRepository(dataSource);
    }

}
