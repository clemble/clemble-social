package com.socialone.data.social;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;
import org.springframework.social.connect.ConnectionKey;

import com.socialone.data.Gender;
import com.socialone.data.Immutable;
import com.socialone.data.date.SocialDate;
import com.socialone.data.date.SocialDate.SimpleSocialDateBuilder;
import com.socialone.data.date.SocialDate.SocialDateBuilder;
import com.socialone.data.markups.Mutable;
import com.socialone.data.social.connection.SocialConnection;
import com.socialone.data.social.connection.SocialConnection.SimpleSocialConnectionBuilder;
import com.socialone.data.social.connection.SocialConnection.SocialConnectionBuilder;

public abstract class SocialPersonProfile implements Serializable, Mutable<SocialPersonProfile> {
    final static private long serialVersionUID = 1L;

    abstract public ConnectionKey getPrimaryConnection();

    abstract public SocialConnection getSocialConnection();

    abstract public String getFirstName();

    abstract public String getLastName();

    abstract public Gender getGender();

    abstract public SocialDate getBirthDate();

    abstract public String getUrl();

    abstract public String getImage();

    @Override
    public SocialPersonProfile freeze() {
        return new ImmutableSocialPersonProfile(getPrimaryConnection(), getSocialConnection(), getFirstName(), getLastName(), getGender(), getBirthDate(),
                getUrl(), getImage());
    }

    @Override
    public SocialPersonProfile merge(SocialPersonProfile other) {
        throw new IllegalAccessError();
    }

    @Override
    public SocialPersonProfile safeMerge(SocialPersonProfile other) {
        throw new IllegalAccessError();
    }

    @Override
    public SocialPersonProfile safeMerge(Collection<? extends SocialPersonProfile> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getPrimaryConnection() == null ? 0 : getPrimaryConnection().hashCode());
        result = 31 * result + (getSocialConnection() == null ? 0 : getSocialConnection().hashCode());
        result = 31 * result + (getFirstName() == null ? 0 : getFirstName().hashCode());
        result = 31 * result + (getLastName() == null ? 0 : getLastName().hashCode());
        result = 31 * result + (getGender() == null ? 0 : getGender().hashCode());
        result = 31 * result + (getBirthDate() == null ? 0 : getBirthDate().hashCode());
        result = 31 * result + (getUrl() == null ? 0 : getUrl().hashCode());
        result = 31 * result + (getImage() == null ? 0 : getImage().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        SocialPersonProfile other = (SocialPersonProfile) obj;
        if (getPrimaryConnection() == null) {
            if (other.getPrimaryConnection() != null)
                return false;
        } else if (!getPrimaryConnection().equals(other.getPrimaryConnection()))
            return false;
        if (getSocialConnection() == null) {
            if (other.getSocialConnection() != null)
                return false;
        } else if (!getSocialConnection().equals(other.getSocialConnection()))
            return false;
        if (getFirstName() == null) {
            if (other.getFirstName() != null)
                return false;
        } else if (!getFirstName().equals(other.getFirstName()))
            return false;
        if (getLastName() == null) {
            if (other.getLastName() != null)
                return false;
        } else if (!getLastName().equals(other.getLastName()))
            return false;
        if (getGender() == null) {
            if (other.getGender() != null)
                return false;
        } else if (!getGender().equals(other.getGender()))
            return false;
        if (getBirthDate() == null) {
            if (other.getBirthDate() != null)
                return false;
        } else if (!getBirthDate().equals(other.getBirthDate()))
            return false;
        if (getUrl() == null) {
            if (other.getUrl() != null)
                return false;
        } else if (!getUrl().equals(other.getUrl()))
            return false;
        if (getImage() == null) {
            if (other.getImage() != null)
                return false;
        } else if (!getImage().equals(other.getImage()))
            return false;
        return true;
    }

    public static abstract class SocialPersonProfileBuilder extends SocialPersonProfile implements Builder<SocialPersonProfile> {

        final static private long serialVersionUID = 1L;

