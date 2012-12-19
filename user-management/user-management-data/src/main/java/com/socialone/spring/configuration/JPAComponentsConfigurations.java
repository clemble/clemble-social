package com.socialone.spring.configuration;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.socialone.service.social.SocialPersonProfileRepository;
import com.socialone.service.social.jpa.JpaSocialPersonProfileRepository;
import com.socialone.service.user.UserRepository;
import com.socialone.service.user.contact.ProfileRepository;
import com.socialone.service.user.contact.jpa.JpaProfileRepository;
import com.socialone.service.user.jpa.JpaUserRepository;

@Configuration
@Import(value = { JPAConfigurations.class })
public class JPAComponentsConfigurations {

    @Bean
    @Singleton
    public SocialPersonProfileRepository socialPersonProfileRepository() {
        return new JpaSocialPersonProfileRepository();
    }

    @Bean
    @Singleton
    public ProfileRepository contactRepository() {
        return new JpaProfileRepository();
    }

    @Bean
    @Singleton
    public UserRepository userRepository() {
        return new JpaUserRepository();
    }

}
