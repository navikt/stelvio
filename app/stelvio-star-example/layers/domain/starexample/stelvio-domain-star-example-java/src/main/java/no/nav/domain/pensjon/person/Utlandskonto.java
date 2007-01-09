/**
 * PselvUtlandskonto.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;

public class Utlandskonto  implements Serializable {
    private String banknavn;
    private String bankadresse1;
    private String bankadresse2;
    private String bankadresse3;
    private String swiftkode;
    private String kontonummer;
    private String valuta;
    private String land;
    private String bankkode;

    public Utlandskonto() {
    }

    public String getBanknavn() {
        return banknavn;
    }

    public void setBanknavn(String banknavn) {
        this.banknavn = banknavn;
    }

    public String getBankadresse1() {
        return bankadresse1;
    }

    public void setBankadresse1(String bankadresse1) {
        this.bankadresse1 = bankadresse1;
    }

    public String getBankadresse2() {
        return bankadresse2;
    }

    public void setBankadresse2(String bankadresse2) {
        this.bankadresse2 = bankadresse2;
    }

    public String getBankadresse3() {
        return bankadresse3;
    }

    public void setBankadresse3(String bankadresse3) {
        this.bankadresse3 = bankadresse3;
    }

    public String getSwiftkode() {
        return swiftkode;
    }

    public void setSwiftkode(String swiftkode) {
        this.swiftkode = swiftkode;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(String kontonummer) {
        this.kontonummer = kontonummer;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getBankkode() {
        return bankkode;
    }

    public void setBankkode(String bankkode) {
        this.bankkode = bankkode;
    }

}
