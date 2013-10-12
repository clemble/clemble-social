package com.clemble.social.event.contact;

import com.google.common.eventbus.Subscribe;

public interface UserContactAddedEventListener<T> {

    @Subscribe
    public T contactAdded(UserContactAddedEvent contactAddedEvent);

}
