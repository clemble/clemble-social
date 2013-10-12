package com.clemble.social.service.social.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.social.SocialPersonProfile.SocialPersonProfileBuilder;
import com.clemble.social.data.social.entity.SocialPersonProfileEntity;
import com.clemble.social.service.social.SocialPersonProfileRepository;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class JpaSocialPersonProfileRepository implements SocialPersonProfileRepository {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    @Override
    public SocialPersonProfile addSocialPersonProfile(SocialPersonProfile socialPersonProfile) {
        return updateSocialPersonProfile(socialPersonProfile, false);
    }

    @Override
    public void addSocialPersonProfiles(Collection<SocialPersonProfile> socialPersonProfile) {
        // Step 1. Sanity checks
        if (socialPersonProfile == null || socialPersonProfile.size() == 0)
            return;
        socialPersonProfile = Collections2.filter(socialPersonProfile, Predicates.notNull());
        // Step 2. Persisting as a collection
        Collection<SocialPersonProfileEntity> personProfileEntities = Collections2.transform(socialPersonProfile,
                new Function<SocialPersonProfile, SocialPersonProfileEntity>() {
                    @Override
                    public SocialPersonProfileEntity apply(SocialPersonProfile socialPersonProfile) {
                        return new SocialPersonProfileEntity(socialPersonProfile);
                    }
                });
        // Step 3. Persisting all provided entities
        for (SocialPersonProfileEntity personProfileEntity : personProfileEntities) {
            entityManager.persist(personProfileEntity);
        }
    }

    @Override
    public SocialPersonProfile updateSocialPersonProfile(SocialPersonProfile socialPersonProfile) {
        return updateSocialPersonProfile(socialPersonProfile, true);
    }

    private SocialPersonProfile updateSocialPersonProfile(SocialPersonProfile socialPersonProfile, boolean overwriteIfExists) {
        // Step 1. Sanity check
        if (socialPersonProfile == null)
            return null;
        // Step 2. Checking primary connection exists for the SocialPersonProfile
        if (socialPersonProfile.getPrimaryConnection() == null)
            throw new IllegalArgumentException("Primary Profile can't be null");
        // Step 3. Checking for appropriate action
        SocialPersonProfileEntity existingSocialPersonProfile = entityManager.find(SocialPersonProfileEntity.class, socialPersonProfile.getPrimaryConnection());
        if (existingSocialPersonProfile == null) {
            existingSocialPersonProfile = (socialPersonProfile instanceof SocialPersonProfileEntity) ? (SocialPersonProfileEntity) socialPersonProfile
                    : new SocialPersonProfileEntity(socialPersonProfile);
            entityManager.persist(existingSocialPersonProfile);
        } else if (overwriteIfExists) {
            existingSocialPersonProfile.merge(socialPersonProfile);
        }
        return existingSocialPersonProfile != null ? existingSocialPersonProfile.freeze() : null;
    }

    @Override
    public SocialPersonProfile getSocialPersonProfile(ConnectionKey connectionKey) {
        // Step 1. Performing actual search
        SocialPersonProfileEntity personProfileEntity = entityManager.find(SocialPersonProfileEntity.class, connectionKey);
        // Step 2. If found something try to create it
        return personProfileEntity != null ? personProfileEntity.freeze() : personProfileEntity;
    }

    @Override
    public Collection<? extends SocialPersonProfile> getSocialPersonProfiles(String providerID, Collection<String> connectionUserIds) {
        // Step 0. Sanity check
        if (providerID == null || connectionUserIds == null || connectionUserIds.size() == 0)
            return ImmutableList.of();
        // Step 1. Convert to ConnectionKeys
        Collection<ConnectionKey> connectionKeys = Lists.newArrayList();
        for (String connectionKey : connectionUserIds)
            connectionKeys.add(new ConnectionKey(providerID, connectionKey));
        // Step 2. Extracting all profiles in the list
        List<SocialPersonProfileEntity> existingProfiles = entityManager
                .createNamedQuery(SocialPersonProfileEntity.QUERY_SOCIAL_PROFILE_GET_WITH_ID_IN, SocialPersonProfileEntity.class)
                .setParameter("primaryConnection", connectionKeys).getResultList();
        return existingProfiles;
    }

    @Override
    public void removeSocialPersonProfile(ConnectionKey connectionKey) {
        if (connectionKey != null) {
            // Step 1. Searching for existing social person profile
            SocialPersonProfileEntity existingSocialPersonProfile = entityManager.find(SocialPersonProfileEntity.class, connectionKey);
            // Step 2. Removing existing social person profile
            if (existingSocialPersonProfile != null)
                entityManager.remove(existingSocialPersonProfile);
        }
    }

    @Override
    public Collection<ConnectionKey> getPrimaryConnections(Collection<ConnectionKey> connectionKeys) {
        if (connectionKeys == null || connectionKeys.size() == 0)
            return ImmutableList.of();
        // Step 1. Grouping all connection keys based on provider
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        for (ConnectionKey connectionKey : connectionKeys) {
            multiValueMap.add(connectionKey.getProviderId(), connectionKey.getProviderUserId());
        }
        // Step 2. Extracting all primary Connections based on provider identifier of the same type
        return null;
    }

}
