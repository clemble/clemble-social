package com.socialone.data.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

public class PagingConfiguration implements Serializable {

    /**
     * Generated 11/12/12
     */
    private static final long serialVersionUID = -1903322681011249363L;

    final public static PagingConfiguration DEFAULT_CONFIGURATION = PagingConfiguration.newBuilder().build();

    final private List<SortDefinition> sortDefinitions;
    final private int page;
    final private int pageSize;
    final private int firstResult;

    private PagingConfiguration(final Collection<SortDefinition> sortDefinitions, final int newPage, final int newPageSize) {
        // Step 1. Checking sort definitions
        if (sortDefinitions == null || sortDefinitions.size() == 0) {
            // Step 1.1 If there are no fields to sort with ignore
            this.sortDefinitions = ImmutableList.<SortDefinition> of();
        } else {
            // Step 1.2 Filter non null values and save them in sort definitions
            Collection<SortDefinition> filteredCollection = Collections2.filter(sortDefinitions, Predicates.notNull());
            this.sortDefinitions = ImmutableList.<SortDefinition> copyOf(filteredCollection);
        }
        // Step 2. Checking page size
        this.page = newPage;
        this.pageSize = newPageSize;
        this.firstResult = page * pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<SortDefinition> getSortDefinitions() {
        return sortDefinitions;
    }

    public int getFirstResult() {
        return firstResult;
    }

    final public static PagingConfigurationBuilder newBuilder() {
        return new PagingConfigurationBuilder();
    }

    final public static class PagingConfigurationBuilder {
        final public static int DEFAULT_PAGE_SIZE = 20;
        final public static int DEFAULT_PAGE = 0;

        private List<SortDefinition> sortDefinitions = new ArrayList<SortDefinition>();
        private int page = DEFAULT_PAGE;
        private int pageSize = DEFAULT_PAGE_SIZE;

        public PagingConfigurationBuilder setPage(int page) {
            this.page = Math.max(DEFAULT_PAGE, page);
            return this;
        }

        public PagingConfigurationBuilder setPageSize(int pageSize) {
            this.pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
            return this;
        }

        public PagingConfigurationBuilder addSortDefinition(SortDefinition sortDefinition) {
            if (sortDefinition != null)
                this.sortDefinitions.add(sortDefinition);
            return this;
        }

        public PagingConfiguration build() {
            return new PagingConfiguration(sortDefinitions, page, pageSize);
        }
    }
}
