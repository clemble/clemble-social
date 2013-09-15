package com.socialone.service.user;

import org.springframework.social.connect.ConnectionKey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.socialone.data.Gender;
import com.socialone.data.date.SocialDate;
import com.socialone.data.date.SocialDate.SimpleSocialDateBuilder;
import com.socialone.data.social.SocialNetworkType;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.connection.ConnectionKeyFactory;
import com.socialone.data.social.connection.SocialConnection;
import com.socialone.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.socialone.data.social.entity.SocialPersonProfileEntity;
import com.socialone.data.user.User;
import com.socialone.data.user.User.ImmutableUser;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.Profile.SimpleProfileBuilder;
import com.stresstest.random.ObjectGenerator;

public class DataGenerationUtils {

    public static SocialConnection generateSocialConnection() {
        ConnectionKey primaryConnectionKey = ConnectionKeyFactory.get(ObjectGenerator.generate(SocialNetworkType.class).name(), ObjectGenerator.generate(String.class));
        ConnectionKey link = ConnectionKeyFactory.get(ObjectGenerator.generate(SocialNetworkType.class).name(), ObjectGenerator.generate(String.class));
        return new ImmutableSocialConnection(primaryConnectionKey, ImmutableList.<ConnectionKey> of(link));
    }

    public static SocialPersonProfile generateSocialPersonProfile() {
        SocialConnection socialConnection = generateSocialConnection();
        SocialDate socialDate = new SimpleSocialDateBuilder().setDay(ObjectGenerator.generate(Integer.class))
                .setMonth(ObjectGenerator.generate(Integer.class)).setYear(ObjectGenerator.generate(Integer.class));
        return new SocialPersonProfileEntity().setSocialConnection(socialConnection).setPrimaryConnection(socialConnection.getPrimaryConnection())
                .setFirstName(ObjectGenerator.generate(String.class)).setLastName(ObjectGenerator.generate(String.class)).setImage(ObjectGenerator.generate(String.class))
                .setUrl(ObjectGenerator.generate(String.class)).setBirthDate(socialDate).setGender(ObjectGenerator.generate(Gender.class));
    }

    public static SocialPersonProfileEntity generateSocialPersonProfileEntity() {
        SocialConnection socialConnection = generateSocialConnection();
        SocialPersonProfileEntity entitySocialPersonProfile = new SocialPersonProfileEntity();
        entitySocialPersonProfile.setSocialConnection(socialConnection);
        entitySocialPersonProfile.setBirthDate(new SimpleSocialDateBuilder().setDay(ObjectGenerator.generate(Integer.class))
                .setMonth(ObjectGenerator.generate(Integer.class)).setYear(ObjectGenerator.generate(Integer.class)));
        entitySocialPersonProfile.setGender(ObjectGenerator.generate(Gender.class));
        entitySocialPersonProfile.setFirstName(ObjectGenerator.generate(String.class));
        entitySocialPersonProfile.setLastName(ObjectGenerator.generate(String.class));
        entitySocialPersonProfile.setImage(ObjectGenerator.generate(String.class));
        entitySocialPersonProfile.setUrl(ObjectGenerator.generate(String.class));
        return entitySocialPersonProfile;
    }

    public static Profile generateProfile() {
        return generateProfile(ObjectGenerator.generate(String.class));
    }

    public static Profile generateProfile(String profileId) {
        return new SimpleProfileBuilder().setProfileId(profileId).setSocialProfiles(ImmutableList.of(generateSocialPersonProfile()));
    }

    public static User generateUser() {
        String userId = ObjectGenerator.generate(String.class);
        return new ImmutableUser(userId, generateProfile(userId), Lists.newArrayList(generateProfile()));
    }

}
