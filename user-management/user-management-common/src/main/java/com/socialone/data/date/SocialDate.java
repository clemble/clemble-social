package com.socialone.data.date;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import com.socialone.data.Immutable;
import com.socialone.data.markups.Mutable;

public abstract class SocialDate implements Serializable, Mutable<SocialDate> {
    final static private long serialVersionUID = 1L;

    abstract public int getYear();

    abstract public int getMonth();

    abstract public int getDay();

    @Override
    public SocialDate freeze() {
        return new ImmutableSocialDate(getYear(), getMonth(), getDay());
    }

    @Override
    public SocialDate merge(SocialDate other) {
        throw new IllegalAccessError();
    }

    @Override
    public SocialDate safeMerge(SocialDate other) {
        throw new IllegalAccessError();
    }

    @Override
    public SocialDate safeMerge(Collection<? extends SocialDate> others) {
        throw new IllegalAccessError();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + 31 * ((int) getYear());
        result = 31 * result + 31 * ((int) getMonth());
        result = 31 * result + 31 * ((int) getDay());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        SocialDate other = (SocialDate) obj;
        if (getYear() != other.getYear())
            return false;
        if (getMonth() != other.getMonth())
            return false;
        if (getDay() != other.getDay())
            return false;

        return true;
    }

    public static abstract class SocialDateBuilder extends SocialDate implements Builder<SocialDate> {

        final static private long serialVersionUID = 1L;

        public SocialDateBuilder() {
        }

        public SocialDateBuilder(SocialDate other) {
            merge(other);
        }

        abstract public SocialDateBuilder setYear(int newYear);

        abstract public SocialDateBuilder setMonth(int newMonth);

        abstract public SocialDateBuilder setDay(int newDay);

        @Override
        public SocialDateBuilder merge(SocialDate other) {
            if (other != null) {
                setYear(other.getYear()).setMonth(other.getMonth()).setDay(other.getDay());
            }
            return this;
        }

        @Override
        public SocialDateBuilder safeMerge(SocialDate other) {
            if (other != null) {
                if (getYear() == -1)
                    setYear(other.getYear());
                if (getMonth() == -1)
                    setMonth(other.getMonth());
                if (getDay() == -1)
                    setDay(other.getDay());
            }
            return this;
        }

        @Override
        public SocialDateBuilder safeMerge(Collection<? extends SocialDate> others) {
            if (others != null && !others.isEmpty()) {
                for (SocialDate other : others)
                    safeMerge(other);
            }
            return this;
        }

        @Override
        public SocialDate build() {
            return freeze();
        }
    }

    final public static class ImmutableSocialDate extends SocialDate implements Immutable {
        final static private long serialVersionUID = 1L;

        final private int year;
        final private int month;
        final private int day;

        public ImmutableSocialDate(SocialDate other) {
            this((other != null ? other.getYear() : -1), (other != null ? other.getMonth() : -1), (other != null ? other.getDay() : -1));
        }

        public ImmutableSocialDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public int getYear() {
            return this.year;
        }

        @Override
        public int getMonth() {
            return this.month;
        }

        @Override
        public int getDay() {
            return this.day;
        }

        @Override
        public SocialDate freeze() {
            return this;
        }
    }

    public static class SimpleSocialDateBuilder extends SocialDateBuilder {
        final static private long serialVersionUID = 1L;

        private int year;
        private int month;
        private int day;

        public SimpleSocialDateBuilder() {
        }

        public SimpleSocialDateBuilder(SocialDate other) {
            super(other);
        }

        public SimpleSocialDateBuilder(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public int getYear() {
            return this.year;
        }

        @Override
        public SocialDateBuilder setYear(int newYear) {
            this.year = newYear;
            return this;
        }

        @Override
        public int getMonth() {
            return this.month;
        }

        @Override
        public SocialDateBuilder setMonth(int newMonth) {
            this.month = newMonth;
            return this;
        }

        @Override
        public int getDay() {
            return this.day;
        }

        @Override
        public SocialDateBuilder setDay(int newDay) {
            this.day = newDay;
            return this;
        }
    }
}