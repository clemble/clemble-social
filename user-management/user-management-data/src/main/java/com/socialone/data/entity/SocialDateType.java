package com.socialone.data.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.IntegerType;

import com.socialone.data.date.SocialDate;
import com.socialone.data.date.SocialDate.SimpleSocialDateBuilder;

public class SocialDateType extends AbstractImmutableType {

    @Override
    public int[] sqlTypes() {
        return new int[] { 4,// IntegerType.INSTANCE.sqlType(),
                4,// IntegerType.INSTANCE.sqlType(),
                4 // IntegerType.INSTANCE.sqlType()
        };
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class returnedClass() {
        return SocialDate.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        assert names.length == 3;
        return new SimpleSocialDateBuilder()
            .setDay((Integer) IntegerType.INSTANCE.get(rs, names[0], session))
            .setMonth((Integer) IntegerType.INSTANCE.get(rs, names[1], session))
            .setYear((Integer) IntegerType.INSTANCE.get(rs, names[2], session));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        SocialDate socialDate = (SocialDate) value;
        IntegerType.INSTANCE.set(st, socialDate.getDay(), index, session);
        IntegerType.INSTANCE.set(st, socialDate.getMonth(), index + 1, session);
        IntegerType.INSTANCE.set(st, socialDate.getYear(), index + 2, session);
    }

}
