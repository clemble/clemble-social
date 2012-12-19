package com.socialone.service.connection.adapter;

import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.connect.TwitterAdapter;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.socialone.data.social.SocialNetworkType;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.socialone.data.social.connection.ConnectionKeyFactory;
import com.socialone.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.socialone.service.connection.UserConnectionApiAdapter;

@Component
public class TwitterConnectionApiAdapter extends TwitterAdapter implements UserConnectionApiAdapter<Twitter> {

    final private Converter<TwitterProfile, SocialPersonProfile> twitterConverter = new TwitterProfileToContactConverter();

    @Override
    public Collection<String> getConnections(Twitter api) {
        List<Long> twitterIDs = api.friendOperations().getFriendIds();
        return Collections2.transform(twitterIDs, new Function<Long, String>() {
            @Override
            public String apply(Long longVariable) {
                return longVariable != null ? longVariable.toString() : "";
            }
        });
    }

    @Override
    public Collection<SocialPersonProfile> getAllContacts(Twitter api) {
        Collection<TwitterProfile> twitterProfiles = api.friendOperations().getFriends();
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        for (TwitterProfile twitterProfile : twitterProfiles) {
            contacts.add(twitterConverter.convert(twitterProfile));
        }
        return contacts;
    }

    @Override
    public SocialPersonProfile getContact(Twitter api, String connectionKey) {
        return twitterConverter.convert(api.userOperations().getUserProfile(Long.valueOf(connectionKey)));
    }

    @Override
    public Collection<SocialPersonProfile> getContacts(Twitter api, Collection<String> connectionKeys) {
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        for (String connectionKey : connectionKeys) {
            contacts.add(getContact(api, connectionKey));
        }
        return contacts;
    }

    @Override
    public String getProviderId() {
        return SocialNetworkType.twitter.name();
    }

    private class TwitterProfileToContactConverter implements Converter<TwitterProfile, SocialPersonProfile> {

        @Override
        public SocialPersonProfile convert(TwitterProfile twitterProfile) {
            // Step 1. Generating primary connection
            ConnectionKey primaryIdentifier = ConnectionKeyFactory.get(SocialNetworkType.twitter, String.valueOf(twitterProfile.getId()));
            // Step 2. Generating contact profile
            return new SimpleSocialPersonProfileBuilder()
                    .setSocialConnection(new ImmutableSocialConnection(primaryIdentifier, ImmutableList.<ConnectionKey> of(primaryIdentifier)))
                    .setFirstName(twitterProfile.getName()).setUrl(twitterProfile.getProfileUrl()).setImage(twitterProfile.getProfileImageUrl()).freeze();
        }

    }

}
