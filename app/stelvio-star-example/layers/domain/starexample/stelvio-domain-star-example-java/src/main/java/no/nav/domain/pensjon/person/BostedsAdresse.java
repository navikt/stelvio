/**
 * PselvBostedsAdresse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;

public class BostedsAdresse implements Serializable {
    private String boadresse1;
    private String boadresse2;
    private String bolignr;
    private String postnummer;
    private String poststed;
    private String kommunenr;
    private String navEnhet;

    public BostedsAdresse() {
    }

    public String getBoadresse1() {
        return boadresse1;
    }

    public void setBoadresse1(String boadresse1) {
        this.boadresse1 = boadresse1;
    }

    public String getBoadresse2() {
        return boadresse2;
    }

    public void setBoadresse2(String boadresse2) {
        this.boadresse2 = boadresse2;
    }

    public String getBolignr() {
        return bolignr;
    }

    public void setBolignr(String bolignr) {
        this.bolignr = bolignr;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postnummer) {
        this.postnummer = postnummer;
    }

    public String getPoststed() {
        return poststed;
    }

    public void setPoststed(String poststed) {
        this.poststed = poststed;
    }

    public String getKommunenr() {
        return kommunenr;
    }

    public void setKommunenr(String kommunenr) {
        this.kommunenr = kommunenr;
    }

    public String getNavEnhet() {
        return navEnhet;
    }

    public void setNavEnhet(String navEnhet) {
        this.navEnhet = navEnhet;
    }

}
