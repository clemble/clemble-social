package com.clemble.social.provider.service.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.social.data.markups.Mutable;
import com.clemble.social.data.markups.MutableUtils;
import com.clemble.social.data.query.PagingConfiguration;
import com.clemble.social.data.query.jpa.PagingConfigurationApplier;
import com.clemble.social.provider.data.ProviderConfiguration;
import com.clemble.social.provider.data.ProviderConfigurationsEntity;
import com.clemble.social.provider.service.ProviderConfigurationRepository;

@Transactional(propagation = Propagation.REQUIRED)
public class JpaProviderConfigurationRepository implements ProviderConfigurationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProviderConfiguration> getAllProviderConfigurations(PagingConfiguration pagingConfiguration) {
        // Step 1. Formulating Query with predefined sorting configurations
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProviderConfigurationsEntity> criteriaQuery = criteriaBuilder.createQuery(ProviderConfigurationsEntity.class);
        Root<ProviderConfigurationsEntity> root = criteriaQuery.from(ProviderConfigurationsEntity.class);
        criteriaQuery.orderBy(PagingConfigurationApplier.getOrdering(criteriaBuilder, root, pagingConfiguration));
        // Step 2. Generating appropriate Query
        TypedQuery<ProviderConfigurationsEntity> query = entityManager.createQuery(criteriaQuery);
        // Step 2.1. Applying paging configurations
        PagingConfigurationApplier.setPaging(query, pagingConfiguration);
        // Step 3. Retrieving and processing result
        Collection<Mutable<ProviderConfiguration>> allProviders = (Collection<Mutable<ProviderConfiguration>>) (Collection<?>) query.getResultList();
        // Step 2. Transforming to immutable Collection
        return MutableUtils.<ProviderConfiguration> freeze(allProviders);
    }

    @Override
    public ProviderConfiguration getProviderConfiguration(String providerName) {
        // Step 1. Retrieving stored entity
        ProviderConfigurationsEntity configurationsEntity = entityManager.find(ProviderConfigurationsEntity.class, providerName);
        // Step 2. Building appropriate immutable object
        return configurationsEntity != null ? configurationsEntity.freeze() : null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addProviderConfiguration(ProviderConfiguration providerConfigurations) {
        // Step 1. Persisting data
        entityManager.persist(new ProviderConfigurationsEntity(providerConfigurations));
    }

    @Override
    public void updateProviderConfiguration(ProviderConfiguration providerConfigurations) {
        // Step 1. Retrieving stored entity
        ProviderConfigurationsEntity configurationsEntity = entityManager.find(ProviderConfigurationsEntity.class, providerConfigurations.getProvider());
        if (configurationsEntity != null) {
            // Step 2. Merging data otherwise ignoring
            configurationsEntity.safeMerge(providerConfigurations);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
