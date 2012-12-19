package com.socialone.event.user;

import com.google.common.eventbus.Subscribe;

public interface UserProfileUpdatedEventListener<T> {

    @Subscribe
    public T userProfileUpdated(UserProfileUpdatedEvent userProfileUpdatedEvent);

}
