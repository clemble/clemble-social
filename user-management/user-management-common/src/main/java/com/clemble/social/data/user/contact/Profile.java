package com.clemble.social.data.user.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;
import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.google.common.collect.ImmutableList;

public abstract class Profile implements Serializable, Mutable<Profile> {
    final static private long serialVersionUID = 1L;

    abstract public String getProfileId();

    abstract public Collection<? extends com.clemble.social.data.social.SocialPersonProfile> getSocialProfiles();

    @Override
    public Profile freeze() {
        return new ImmutableProfile(getProfileId(), getSocialProfiles());
    }

    @Override
    public Profile merge(Profile other) {
        throw new IllegalAccessError();
    }

    @Override
    public Profile safeMerge(Profile other) {
        throw new IllegalAccessError();
    }

    @Override
    public Profile safeMerge(Collection<? extends Profile> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getProfileId() == null ? 0 : getProfileId().hashCode());
        result = 31 * result + (getSocialProfiles() == null ? 0 : getSocialProfiles().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        Profile other = (Profile) obj;
        if (getProfileId() == null) {
            if (other.getProfileId() != null)
                return false;
        } else if (!getProfileId().equals(other.getProfileId()))
            return false;
        if (getSocialProfiles() == null) {
            if (getSocialProfiles() != null)
                return false;
        } else if (other.getSocialProfiles() == null) {
            return false;
        } else if (!getSocialProfiles().containsAll(other.getSocialProfiles()) || !other.getSocialProfiles().containsAll(getSocialProfiles()))
            return false;
        return true;
    }

    public static abstract class ProfileBuilder extends Profile implements Builder<Profile> {

        final static private long serialVersionUID = 1L;

        public ProfileBuilder() {
        }

        public ProfileBuilder(Profile other) {
            merge(other);
        }

        abstract public Collection<? extends SocialPersonProfileBuilder> getSocialProfiles();

        abstract public ProfileBuilder setProfileId(String newProfileId);

        final public ProfileBuilder setSocialProfiles(Collection<? extends com.clemble.social.data.social.SocialPersonProfile> additional) {
            getSocialProfiles().clear();
            if (additional != null && !additional.isEmpty())
                for (SocialPersonProfile addition : additional)
                    addSocialProfiles(addition);
            return this;
        }

        abstract public ProfileBuilder addSocialProfiles(SocialPersonProfile additional);

        @Override
        public ProfileBuilder merge(Profile other) {
            if (other != null) {
                setProfileId(other.getProfileId()).setSocialProfiles(other.getSocialProfiles());
            }
            return this;
        }

        @Override
        public ProfileBuilder safeMerge(Profile other) {
            if (other != null) {
                if (getProfileId() == null)
                    setProfileId(other.getProfileId());
                if (other.getSocialProfiles() != null && !other.getSocialProfiles().isEmpty()) {
                    for (com.clemble.social.data.social.SocialPersonProfile otherSocialProfiles : other.getSocialProfiles())
                        addSocialProfiles(otherSocialProfiles);
                }

            }
            return this;
        }

        @Override
        public ProfileBuilder safeMerge(Collection<? extends Profile> others) {
            if (others != null && !others.isEmpty()) {
                for (Profile other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public Profile build() {
            return freeze();
        }
    }

    final public static class ImmutableProfile extends Profile implements Immutable {
        final static private long serialVersionUID = 1L;

        final private String profileId;
        final private Collection<? extends com.clemble.social.data.social.SocialPersonProfile> socialProfiles;

        public ImmutableProfile(Profile other) {
            this((other != null ? other.getProfileId() : null), (other != null ? other.getSocialProfiles() : ImmutableList
                    .<com.clemble.social.data.social.SocialPersonProfile> of()));
        }

        public ImmutableProfile(String profileId, Collection<? extends com.clemble.social.data.social.SocialPersonProfile> socialProfiles) {
            this.profileId = profileId;
            if (socialProfiles != null) {
                ArrayList<SocialPersonProfile> tmpSocialProfiles = new ArrayList<SocialPersonProfile>();
                for (SocialPersonProfile value : socialProfiles) {
                    if (value != null) {
                        tmpSocialProfiles.add(value.freeze());
                    }
                }
                this.socialProfiles = ImmutableList.<SocialPersonProfile> copyOf(tmpSocialProfiles);
            } else {
                this.socialProfiles = ImmutableList.<SocialPersonProfile> of();
            }
        }

        @Override
        public String getProfileId() {
            return this.profileId;
        }

        @Override
        public Collection<? extends com.clemble.social.data.social.SocialPersonProfile> getSocialProfiles() {
            return this.socialProfiles;
        }

        @Override
        public Profile freeze() {
            return this;
        }
    }

    public static class SimpleProfileBuilder extends ProfileBuilder {
        final static private long serialVersionUID = 1L;

        private String profileId;
        private Collection<? extends SocialPersonProfileBuilder> socialProfiles = new ArrayList();

        public SimpleProfileBuilder() {
        }

        public SimpleProfileBuilder(Profile other) {
            merge(other);
        }

        public SimpleProfileBuilder(String profileId, Collection<? extends com.clemble.social.data.social.SocialPersonProfile> socialProfiles) {
            this.profileId = profileId;
            for (SocialPersonProfile additional : socialProfiles)
                addSocialProfiles(additional);

        }

        @Override
        public String getProfileId() {
            return this.profileId;
        }

        @Override
        public ProfileBuilder setProfileId(String newProfileId) {
            this.profileId = newProfileId;
            return this;
        }

        @Override
        public Collection<? extends SocialPersonProfileBuilder> getSocialProfiles() {
            return this.socialProfiles;
        }

        @Override
        public ProfileBuilder addSocialProfiles(SocialPersonProfile additional) {
            if (additional != null)
                ((Collection<SocialPersonProfile>) (Collection<?>) this.socialProfiles).add(additional);
            return this;
        }
    }
}