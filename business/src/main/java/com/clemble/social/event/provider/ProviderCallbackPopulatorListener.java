package com.clemble.social.event.provider;

import java.util.Collection;

import javax.inject.Inject;

import com.clemble.social.provider.data.ProviderAware;
import com.clemble.social.provider.data.callback.CallbackConfiguration;
import com.clemble.social.provider.data.callback.delivery.DeliveryConfiguration;
import com.clemble.social.provider.service.ProviderConfigurationRepository;
import com.clemble.social.service.delivery.DeliveryService;
import com.google.common.eventbus.Subscribe;

public class ProviderCallbackPopulatorListener {

    @Inject
    private ProviderConfigurationRepository providerConfigurationRepository;
    
    @Inject
    private DeliveryService deliveryService;

    @Subscribe
    public void callbackListener(ProviderAware providerAware) {
        // Step 1. Sanity check
        if(providerAware == null || providerAware.getProviderId() == null)
            return;
        // Step 2. Searching for associated callback URL
        CallbackConfiguration callbackConfiguration = providerConfigurationRepository.getProviderConfiguration(providerAware.getProviderId()).getCallbackConfiguration();
        Collection<DeliveryConfiguration> deliveries = callbackConfiguration.getMessageCallbacks().get(providerAware.getClass());
        if(deliveries == null || deliveries.size() == 0)
            return;
        // Step 3. Sending appropriate notification to the provider
        for(DeliveryConfiguration deliveryConfiguration: deliveries) {
            deliveryService.publish(providerAware, deliveryConfiguration);
        }
    }
}
