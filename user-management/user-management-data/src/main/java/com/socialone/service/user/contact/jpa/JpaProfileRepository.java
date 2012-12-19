package com.socialone.service.user.contact.jpa;

import java.util.Collection;
import java.util.Map.Entry;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.socialone.data.entity.ConnectionKeyType;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.entity.SocialPersonProfileEntity;
import com.socialone.data.user.contact.Profile;
import com.socialone.data.user.contact.entity.ProfileEntity;
import com.socialone.service.user.contact.ProfileRepository;

public class JpaProfileRepository implements ProfileRepository {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    @Override
    public Profile getProfile(String profileId) {
        if (profileId == null)
            return null;
        // Step 1. Retrieving ContactEntity
        ProfileEntity existingProfile = entityManager.find(ProfileEntity.class, profileId);
        // Step 2. Sending retrieved data
        return existingProfile != null ? existingProfile.build() : null;
    }

    @Override
    public void addProfile(Profile newProfile) {
        // Step 1. Sanity check
        if(newProfile == null || newProfile.getProfileId() == null)
            throw new IllegalArgumentException();
        // Step 2. Persist entity
        try {
            entityManager.persist(new ProfileEntity(newProfile));
        } catch (EntityExistsException entityExistsException) {
            entityExistsException.printStackTrace();
        }
    }

    @Override
    public Profile addConnection(String profileId, SocialPersonProfile personProfile) {
        if (profileId == null || personProfile == null || personProfile.getPrimaryConnection() == null)
            return null;
        ProfileEntity existingProfile = entityManager.find(ProfileEntity.class, profileId);
        if (existingProfile != null) {
            Collection<ConnectionKey> existingConnection = Collections2.transform(existingProfile.getSocialProfiles(),
                    new Function<SocialPersonProfile, ConnectionKey>() {
                        @Override
                        public ConnectionKey apply(SocialPersonProfile socialPersonProfile) {
                            return socialPersonProfile.getPrimaryConnection();
                        }
                    });
            if(!existingConnection.contains(personProfile.getPrimaryConnection()))
                existingProfile.getSocialProfiles().add(new SocialPersonProfileEntity(personProfile));
        } else {
            Profile newProfile = new ProfileEntity().setProfileId(profileId).addSocialProfiles(personProfile);
            addProfile(newProfile);
            return newProfile;
        }
        return existingProfile.build();
    }

    @Override
    public Profile updateProfile(Profile newProfile) {
        return updateProfile(newProfile, true);
    }

    private Profile updateProfile(Profile profile, boolean overwrite) {
        // Step 1. Sanity check
        if (profile == null || profile.getProfileId() == null)
            return profile;
        // Step 2. Retrieving ContactEntity
        ProfileEntity existingProfile = entityManager.find(ProfileEntity.class, profile.getProfileId());
        // Step 3. Performing required operation
        if (existingProfile == null) {
            existingProfile = new ProfileEntity(profile);
            entityManager.persist(existingProfile);
        } else if (overwrite) {
            existingProfile.safeMerge(profile);
        }
        // Step 3. Building entity to process
        return existingProfile.build();
    }

    @Override
    public void removeProfile(String profileId) {
        // Step 1. Sanity check
        if (profileId == null)
            return;
        // Step 2. Removing entity
        entityManager.createNamedQuery(ProfileEntity.QUERY_PROFILE_REMOVE_BY_ID).setParameter("profileId", profileId).executeUpdate();
        entityManager.createNativeQuery(ProfileEntity.QUERY_PROFILE_REMOVE_BY_ID_PROFILE_SOCIAL_LINKS).setParameter(1, profileId).executeUpdate();
        entityManager.createNativeQuery(ProfileEntity.QUERY_PROFILE_REMOVE_BY_ID_PROFILE_USER_LINKS).setParameter(1, profileId).executeUpdate();
    }

