package com.socialone.service.connection.adapter;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookAdapter;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.socialone.data.Gender;
import com.socialone.data.date.SocialDate;
import com.socialone.data.date.SocialDate.SimpleSocialDateBuilder;
import com.socialone.data.date.SocialDate.SocialDateBuilder;
import com.socialone.data.social.SocialNetworkType;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.ImmutableSocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.socialone.data.social.connection.ConnectionKeyFactory;
import com.socialone.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.socialone.service.connection.UserConnectionApiAdapter;

@Component
public class FacebookConnectionApiAdapter extends FacebookAdapter implements UserConnectionApiAdapter<Facebook> {

    private Converter<FacebookProfile, SocialPersonProfile> contactConverter = new FacebookProfileToContactConverter();

    @Override
    public Collection<String> getConnections(Facebook api) {
        return api.friendOperations().getFriendIds();
    }

    @Override
    public Collection<SocialPersonProfile> getAllContacts(Facebook api) {
        Collection<FacebookProfile> facebookProfiles = api.friendOperations().getFriendProfiles(0, Integer.MAX_VALUE);
        return Collections2.transform(facebookProfiles, new Function<FacebookProfile, SocialPersonProfile>() {
            @Override
            public SocialPersonProfile apply(FacebookProfile facebookProfile) {
                return contactConverter.convert(facebookProfile);
            }
        });
    }

    @Override
    public SocialPersonProfile getContact(Facebook api, String connectionKey) {
        return contactConverter.convert(api.userOperations().getUserProfile(connectionKey));
    }

    @Override
    public Collection<SocialPersonProfile> getContacts(Facebook api, Collection<String> connectionKeys) {
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        for (String connectionKey : connectionKeys) {
            contacts.add(getContact(api, connectionKey));
        }
        return contacts;
    }

    @Override
    public String getProviderId() {
        return SocialNetworkType.facebook.name();
    }

    private class FacebookProfileToContactConverter implements Converter<FacebookProfile, SocialPersonProfile> {

        @Override
        public SocialPersonProfile convert(FacebookProfile facebookProfile) {
            // Step 1. Creating default connection associated with Facebook
            // Profile
            ConnectionKey primaryKey = ConnectionKeyFactory.get(SocialNetworkType.facebook, facebookProfile.getId());
            // Step 2. Adding email descriptor to the collection
            Collection<ConnectionKey> links = Lists.newArrayList();
            if (facebookProfile.getEmail() != null)
                links.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.email, facebookProfile.getEmail()));
            String facebookBirthDate = facebookProfile.getBirthday();
            SocialDate birthDate = null;
            if (!isNullOrEmpty(facebookBirthDate)) {
                String[] date = facebookBirthDate.split("/");
                SocialDateBuilder socialDateBuilder = new SimpleSocialDateBuilder();
                if(date.length >= 1 && !isNullOrEmpty(date[0]))
                    socialDateBuilder.setMonth(Integer.valueOf(date[0]));
                if(date.length >= 2 && !isNullOrEmpty(date[1]))
                    socialDateBuilder.setDay(Integer.valueOf(date[1]));
                if(date.length >= 3 && !isNullOrEmpty(date[2]))
                    socialDateBuilder.setYear(Integer.valueOf(date[2]));
                birthDate = socialDateBuilder.freeze();
            }
            // Step 4. Get facebook contact profile
            return new SimpleSocialPersonProfileBuilder().setSocialConnection(new ImmutableSocialConnection(primaryKey, links)).
                    setFirstName(facebookProfile.getFirstName())
                    .setLastName(facebookProfile.getLastName())
                    .setUrl(facebookProfile.getLink())
                    .setImage("http://graph.facebook.com/" + facebookProfile.getId())
                    .setBirthDate(birthDate)
                    .setGender(Gender.parse(facebookProfile.getGender())).freeze();
        }
    }

}
