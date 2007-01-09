/**
 * GBOPerson.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.henvendelse;

import java.io.Serializable;
import java.util.Calendar;

import no.nav.domain.pensjon.person.Fodselsnummer;

public class NewPerson implements Serializable {
    private Fodselsnummer fnr;
    private String kommentar;
    private Calendar kommentarDato;
    private String kommentarIdent;
    private String personId;

    public NewPerson() {
    }

    public Fodselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fodselsnummer fnr) {
        this.fnr = fnr;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public Calendar getKommentarDato() {
        return kommentarDato;
    }

    public void setKommentarDato(Calendar kommentarDato) {
        this.kommentarDato = kommentarDato;
    }

    public String getKommentarIdent() {
        return kommentarIdent;
    }

    public void setKommentarIdent(String kommentarIdent) {
        this.kommentarIdent = kommentarIdent;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

}
