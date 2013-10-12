package com.clemble.social.data.social.connection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.Immutable;
import com.clemble.social.data.markups.Mutable;
import com.google.common.collect.ImmutableList;

public abstract class SocialConnection implements Serializable, Mutable<SocialConnection> {
    final static private long serialVersionUID = 1L;

    abstract public ConnectionKey getPrimaryConnection();

    abstract public Collection<? extends org.springframework.social.connect.ConnectionKey> getConnectionKey();

    @Override
    public SocialConnection freeze() {
        return new ImmutableSocialConnection(getPrimaryConnection(), getConnectionKey());
    }

    @Override
    public SocialConnection merge(SocialConnection other) {
        throw new IllegalAccessError();
    }

    @Override
    public SocialConnection safeMerge(SocialConnection other) {
        throw new IllegalAccessError();
    }

    @Override
    public SocialConnection safeMerge(Collection<? extends SocialConnection> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getPrimaryConnection() == null ? 0 : getPrimaryConnection().hashCode());
        result = 31 * result + (getConnectionKey() == null ? 0 : getConnectionKey().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        SocialConnection other = (SocialConnection) obj;
        if (getPrimaryConnection() == null) {
            if (other.getPrimaryConnection() != null)
                return false;
        } else if (!getPrimaryConnection().equals(other.getPrimaryConnection()))
            return false;
        if (getConnectionKey() == null) {
            if (getConnectionKey() != null)
                return false;
        } else if (other.getConnectionKey() == null) {
            return false;
        } else if (!getConnectionKey().containsAll(other.getConnectionKey()) || !other.getConnectionKey().containsAll(getConnectionKey()))
            return false;
        return true;
    }

    public static abstract class SocialConnectionBuilder extends SocialConnection implements Builder<SocialConnection> {

        final static private long serialVersionUID = 1L;

        public SocialConnectionBuilder() {
        }

        public SocialConnectionBuilder(SocialConnection other) {
            merge(other);
        }

        abstract public Collection<? extends ConnectionKey> getConnectionKey();

        abstract public SocialConnectionBuilder setPrimaryConnection(ConnectionKey newPrimaryConnection);

        final public SocialConnectionBuilder setConnectionKey(Collection<? extends org.springframework.social.connect.ConnectionKey> additional) {
            getConnectionKey().clear();
            if (additional != null && !additional.isEmpty())
                for (ConnectionKey addition : additional)
                    addConnectionKey(addition);
            return this;
        }

        abstract public SocialConnectionBuilder addConnectionKey(ConnectionKey additional);

        @Override
        public SocialConnectionBuilder merge(SocialConnection other) {
            if (other != null) {
                setPrimaryConnection(other.getPrimaryConnection()).setConnectionKey(other.getConnectionKey());
            }
            return this;
        }

        @Override
        public SocialConnectionBuilder safeMerge(SocialConnection other) {
            if (other != null) {
                if (getPrimaryConnection() == null) {
                    setPrimaryConnection(other.getPrimaryConnection());
                }

                if (other.getConnectionKey() != null && !other.getConnectionKey().isEmpty()) {
                    for (org.springframework.social.connect.ConnectionKey otherConnectionKey : other.getConnectionKey())
                        addConnectionKey(otherConnectionKey);
                }

            }
            return this;
        }

        @Override
        public SocialConnectionBuilder safeMerge(Collection<? extends SocialConnection> others) {
            if (others != null && !others.isEmpty()) {
                for (SocialConnection other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public SocialConnection build() {
            return freeze();
        }
    }

    final public static class ImmutableSocialConnection extends SocialConnection implements Immutable {
        final static private long serialVersionUID = 1L;

        final private ConnectionKey primaryConnection;
        final private Collection<? extends org.springframework.social.connect.ConnectionKey> connectionKey;

        public ImmutableSocialConnection(SocialConnection other) {
            this((other != null ? other.getPrimaryConnection() : null), (other != null ? other.getConnectionKey() : ImmutableList
                    .<org.springframework.social.connect.ConnectionKey> of()));
        }

        public ImmutableSocialConnection(ConnectionKey primaryConnection, Collection<? extends org.springframework.social.connect.ConnectionKey> connectionKey) {
            this.primaryConnection = primaryConnection;
            if (connectionKey != null) {
                ArrayList<ConnectionKey> tmpConnectionKey = new ArrayList<ConnectionKey>();
                for (ConnectionKey value : connectionKey) {
                    if (value != null) {
                        tmpConnectionKey.add(value);
                    }
                }
                this.connectionKey = ImmutableList.<ConnectionKey> copyOf(tmpConnectionKey);
            } else {
                this.connectionKey = ImmutableList.<ConnectionKey> of();
            }
        }

        @Override
        public ConnectionKey getPrimaryConnection() {
            return this.primaryConnection;
        }

        @Override
        public Collection<? extends org.springframework.social.connect.ConnectionKey> getConnectionKey() {
            return this.connectionKey;
        }

        @Override
        public SocialConnection freeze() {
            return this;
        }
    }

    public static class SimpleSocialConnectionBuilder extends SocialConnectionBuilder {
        final static private long serialVersionUID = 1L;

        private ConnectionKey primaryConnection;
        private Collection<? extends ConnectionKey> connectionKey = new ArrayList();

        public SimpleSocialConnectionBuilder() {
        }

        public SimpleSocialConnectionBuilder(SocialConnection other) {
            super(other);
        }

        public SimpleSocialConnectionBuilder(ConnectionKey primaryConnection,
                Collection<? extends org.springframework.social.connect.ConnectionKey> connectionKey) {
            this.primaryConnection = primaryConnection;
            for (ConnectionKey additional : connectionKey)
                addConnectionKey(additional);

        }

        @Override
        public ConnectionKey getPrimaryConnection() {
            return this.primaryConnection;
        }

        @Override
        public SocialConnectionBuilder setPrimaryConnection(ConnectionKey newPrimaryConnection) {
            this.primaryConnection = newPrimaryConnection;
            return this;
        }

        @Override
        public Collection<? extends ConnectionKey> getConnectionKey() {
            return this.connectionKey;
        }

        @Override
        public SocialConnectionBuilder addConnectionKey(ConnectionKey additional) {
            if (additional != null)
                ((Collection<ConnectionKey>) (Collection<?>) this.connectionKey).add(additional);
            return this;
        }
    }
}