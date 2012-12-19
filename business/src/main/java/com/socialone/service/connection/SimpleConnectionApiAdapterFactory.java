package com.socialone.service.connection;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.socialone.service.connection.ConnectionApiFactory;
import com.socialone.service.connection.UserConnectionApiAdapter;
import com.socialone.service.factory.AbstractBeanMapFactory;

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