        public SocialPersonProfileBuilder() {
        }

        public SocialPersonProfileBuilder(SocialPersonProfile other) {
            merge(other);
        }

        abstract public SocialConnectionBuilder getSocialConnection();

        abstract public SocialDateBuilder getBirthDate();

        abstract public SocialPersonProfileBuilder setPrimaryConnection(ConnectionKey newPrimaryConnection);

        abstract public SocialPersonProfileBuilder setSocialConnection(SocialConnection newSocialConnection);

        abstract public SocialPersonProfileBuilder setFirstName(String newFirstName);

        abstract public SocialPersonProfileBuilder setLastName(String newLastName);

        abstract public SocialPersonProfileBuilder setGender(Gender newGender);

        abstract public SocialPersonProfileBuilder setBirthDate(SocialDate newBirthDate);

        abstract public SocialPersonProfileBuilder setUrl(String newUrl);

        abstract public SocialPersonProfileBuilder setImage(String newImage);

        @Override
        public SocialPersonProfileBuilder merge(SocialPersonProfile other) {
            if (other != null) {
                setPrimaryConnection(other.getPrimaryConnection()).setSocialConnection(other.getSocialConnection()).setFirstName(other.getFirstName())
                        .setLastName(other.getLastName()).setGender(other.getGender()).setBirthDate(other.getBirthDate()).setUrl(other.getUrl())
                        .setImage(other.getImage());
            }
            return this;
        }

        @Override
        public SocialPersonProfileBuilder safeMerge(SocialPersonProfile other) {
            if (other != null) {
                if (getPrimaryConnection() == null) {
                    setPrimaryConnection(other.getPrimaryConnection());
                }

                if (getSocialConnection() != null) {
                    getSocialConnection().safeMerge(other.getSocialConnection());
                } else {
                    setSocialConnection(other.getSocialConnection());
                }

                if (getFirstName() == null)
                    setFirstName(other.getFirstName());
                if (getLastName() == null)
                    setLastName(other.getLastName());
                if (getGender() == null)
                    setGender(other.getGender());
                if (getBirthDate() != null) {
                    getBirthDate().safeMerge(other.getBirthDate());
                } else {
                    setBirthDate(other.getBirthDate());
                }

                if (getUrl() == null)
                    setUrl(other.getUrl());
                if (getImage() == null)
                    setImage(other.getImage());
            }
            return this;
        }

