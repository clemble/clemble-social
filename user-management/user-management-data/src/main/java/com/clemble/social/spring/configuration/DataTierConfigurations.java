package com.clemble.social.spring.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;

import com.clemble.social.SimpleConnectionSignUp;

@Configuration
@ComponentScan(basePackages = "com.clemble.social.data", excludeFilters = { @Filter(Configuration.class) })
@Import(value = {DataCacheConfigurations.class, DataSourceConfigurations.class, JPAComponentsConfigurations.class})
public class DataTierConfigurations {

    @Inject
    private DataSource dataSource;

    @Bean
    @Singleton
    public ConnectionFactoryRegistry connectionFactoryRegistry() {
        return new ConnectionFactoryRegistry();
    }

    /**
     * Singleton data access object providing access to connections across all
     * users.
     */
    @Bean
    @Singleton
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryRegistry(), Encryptors.noOpText());
        // Needed to exclude signup step in authentication, remove this when
        // proper signup will be provided
        repository.setConnectionSignUp(new SimpleConnectionSignUp());
        return repository;
    }
    
}
