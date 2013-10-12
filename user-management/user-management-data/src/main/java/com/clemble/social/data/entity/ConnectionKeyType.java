package com.clemble.social.data.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.springframework.social.connect.ConnectionKey;

import com.google.common.base.Function;

public class ConnectionKeyType extends AbstractImmutableType {

    final public static Function<ConnectionKey, String> CONNECTION_KEY_TO_STRING = new Function<ConnectionKey, String>() {
        @Override
        public String apply(ConnectionKey connectionKey) {
            return connectionKey != null ? connectionKey.getProviderId() + ":" + connectionKey.getProviderUserId() : null;
        }
    };

    final public static Function<String, ConnectionKey> STRING_TO_CONNECTION_KEY = new Function<String, ConnectionKey>() {
        @Override
        public ConnectionKey apply(String connectionKey) {
            if (connectionKey == null)
                return null;
            String[] splittedString = connectionKey.split(":");
            return splittedString.length == 2 ? new ConnectionKey(splittedString[0], splittedString[1]) : null;

        }
    };

    @Override
    public int[] sqlTypes() {
        return new int[] { 12 // StringType.INSTANCE.sqlType()
        };
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class returnedClass() {
        return ConnectionKey.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        assert names.length == 1;
        Object connectionKey = StringType.INSTANCE.get(rs, names[0], session);
        if (connectionKey == null)
            return null;
        return STRING_TO_CONNECTION_KEY.apply(connectionKey.toString());
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            StringType.INSTANCE.set(st, null, index, session);
        } else {
            ConnectionKey connectionKey = (ConnectionKey) value;
            StringType.INSTANCE.set(st, CONNECTION_KEY_TO_STRING.apply(connectionKey), index, session);
        }
    }

}
