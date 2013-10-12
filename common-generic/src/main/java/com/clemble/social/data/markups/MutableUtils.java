package com.clemble.social.data.markups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class MutableUtils {

    public static <T> List<T> freeze(Collection<Mutable<T>> mutableCollection) {
        // Step 1. Sanity check
        if(mutableCollection == null || mutableCollection.size() == 0)
            return ImmutableList.<T>of();
        // Step 2. Generating immutable collection
        Collection<T> tmpCollection = new ArrayList<T>();
        for(Mutable<T> mutable: mutableCollection) {
            if(mutable != null)
                tmpCollection.add(mutable.freeze());
        }
        // Step 3. Transforming to immutable collection
        return ImmutableList.<T>copyOf(tmpCollection);
    }
}
