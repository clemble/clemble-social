package com.socialone.service.deduplication;

import java.util.Collection;

public interface DeduplicationCriteriaFactory {

    public Collection<DeduplicationCriteria> getRegisteredCriterias();

}
