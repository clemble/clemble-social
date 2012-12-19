package com.socialone.data.query.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;


import com.google.common.collect.ImmutableList;
import com.socialone.data.query.PagingConfiguration;
import com.socialone.data.query.SortDefinition;

public class PagingConfigurationApplier {

    public static<T> List<Order> getOrdering(CriteriaBuilder criteriaBuilder, Root<T> query, PagingConfiguration pagingConfiguration) {
        // Step 1. Checking there is paging configuration provided
        if(pagingConfiguration == null || query == null || criteriaBuilder == null || pagingConfiguration.getSortDefinitions().size() == 0)
            return ImmutableList.<Order>of();
        // Step 2. Setting paging size
        List<Order> sortingOrder = new ArrayList<Order>();
        for(SortDefinition sortDefinition: pagingConfiguration.getSortDefinitions()) {
            if(sortDefinition.isAscending()) {
                sortingOrder.add(criteriaBuilder.asc(query.get(sortDefinition.getProperty())));
            } else{
                sortingOrder.add(criteriaBuilder.desc(query.get(sortDefinition.getProperty())));
            }
        }
        // Step 3. Setting ordering to the Query
        return sortingOrder;
    }
    
    public static<T> void setPaging(TypedQuery<T> query, PagingConfiguration pagingConfiguration) {
        // Step 1. Checking there is paging configuration provided
        if(pagingConfiguration == null || query == null)
            return;
        // Step 2. Setting paging size
        query.setFirstResult(pagingConfiguration.getFirstResult());
        query.setMaxResults(pagingConfiguration.getPageSize());
    }
}
