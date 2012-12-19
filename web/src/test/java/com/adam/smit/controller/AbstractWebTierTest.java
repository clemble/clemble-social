package com.adam.smit.controller;

import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.socialone.spring.configuration.WebTierConfigurations;

abstract public class AbstractWebTierTest {

    final private MockMvc mockMvcBuilder = MockMvcBuilders.annotationConfigSetup(WebTierConfigurations.class).activateProfiles("test").build();

    public MockMvc getMockMvcBuilder() {
        return mockMvcBuilder;
    }
}
