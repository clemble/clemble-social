package com.clemble.social.provider.data.signin;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;

public abstract class SignInConfiguration implements Serializable, Mutable<SignInConfiguration> {
    final static private long serialVersionUID = 1L;

    abstract public String getSignInURL();

    abstract public String getSignUpURL();

    abstract public String getPostSignInURL();

    @Override
    public SignInConfiguration freeze() {
        return new ImmutableSignInConfiguration(getSignInURL(), getSignUpURL(), getPostSignInURL());
    }

    @Override
    public SignInConfiguration merge(SignInConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public SignInConfiguration safeMerge(SignInConfiguration other) {
        throw new IllegalAccessError();
    }

    @Override
    public SignInConfiguration safeMerge(Collection<? extends SignInConfiguration> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getSignInURL() == null ? 0 : getSignInURL().hashCode());
        result = 31 * result + (getSignUpURL() == null ? 0 : getSignUpURL().hashCode());
        result = 31 * result + (getPostSignInURL() == null ? 0 : getPostSignInURL().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        SignInConfiguration other = (SignInConfiguration) obj;
        if (getSignInURL() == null) {
            if (getSignInURL() != null)
                return false;
        } else if (!getSignInURL().equals(other.getSignInURL()))
            return false;
        if (getSignUpURL() == null) {
            if (getSignUpURL() != null)
                return false;
        } else if (!getSignUpURL().equals(other.getSignUpURL()))
            return false;
        if (getPostSignInURL() == null) {
            if (getPostSignInURL() != null)
                return false;
        } else if (!getPostSignInURL().equals(other.getPostSignInURL()))
            return false;
        return true;
    }

    public static abstract class SignInConfigurationBuilder extends SignInConfiguration implements Builder<SignInConfiguration> {

        final static private long serialVersionUID = 1L;

        public SignInConfigurationBuilder() {
        }

        public SignInConfigurationBuilder(SignInConfiguration other) {
            merge(other);
        }

        abstract public SignInConfigurationBuilder setSignInURL(String newSignInURL);

        abstract public SignInConfigurationBuilder setSignUpURL(String newSignUpURL);

        abstract public SignInConfigurationBuilder setPostSignInURL(String newPostSignInURL);

        @Override
        public SignInConfigurationBuilder merge(SignInConfiguration other) {
            if (other != null) {
                setSignInURL(other.getSignInURL()).setSignUpURL(other.getSignUpURL()).setPostSignInURL(other.getPostSignInURL());
            }
            return this;
        }

        @Override
        public SignInConfigurationBuilder safeMerge(SignInConfiguration other) {
            if (other != null) {
                setSignInURL(other.getSignInURL());
                setSignUpURL(other.getSignUpURL());
                setPostSignInURL(other.getPostSignInURL());
            }
            return this;
        }

        @Override
        public SignInConfigurationBuilder safeMerge(Collection<? extends SignInConfiguration> others) {
            if (others != null && !others.isEmpty()) {
                for (SignInConfiguration other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public SignInConfiguration build() {
            return freeze();
        }
    }

    final public static class ImmutableSignInConfiguration extends SignInConfiguration implements Immutable {
        final static private long serialVersionUID = 1L;

        final private String signInURL;
        final private String signUpURL;
        final private String postSignInURL;

        public ImmutableSignInConfiguration(SignInConfiguration other) {
            this((other != null ? other.getSignInURL() : null), (other != null ? other.getSignUpURL() : null),
                    (other != null ? other.getPostSignInURL() : null));
        }

        public ImmutableSignInConfiguration(String signInURL, String signUpURL, String postSignInURL) {
            this.signInURL = signInURL;
            this.signUpURL = signUpURL;
            this.postSignInURL = postSignInURL;
        }

        @Override
        public String getSignInURL() {
            return this.signInURL;
        }

        @Override
        public String getSignUpURL() {
            return this.signUpURL;
        }

        @Override
        public String getPostSignInURL() {
            return this.postSignInURL;
        }

        @Override
        public SignInConfiguration freeze() {
            return this;
        }
    }

    public static class SimpleSignInConfigurationBuilder extends SignInConfigurationBuilder {
        final static private long serialVersionUID = 1L;

        private String signInURL;
        private String signUpURL;
        private String postSignInURL;

        public SimpleSignInConfigurationBuilder() {
        }

        public SimpleSignInConfigurationBuilder(SignInConfiguration other) {
            super(other);
        }

        public SimpleSignInConfigurationBuilder(String signInURL, String signUpURL, String postSignInURL) {
            this.signInURL = signInURL;
            this.signUpURL = signUpURL;
            this.postSignInURL = postSignInURL;
        }

        @Override
        public String getSignInURL() {
            return this.signInURL;
        }

        @Override
        public SignInConfigurationBuilder setSignInURL(String newSignInURL) {
            this.signInURL = newSignInURL;
            return this;
        }

        @Override
        public String getSignUpURL() {
            return this.signUpURL;
        }

        @Override
        public SignInConfigurationBuilder setSignUpURL(String newSignUpURL) {
            this.signUpURL = newSignUpURL;
            return this;
        }

        @Override
        public String getPostSignInURL() {
            return this.postSignInURL;
        }

        @Override
        public SignInConfigurationBuilder setPostSignInURL(String newPostSignInURL) {
            this.postSignInURL = newPostSignInURL;
            return this;
        }
    }
}