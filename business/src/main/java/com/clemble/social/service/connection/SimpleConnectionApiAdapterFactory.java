package com.clemble.social.service.connection;

import org.springframework.stereotype.Component;

import com.clemble.social.service.connection.ConnectionApiFactory;
import com.clemble.social.service.connection.UserConnectionApiAdapter;
import com.clemble.social.service.factory.AbstractBeanMapFactory;
import com.google.common.base.Function;

@Component
public class SimpleConnectionApiAdapterFactory extends AbstractBeanMapFactory<String, UserConnectionApiAdapter> implements ConnectionApiFactory {

    public SimpleConnectionApiAdapterFactory() {
        super(UserConnectionApiAdapter.class, new Function<UserConnectionApiAdapter, String>() {
            @Override
            public String apply(UserConnectionApiAdapter input) {
                return input.getProviderId();
            }
        });
    }

}
