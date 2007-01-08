/**
 * Utbetalingsinformasjon.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;
import java.util.Calendar;

public class Utbetalingsinformasjon  implements Serializable {
    private String utbetalingsType;
    private String endretAv;
    private String endretAvSystem;
    private Calendar endretAvTidspunkt;
    private NorskKonto norskKonto;
    private Utlandskonto utlandsKonto;

    public Utbetalingsinformasjon() {
    }

    public String getUtbetalingsType() {
        return utbetalingsType;
    }

    public void setUtbetalingsType(String utbetalingsType) {
        this.utbetalingsType = utbetalingsType;
    }

    public String getEndretAv() {
        return endretAv;
    }

    public void setEndretAv(String endretAv) {
        this.endretAv = endretAv;
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

    public NorskKonto getNorskKonto() {
        return norskKonto;
    }

    public void setNorskKonto(NorskKonto norskKonto) {
        this.norskKonto = norskKonto;
    }

    public Utlandskonto getUtlandsKonto() {
        return utlandsKonto;
    }

    public void setUtlandsKonto(Utlandskonto utlandsKonto) {
        this.utlandsKonto = utlandsKonto;
    }

}
