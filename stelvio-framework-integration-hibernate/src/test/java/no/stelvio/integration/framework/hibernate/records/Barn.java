package no.stelvio.integration.framework.hibernate.records;

import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.framework.core.DomainObject;


/** @author Hibernate CodeGenerator */
public class Barn extends DomainObject {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String objectNr;

    /** nullable persistent field */
    private String personId;

    /** nullable persistent field */
    private String bidragFl;

    /** nullable persistent field */
    private String tillBidragFl;

    /** nullable persistent field */
    private String forskuddFl;

    /** nullable persistent field */
    private String saerTilskFl;

    /** nullable persistent field */
    private String samvaerKls;

    /** nullable persistent field */
    private String innkrevFl;

    /** nullable persistent field */
    private String belopLopBidrag;

    /** nullable persistent field */
    private String feilPeker;

    /** nullable persistent field */
    private no.stelvio.integration.framework.hibernate.records.Foreldre parent;

    /** full constructor */
    public Barn(String objectNr, String personId, String bidragFl, String tillBidragFl, String forskuddFl, String saerTilskFl, String samvaerKls, String innkrevFl, String belopLopBidrag, String feilPeker, no.stelvio.integration.framework.hibernate.records.Foreldre parent) {
        this.objectNr = objectNr;
        this.personId = personId;
        this.bidragFl = bidragFl;
        this.tillBidragFl = tillBidragFl;
        this.forskuddFl = forskuddFl;
        this.saerTilskFl = saerTilskFl;
        this.samvaerKls = samvaerKls;
        this.innkrevFl = innkrevFl;
        this.belopLopBidrag = belopLopBidrag;
        this.feilPeker = feilPeker;
        this.parent = parent;
    }

    /** default constructor */
    public Barn() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectNr() {
        return this.objectNr;
    }

    public void setObjectNr(String objectNr) {
        this.objectNr = objectNr;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getBidragFl() {
        return this.bidragFl;
    }

    public void setBidragFl(String bidragFl) {
        this.bidragFl = bidragFl;
    }

    public String getTillBidragFl() {
        return this.tillBidragFl;
    }

    public void setTillBidragFl(String tillBidragFl) {
        this.tillBidragFl = tillBidragFl;
    }

    public String getForskuddFl() {
        return this.forskuddFl;
    }

    public void setForskuddFl(String forskuddFl) {
        this.forskuddFl = forskuddFl;
    }

    public String getSaerTilskFl() {
        return this.saerTilskFl;
    }

    public void setSaerTilskFl(String saerTilskFl) {
        this.saerTilskFl = saerTilskFl;
    }

    public String getSamvaerKls() {
        return this.samvaerKls;
    }

    public void setSamvaerKls(String samvaerKls) {
        this.samvaerKls = samvaerKls;
    }

    public String getInnkrevFl() {
        return this.innkrevFl;
    }

    public void setInnkrevFl(String innkrevFl) {
        this.innkrevFl = innkrevFl;
    }

    public String getBelopLopBidrag() {
        return this.belopLopBidrag;
    }

    public void setBelopLopBidrag(String belopLopBidrag) {
        this.belopLopBidrag = belopLopBidrag;
    }

    public String getFeilPeker() {
        return this.feilPeker;
    }

    public void setFeilPeker(String feilPeker) {
        this.feilPeker = feilPeker;
    }

    public no.stelvio.integration.framework.hibernate.records.Foreldre getParent() {
        return this.parent;
    }

    public void setParent(no.stelvio.integration.framework.hibernate.records.Foreldre parent) {
        this.parent = parent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