        @Override
        public SocialPersonProfileBuilder safeMerge(Collection<? extends SocialPersonProfile> others) {
            if (others != null && !others.isEmpty()) {
                for (SocialPersonProfile other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public SocialPersonProfile build() {
            return freeze();
        }
    }

    final public static class ImmutableSocialPersonProfile extends SocialPersonProfile implements Immutable {
        final static private long serialVersionUID = 1L;

        final private ConnectionKey primaryConnection;
        final private SocialConnection socialConnection;
        final private String firstName;
        final private String lastName;
        final private Gender gender;
        final private SocialDate birthDate;
        final private String url;
        final private String image;

        public ImmutableSocialPersonProfile(SocialPersonProfile other) {
            this((other != null ? other.getPrimaryConnection() : null), (other != null ? other.getSocialConnection() : null), (other != null ? other
                    .getFirstName() : null), (other != null ? other.getLastName() : null), (other != null ? other.getGender() : null), (other != null ? other
                    .getBirthDate() : null), (other != null ? other.getUrl() : null), (other != null ? other.getImage() : null));
        }

        public ImmutableSocialPersonProfile(ConnectionKey primaryConnection, SocialConnection socialConnection, String firstName, String lastName,
                Gender gender, SocialDate birthDate, String url, String image) {
            this.primaryConnection = primaryConnection;
            this.socialConnection = socialConnection.freeze();
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.birthDate = birthDate.freeze();
            this.url = url;
            this.image = image;
        }

        @Override
        public ConnectionKey getPrimaryConnection() {
            return this.primaryConnection;
        }

        @Override
        public SocialConnection getSocialConnection() {
            return this.socialConnection;
        }

        @Override
        public String getFirstName() {
            return this.firstName;
        }

        @Override
        public String getLastName() {
            return this.lastName;
        }

        @Override
        public Gender getGender() {
            return this.gender;
        }

        @Override
        public SocialDate getBirthDate() {
            return this.birthDate;
        }

        @Override
        public String getUrl() {
            return this.url;
        }

        @Override
        public String getImage() {
            return this.image;
        }

        @Override
        public SocialPersonProfile freeze() {
            return this;
        }
    }

    public static class SimpleSocialPersonProfileBuilder extends SocialPersonProfileBuilder {
        final static private long serialVersionUID = 1L;

        private ConnectionKey primaryConnection;
        private SocialConnectionBuilder socialConnection;
        private String firstName;
        private String lastName;
        private Gender gender;
        private SocialDateBuilder birthDate;
        private String url;
        private String image;

        public SimpleSocialPersonProfileBuilder() {
        }

        public SimpleSocialPersonProfileBuilder(SocialPersonProfile other) {
            super(other);
        }

        public SimpleSocialPersonProfileBuilder(ConnectionKey primaryConnection, SocialConnection socialConnection, String firstName, String lastName,
                Gender gender, SocialDate birthDate, String url, String image) {
            this.primaryConnection = primaryConnection;
            if (socialConnection instanceof SocialConnectionBuilder)
                this.socialConnection = (SocialConnectionBuilder) socialConnection;
            else
                this.socialConnection = new SimpleSocialConnectionBuilder(socialConnection);
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            if (birthDate instanceof SocialDateBuilder)
                this.birthDate = (SocialDateBuilder) birthDate;
            else
                this.birthDate = new SimpleSocialDateBuilder(birthDate);
            this.url = url;
            this.image = image;
        }

        @Override
        public ConnectionKey getPrimaryConnection() {
            return this.primaryConnection;
        }

        @Override
        public SocialPersonProfileBuilder setPrimaryConnection(ConnectionKey newPrimaryConnection) {
            this.primaryConnection = newPrimaryConnection;
            return this;
        }

        @Override
        public SocialConnectionBuilder getSocialConnection() {
            return this.socialConnection;
        }

        @Override
        public SocialPersonProfileBuilder setSocialConnection(SocialConnection newSocialConnection) {
            if (newSocialConnection instanceof SocialConnectionBuilder)
                this.socialConnection = (SocialConnectionBuilder) newSocialConnection;
            else
                this.socialConnection = new SimpleSocialConnectionBuilder(newSocialConnection);
            return this;
        }

        @Override
        public String getFirstName() {
            return this.firstName;
        }

        @Override
        public SocialPersonProfileBuilder setFirstName(String newFirstName) {
            this.firstName = newFirstName;
            return this;
        }

        @Override
        public String getLastName() {
            return this.lastName;
        }

        @Override
        public SocialPersonProfileBuilder setLastName(String newLastName) {
            this.lastName = newLastName;
            return this;
        }

        @Override
        public Gender getGender() {
            return this.gender;
        }

        @Override
        public SocialPersonProfileBuilder setGender(Gender newGender) {
            this.gender = newGender;
            return this;
        }

        @Override
        public SocialDateBuilder getBirthDate() {
            return this.birthDate;
        }

        @Override
        public SocialPersonProfileBuilder setBirthDate(SocialDate newBirthDate) {
            if (birthDate instanceof SocialDateBuilder)
                this.birthDate = (SocialDateBuilder) newBirthDate;
            else
                this.birthDate = new SimpleSocialDateBuilder(newBirthDate);
            return this;
        }

        @Override
        public String getUrl() {
            return this.url;
        }

        @Override
        public SocialPersonProfileBuilder setUrl(String newUrl) {
            this.url = newUrl;
            return this;
        }

        @Override
        public String getImage() {
            return this.image;
        }

        @Override
        public SocialPersonProfileBuilder setImage(String newImage) {
            this.image = newImage;
            return this;
        }
    }
}