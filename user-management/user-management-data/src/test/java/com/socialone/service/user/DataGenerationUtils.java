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
import com.socialone.test.utils.TestRandomUtils;

public class DataGenerationUtils {

    public static SocialConnection generateSocialConnection() {
        ConnectionKey primaryConnectionKey = ConnectionKeyFactory.get(TestRandomUtils.randomName(SocialNetworkType.class), TestRandomUtils.randomString(10));
        ConnectionKey link = ConnectionKeyFactory.get(TestRandomUtils.randomElement(SocialNetworkType.class).name(), TestRandomUtils.randomString(10));
        return new ImmutableSocialConnection(primaryConnectionKey, ImmutableList.<ConnectionKey> of(link));
    }

    public static SocialPersonProfile generateSocialPersonProfile() {
        SocialConnection socialConnection = generateSocialConnection();
        SocialDate socialDate = new SimpleSocialDateBuilder().setDay(TestRandomUtils.nextInt(Integer.MAX_VALUE))
                .setMonth(TestRandomUtils.nextInt(Integer.MAX_VALUE)).setYear(TestRandomUtils.nextInt(Integer.MAX_VALUE));
        return new SocialPersonProfileEntity().setSocialConnection(socialConnection).setPrimaryConnection(socialConnection.getPrimaryConnection())
                .setFirstName(TestRandomUtils.randomString(10)).setLastName(TestRandomUtils.randomString(10)).setImage(TestRandomUtils.randomString(10))
                .setUrl(TestRandomUtils.randomString(10)).setBirthDate(socialDate).setGender(TestRandomUtils.randomElement(Gender.class));
    }

    public static SocialPersonProfileEntity generateSocialPersonProfileEntity() {
        SocialConnection socialConnection = generateSocialConnection();
        SocialPersonProfileEntity entitySocialPersonProfile = new SocialPersonProfileEntity();
        entitySocialPersonProfile.setSocialConnection(socialConnection);
        entitySocialPersonProfile.setBirthDate(new SimpleSocialDateBuilder().setDay(TestRandomUtils.nextInt(Integer.MAX_VALUE))
                .setMonth(TestRandomUtils.nextInt(Integer.MAX_VALUE)).setYear(TestRandomUtils.nextInt(Integer.MAX_VALUE)));
        entitySocialPersonProfile.setGender(TestRandomUtils.randomElement(Gender.class));
        entitySocialPersonProfile.setFirstName(TestRandomUtils.randomString(10));
        entitySocialPersonProfile.setLastName(TestRandomUtils.randomString(10));
        entitySocialPersonProfile.setImage(TestRandomUtils.randomString(10));
        entitySocialPersonProfile.setUrl(TestRandomUtils.randomString(10));
        return entitySocialPersonProfile;
    }

    public static Profile generateProfile() {
        return generateProfile(TestRandomUtils.randomString(10));
    }

    public static Profile generateProfile(String profileId) {
        return new SimpleProfileBuilder().setProfileId(profileId).setSocialProfiles(ImmutableList.of(generateSocialPersonProfile()));
    }

    public static User generateUser() {
        String userId = TestRandomUtils.randomString(10);
        return new ImmutableUser(userId, generateProfile(userId), Lists.newArrayList(generateProfile()));
    }

}
