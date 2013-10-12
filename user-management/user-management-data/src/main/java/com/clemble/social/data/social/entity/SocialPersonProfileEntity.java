package com.clemble.social.data.social.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.data.Gender;
import com.clemble.social.data.date.SocialDate;
import com.clemble.social.data.date.SocialDate.SimpleSocialDateBuilder;
import com.clemble.social.data.date.SocialDate.SocialDateBuilder;
import com.clemble.social.data.entity.ConnectionKeyType;
import com.clemble.social.data.entity.SocialDateType;
import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.clemble.social.data.social.connection.SocialConnection;
import com.clemble.social.data.social.connection.SocialConnection.SocialConnectionBuilder;
import com.clemble.social.data.social.connection.entity.SocialConnectionEntity;

@Entity
@Table(name = "SOCIAL_PERSON_PROFILES")
@TypeDefs(value = { @TypeDef(name = "connectionKey", defaultForType = ConnectionKey.class, typeClass = ConnectionKeyType.class),
        @TypeDef(name = "socialDate", defaultForType = SocialDate.class, typeClass = SocialDateType.class) })
@NamedQueries(value = { @NamedQuery(name = SocialPersonProfileEntity.QUERY_SOCIAL_PROFILE_GET_WITH_ID_IN, query = "select profile from SocialPersonProfileEntity profile left join fetch profile.socialConnection where profile.primaryConnection in :primaryConnection") // ,
// @NamedQuery(name = SocialPersonProfileEntity.QUERY_SOCIAL_PROFILE_GET_PRIMARY_CONNECTIONS_WITH_ID_IN, query =
// "select profile.primaryConnection from SocialPersonProfileEntity profile left join fetch profile.socialConnection where profile.socialConnection.connectionKeys.providerId = :providerId and profile.socialConnection.connectionKeys.providerUserId in :providerUserIds")
})
public class SocialPersonProfileEntity extends SocialPersonProfileBuilder {

    /**
     * Generated 12/08/2012
     */
    private static final long serialVersionUID = -6998837770526070270L;

    final public static String QUERY_SOCIAL_PROFILE_GET_WITH_ID_IN = "QUERY_SOCIAL_PROFILE_GET_WITH_ID_IN";
    final public static String QUERY_SOCIAL_PROFILE_GET_PRIMARY_CONNECTIONS_WITH_ID_IN = "QUERY_SOCIAL_PROFILE_GET_PRIMARY_CONNECTIONS_WITH_ID_IN";

    @Id
    @Type(type = "connectionKey")
    @Columns(columns = { @Column(name = "PRIMARY_CONNECTION") })
    private ConnectionKey primaryConnection;
    @Column(name = "PROFILE_URL")
    private String url;
    @Column(name = "PROFILE_IMAGE")
    private String image;
    @Embedded
    private SocialConnectionEntity socialConnection;

    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Type(type = "socialDate")
    @Columns(columns = { @Column(name = "BIRTH_DAY"), @Column(name = "BIRTH_MONTH"), @Column(name = "BIRTH_YEAR") })
    private SocialDateBuilder birthDate;
    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public SocialPersonProfileEntity() {
    }
    
    public SocialPersonProfileEntity(SocialPersonProfile socialPersonProfile) {
        merge(socialPersonProfile);
    }
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public SocialPersonProfileEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public SocialPersonProfileEntity setImage(String image) {
        this.image = image;
        return this;
    }

    @Override
    public ConnectionKey getPrimaryConnection() {
        return primaryConnection;
    }

    @Override
    public SocialPersonProfileEntity setPrimaryConnection(ConnectionKey primaryConnection) {
        this.primaryConnection = primaryConnection;
        return this;
    }

    @Override
    public SocialConnectionBuilder getSocialConnection() {
        return socialConnection;
    }

    @Override
    public SocialPersonProfileEntity setSocialConnection(SocialConnection socialConnection) {
        if(this.socialConnection == null)
            this.socialConnection = new SocialConnectionEntity();
        this.socialConnection.safeMerge(socialConnection);
        return this;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public SocialPersonProfileEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public SocialPersonProfileEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public SocialDateBuilder getBirthDate() {
        return birthDate;
    }

    @Override
    public SocialPersonProfileEntity setBirthDate(SocialDate birthDate) {
        if(birthDate instanceof SocialDateBuilder)
            this.birthDate = (SocialDateBuilder) birthDate;
        else
            this.birthDate = new SimpleSocialDateBuilder(birthDate);
        return this;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public SocialPersonProfileEntity setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

}
