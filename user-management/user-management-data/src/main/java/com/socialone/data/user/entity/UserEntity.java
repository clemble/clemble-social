package com.socialone.data.user.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.google.common.collect.Lists;
import com.socialone.data.user.User;
import com.socialone.data.user.User.UserBuilder;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.entity.ProfileEntity;

@Entity
@Table(name = "USERS")
@NamedQueries(value = {
        @NamedQuery(name = UserEntity.QUERY_USER_GET_BY_ID, query = "select user from UserEntity user left join fetch user.connections where user.userId = :userId"),
        @NamedQuery(name = UserEntity.QUERY_USER_REMOVE_BY_ID, query = "delete from UserEntity user where user.userId = :userId") })
public class UserEntity extends UserBuilder {

    /**
     * Generated 18/08/2012
     */
    private static final long serialVersionUID = 9169071662960761955L;

    final public static String QUERY_USER_GET_BY_ID = "QUERY_USER_GET_BY_ID";
    final public static String QUERY_USER_GET_USER_WITH_CONNECTIONS = "SELECT PROFILE_ID FROM PROFILES WHERE PROFILE_ID IN (SELECT PROFILE_ID FROM PROFILE_TO_SOCIAL_PROFILES WHERE PRIMARY_CONNECTION IN (?)) AND PROFILE_ID IN (SELECT USER_ID FROM USERS)";
    final public static String QUERY_USER_BY_CONNECTION_KEY = "SELECT USER_ID FROM USERS WHERE USER_ID IN (SELECT PROFILE_ID FROM PROFILE_TO_SOCIAL_PROFILES WHERE PRIMARY_CONNECTION = ?)";
    final public static String QUERY_USER_REMOVE_BY_ID = "QUERY_USER_REMOVE_BY_ID";

    @Id
    @Column(name = "USER_ID")
    private String userId;
    @OneToOne(targetEntity = ProfileEntity.class, fetch = FetchType.EAGER, optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false, referencedColumnName = "PROFILE_ID")
    private ProfileEntity profile;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(targetEntity = ProfileEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "USER_TO_CONNECTIONS", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "PROFILE_ID", referencedColumnName = "PROFILE_ID"))
    private Collection<ProfileEntity> connections = Lists.newArrayList();

    public UserEntity() {
    }
    
    public UserEntity(User base) {
        merge(base);
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public UserEntity setId(String identifier) {
        this.userId = identifier;
        return this;
    }

    @Override
    public ProfileEntity getProfile() {
        return profile;
    }

    @Override
    public UserEntity setProfile(Profile profile) {
        this.profile = new ProfileEntity(profile);
        this.profile.setProfileId(getId());
        return this;
    }

    @Override
    public Collection<ProfileEntity> getConnections() {
        return connections;
    }

    @Override
    public UserEntity addConnections(Profile additional) {
        if (additional instanceof ProfileEntity) {
            connections.add((ProfileEntity) additional);
        } else {
            connections.add(new ProfileEntity(additional));
        }
        return this;
    }

}
