package com.clemble.social.data.social.connection.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.social.connection.SocialConnection.SocialConnectionBuilder;
import com.google.common.collect.Sets;

@Embeddable
public class SocialConnectionEntity extends SocialConnectionBuilder {
    /**
     * 
     */
    private static final long serialVersionUID = -7378565263951956094L;

    @Type(type = "connectionKey")
    @Columns(columns = { @Column(name = "PRIMARY_CONNECTION", insertable = false, updatable = false) })
    private ConnectionKey primaryConnection;

    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "SOCIAL_PERSON_CONNECTIONS", joinColumns = {
            @JoinColumn(name = "PRIMARY_CONNECTION", insertable = false, updatable = false, referencedColumnName = "PRIMARY_CONNECTION")})
    @Type(type = "connectionKey")
    @Columns(columns = { @Column(name = "CONNECTION") })
    private Set<ConnectionKey> connectionKeys = Sets.newHashSet();

    @Override
    public ConnectionKey getPrimaryConnection() {
        return primaryConnection;
    }

    @Override
    public SocialConnectionEntity setPrimaryConnection(ConnectionKey primaryConnection) {
        this.primaryConnection = primaryConnection;
        return this;
    }

    @Override
    public SocialConnectionEntity addConnectionKey(ConnectionKey additional) {
        connectionKeys.add(additional);
        return this;
    }

    @Override
    public Collection<? extends ConnectionKey> getConnectionKey() {
        return connectionKeys;
    }

}
