package com.clemble.social;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ConnectionRepositoryCache {
    
    final private UsersConnectionRepository connectionRepository;

    final private LoadingCache<String, ConnectionRepository> connectionRespositories = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(100000).build(new CacheLoader<String, ConnectionRepository>() {

                @Override
                public ConnectionRepository load(String userID) throws Exception {
                    return connectionRepository.createConnectionRepository(userID);
                }
            });
    
    @Inject
    public ConnectionRepositoryCache(UsersConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }
    
    public ConnectionRepository getRepository(String userID) {
        if(userID == null)
            return null;
        try {
            return connectionRespositories.get(userID);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
