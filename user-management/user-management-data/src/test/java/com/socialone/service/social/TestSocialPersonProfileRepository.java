package com.socialone.service.social;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;

import com.google.common.collect.Lists;
import com.socialone.AbstractDataTierTest;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.socialone.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.socialone.data.social.entity.SocialPersonProfileEntity;
import com.socialone.service.social.SocialPersonProfileRepository;
import com.socialone.service.user.DataGenerationUtils;
import com.socialone.test.utils.TestRandomUtils;

public class TestSocialPersonProfileRepository extends AbstractDataTierTest {

    @Autowired
    private SocialPersonProfileRepository socialPersonProfileRepository;

    @Test
    public void testAddGetUpdateSocialPersonProfile() {
        SocialPersonProfile socialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        // Step 1. Testing add
        socialPersonProfileRepository.updateSocialPersonProfile(socialPersonProfile);
        // Step 2. Testing get
        SocialPersonProfile savedProfile = socialPersonProfileRepository.getSocialPersonProfile(socialPersonProfile.getPrimaryConnection());
        Assert.assertEquals(savedProfile, socialPersonProfile);
        // Step 3. Testing update
        SocialPersonProfileBuilder modifiedSocialPersonProfile = new SimpleSocialPersonProfileBuilder(socialPersonProfile)
            .setFirstName(TestRandomUtils.randomString(10));
        modifiedSocialPersonProfile.getSocialConnection().addConnectionKey(new ConnectionKey("newService", "newUser"));
        socialPersonProfile = modifiedSocialPersonProfile.freeze();

        socialPersonProfileRepository.updateSocialPersonProfile(socialPersonProfile);

        savedProfile = socialPersonProfileRepository.getSocialPersonProfile(socialPersonProfile.getPrimaryConnection());
        Assert.assertEquals(savedProfile, socialPersonProfile);
        // Step 4. Testing remove
        socialPersonProfileRepository.removeSocialPersonProfile(socialPersonProfile.getPrimaryConnection());
        Assert.assertNull(socialPersonProfileRepository.getSocialPersonProfile(socialPersonProfile.getPrimaryConnection()));
    }
    
    @Test
    public void testAddAndGetFewProfiles() {
        SocialPersonProfile socialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        // Step 1. Testing adding 2 social person profiles
        socialPersonProfileRepository.addSocialPersonProfile(socialPersonProfile);
        SocialPersonProfileEntity anotherSocialPersonProfile = new SocialPersonProfileEntity(DataGenerationUtils.generateSocialPersonProfile());
        anotherSocialPersonProfile.setPrimaryConnection(new ConnectionKey(socialPersonProfile.getPrimaryConnection().getProviderId(), TestRandomUtils.randomString(10)));
        socialPersonProfileRepository.addSocialPersonProfile(anotherSocialPersonProfile);
        // Step 2. Testing get
        Collection<? extends SocialPersonProfile> socialPersonProfiles = socialPersonProfileRepository.getSocialPersonProfiles(socialPersonProfile.getPrimaryConnection().getProviderId(), Lists.newArrayList(socialPersonProfile.getPrimaryConnection().getProviderUserId(), anotherSocialPersonProfile.getPrimaryConnection().getProviderUserId()));
        Assert.assertEquals(2, socialPersonProfiles.size());
        Assert.assertTrue(socialPersonProfiles.contains(socialPersonProfile));
        Assert.assertTrue(socialPersonProfiles.contains(anotherSocialPersonProfile.freeze()));
    }
    
    @Test
    public void testAddBatch() {
        SocialPersonProfile socialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        SocialPersonProfileEntity anotherSocialPersonProfile = new SocialPersonProfileEntity(DataGenerationUtils.generateSocialPersonProfile());
        anotherSocialPersonProfile.setPrimaryConnection(new ConnectionKey(socialPersonProfile.getPrimaryConnection().getProviderId(), TestRandomUtils.randomString(10)));
        // Step 1. Testing adding 2 social person profiles
        socialPersonProfileRepository.addSocialPersonProfiles(Lists.newArrayList(socialPersonProfile, anotherSocialPersonProfile));
        // Step 2. Testing get
        Collection<? extends SocialPersonProfile> socialPersonProfiles = socialPersonProfileRepository.getSocialPersonProfiles(socialPersonProfile.getPrimaryConnection().getProviderId(), Lists.newArrayList(socialPersonProfile.getPrimaryConnection().getProviderUserId(), anotherSocialPersonProfile.getPrimaryConnection().getProviderUserId()));
        Assert.assertEquals(2, socialPersonProfiles.size());
        Assert.assertTrue(socialPersonProfiles.contains(socialPersonProfile));
        Assert.assertTrue(socialPersonProfiles.contains(anotherSocialPersonProfile.freeze()));
    }
}
