package com.socialone.data.user.contact.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.apache.commons.lang3.tuple.MutablePair;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.google.common.collect.Lists;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.socialone.data.social.entity.SocialPersonProfileEntity;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.Profile.ProfileBuilder;

@Entity
@Table(name = "PROFILES")
@SqlResultSetMappings(value = @SqlResultSetMapping(name = ProfileEntity.KEY_VALUE_MAPPING, entities = { @EntityResult(entityClass = MutablePair.class, fields = {
        @FieldResult(column = "LEFT", name = "left"), @FieldResult(column = "RIGHT", name = "right") }) }))
@NamedQueries(value = { @NamedQuery(name = ProfileEntity.QUERY_PROFILE_REMOVE_BY_ID, query = "delete from ProfileEntity profile where profile.profileId = :profileId") })
public class ProfileEntity extends ProfileBuilder {

    /**
     * Generated 12/08/2012
     */
    private static final long serialVersionUID = -4580958148923363270L;

    final public static String KEY_VALUE_MAPPING = "KEY_VALUE_MAPPING";

    final public static String QUERY_PROFILE_REMOVE_BY_ID = "QUERY_PROFILE_REMOVE_BY_ID";
    final public static String QUERY_PROFILE_GET_WITH_PRIMARY_CONNECTION_IN = "SELECT USER_ID AS LEFT, PROFILE_ID AS RIGHT FROM USER_TO_CONNECTIONS WHERE PROFILE_ID IN (SELECT PROFILE_ID FROM PROFILE_TO_SOCIAL_PROFILES WHERE PRIMARY_CONNECTION IN (?) AND PROFILE_ID NOT IN (SELECT PROFILE_ID FROM PROFILE_TO_SOCIAL_PROFILES WHERE PRIMARY_CONNECTION IN (?)))";
    final public static String QUERY_PROFILE_REMOVE_BY_ID_PROFILE_SOCIAL_LINKS = "DELETE FROM PROFILE_TO_SOCIAL_PROFILES WHERE PROFILE_ID = ?";
    final public static String QUERY_PROFILE_REMOVE_BY_ID_PROFILE_USER_LINKS = "DELETE FROM USER_TO_CONNECTIONS WHERE PROFILE_ID = ?";

    @Id
    @Column(name = "PROFILE_ID")
    private String profileId;
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(targetEntity = SocialPersonProfileEntity.class, fetch = FetchType.EAGER)
    @JoinTable(name = "PROFILE_TO_SOCIAL_PROFILES", joinColumns = @JoinColumn(name = "PROFILE_ID", referencedColumnName = "PROFILE_ID"), inverseJoinColumns = @JoinColumn(name = "PRIMARY_CONNECTION", referencedColumnName = "PRIMARY_CONNECTION"))
    private Collection<SocialPersonProfileEntity> socialPersonProfiles = Lists.newArrayList();

    public ProfileEntity() {
    }

    public ProfileEntity(Profile other) {
        merge(other);
    }

    @Override
    public String getProfileId() {
        return profileId;
    }

    @Override
    public ProfileEntity setProfileId(String contactIdentifier) {
        this.profileId = contactIdentifier;
        return this;
    }

    @Override
    public Collection<SocialPersonProfileEntity> getSocialProfiles() {
        return socialPersonProfiles;
    }

    @Override
    public ProfileBuilder addSocialProfiles(SocialPersonProfile additional) {
        if (additional instanceof SocialPersonProfileEntity) {
            socialPersonProfiles.add((SocialPersonProfileEntity) additional);
        } else
            socialPersonProfiles.add(new SocialPersonProfileEntity(additional));
        return this;
    }

}
