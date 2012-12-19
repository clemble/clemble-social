package com.socialone.service.connection.adapter;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.linkedin.api.ImAccount;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInDate;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.api.PhoneNumber;
import org.springframework.social.linkedin.api.TwitterAccount;
import org.springframework.social.linkedin.connect.LinkedInAdapter;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.socialone.data.date.SocialDate;
import com.socialone.data.date.SocialDate.SimpleSocialDateBuilder;
import com.socialone.data.social.SocialNetworkType;
import com.socialone.data.social.SocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.ImmutableSocialPersonProfile;
import com.socialone.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.socialone.data.social.connection.ConnectionKeyFactory;
import com.socialone.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.socialone.service.connection.UserConnectionApiAdapter;

@Component
public class LinkedInConnectionApiAdapter extends LinkedInAdapter implements UserConnectionApiAdapter<LinkedIn> {

    final private Converter<LinkedInProfileFull, SocialPersonProfile> linkedInProfileConverter = new Converter<LinkedInProfileFull, SocialPersonProfile> (){
        @Override
        public SocialPersonProfile convert(LinkedInProfileFull linkedInProfile) {
            // Step 1. Generating primary key
            ConnectionKey prmaryKey = ConnectionKeyFactory.get(SocialNetworkType.linkedin, linkedInProfile.getId());
            Collection<ConnectionKey> links = Lists.newArrayList();
            linkedInProfile.getLocation();
            // Step 2. Adding phone numbers
            Collection<PhoneNumber> phoneNumbers = linkedInProfile.getPhoneNumbers();
            if (phoneNumbers != null && phoneNumbers.size() > 0) {
                for (PhoneNumber phoneNumber : linkedInProfile.getPhoneNumbers()) {
                    if(linkedInProfile.getLocation() != null) {
                        links.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.phone, phoneNumber.getPhoneNumber(), linkedInProfile.getLocation().getCountry()));
                    } else {
                        links.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.phone, phoneNumber.getPhoneNumber()));
                    }
                }
            }
            // Step 3. Populating IM accounts
            Collection<ImAccount> imAccounts = linkedInProfile.getImAccounts();
            if(imAccounts != null && imAccounts.size() > 0) {
                for(ImAccount imAccount: imAccounts) {
                    links.addAll(ConnectionKeyFactory.normalize(imAccount.getImAccountType(), imAccount.getImAccountName()));
                }
            }
            // Step 5. Adding Twitter account information
            Collection<TwitterAccount> twitterAccounts = linkedInProfile.getTwitterAccounts();
            if(twitterAccounts != null && twitterAccounts.size() > 0) {
                for(TwitterAccount twitterAccount: twitterAccounts) {
                    links.addAll(ConnectionKeyFactory.normalize(twitterAccount.getProviderAccountName(), twitterAccount.getProviderAccountId()));
                }
            }
            // Step 7. Reading birth date and generating appropriate ContactProfile
            SocialDate birthDate = null;
            LinkedInDate dateOfBirth = linkedInProfile.getDateOfBirth();
            if(dateOfBirth != null) {
                birthDate = new SimpleSocialDateBuilder().setDay(dateOfBirth.getDay()).setMonth(dateOfBirth.getMonth()).setYear(dateOfBirth.getYear()).freeze();
            }
            return new SimpleSocialPersonProfileBuilder()
                .setSocialConnection(new ImmutableSocialConnection(prmaryKey, links))
                .setFirstName(linkedInProfile.getFirstName())
                .setLastName(linkedInProfile.getLastName())
                .setUrl(linkedInProfile.getPublicProfileUrl())
                .setImage(linkedInProfile.getProfilePictureUrl())
                .setBirthDate(birthDate).freeze();
        }

    };

    @Override
    public Collection<String> getConnections(LinkedIn api) {
        Collection<LinkedInProfile> connections = api.connectionOperations().getConnections();
        Collection<String> connectionKeys = Lists.newArrayList();
        for (LinkedInProfile linkedInProfile : connections) {
            connectionKeys.add(linkedInProfile.getId());
        }
        return connectionKeys;
    }

    @Override
    public Collection<SocialPersonProfile> getAllContacts(LinkedIn api) {
        // This way full profile can be processed not part of it
        Collection<String> connectionKeys = getConnections(api);
        return getContacts(api, connectionKeys);
    }

    @Override
    public SocialPersonProfile getContact(LinkedIn api, String connectionKey) {
        return linkedInProfileConverter.convert(api.profileOperations().getProfileFullById(connectionKey));
    }

    @Override
    public Collection<SocialPersonProfile> getContacts(LinkedIn api, Collection<String> connectionKeys) {
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        for (String connectionKey : connectionKeys) {
            try {
                contacts.add(getContact(api, connectionKey));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return contacts;
    }

    @Override
    public String getProviderId() {
        return SocialNetworkType.linkedin.name();
    }

}
