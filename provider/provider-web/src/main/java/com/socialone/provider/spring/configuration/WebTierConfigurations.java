package com.socialone.provider.spring.configuration;

import java.util.List;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@Configuration
@ComponentScan(basePackages = "com.socialone.provider", excludeFilters = { @Filter(Configuration.class) })
@Import(value = { WebHttpMessageConverters.class, ProviderBusinessTierConfigurations.class })
@EnableWebMvc
public class WebTierConfigurations extends WebMvcConfigurerAdapter {

    @Bean
    @Singleton
    public ViewResolver getViewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        negotiatingViewResolver.setMediaTypes(ImmutableMap.of("json", "application/json"));

        List<View> views = Lists.newArrayList();

        views.add(jacksonJsonView());
        negotiatingViewResolver.setDefaultViews(views);
        return negotiatingViewResolver;
    }

    @Bean
    @Singleton
    public MappingJacksonJsonView jacksonJsonView() {
        return new MappingJacksonJsonView();
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
