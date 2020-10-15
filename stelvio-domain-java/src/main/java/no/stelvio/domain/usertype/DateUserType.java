package no.stelvio.domain.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * When using Date columns, Oracle stores the time component as well. While @Temporal(DATE) does trigger Hibernate to use java.sql.Date (as intented), that class is simply a
 * wrapper of java.util.Date, and the time component is retained. The Oracle jdbc driver uses the entire information when convertering to its own date representation further
 * down the persistence process.
 * <p>
 * This is from a somewhat older doc (https://docs.oracle.com/cd/B13789_01/server.101/b10749/ch4datet.htm), but still applies:
 * <quote><i>Oracle DATE columns always contain fields for both date and time. If your queries use a date format without a time portion, then you must ensure that the time
 * fields in the DATE column are set to midnight. You can use the TRUNC (date) SQL function to ensure that the time fields are set to midnight, or you can make the query a test
 * of greater than or less than (<, <=, >=, or >) instead of equality or inequality (= or !=). Otherwise, Oracle may not return the query results you expect.</i></quote>
 */
@SuppressWarnings("unused")
public class DateUserType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.DATE};
    }

    @Override
    public Class returnedClass() {
        return Date.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        java.sql.Date date = rs.getDate(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new Date(date.getTime());
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.DATE);
        } else {
            Date date = (Date) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            st.setDate(index, new java.sql.Date(cal.getTimeInMillis()));
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
