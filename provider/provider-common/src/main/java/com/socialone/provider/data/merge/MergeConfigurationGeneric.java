package com.socialone.provider.data.merge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

abstract class MergeConfigurationGeneric {

    abstract public Collection<? extends ProviderPriority> getProviderPriority();
    
    public Map<String, Integer> providersOrder;
    
    public Map<String, Integer> getProvidersOrder() {
        if(providersOrder == null || providersOrder.isEmpty()) {
            Map<String, Integer> temporaryMap = new HashMap<String, Integer>();
            for(ProviderPriority providerPriority: getProviderPriority()) {
                temporaryMap.put(providerPriority.getProviderId(), providerPriority.getPriority());
            }
            providersOrder = ImmutableMap.<String, Integer>copyOf(temporaryMap);
        }
        return providersOrder;
    }

}
