package com.socialone.data.entity;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class AbstractImmutableType implements UserType {

    @Override
    final public boolean equals(Object x, Object y) throws HibernateException {
        return x != null ? (x.equals(y)) : (y == null ? true : false);
    }

    @Override
    final public int hashCode(Object x) throws HibernateException {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    final public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    final public boolean isMutable() {
        return false;
    }

    @Override
    final public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    final public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    final public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return target;
    }

}
