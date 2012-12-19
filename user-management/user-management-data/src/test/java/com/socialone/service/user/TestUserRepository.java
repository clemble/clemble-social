package com.socialone.service.user;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;

import com.socialone.AbstractDataTierTest;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.user.User;
import com.socialone.data.user.User.SimpleUserBuilder;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.Profile.SimpleProfileBuilder;
import com.socialone.data.user.entity.UserEntity;
import com.socialone.service.user.UserRepository;
import com.socialone.test.utils.TestRandomUtils;

public class TestUserRepository extends AbstractDataTierTest {

    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void testAddEmptyUser() {
        // Step 1. Generating empty user with only fuse identifier
        User user = new SimpleUserBuilder().setId(TestRandomUtils.randomString(10));
        userRepository.addUserProfile(user);
        // Step 2. Check extracted data
        User savedUser = userRepository.getUserProfile(user.getId());
        Assert.assertEquals(savedUser, new SimpleUserBuilder().setId(user.getId()).setProfile(new SimpleProfileBuilder().setProfileId(user.getId()))); // Empty profile created by default
    }

    @Test
    public void testAddGetUpdateDelete() {
        // Step 1. Check add
        User user = DataGenerationUtils.generateUser();
        userRepository.addUserProfile(user);
        // Step 2. Check match
        User savedUser = userRepository.getUserProfile(user.getId());
        Assert.assertEquals(user, savedUser);
        // Step 3. Adding another personProfile to the user
        UserEntity changedEntity = new UserEntity(savedUser);
        changedEntity.addConnections(DataGenerationUtils.generateProfile());
        user = changedEntity.freeze();
        entityManager.flush();
        entityManager.clear();
        // Step 4. Updating modified entity
        userRepository.updateUserProfile(user);
        savedUser = userRepository.getUserProfile(user.getId());
        Assert.assertEquals(user, savedUser);
    }
    
    @Test
    public void testConnections() {
        // Step 1. Check add
        User user = DataGenerationUtils.generateUser();
        userRepository.addUserProfile(user);
        // Step 2. Check match
        User savedUser = userRepository.getUserProfile(user.getId());
        Assert.assertEquals(user, savedUser);
        // Step 3. Adding another personProfile to the user
        Profile newProfile = DataGenerationUtils.generateProfile();
        // Emulating result presentation
        UserEntity changedEntity = new UserEntity(savedUser);
        changedEntity.addConnections(newProfile);
        user = changedEntity.freeze();
        // Step 4. Updating modified entity
        userRepository.addConnection(user.getId(), newProfile);
        savedUser = userRepository.getUserProfile(user.getId());
        Assert.assertEquals(user, savedUser);
        // Step 5. Checking associated connections
        Collection<? extends Profile> associatedConnections = userRepository.getConnections(user.getId());
        Assert.assertTrue(associatedConnections.containsAll(user.getConnections()));
        Assert.assertTrue(user.getConnections().containsAll(associatedConnections));
    }
    
    @Test
    public void testGetUserByConnectionKey(){
        // Step 1. Check add
        User user = DataGenerationUtils.generateUser();
        userRepository.addUserProfile(user);
        // Step 2. Check connection Key lookup
        entityManager.flush();
        Assert.assertTrue(user.getProfile().getSocialProfiles().size() > 0);
        for(SocialPersonProfile socialPersonProfile: user.getProfile().getSocialProfiles()) {
            ConnectionKey connectionKey = socialPersonProfile.getPrimaryConnection();
            Assert.assertNotNull(connectionKey);
            Assert.assertEquals(userRepository.getUserIdentifier(connectionKey), user.getId());
        }
    }

}
