package com.clemble.social.service.match.word;

import javax.inject.Singleton;
import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.social.utils.spring.configuration.UtilServicesConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UtilServicesConfiguration.class, AbstractBusinessTierTest.DataSourceConfiguration.class})
@ActiveProfiles(value = "test")
abstract public class AbstractBusinessTierTest {

    @Configuration
    @Profile(value = { "test" })
    public static class DataSourceConfiguration {
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
            populator.addScript(new ClassPathResource("/h2-schema.sql"));
            populator.addScript(new ClassPathResource("/test-tokens-data.sql"));
            return populator;
        }
    }
}
