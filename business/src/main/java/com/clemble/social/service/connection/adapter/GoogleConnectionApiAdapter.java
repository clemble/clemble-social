package com.clemble.social.service.connection.adapter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.person.Email;
import org.springframework.social.google.api.plus.person.Person;
import org.springframework.social.google.api.plus.person.Phone;
import org.springframework.social.google.connect.GoogleAdapter;
import org.springframework.stereotype.Component;

import com.clemble.social.data.Gender;
import com.clemble.social.data.date.SocialDate;
import com.clemble.social.data.date.SocialDate.SimpleSocialDateBuilder;
import com.clemble.social.data.social.SocialNetworkType;
import com.clemble.social.data.social.SocialPersonProfile;
import com.clemble.social.data.social.SocialPersonProfile.SimpleSocialPersonProfileBuilder;
import com.clemble.social.data.social.connection.ConnectionKeyFactory;
import com.clemble.social.data.social.connection.SocialConnection.ImmutableSocialConnection;
import com.clemble.social.service.connection.UserConnectionApiAdapter;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@Component
public class GoogleConnectionApiAdapter extends GoogleAdapter implements UserConnectionApiAdapter<Google> {

    final private Converter<Person, SocialPersonProfile> personConverter = new GooglePersonToContactConverter();

    @Override
    @SuppressWarnings("unchecked")
    public Collection<String> getConnections(Google api) {
        List<Person> persons = (List<Person>) api.personOperations().contactQuery().maxResultsNumber(Integer.MAX_VALUE).getPage().getItems();
        Collection<String> connections = Collections2.transform(persons, new Function<Person, String>() {
            @Override
            public String apply(Person person) {
                return person.getId();
            }
        });
        // TODO figure out how to deal with NULL connections
        return Collections2.filter(connections, Predicates.notNull());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<SocialPersonProfile> getAllContacts(Google api) {
        Collection<SocialPersonProfile> contacts = Lists.newArrayList();
        // Step 1. Retrieving complete list of persons based on Contact Query
        List<Person> persons = (List<Person>) api.personOperations().contactQuery().maxResultsNumber(Integer.MAX_VALUE).getPage().getItems();
        // Step 2. Converting retrieved List to appropriate Person lists
        for (Person person : persons) {
            contacts.add(personConverter.convert(person));
        }
        return contacts;
    }

    @Override
    public SocialPersonProfile getContact(Google api, String connectionKey) {
        return personConverter.convert(api.personOperations().getContact(connectionKey));
    }

    @Override
    public Collection<SocialPersonProfile> getContacts(Google api, Collection<String> connectionKeys) {
        Collection<SocialPersonProfile> resultContacts = Lists.newArrayList();
        for (String connectionKey : connectionKeys) {
            resultContacts.add(getContact(api, connectionKey));
        }
        return resultContacts;
    }

    @Override
    public String getProviderId() {
        return SocialNetworkType.google.name();
    }

    private class GooglePersonToContactConverter implements Converter<Person, SocialPersonProfile> {

        @Override
        public SocialPersonProfile convert(Person source) {
            ConnectionKey primaryKey = ConnectionKeyFactory.get(SocialNetworkType.google, source.getId());
            Collection<ConnectionKey> links = Lists.newArrayList();
            // Step 1. Adding email as a service to the account
            Collection<Email> emails = source.getEmails();
            if (emails != null && emails.size() > 0) {
                for (Email email : source.getEmails()) {
                    links.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.email, email.getValue()));
                }
            }
            // Step 2. Adding phone as a service to the account
            Collection<Phone> phones = source.getPhoneNumbers();
            if (phones != null && phones.size() > 0) {
                for (Phone phoneNumber : source.getPhoneNumbers()) {
                    links.addAll(ConnectionKeyFactory.normalize(SocialNetworkType.phone, phoneNumber.getValue()));
                }
            }
            // Step 3. Generating profile
            SocialDate birthDate = null;
            Date birthD = source.getBirthday();
            if(birthD != null) {
                birthDate = new SimpleSocialDateBuilder().setDay(birthD.getDay()).setMonth(birthD.getMonth()).setYear(birthD.getYear()).freeze();
            }
            return new SimpleSocialPersonProfileBuilder().setSocialConnection(new ImmutableSocialConnection(primaryKey, links))
                    .setFirstName(source.getGivenName())
                    .setLastName(source.getFamilyName())
                    .setImage(source.getImageUrl())
                    .setBirthDate(birthDate)
                    .setGender(Gender.parse(source.getGender())).freeze();
        }
    }
}
