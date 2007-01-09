/**
 * PselvEndagspassord.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.brukeroversikt;

import java.io.Serializable;
import java.util.Calendar;

public class NewEndagspassord implements Serializable {
    private String kryptertEndagspassord;
    private Calendar FOM;
    private Calendar TOM;
    private String rolle;
    private Integer antallFeilInnlogginger;
    private String brukerId;
    private String passord;

    public NewEndagspassord() {
    }

    public String getKryptertEndagspassord() {
        return kryptertEndagspassord;
    }

    public void setKryptertEndagspassord(String kryptertEndagspassord) {
        this.kryptertEndagspassord = kryptertEndagspassord;
    }

    public Calendar getFOM() {
        return FOM;
    }

    public void setFOM(Calendar FOM) {
        this.FOM = FOM;
    }

    public Calendar getTOM() {
        return TOM;
    }

    public void setTOM(Calendar TOM) {
        this.TOM = TOM;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public Integer getAntallFeilInnlogginger() {
        return antallFeilInnlogginger;
    }

    public void setAntallFeilInnlogginger(Integer antallFeilInnlogginger) {
        this.antallFeilInnlogginger = antallFeilInnlogginger;
    }

    public String getBrukerId() {
        return brukerId;
    }

    public void setBrukerId(String brukerId) {
        this.brukerId = brukerId;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

}
