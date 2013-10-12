package com.clemble.social.spring.configuration;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.clemble.social.ConnectionRepositoryCache;
import com.clemble.social.SimpleSignInAdapter;
import com.clemble.social.UserCookieGenerator;
import com.clemble.social.UserInterceptor;
import com.clemble.social.spring.configuration.BusinessTierConfigurations;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@Configuration
@ComponentScan(basePackages = "com.clemble.social", excludeFilters = { @Filter(Configuration.class) })
@Import(value = { WebHttpMessageConverters.class, BusinessTierConfigurations.class })
@EnableWebMvc
public class WebTierConfigurations extends WebMvcConfigurerAdapter {

    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Inject
    private UsersConnectionRepository connectionRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(connectionRepositoryCache()));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/signin");
        registry.addViewController("/signout");
    }
    
    @Bean
    @Singleton
    public ViewResolver getViewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        negotiatingViewResolver.setMediaTypes(ImmutableMap.of("json", "application/json", "xml", "application/xml"));
        List<View> views = Lists.newArrayList();
        views.add(jacksonJsonView());
        views.add(xmpMarshallingView());
        negotiatingViewResolver.setDefaultViews(views);
        return negotiatingViewResolver;
    }
    
    @Bean
    @Singleton
    public MappingJacksonJsonView jacksonJsonView(){
        return new MappingJacksonJsonView();
    }
    
    @Bean
    @Singleton
    public MarshallingView xmpMarshallingView() {
        return new MarshallingView(new XStreamMarshaller());
    }

    @Bean
    @Singleton
    public UserCookieGenerator userCookieGenerator() {
        return new UserCookieGenerator();
    }

    @Bean
    @Singleton
    public ConnectionRepositoryCache connectionRepositoryCache() {
        return new ConnectionRepositoryCache(connectionRepository);
    }

    /**
     * The Spring MVC Controller that allows users to sign-in with their
     * provider accounts.
     */
    @Bean
    @Singleton
    public ProviderSignInController providerSignInController() {
        return new ProviderSignInController(connectionFactoryLocator, connectionRepository, signInAdapter());
    }
    
    @Bean
    @Singleton
    public SignInAdapter signInAdapter() {
        return new SimpleSignInAdapter();
    }

    @Bean
    @Singleton
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
