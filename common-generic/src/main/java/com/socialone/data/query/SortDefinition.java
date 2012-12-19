package com.socialone.data.query;

/**
 * Definition for sorting bean instances by a property.
 */
final public class SortDefinition {
    
    final String property;
    final boolean ascending;

    public SortDefinition(final String propertyToUse, final boolean ascending) {
        this.property = propertyToUse;
        this.ascending = ascending;
    }
    
    /**
     * Return the name of the bean property to compare.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Return whether to sort ascending (true) or descending (false).
     */
    public boolean isAscending() {
        return ascending;
    }

}
