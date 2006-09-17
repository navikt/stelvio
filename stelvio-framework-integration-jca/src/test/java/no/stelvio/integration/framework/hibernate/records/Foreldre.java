package no.stelvio.integration.framework.hibernate.records;

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.framework.core.DomainObject;


/** @author Hibernate CodeGenerator */
public class Foreldre extends DomainObject {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String aksjonskode;

    /** nullable persistent field */
    private String sakNr;

    /** nullable persistent field */
    private String soknadsType;

    /** nullable persistent field */
    private String ugKode;

    /** nullable persistent field */
    private String datoSoknad;

    /** nullable persistent field */
    private String datoSokFOM;

    /** nullable persistent field */
    private String datoBerTOM;

    /** nullable persistent field */
    private String brukerId;

    /** nullable persistent field */
    private String objectnrBP;

    /** nullable persistent field */
    private String personIdBP;

    /** nullable persistent field */
    private String soktGebyrFriBP;

    /** nullable persistent field */
    private String objectnrBM;

    /** nullable persistent field */
    private String personIdBM;

    /** nullable persistent field */
    private String soktGebyrFriBM;

    /** nullable persistent field */
    private String feilPeker;

    /** nullable persistent field */
    private int numRecords;

    /** persistent field */
    private Set barn;

    /** full constructor */
    public Foreldre(String aksjonskode, String sakNr, String soknadsType, String ugKode, String datoSoknad, String datoSokFOM, String datoBerTOM, String brukerId, String objectnrBP, String personIdBP, String soktGebyrFriBP, String objectnrBM, String personIdBM, String soktGebyrFriBM, String feilPeker, int numRecords, Set barn) {
        this.aksjonskode = aksjonskode;
        this.sakNr = sakNr;
        this.soknadsType = soknadsType;
        this.ugKode = ugKode;
        this.datoSoknad = datoSoknad;
        this.datoSokFOM = datoSokFOM;
        this.datoBerTOM = datoBerTOM;
        this.brukerId = brukerId;
        this.objectnrBP = objectnrBP;
        this.personIdBP = personIdBP;
        this.soktGebyrFriBP = soktGebyrFriBP;
        this.objectnrBM = objectnrBM;
        this.personIdBM = personIdBM;
        this.soktGebyrFriBM = soktGebyrFriBM;
        this.feilPeker = feilPeker;
        this.numRecords = numRecords;
        this.barn = barn;
    }

    /** default constructor */
    public Foreldre() {
    }

    /** minimal constructor */
    public Foreldre(Set barn) {
        this.barn = barn;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAksjonskode() {
        return this.aksjonskode;
    }

    public void setAksjonskode(String aksjonskode) {
        this.aksjonskode = aksjonskode;
    }

    public String getSakNr() {
        return this.sakNr;
    }

    public void setSakNr(String sakNr) {
        this.sakNr = sakNr;
    }

    public String getSoknadsType() {
        return this.soknadsType;
    }

    public void setSoknadsType(String soknadsType) {
        this.soknadsType = soknadsType;
    }

    public String getUgKode() {
        return this.ugKode;
    }

    public void setUgKode(String ugKode) {
        this.ugKode = ugKode;
    }

    public String getDatoSoknad() {
        return this.datoSoknad;
    }

    public void setDatoSoknad(String datoSoknad) {
        this.datoSoknad = datoSoknad;
    }

    public String getDatoSokFOM() {
        return this.datoSokFOM;
    }

    public void setDatoSokFOM(String datoSokFOM) {
        this.datoSokFOM = datoSokFOM;
    }

    public String getDatoBerTOM() {
        return this.datoBerTOM;
    }

    public void setDatoBerTOM(String datoBerTOM) {
        this.datoBerTOM = datoBerTOM;
    }

    public String getBrukerId() {
        return this.brukerId;
    }

    public void setBrukerId(String brukerId) {
        this.brukerId = brukerId;
    }

    public String getObjectnrBP() {
        return this.objectnrBP;
    }

    public void setObjectnrBP(String objectnrBP) {
        this.objectnrBP = objectnrBP;
    }

    public String getPersonIdBP() {
        return this.personIdBP;
    }

    public void setPersonIdBP(String personIdBP) {
        this.personIdBP = personIdBP;
    }

    public String getSoktGebyrFriBP() {
        return this.soktGebyrFriBP;
    }

    public void setSoktGebyrFriBP(String soktGebyrFriBP) {
        this.soktGebyrFriBP = soktGebyrFriBP;
    }

    public String getObjectnrBM() {
        return this.objectnrBM;
    }

    public void setObjectnrBM(String objectnrBM) {
        this.objectnrBM = objectnrBM;
    }

    public String getPersonIdBM() {
        return this.personIdBM;
    }

    public void setPersonIdBM(String personIdBM) {
        this.personIdBM = personIdBM;
    }

    public String getSoktGebyrFriBM() {
        return this.soktGebyrFriBM;
    }

    public void setSoktGebyrFriBM(String soktGebyrFriBM) {
        this.soktGebyrFriBM = soktGebyrFriBM;
    }

    public String getFeilPeker() {
        return this.feilPeker;
    }

    public void setFeilPeker(String feilPeker) {
        this.feilPeker = feilPeker;
    }

    public int getNumRecords() {
        return this.numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }

    public Set getBarn() {
        return this.barn;
    }

    public void setBarn(Set barn) {
        this.barn = barn;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
