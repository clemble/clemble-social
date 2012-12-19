package com.socialone.event.contact;

import com.google.common.eventbus.Subscribe;

public interface UserContactAddedEventListener<T> {

    @Subscribe
    public T contactAdded(UserContactAddedEvent contactAddedEvent);

}
