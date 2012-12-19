package com.socialone.event.contact;

import com.google.common.eventbus.Subscribe;

public interface UserContactUpdatedEventListener<T> {

    @Subscribe
    public T contactUpdated(UserContactUpdatedEvent contactUpdatedEvent);

}
