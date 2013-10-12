package com.clemble.social.provider.data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.clemble.social.provider.data.ProviderConfiguration;
import com.clemble.social.provider.data.ProviderConfiguration.ProviderConfigurationBuilder;
import com.clemble.social.provider.data.autodiscovery.AutodiscoveryConfiguration;
import com.clemble.social.provider.data.autodiscovery.AutodiscoveryConfigurationEntity;
import com.clemble.social.provider.data.callback.CallbackConfiguration;
import com.clemble.social.provider.data.callback.CallbackConfigurationEntity;
import com.clemble.social.provider.data.merge.MergeConfiguration;
import com.clemble.social.provider.data.merge.MergeConfigurationsEntity;
import com.clemble.social.provider.data.signin.SignInConfiguration;
import com.clemble.social.provider.data.signin.SignInConfigurationEntity;

@Entity
@Table(name = "PROVIDER_CONFIGURATIONS")
@NamedQuery(name = ProviderConfigurationsEntity.PROVIDERS_GET_ALL, query = "select provider from ProviderConfigurationsEntity provider")
public class ProviderConfigurationsEntity extends ProviderConfigurationBuilder {

    /**
     * Generated 30/08/2012
     */
    private static final long serialVersionUID = 4694671750956592815L;

    final public static String PROVIDERS_GET_ALL = "PROVIDERS_GET_ALL";

    @Id
    @Column(name = "PROVIDER_NAME")
    private String provider;

    @Embedded
    private SignInConfigurationEntity signInConfigurationEntity;
    @Embedded
    private MergeConfigurationsEntity mergeConfigurations = new MergeConfigurationsEntity();
    @Embedded
    private CallbackConfigurationEntity callbackConfigurationEntity = new CallbackConfigurationEntity();
    @Embedded
    private AutodiscoveryConfigurationEntity autodiscoveryConfigurationEntity = new AutodiscoveryConfigurationEntity();

    public ProviderConfigurationsEntity() {
    }

    public ProviderConfigurationsEntity(ProviderConfiguration providerConfiguration) {
        merge(providerConfiguration);
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public ProviderConfigurationsEntity setProvider(String providerName) {
        this.provider = providerName;
        return this;
    }

    @Override
    public MergeConfiguration getMergeConfiguration() {
        return mergeConfigurations;
    }

    @Override
    public ProviderConfigurationBuilder setMergeConfiguration(MergeConfiguration newMergeConfigurations) {
        this.mergeConfigurations = new MergeConfigurationsEntity(newMergeConfigurations);
        return this;
    }

    @Override
    public ProviderConfigurationBuilder setCallbackConfiguration(CallbackConfiguration callbackConfiguration) {
        this.callbackConfigurationEntity = new CallbackConfigurationEntity(callbackConfiguration);
        return this;
    }

    @Override
    public CallbackConfiguration getCallbackConfiguration() {
        return callbackConfigurationEntity;
    }

    @Override
    public AutodiscoveryConfiguration getAutodiscoveryConfiguration() {
        return autodiscoveryConfigurationEntity;
    }

    @Override
    public ProviderConfigurationBuilder setAutodiscoveryConfiguration(AutodiscoveryConfiguration autodiscoveryConfiguration) {
        this.autodiscoveryConfigurationEntity = new AutodiscoveryConfigurationEntity(autodiscoveryConfiguration);
        return this;
    }

    @Override
    public SignInConfiguration getSignInConfiguration() {
        return signInConfigurationEntity;
    }

    @Override
    public ProviderConfigurationBuilder setSignInConfiguration(SignInConfiguration newSignInConfiguration) {
        this.signInConfigurationEntity = new SignInConfigurationEntity(newSignInConfiguration);
        return this;
    }

}
