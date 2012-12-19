package com.socialone.provider.data.merge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import com.socialone.provider.data.merge.MergeConfiguration.MergeConfigurationBuilder;

@Embeddable
public class MergeConfigurationsEntity extends MergeConfigurationBuilder {

    /**
     * Generated 30/08/2012
     */
    private static final long serialVersionUID = 3873590809187858939L;

    @ElementCollection
    @CollectionTable(name = "PROVIDER_MERGE_CONFIGURATIONS", joinColumns = @JoinColumn(name = "PROVIDER_NAME"))
    private Set<ProviderPriorityEntity> providerPriorityEntities = new HashSet<ProviderPriorityEntity>();

    public MergeConfigurationsEntity() {
    }

    public MergeConfigurationsEntity(MergeConfiguration mergeConfiguration) {
        merge(mergeConfiguration);
    }

    @Override
    public MergeConfigurationBuilder addProviderPriority(ProviderPriority providerPriority) {
        providerPriorityEntities.add(new ProviderPriorityEntity(providerPriority));
        return this;
    }

    @Override
    public Collection<? extends ProviderPriority> getProviderPriority() {
        return providerPriorityEntities;
    }

}
