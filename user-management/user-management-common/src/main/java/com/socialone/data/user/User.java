package com.socialone.data.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.google.common.collect.ImmutableList;
import com.socialone.data.Immutable;
import com.socialone.data.markups.Mutable;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.Profile.ProfileBuilder;
import com.socialone.data.user.contact.Profile.SimpleProfileBuilder;

public abstract class User implements Serializable, Mutable<User> {
    final static private long serialVersionUID = 1L;

    abstract public String getId();

    abstract public Profile getProfile();

    abstract public Collection<? extends com.socialone.data.user.contact.Profile> getConnections();

    @Override
    public User freeze() {
        return new ImmutableUser(getId(), getProfile(), getConnections());
    }

    @Override
    public User merge(User other) {
        throw new IllegalAccessError();
    }

    @Override
    public User safeMerge(User other) {
        throw new IllegalAccessError();
    }

    @Override
    public User safeMerge(Collection<? extends User> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getId() == null ? 0 : getId().hashCode());
        result = 31 * result + (getProfile() == null ? 0 : getProfile().hashCode());
        result = 31 * result + (getConnections() == null ? 0 : getConnections().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        User other = (User) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        if (getProfile() == null) {
            if (other.getProfile() != null)
                return false;
        } else if (!getProfile().equals(other.getProfile()))
            return false;
        if (getConnections() == null) {
            if (getConnections() != null)
                return false;
        } else if (other.getConnections() == null) {
            return false;
        } else if (!getConnections().containsAll(other.getConnections()) || !other.getConnections().containsAll(getConnections()))
            return false;
        return true;
    }

    public static abstract class UserBuilder extends User implements Builder<User> {

        final static private long serialVersionUID = 1L;

        public UserBuilder() {
        }

        public UserBuilder(User other) {
            merge(other);
        }

        abstract public ProfileBuilder getProfile();

        abstract public Collection<? extends ProfileBuilder> getConnections();

        abstract public UserBuilder setId(String newId);

        abstract public UserBuilder setProfile(Profile newProfile);

        final public UserBuilder setConnections(Collection<? extends com.socialone.data.user.contact.Profile> additional) {
            getConnections().clear();
            if (additional != null && !additional.isEmpty())
                for (Profile addition : additional) {
                    if (addition != null)
                        addConnections(addition);
                }
            return this;
        }

        abstract public UserBuilder addConnections(Profile additional);

        @Override
        public UserBuilder merge(User other) {
            if (other != null) {
                setId(other.getId()).setProfile(other.getProfile()).setConnections(other.getConnections());
            }
            return this;
        }

        @Override
        public UserBuilder safeMerge(User other) {
            if (other != null) {
                if (getId() == null)
                    setId(other.getId());
                if (getProfile() != null) {
                    getProfile().safeMerge(other.getProfile());
                } else {
                    setProfile(other.getProfile());
                }

                if (other.getConnections() != null && !other.getConnections().isEmpty()) {
                    for (com.socialone.data.user.contact.Profile addition : other.getConnections()) {
                        if (addition != null)
                            addConnections(addition);
                    }
                }

            }
            return this;
        }

        @Override
        public UserBuilder safeMerge(Collection<? extends User> others) {
            if (others != null && !others.isEmpty()) {
                for (User other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public User build() {
            return freeze();
        }
    }

    final public static class ImmutableUser extends User implements Immutable {
        final static private long serialVersionUID = 1L;

        final private String id;
        final private Profile profile;
        final private Collection<? extends com.socialone.data.user.contact.Profile> connections;

        public ImmutableUser(User other) {
            this((other != null ? other.getId() : null), (other != null ? other.getProfile() : null), (other != null ? other.getConnections() : ImmutableList
                    .<com.socialone.data.user.contact.Profile> of()));
        }

        public ImmutableUser(String id, Profile profile, Collection<? extends com.socialone.data.user.contact.Profile> connections) {
            this.id = id;
            this.profile = profile != null ? profile.freeze() : null;
            if (connections != null) {
                ArrayList<Profile> tmpConnections = new ArrayList<Profile>();
                for (Profile value : connections) {
                    if (value != null) {
                        tmpConnections.add(value.freeze());
                    }
                }
                this.connections = ImmutableList.<Profile> copyOf(tmpConnections);
            } else {
                this.connections = ImmutableList.<Profile> of();
            }
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public Profile getProfile() {
            return this.profile;
        }

        @Override
        public Collection<? extends com.socialone.data.user.contact.Profile> getConnections() {
            return this.connections;
        }

        @Override
        public User freeze() {
            return this;
        }
    }

    public static class SimpleUserBuilder extends UserBuilder {
        final static private long serialVersionUID = 1L;

        private String id;
        private ProfileBuilder profile;
        private Collection<? extends ProfileBuilder> connections = new ArrayList();

        public SimpleUserBuilder() {
        }

        public SimpleUserBuilder(User other) {
            merge(other);
        }

        public SimpleUserBuilder(String id, Profile profile, Collection<? extends com.socialone.data.user.contact.Profile> connections) {
            this.id = id;
            if (profile instanceof ProfileBuilder)
                this.profile = (ProfileBuilder) profile;
            else
                this.profile = new SimpleProfileBuilder(profile);
            for (Profile addition : connections) {
                if (addition != null)
                    addConnections(addition);
            }

        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public UserBuilder setId(String newId) {
            this.id = newId;
            return this;
        }

        @Override
        public ProfileBuilder getProfile() {
            return this.profile;
        }

        @Override
        public UserBuilder setProfile(Profile newProfile) {
            if (newProfile instanceof ProfileBuilder)
                this.profile = (ProfileBuilder) newProfile;
            else
                this.profile = new SimpleProfileBuilder(newProfile);
            return this;
        }

        @Override
        public Collection<? extends ProfileBuilder> getConnections() {
            return this.connections;
        }

        @Override
        public UserBuilder addConnections(Profile additional) {
            ((Collection<Profile>) (Collection<?>) this.connections).add(additional);
            return this;
        }
    }
}