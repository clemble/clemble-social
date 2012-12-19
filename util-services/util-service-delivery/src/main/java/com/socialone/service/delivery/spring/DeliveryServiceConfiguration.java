package com.socialone.service.delivery.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(basePackages = "com.socialone.service.delivery", excludeFilters = { @Filter(Configuration.class) })
public class DeliveryServiceConfiguration {

}
