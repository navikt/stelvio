/**
 * GBOPerson.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.stelvio.domain.star.example.henvendelse;

import java.io.Serializable;
import java.util.Calendar;

import no.stelvio.domain.person.Pid;

public class NewPerson implements Serializable {
    private Pid fnr;
    private String kommentar;
    private Calendar kommentarDato;
    private String kommentarIdent;
    private String personId;

    public NewPerson() {
    }

    public Pid getPid() {
        return fnr;
    }

    public void setPid(Pid fnr) {
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
