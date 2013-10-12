package com.clemble.social.provider.spring.configuration;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.relational.CloudDataSourceFactory;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.clemble.social.provider.service.ProviderConfigurationRepository;
import com.clemble.social.provider.service.jpa.JpaProviderConfigurationRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@EnableTransactionManagement
@Import({ ProviderDataTierConfigurations.JPAConfigurations.class, ProviderDataTierConfigurations.CloudFoundryConfigurations.class,
        ProviderDataTierConfigurations.DefaultConfigurations.class, ProviderDataTierConfigurations.TestConfigurations.class })
public class ProviderDataTierConfigurations {

    @Bean
    @Singleton
    public ProviderConfigurationRepository providerConfigurationsRepository() {
        return new JpaProviderConfigurationRepository();
    }

    @Configuration
    @Profile(value = { "providerCloud", "providerDefault", "providerTest" })
    static class JPAConfigurations {
        @Inject
        @Named("providerDataSource")
        private DataSource providerDataSource;

        @Inject
        private JpaVendorAdapter providerJPAVendorAdapter;

        @Inject
        public JpaDialect providerJPADialect() {
            return new HibernateJpaDialect();
        }

        @Bean(name = "transactionManager")
        @Singleton
        public PlatformTransactionManager annotationDrivenTransactionManager() {
            JpaTransactionManager transactionManager = new JpaTransactionManager(providerEntityManagerFactory());
            transactionManager.setPersistenceUnitName("providerEntityManager");
            transactionManager.setJpaDialect(providerJPADialect());
            transactionManager.setDataSource(providerDataSource);
            return transactionManager;
        }

        @Bean
        @Singleton
        public EntityManagerFactory providerEntityManagerFactory() {
            LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
            entityManagerFactory.setDataSource(providerDataSource);
            entityManagerFactory.setPackagesToScan(new String[] { "com.clemble.social.provider.data" });
            entityManagerFactory.setPersistenceProvider(new HibernatePersistence());
            entityManagerFactory.setJpaVendorAdapter(providerJPAVendorAdapter);
            entityManagerFactory.setPersistenceUnitName("providerEntityManager");
            entityManagerFactory.afterPropertiesSet();
            return entityManagerFactory.getObject();
        }
    }

    @Configuration
    @Profile(value = "providerCloud")
    static class CloudFoundryConfigurations {
        @Inject
        private CloudEnvironment cloudEnvironment;

        @Bean(name = "providerDataSource")
        @Singleton
        public DataSource providerDataSource() {
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

        @Bean
        @Singleton
        public JpaVendorAdapter providerJPAVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
            hibernateJpaVendorAdapter.setShowSql(false);
            return hibernateJpaVendorAdapter;
        }
    }

    @Configuration
    @Profile(value = { "providerDefault" })
    static class DefaultConfigurations {

        @Bean(name = "providerDataSource")
        @Singleton
        public DataSource providerDataSource() throws PropertyVetoException {
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/mavarazy");
            comboPooledDataSource.setUser("mavarazy");
            comboPooledDataSource.setPassword("mavarazy");
            comboPooledDataSource.setInitialPoolSize(50);
            comboPooledDataSource.setMaxPoolSize(50);
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
            return comboPooledDataSource;
        }

        @Bean
        @Singleton
        public JpaVendorAdapter providerJPAVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
            hibernateJpaVendorAdapter.setShowSql(true);
            return hibernateJpaVendorAdapter;
        }

    }

    @Configuration
    @Profile(value = { "providerTest" })
    static class TestConfigurations {

        @Bean(name = "providerDataSource")
        @Singleton
        public DataSource providerDataSource() throws IOException, SQLException, PropertyVetoException {
            DataSource dataSource = dataSource();
            databasePopulator().populate(dataSource.getConnection());
            return dataSource;
        }

        private ComboPooledDataSource comboPooledDataSource() throws PropertyVetoException {
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/clemble_social");
            comboPooledDataSource.setUser("clemble_social");
            comboPooledDataSource.setPassword("clemble_social");
            comboPooledDataSource.setInitialPoolSize(50);
            comboPooledDataSource.setMaxPoolSize(50);
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
            comboPooledDataSource.setCheckoutTimeout(18000);
            return comboPooledDataSource;
        }
        
        @Bean
        @Singleton
        public DataSource dataSource() {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setEncoding("UTF-8");
            dataSource.setServerName("localhost");
            dataSource.setUser("clemble_social");
            dataSource.setPassword("clemble_social");
            dataSource.setDatabaseName("clemble_social");
            dataSource.setPort(3306);
            return dataSource;
        }

        // Populates DB with appropriate Schema
        private DatabasePopulator databasePopulator() throws IOException {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("/com/clemble/social/data/Providers.sql"));
            return populator;
        }

        @Bean
        @Singleton
        public JpaVendorAdapter providerJPAVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
            hibernateJpaVendorAdapter.setShowSql(true);
            return hibernateJpaVendorAdapter;
        }
    }

}
