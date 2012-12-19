package com.socialone.service.connection.adapter;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.social.vkontakte.api.VKontakteDate;
import org.springframework.social.vkontakte.api.VKontakteProfile;
import org.springframework.social.vkontakte.connect.VKontakteAdapter;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.socialone.data.Gender;
import com.socialone.data.date.SocialDate;
import com.socialone.data.date.SocialDate.SimpleSocialDateBuilder;
import com.socialone.data.social.SocialNetworkType;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.socialone.data.social.connection.ConnectionKeyFactory;
import com.socialone.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.socialone.service.connection.UserConnectionApiAdapter;

@Component
public class VKConnectionApiAdapter extends VKontakteAdapter implements UserConnectionApiAdapter<VKontakte> {

    final private Converter<VKontakteProfile, SocialPersonProfile> vkontakteConverter = new VKProfileToContactConverter();

    @Override
    public Collection<String> getConnections(VKontakte api) {
        Collection<VKontakteProfile> vKontakteProfiles = api.friendsOperations().get();
        Collection<String> connectionKeys = Lists.newArrayList();
        for (VKontakteProfile kontaktProfile : vKontakteProfiles) {
            connectionKeys.add(kontaktProfile.getUid());
        }
        return connectionKeys;
    }

    @Override
    public Collection<SocialPersonProfile> getAllContacts(VKontakte api) {
        Collection<VKontakteProfile> vKontakteProfiles = api.friendsOperations().get();
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        for (VKontakteProfile kontaktProfile : vKontakteProfiles) {
            contacts.add(vkontakteConverter.convert(kontaktProfile));
        }
        return contacts;
    }

    @Override
    public SocialPersonProfile getContact(VKontakte api, String connectionKey) {
        Collection<SocialPersonProfile> contacts = getContacts(api, ImmutableList.of(connectionKey));
        return contacts.size() > 0 ? contacts.iterator().next() : null;
    }

    @Override
    public Collection<SocialPersonProfile> getContacts(VKontakte api, Collection<String> userIDs) {
        Collection<VKontakteProfile> kontakteProfiles = api.usersOperations().getProfiles(Lists.newArrayList(userIDs));
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        for (VKontakteProfile kontakteProfile : kontakteProfiles) {
            contacts.add(vkontakteConverter.convert(kontakteProfile));
        }
        return contacts;
    }

    @Override
    public String getProviderId() {
        return SocialNetworkType.vkontakte.name();
    }

    private class VKProfileToContactConverter implements Converter<VKontakteProfile, SocialPersonProfile> {

        @Override
        public SocialPersonProfile convert(VKontakteProfile source) {
            // Step 1. Generating primary key for the connection
            ConnectionKey primaryKey = ConnectionKeyFactory.get(SocialNetworkType.vkontakte, source.getUid());
            // Step 2. Extracting phone numbers
            Collection<ConnectionKey> connectionsKeys = Lists.newArrayList();
            String phone = source.getHomePhone();
            if (phone != null && phone.length() > 0)
                connectionsKeys.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.phone, phone));
            phone = source.getMobilePhone();
            if (phone != null && phone.length() > 0)
                connectionsKeys.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.phone, phone));
            // Step 3. Extracting birth date
            SocialDate birthDate = null;
            VKontakteDate dateOfBirth = source.getBirthDate();
            if(dateOfBirth != null) {
                birthDate = new SimpleSocialDateBuilder(dateOfBirth.getDay(), dateOfBirth.getMonth(), dateOfBirth.getYear());
            }
            // Step 4. Generating final SocialPersonProfile
            return new SimpleSocialPersonProfileBuilder().
                    setSocialConnection(new ImmutableSocialConnection(primaryKey, connectionsKeys))
                    .setFirstName(source.getFirstName())
                    .setLastName(source.getLastName())
                    .setUrl(source.getProfileURL())
                    .setImage(source.getPhoto())
                    .setBirthDate(birthDate)
                    .setGender(Gender.parse(source.getGender())).freeze();
        }

    }

}
