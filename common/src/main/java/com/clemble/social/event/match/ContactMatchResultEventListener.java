package com.clemble.social.event.match;

import com.google.common.eventbus.Subscribe;

public interface ContactMatchResultEventListener<T> {

    @Subscribe
    public T contactMatched(ContactMatchResultEvent contactMatchResultEvent);

}
