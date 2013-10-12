package com.clemble.social.provider.data;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;
import com.clemble.social.provider.data.autodiscovery.AutodiscoveryConfiguration;
import com.clemble.social.provider.data.callback.CallbackConfiguration;
import com.clemble.social.provider.data.merge.MergeConfiguration;
import com.clemble.social.provider.data.merge.MergeConfiguration.MergeConfigurationBuilder;
import com.clemble.social.provider.data.merge.MergeConfiguration.SimpleMergeConfigurationBuilder;
import com.clemble.social.provider.data.signin.SignInConfiguration;

public abstract class ProviderConfiguration implements Serializable, Mutable<ProviderConfiguration> {
    final static private long serialVersionUID = 1L;

    abstract public String getProvider();

    abstract public AutodiscoveryConfiguration getAutodiscoveryConfiguration();

    abstract public SignInConfiguration getSignInConfiguration();

    abstract public MergeConfiguration getMergeConfiguration();

    abstract public CallbackConfiguration getCallbackConfiguration();

    @Override
    public ProviderConfiguration freeze() {
        return new ImmutableProviderConfiguration(getProvider(), getAutodiscoveryConfiguration(), getSignInConfiguration(), getMergeConfiguration(),
                getCallbackConfiguration());
    }

    @Override
    public ProviderConfiguration merge(ProviderConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public ProviderConfiguration safeMerge(ProviderConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public ProviderConfiguration safeMerge(Collection<? extends ProviderConfiguration> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getProvider() == null ? 0 : getProvider().hashCode());
        result = 31 * result + (getAutodiscoveryConfiguration() == null ? 0 : getAutodiscoveryConfiguration().hashCode());
        result = 31 * result + (getSignInConfiguration() == null ? 0 : getSignInConfiguration().hashCode());
        result = 31 * result + (getMergeConfiguration() == null ? 0 : getMergeConfiguration().hashCode());
        result = 31 * result + (getCallbackConfiguration() == null ? 0 : getCallbackConfiguration().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        ProviderConfiguration other = (ProviderConfiguration) obj;
        if (getProvider() == null) {
            if (getProvider() != null)
                return false;
        } else if (!getProvider().equals(other.getProvider()))
            return false;
        if (getAutodiscoveryConfiguration() == null) {
            if (getAutodiscoveryConfiguration() != null)
                return false;
        } else if (!getAutodiscoveryConfiguration().equals(other.getAutodiscoveryConfiguration()))
            return false;
        if (getSignInConfiguration() == null) {
            if (getSignInConfiguration() != null)
                return false;
        } else if (!getSignInConfiguration().equals(other.getSignInConfiguration()))
            return false;
        if (getMergeConfiguration() == null) {
            if (getMergeConfiguration() != null)
                return false;
        } else if (!getMergeConfiguration().equals(other.getMergeConfiguration()))
            return false;
        if (getCallbackConfiguration() == null) {
            if (getCallbackConfiguration() != null)
                return false;
        } else if (!getCallbackConfiguration().equals(other.getCallbackConfiguration()))
            return false;
        return true;
    }

    public static abstract class ProviderConfigurationBuilder extends ProviderConfiguration implements Builder<ProviderConfiguration> {

        final static private long serialVersionUID = 1L;

        public ProviderConfigurationBuilder() {
        }

        public ProviderConfigurationBuilder(ProviderConfiguration other) {
            merge(other);
        }

        abstract public ProviderConfigurationBuilder setProvider(String newProvider);

        abstract public ProviderConfigurationBuilder setAutodiscoveryConfiguration(AutodiscoveryConfiguration newAutodiscoveryConfiguration);

        abstract public ProviderConfigurationBuilder setSignInConfiguration(SignInConfiguration newSignInConfiguration);

        abstract public ProviderConfigurationBuilder setMergeConfiguration(MergeConfiguration newMergeConfiguration);

        abstract public ProviderConfigurationBuilder setCallbackConfiguration(CallbackConfiguration newCallbackConfiguration);

        @Override
        public ProviderConfigurationBuilder merge(ProviderConfiguration other) {
            if (other != null) {
                setProvider(other.getProvider()).setAutodiscoveryConfiguration(other.getAutodiscoveryConfiguration())
                        .setSignInConfiguration(other.getSignInConfiguration()).setMergeConfiguration(other.getMergeConfiguration())
                        .setCallbackConfiguration(other.getCallbackConfiguration());
            }
            return this;
        }

        @Override
        public ProviderConfigurationBuilder safeMerge(ProviderConfiguration other) {
            if (other != null) {
                if (getProvider() == null)
                    setProvider(other.getProvider());
                if (getAutodiscoveryConfiguration() != null) {
                    getAutodiscoveryConfiguration().safeMerge(other.getAutodiscoveryConfiguration());
                } else {
                    setAutodiscoveryConfiguration(other.getAutodiscoveryConfiguration());
                }

                if (getSignInConfiguration() != null) {
                    getSignInConfiguration().safeMerge(other.getSignInConfiguration());
                } else {
                    setSignInConfiguration(other.getSignInConfiguration());
                }

                if (getMergeConfiguration() != null) {
                    getMergeConfiguration().safeMerge(other.getMergeConfiguration());
                } else {
                    setMergeConfiguration(other.getMergeConfiguration());
                }

                if (getCallbackConfiguration() != null) {
                    getCallbackConfiguration().safeMerge(other.getCallbackConfiguration());
                } else {
                    setCallbackConfiguration(other.getCallbackConfiguration());
                }

            }
            return this;
        }

        @Override
        public ProviderConfigurationBuilder safeMerge(Collection<? extends ProviderConfiguration> others) {
            if (others != null && !others.isEmpty()) {
                for (ProviderConfiguration other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public ProviderConfiguration build() {
            return freeze();
        }
    }

    final public static class ImmutableProviderConfiguration extends ProviderConfiguration implements Immutable {
        final static private long serialVersionUID = 1L;

        final private String provider;
        final private AutodiscoveryConfiguration autodiscoveryConfiguration;
        final private SignInConfiguration signInConfiguration;
        final private MergeConfiguration mergeConfiguration;
        final private CallbackConfiguration callbackConfiguration;

        public ImmutableProviderConfiguration(ProviderConfiguration other) {
            this((other != null ? other.getProvider() : null), (other != null ? other.getAutodiscoveryConfiguration() : null), (other != null ? other
                    .getSignInConfiguration() : null), (other != null ? other.getMergeConfiguration() : null), (other != null ? other
                    .getCallbackConfiguration() : null));
        }

        public ImmutableProviderConfiguration(String provider, AutodiscoveryConfiguration autodiscoveryConfiguration, SignInConfiguration signInConfiguration,
                MergeConfiguration mergeConfiguration, CallbackConfiguration callbackConfiguration) {
            this.provider = provider;
            this.autodiscoveryConfiguration = autodiscoveryConfiguration.freeze();
            this.signInConfiguration = signInConfiguration.freeze();
            this.mergeConfiguration = mergeConfiguration.freeze();
            this.callbackConfiguration = callbackConfiguration.freeze();
        }

        @Override
        public String getProvider() {
            return this.provider;
        }

        @Override
        public AutodiscoveryConfiguration getAutodiscoveryConfiguration() {
            return this.autodiscoveryConfiguration;
        }

        @Override
        public SignInConfiguration getSignInConfiguration() {
            return this.signInConfiguration;
        }

        @Override
        public MergeConfiguration getMergeConfiguration() {
            return this.mergeConfiguration;
        }

        @Override
        public CallbackConfiguration getCallbackConfiguration() {
            return this.callbackConfiguration;
        }

        @Override
        public ProviderConfiguration freeze() {
            return this;
        }
    }

    public static class SimpleProviderConfigurationBuilder extends ProviderConfigurationBuilder {
        final static private long serialVersionUID = 1L;

        private String provider;
        private AutodiscoveryConfiguration autodiscoveryConfiguration;
        private SignInConfiguration signInConfiguration;
        private MergeConfiguration mergeConfiguration;
        private CallbackConfiguration callbackConfiguration;

        public SimpleProviderConfigurationBuilder() {
        }

        public SimpleProviderConfigurationBuilder(ProviderConfiguration other) {
            merge(other);
        }

        public SimpleProviderConfigurationBuilder(String provider, AutodiscoveryConfiguration autodiscoveryConfiguration,
                SignInConfiguration signInConfiguration, MergeConfiguration mergeConfiguration, CallbackConfiguration callbackConfiguration) {
            this.provider = provider;
            this.autodiscoveryConfiguration = autodiscoveryConfiguration;
            this.signInConfiguration = signInConfiguration;
            this.mergeConfiguration = mergeConfiguration;
            this.callbackConfiguration = callbackConfiguration;
        }

        @Override
        public String getProvider() {
            return this.provider;
        }

        @Override
        public ProviderConfigurationBuilder setProvider(String newProvider) {
            this.provider = newProvider;
            return this;
        }

        @Override
        public AutodiscoveryConfiguration getAutodiscoveryConfiguration() {
            return this.autodiscoveryConfiguration;
        }

        @Override
        public ProviderConfigurationBuilder setAutodiscoveryConfiguration(AutodiscoveryConfiguration newAutodiscoveryConfiguration) {
            this.autodiscoveryConfiguration = newAutodiscoveryConfiguration;
            return this;
        }

        @Override
        public SignInConfiguration getSignInConfiguration() {
            return this.signInConfiguration;
        }

        @Override
        public ProviderConfigurationBuilder setSignInConfiguration(SignInConfiguration newSignInConfiguration) {
            this.signInConfiguration = newSignInConfiguration;
            return this;
        }

        @Override
        public MergeConfiguration getMergeConfiguration() {
            return this.mergeConfiguration;
        }

        @Override
        public ProviderConfigurationBuilder setMergeConfiguration(MergeConfiguration newMergeConfiguration) {
            this.mergeConfiguration = (newMergeConfiguration instanceof MergeConfigurationBuilder) ? newMergeConfiguration : new SimpleMergeConfigurationBuilder(newMergeConfiguration);
            return this;
        }

        @Override
        public CallbackConfiguration getCallbackConfiguration() {
            return this.callbackConfiguration;
        }

        @Override
        public ProviderConfigurationBuilder setCallbackConfiguration(CallbackConfiguration newCallbackConfiguration) {
            this.callbackConfiguration = newCallbackConfiguration;
            return this;
        }
    }
}