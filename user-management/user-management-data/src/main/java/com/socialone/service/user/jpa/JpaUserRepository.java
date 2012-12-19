package com.socialone.service.user.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.socialone.data.entity.ConnectionKeyType;
import com.socialone.data.user.User;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.entity.ProfileEntity;
import com.socialone.data.user.entity.UserEntity;
import com.socialone.service.user.UserRepository;

@Transactional
public class JpaUserRepository implements UserRepository {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    @Override
    public User getUserProfile(String userIdentifier) {
        // Step 1. Sanity check
        if (userIdentifier == null)
            return null;
        // Step 2. Retrieving list of mapped entities
        UserEntity existingUser = entityManager.find(UserEntity.class, userIdentifier);
        // Step 3. Checking result user
        return existingUser != null ? existingUser.freeze() : null;
    }

    @Override
    public String getUserIdentifier(ConnectionKey connectionKey) {
        // Step 1. Sanity check
        if (connectionKey == null)
            return null;
        // Step 2. Querying for the value
        List<String> userIdentifiers = entityManager.createNativeQuery(UserEntity.QUERY_USER_BY_CONNECTION_KEY)
                .setParameter(1, ConnectionKeyType.CONNECTION_KEY_TO_STRING.apply(connectionKey)).getResultList();
        // Step 3. Check returned result
        if (userIdentifiers.size() == 1)
            return userIdentifiers.get(0);
        return null;
    }

    @Override
    public User addUserProfile(User user) {
        return updateUserProfile(user, false);
    }


    @Override
    public User updateUserProfile(User user) {
        return updateUserProfile(user, true);
    }

    private User updateUserProfile(User user, boolean overwrite) {
        if (user == null || user.getId() == null)
            throw new IllegalArgumentException();
        UserEntity existingUser = entityManager.find(UserEntity.class, user.getId());
        if (existingUser == null) {
            existingUser = new UserEntity(user);
            entityManager.persist(existingUser);
        } else {
            existingUser.merge(user);
        }
        return existingUser.freeze();
    }

    @Override
    public void removeUserProfile(String userId) {
        entityManager.createNamedQuery(UserEntity.QUERY_USER_REMOVE_BY_ID).setParameter("userId", userId).executeUpdate();
    }

    @Override
    public Collection<? extends Profile> getConnections(String userIdentifier) {
        // Step 1. Fetching data from database
        UserEntity userEntity = entityManager.find(UserEntity.class, userIdentifier);
        if(userEntity == null)
            return ImmutableList.<Profile>of();
        // Step 2. Transforming fetched data to appropriate format
        return userEntity.getConnections();
    }

    @Override
    public void addConnection(String userIdentifier, Profile profile) {
        if (userIdentifier == null || profile == null)
            return;
        UserEntity existingUser = entityManager.find(UserEntity.class, userIdentifier);
        if (existingUser == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userIdentifier);
            userEntity.setConnections(Lists.newArrayList(new ProfileEntity(profile)));
            entityManager.persist(userEntity);
        } else {
            Collection<ProfileEntity> existingProfiles = existingUser.getConnections();
            if (!existingProfiles.contains(profile)) {
                existingProfiles.add(new ProfileEntity(profile));
            }
        }
    }
    
    @Override
    public void removeConnection(String userIdentifier, final Profile profile) {
        if (userIdentifier == null || profile == null || profile.getProfileId() == null)
            return;
        UserEntity existingUser = entityManager.find(UserEntity.class, userIdentifier);
        if (existingUser != null) {
            Collection<ProfileEntity> existingProfiles = existingUser.getConnections();
            Collection<ProfileEntity> filteredProfiles = Collections2.filter(existingProfiles, new Predicate<Profile>(){
                public boolean apply(Profile filteredProfile) {
                    return profile.getProfileId().equals(profile.getProfileId());
                }
            });
            existingProfiles.removeAll(filteredProfiles);
        }
    }

    @Override
    public Collection<String> getUsersWithConnections(Collection<ConnectionKey> primaryConnection) {
        // Step 1. Sanity check
        if(primaryConnection == null || primaryConnection.size() == 0)
            return ImmutableList.<String>of();
        // Step 2. Preparing queury
        Collection<String> connectionKeys = Collections2.transform(primaryConnection, ConnectionKeyType.CONNECTION_KEY_TO_STRING);
        String commaSeparatedValue = StringUtils.collectionToDelimitedString(connectionKeys, ",", "'", "'");
        String query = UserEntity.QUERY_USER_GET_USER_WITH_CONNECTIONS.replace("?", commaSeparatedValue);
        // Step 3. Performing actual Query
        return entityManager.createNativeQuery(query).getResultList();
    }
}
