package com.clemble.social.provider.data.autodiscovery;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.clemble.social.provider.data.autodiscovery.AutodiscoveryConfiguration;
import com.clemble.social.provider.data.autodiscovery.AutodiscoveryConfiguration.AutodiscoveryConfigurationBuilder;

@Embeddable
public class AutodiscoveryConfigurationEntity extends AutodiscoveryConfigurationBuilder {

    /**
     * Generated 10/09/2012
     */
    private static final long serialVersionUID = 5485209032761564012L;

    @Column(name = "MULTIDISCOVERY")
    private boolean multidiscovery;

    public AutodiscoveryConfigurationEntity(){
    }
    
    public AutodiscoveryConfigurationEntity(AutodiscoveryConfiguration autodiscoveryConfiguration){
        super(autodiscoveryConfiguration);
    }
    
    @Override
    public AutodiscoveryConfigurationEntity setMultidiscoveryEnabled(boolean multidiscovery) {
        this.multidiscovery = multidiscovery;
        return this;
    }

    @Override
    public boolean isMultidiscoveryEnabled() {
        return multidiscovery;
    }

}
