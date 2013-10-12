package com.clemble.social.data.markups;

import java.io.Serializable;
import java.util.Collection;

public interface Mutable<T> extends Serializable{

    T freeze();

    T merge(T other);

    T safeMerge(T other);

    T safeMerge(Collection<? extends T> others);
}
