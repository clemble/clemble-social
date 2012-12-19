package com.socialone.service.translation.spring;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.relational.CloudDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@Import({ DataSourceTranslationServiceConfiguration.CloudFoundryConfigurations.class, DataSourceTranslationServiceConfiguration.DefaultConfigurations.class,
        DataSourceTranslationServiceConfiguration.TestConfigurations.class })
public class DataSourceTranslationServiceConfiguration {

    @Inject
    private DataSource dataSource;

    @Configuration
    @Profile(value = "translationCloud")
    static class CloudFoundryConfigurations {
        @Inject
        private CloudEnvironment cloudEnvironment;

        @Bean
        @Singleton
        public DataSource dataSource() {
            CloudDataSourceFactory cloudDataSourceFactory = new CloudDataSourceFactory(cloudEnvironment);
            try {
                Collection<ServiceNameTuple<DataSource>> dataSources = cloudDataSourceFactory.createInstances();
                dataSources = Collections2.filter(dataSources, new Predicate<ServiceNameTuple<DataSource>>() {
                    public boolean apply(ServiceNameTuple<DataSource> input) {
                        return input.name.equalsIgnoreCase("sna-mysql");
                    }
                });
                assert dataSources.size() == 1 : "Returned illegal DataSource";
                return dataSources.iterator().next().service;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Configuration
    @Profile(value = { "translationDefault" })
    static class DefaultConfigurations {

        @Bean
        @Singleton
        public DataSource dataSource() {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setEncoding("UTF-8");
            dataSource.setServerName("localhost");
            dataSource.setUser("mavarazy");
            dataSource.setPassword("mavarazy");
            dataSource.setDatabaseName("mavarazy");
            dataSource.setPort(3306);
            return dataSource;
        }

    }

    @Configuration
    @Profile(value = { "translationTest" })
    static class TestConfigurations {

        @Bean
        @Singleton
        public DataSource dataSource() {
            EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
            factory.setDatabaseName("com-adam-smit");
            factory.setDatabaseType(EmbeddedDatabaseType.H2);
            factory.setDatabasePopulator(databasePopulator());
            return factory.getDatabase();
        }

        // Populates DB with appropriate MySQL Schema
        private DatabasePopulator databasePopulator() {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            // populator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql", JdbcUsersConnectionRepository.class));
            // populator.addScript(new ClassPathResource("/h2-schema.sql"));
            // populator.addScript(new ClassPathResource("/test-tokens-data.sql"));
            return populator;
        }
    }

}