    @Override
    public void addProfiles(Collection<Profile> newProfiles) {
        // Step 1. Sanity check
        if(newProfiles == null || newProfiles.size() == 0)
            return;
        Collection<Profile> nonNullProfiles = Collections2.filter(newProfiles, Predicates.notNull());
        if(nonNullProfiles.size() == 0)
            return;
        // Step 2. Doing actual saving
        for(Profile profile: nonNullProfiles) {
            try {
                entityManager.persist(new ProfileEntity(profile));
            } catch  (EntityExistsException entityExistsException) {
                entityExistsException.printStackTrace();
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void unite(String toProfile, String fromProfile) {
        // Step 1. Sanity check
        if(toProfile == null || fromProfile == null)
            return;
        // Step 2. Retrieving stored profiles
        ProfileEntity toProfileEntity = entityManager.find(ProfileEntity.class, toProfile);
        ProfileEntity fromProfileEntity = entityManager.find(ProfileEntity.class, fromProfile);
        if(toProfileEntity == null || fromProfileEntity == null)
            return;
        // Step 3. Actual merge
        toProfileEntity.getSocialProfiles().addAll(fromProfileEntity.getSocialProfiles());
        // Step 4. Removing profile entity
        removeProfile(fromProfile);
    }

    @Override
    public Profile removeConnection(String profileId, SocialPersonProfile personProfile) {
        // Step 1. Sanity check
        if (profileId == null || personProfile == null || personProfile.getPrimaryConnection() == null)
            return null;
        // Step 2. Searching for existing entity
        ProfileEntity existingProfile = entityManager.find(ProfileEntity.class, profileId);
        if (existingProfile != null) {
            // Step 3. Searching for associated SocialPersonProfile
            SocialPersonProfile profileToRemove = null;
            for(SocialPersonProfile personProfileEntity: existingProfile.getSocialProfiles()) {
                if(personProfileEntity.getPrimaryConnection().equals(personProfile.getPrimaryConnection()))
                        profileToRemove = personProfileEntity;
            }
            // Step 4. Removing fond profile from the entity, this will be propagated automatically
            existingProfile.getSocialProfiles().remove(profileToRemove);
        }
        return existingProfile.build();

    }

    public Collection<Entry<String, String>> getProfileIdentifiers(Collection<ConnectionKey> primaryConnection, Collection<ConnectionKey> excludingConnection) {
        // Step 1. Sanity check
        if(primaryConnection == null || primaryConnection.size() == 0)
            return ImmutableList.<Entry<String, String>>of();
        // Step 2. Preparing query
        // Step 2.1. Replacing present social connections with string identifier
        Collection<String> connectionKeys = Collections2.transform(primaryConnection, ConnectionKeyType.CONNECTION_KEY_TO_STRING);
        String commaSeparatedValue = StringUtils.collectionToDelimitedString(connectionKeys, ",", "'", "'");
        // Step 2.2. Replacing excluded connections with string identifiers
        Collection<String> excludedList = Collections2.transform(excludingConnection, ConnectionKeyType.CONNECTION_KEY_TO_STRING);
        String excludedValues = StringUtils.collectionToDelimitedString(excludedList, ",", "'", "'");
        String query = ProfileEntity.QUERY_PROFILE_GET_WITH_PRIMARY_CONNECTION_IN.replaceFirst("?", commaSeparatedValue).replaceFirst("?", excludedValues);
        // Step 3. Searching database for the result
        return entityManager.createNativeQuery(query, ProfileEntity.KEY_VALUE_MAPPING).getResultList();
    }

    @Override
    public Collection<? extends SocialPersonProfile> getConnections(String profileIdentifier) {
        // Step 1. Sanity check
        if(profileIdentifier == null)
            return ImmutableList.<SocialPersonProfile>of();
        // Step 2. Retrieving connections
        Profile profile = getProfile(profileIdentifier);
        // Step 3. Returning appropriate result
        return profile != null ? profile.getSocialProfiles() : ImmutableList.<SocialPersonProfile>of();
    }

}
