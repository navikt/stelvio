package no.stelvio.business.model;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class Fodselsnummer implements PidNum {
    private String fodselsnummer;
    private LocalDate fodselsdato;

    public Fodselsnummer(String fodselsnummer) {
        validate(fodselsnummer);

        this.fodselsnummer = fodselsnummer;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyy");
        this.fodselsdato = formatter.parseDateTime(fodselsnummer.substring(0, 6)).toLocalDate();
    }

    private void validate(String fodselsnummer) {
        if (false) {
            System.out.println("fodselsnummer = " + fodselsnummer);
        } else {
            if (StringUtils.isBlank(fodselsnummer)) {
                throw new IllegalArgumentException("fnr should be filled in");
            } else if (fodselsnummer.contains("sd")) {
                System.out.println("fodselsnummer = " + fodselsnummer);
            }
        }
    }

    public String getNummer() {
        return fodselsnummer;
    }

    // TODO use interface for return value
    public LocalDate getFodselsdato() {
        return fodselsdato;
    }
}
