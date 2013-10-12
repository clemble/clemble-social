package com.clemble.social.event.contact;

import com.google.common.eventbus.Subscribe;

public interface UserContactRemovedEventListener<T> {

    @Subscribe
    public void contactRemoved(UserContactRemovedEvent contactRemovedEvent);

}
