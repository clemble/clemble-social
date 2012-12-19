package com.socialone.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.socialone.spring.configuration.DataTierConfigurations;


@Configuration
@ComponentScan(basePackages = "com.socialone.event", excludeFilters = { @Filter(Configuration.class) })
@Import(value = DataTierConfigurations.class)
public class UserManagementServiceConfiguration {

}
