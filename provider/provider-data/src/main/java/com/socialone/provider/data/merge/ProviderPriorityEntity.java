package com.socialone.provider.data.merge;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.socialone.provider.data.merge.ProviderPriority;
import com.socialone.provider.data.merge.ProviderPriority.ProviderPriorityBuilder;

@Embeddable
public class ProviderPriorityEntity extends ProviderPriorityBuilder {

    /**
     * Generated 24/11/2012
     */
    private static final long serialVersionUID = 1808828352174402708L;

    @Column(name = "PROVIDER_ID")
    private String providerId;
    @Column(name = "PRIORITY")
    private int priority;
    
    public ProviderPriorityEntity(){}
    
    public ProviderPriorityEntity(ProviderPriority providerPriority){
        super(providerPriority);
    }

    @Override
    public ProviderPriorityBuilder setProviderId(String newProviderId) {
        this.providerId = newProviderId;
        return this;
    }

    @Override
    public ProviderPriorityBuilder setPriority(int newPriority) {
        this.priority = newPriority;
        return this;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
