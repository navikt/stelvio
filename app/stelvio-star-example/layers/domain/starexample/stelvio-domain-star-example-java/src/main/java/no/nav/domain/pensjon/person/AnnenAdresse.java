/**
 * PselvAnnenAdresse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;
import java.util.Calendar;

public class AnnenAdresse implements Serializable {
    private String adresselinje1;
    private String adresselinje2;
    private String adresselinje3;
    private String postnr;
    private String poststed;
    private String landkode;
    private String land;
    private Calendar fom;
    private Calendar tom;
    private String endretAvBruker;
    private String endretAvSystem;
    private Calendar endretAvTidspunkt;

    public AnnenAdresse() {
    }

    public String getAdresselinje1() {
        return adresselinje1;
    }

    public void setAdresselinje1(String adresselinje1) {
        this.adresselinje1 = adresselinje1;
    }

    public String getAdresselinje2() {
        return adresselinje2;
    }

    public void setAdresselinje2(String adresselinje2) {
        this.adresselinje2 = adresselinje2;
    }

    public String getAdresselinje3() {
        return adresselinje3;
    }

    public void setAdresselinje3(String adresselinje3) {
        this.adresselinje3 = adresselinje3;
    }

    public String getPostnr() {
        return postnr;
    }

    public void setPostnr(String postnr) {
        this.postnr = postnr;
    }

    public String getPoststed() {
        return poststed;
    }

    public void setPoststed(String poststed) {
        this.poststed = poststed;
    }

    public String getLandkode() {
        return landkode;
    }

    public void setLandkode(String landkode) {
        this.landkode = landkode;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public Calendar getFom() {
        return fom;
    }

    public void setFom(Calendar fom) {
        this.fom = fom;
    }

    public Calendar getTom() {
        return tom;
    }

    public void setTom(Calendar tom) {
        this.tom = tom;
    }

    public String getEndretAvBruker() {
        return endretAvBruker;
    }

    public void setEndretAvBruker(String endretAvBruker) {
        this.endretAvBruker = endretAvBruker;
    }

    public String getEndretAvSystem() {
        return endretAvSystem;
    }

    public void setEndretAvSystem(String endretAvSystem) {
        this.endretAvSystem = endretAvSystem;
    }

    public Calendar getEndretAvTidspunkt() {
        return endretAvTidspunkt;
    }

    public void setEndretAvTidspunkt(Calendar endretAvTidspunkt) {
        this.endretAvTidspunkt = endretAvTidspunkt;
    }

}
