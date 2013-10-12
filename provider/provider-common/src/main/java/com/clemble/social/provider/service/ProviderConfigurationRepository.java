package com.clemble.social.provider.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.clemble.social.data.query.PagingConfiguration;
import com.clemble.social.provider.data.ProviderConfiguration;

public interface ProviderConfigurationRepository {

    public List<ProviderConfiguration> getAllProviderConfigurations(PagingConfiguration configuration);
    
    @Cacheable(value = "providerConfiguration", key = "#providerName")
    public ProviderConfiguration getProviderConfiguration(String providerName);

    @CacheEvict(value = "providerConfiguration", key = "#providerConfigurations.getProviderName()")
    public void addProviderConfiguration(ProviderConfiguration providerConfigurations);
    
    @CacheEvict(value = "providerConfiguration", key = "#providerConfigurations.getProviderName()")
    public void updateProviderConfiguration(ProviderConfiguration providerConfigurations);

}
