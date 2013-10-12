package com.clemble.social.provider.controller;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.social.data.query.PagingConfiguration;
import com.clemble.social.provider.data.ProviderConfiguration;
import com.clemble.social.provider.service.ProviderConfigurationRepository;

@Controller
public class ProviderController {

    @Inject
    private ProviderConfigurationRepository providerConfigurationRepository;

    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequestMapping(method = RequestMethod.GET, value = "/provider", produces = "application/json")
    public Collection<ProviderConfiguration> getUser() {
        return providerConfigurationRepository.getAllProviderConfigurations(PagingConfiguration.DEFAULT_CONFIGURATION);
    }
    
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/provider", produces = "application/json")
    public void create(@RequestBody ProviderConfiguration newProvider) {
        providerConfigurationRepository.addProviderConfiguration(newProvider);
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/provider/{providerID}", produces = "application/json")
    public ProviderConfiguration getUser(@PathVariable("providerID") String providerID) {
        return providerConfigurationRepository.getProviderConfiguration(providerID);
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/provider/{providerID}", produces = "application/json")
    public void getUser(@PathVariable("providerID") String providerID, @RequestBody ProviderConfiguration updatedProvider) {
        if(providerID.equals(updatedProvider.getProvider()))
            throw new IllegalArgumentException("Can't change provider id from original");
        providerConfigurationRepository.updateProviderConfiguration(updatedProvider);
    }
}
