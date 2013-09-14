package com.socialone.spring.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

import com.socialone.data.social.SocialNetworkType;
import com.socialone.spring.configuration.DataTierConfigurations;

@Configuration
@ComponentScan(basePackages = "com.socialone.service.connection", excludeFilters = { @Filter(Configuration.class) })
@Import(value = DataTierConfigurations.class)
public class SocialTierConfigurations {

    @Inject
    private Environment environment;

    @Inject
    private ConnectionFactoryRegistry registry;

    @Bean
    @Singleton
    public ConnectionFactoryLocator connectionFactoryLocator() {
        registry.addConnectionFactory(validate(new FacebookConnectionFactory(environment.getProperty("facebook.clientId"), environment
                .getProperty("facebook.clientSecret"))));
        registry.addConnectionFactory(validate(new TwitterConnectionFactory(environment.getProperty("twitter.cunsumer.key"), environment
                .getProperty("twitter.consumer.secret"))));
        registry.addConnectionFactory(validate(new LinkedInConnectionFactory(environment.getProperty("linkedin.cunsumer.key"), environment
                .getProperty("linkedin.consumer.secret"))));
//        registry.addConnectionFactory(validate(new VKontakteConnectionFactory(environment.getProperty("vkontakte.cunsumer.key"), environment
//                .getProperty("vkontakte.consumer.secret"))));
        registry.addConnectionFactory(validate(new GoogleConnectionFactory(environment.getProperty("google.cunsumer.key"), environment
                .getProperty("google.consumer.secret"))));
        return registry;
    }

    private ConnectionFactory<?> validate(ConnectionFactory<?> connectionFactory) {
        SocialNetworkType socialNetworkType = SocialNetworkType.valueOf(connectionFactory.getProviderId());
        assert socialNetworkType != null : "Connection Factory missing from the SocialNetworkType list";
        return connectionFactory;
    }

}
