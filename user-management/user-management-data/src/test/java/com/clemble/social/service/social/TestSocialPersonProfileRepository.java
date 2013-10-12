package com.clemble.social.service.social;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.social.AbstractDataTierTest;
import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.clemble.social.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.clemble.social.data.social.entity.SocialPersonProfileEntity;
import com.clemble.social.service.social.SocialPersonProfileRepository;
import com.clemble.social.service.user.DataGenerationUtils;
import com.clemble.test.random.ObjectGenerator;
import com.google.common.collect.Lists;

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
            .setFirstName(ObjectGenerator.generate(String.class));
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
        anotherSocialPersonProfile.setPrimaryConnection(new ConnectionKey(socialPersonProfile.getPrimaryConnection().getProviderId(), ObjectGenerator.generate(String.class)));
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
        anotherSocialPersonProfile.setPrimaryConnection(new ConnectionKey(socialPersonProfile.getPrimaryConnection().getProviderId(), ObjectGenerator.generate(String.class)));
        // Step 1. Testing adding 2 social person profiles
        socialPersonProfileRepository.addSocialPersonProfiles(Lists.newArrayList(socialPersonProfile, anotherSocialPersonProfile));
        // Step 2. Testing get
        Collection<? extends SocialPersonProfile> socialPersonProfiles = socialPersonProfileRepository.getSocialPersonProfiles(socialPersonProfile.getPrimaryConnection().getProviderId(), Lists.newArrayList(socialPersonProfile.getPrimaryConnection().getProviderUserId(), anotherSocialPersonProfile.getPrimaryConnection().getProviderUserId()));
        Assert.assertEquals(2, socialPersonProfiles.size());
        Assert.assertTrue(socialPersonProfiles.contains(socialPersonProfile));
        Assert.assertTrue(socialPersonProfiles.contains(anotherSocialPersonProfile.freeze()));
    }
}
