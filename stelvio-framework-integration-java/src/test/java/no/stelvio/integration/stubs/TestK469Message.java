package no.stelvio.integration.stubs;

import java.io.Serializable;


/** @author Hibernate CodeGenerator */
public class TestK469Message implements Serializable {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String systemId;

    /** nullable persistent field */
    private String kodeMelding;

    /** nullable persistent field */
    private int alvorlighetsgrad;

    /** nullable persistent field */
    private String melding;

    /** nullable persistent field */
    private String sqlkode;

    /** nullable persistent field */
    private String sqlState;

    /** nullable persistent field */
    private String sqlMelding;

    /** nullable persistent field */
    private String mqCompletionCode;

    /** nullable persistent field */
    private String mqReasonCode;

    /** nullable persistent field */
    private String programId;

    /** nullable persistent field */
    private String sectionNavn;

    /** full constructor */
    public TestK469Message(String systemId, String kodeMelding, int alvorlighetsgrad, String melding, String sqlkode, String sqlState, String sqlMelding, String mqCompletionCode, String mqReasonCode, String programId, String sectionNavn) {
        this.systemId = systemId;
        this.kodeMelding = kodeMelding;
        this.alvorlighetsgrad = alvorlighetsgrad;
        this.melding = melding;
        this.sqlkode = sqlkode;
        this.sqlState = sqlState;
        this.sqlMelding = sqlMelding;
        this.mqCompletionCode = mqCompletionCode;
        this.mqReasonCode = mqReasonCode;
        this.programId = programId;
        this.sectionNavn = sectionNavn;
    }

    /** default constructor */
    public TestK469Message() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getKodeMelding() {
        return this.kodeMelding;
    }

    public void setKodeMelding(String kodeMelding) {
        this.kodeMelding = kodeMelding;
    }

    public int getAlvorlighetsgrad() {
        return this.alvorlighetsgrad;
    }

    public void setAlvorlighetsgrad(int alvorlighetsgrad) {
        this.alvorlighetsgrad = alvorlighetsgrad;
    }

    public String getMelding() {
        return this.melding;
    }

    public void setMelding(String melding) {
        this.melding = melding;
    }

    public String getSqlkode() {
        return this.sqlkode;
    }

    public void setSqlkode(String sqlkode) {
        this.sqlkode = sqlkode;
    }

    public String getSqlState() {
        return this.sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public String getSqlMelding() {
        return this.sqlMelding;
    }

    public void setSqlMelding(String sqlMelding) {
        this.sqlMelding = sqlMelding;
    }

    public String getMqCompletionCode() {
        return this.mqCompletionCode;
    }

    public void setMqCompletionCode(String mqCompletionCode) {
        this.mqCompletionCode = mqCompletionCode;
    }

    public String getMqReasonCode() {
        return this.mqReasonCode;
    }

    public void setMqReasonCode(String mqReasonCode) {
        this.mqReasonCode = mqReasonCode;
    }

    public String getProgramId() {
        return this.programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getSectionNavn() {
        return this.sectionNavn;
    }

    public void setSectionNavn(String sectionNavn) {
        this.sectionNavn = sectionNavn;
    }

}
