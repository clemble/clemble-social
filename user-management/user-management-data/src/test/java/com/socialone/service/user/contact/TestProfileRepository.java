package com.socialone.service.user.contact;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.socialone.AbstractDataTierTest;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.Profile.SimpleProfileBuilder;
import com.socialone.service.user.DataGenerationUtils;
import com.stresstest.random.ObjectGenerator;

public class TestProfileRepository extends AbstractDataTierTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void testAddGetUpdate() {
        // Step 1. Adding SocialPersonProfile
        SocialPersonProfile socialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        // Step 2. Saving contact with the reference to this SocialPersonProfile
        Profile profile = new SimpleProfileBuilder().setProfileId(ObjectGenerator.generate(String.class)).addSocialProfiles(socialPersonProfile);
        profileRepository.addProfile(profile);
        // Step 3. Getting social person profile
        Profile savedContact = profileRepository.getProfile(profile.getProfileId());
        Assert.assertEquals(profile, savedContact);
        // Step 4. Generating another SocialPersonProfile and adding it to the
        // Queue
        SocialPersonProfile anotherSocialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        // Step 5. Checking update of SocialPersonProfile would work
        profile = new SimpleProfileBuilder().setProfileId(profile.getProfileId()).setSocialProfiles(
                Lists.newArrayList(socialPersonProfile, anotherSocialPersonProfile));
        profileRepository.updateProfile(profile);
        Assert.assertEquals(profile, profileRepository.getProfile(profile.getProfileId()));
    }

    @Test
    public void testBatchAdd() {
        // Step 1. Adding SocialPersonProfile
        SocialPersonProfile socialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        // Step 2. Saving contact with the reference to this SocialPersonProfile
        Profile profile = new SimpleProfileBuilder().setProfileId(ObjectGenerator.generate(String.class)).addSocialProfiles(socialPersonProfile);
        Profile anotherProfile = new SimpleProfileBuilder().setProfileId(ObjectGenerator.generate(String.class)).addSocialProfiles(socialPersonProfile);
        profileRepository.addProfiles(Lists.newArrayList(profile, anotherProfile));
        // Step 3. Getting social person profile
        Profile savedContact = profileRepository.getProfile(profile.getProfileId());
        Assert.assertEquals(profile, savedContact);
        savedContact = profileRepository.getProfile(anotherProfile.getProfileId());
        Assert.assertEquals(anotherProfile, savedContact);
    }

    @Test
    public void testAddRemove() {
        // Step 1. Adding SocialPersonProfile
        SocialPersonProfile socialPersonProfile = DataGenerationUtils.generateSocialPersonProfile();
        // Step 2. Saving contact with the reference to this SocialPersonProfile
        Profile profile = new SimpleProfileBuilder().setProfileId(ObjectGenerator.generate(String.class)).addSocialProfiles(socialPersonProfile);
        profileRepository.addProfile(profile);
        // Step 3. Getting social person profile
        Profile savedContact = profileRepository.getProfile(profile.getProfileId());
        Assert.assertEquals(profile, savedContact);
        // Step 4. Removing social person profile
        profileRepository.removeProfile(profile.getProfileId());
    }

}
