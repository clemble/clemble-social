package com.clemble.social.event.connection;

import com.google.common.eventbus.Subscribe;

public interface UserConnectionAddedEventListener<T> {

    @Subscribe
    public T userConnectionAddedEvent(UserConnectionAddedEvent connectionAddedEvent);

}
