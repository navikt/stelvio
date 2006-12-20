package no.stelvio.business.model;

import java.util.Date;

import org.joda.time.LocalDate;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo what should be in this interface?
 */
public interface PidNum {
    String getNummer();
    LocalDate getFodselsdato();
}
