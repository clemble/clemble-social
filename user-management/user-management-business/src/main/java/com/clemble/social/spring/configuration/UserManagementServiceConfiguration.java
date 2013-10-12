package com.clemble.social.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.social.spring.configuration.DataTierConfigurations;


@Configuration
@ComponentScan(basePackages = "com.clemble.social.event", excludeFilters = { @Filter(Configuration.class) })
@Import(value = DataTierConfigurations.class)
public class UserManagementServiceConfiguration {

}
