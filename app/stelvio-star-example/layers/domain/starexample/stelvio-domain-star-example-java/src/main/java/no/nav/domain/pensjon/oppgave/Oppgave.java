package no.nav.domain.pensjon.oppgave;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * Represents a Oppgave (Task) that belongs to a Henvendelse
 * Instances of this class are Java Persistence API Entities, and may be persisted by an JPA EntityManager
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
@NamedQueries({
	@NamedQuery(name="Oppgave.findByOpprettetAv", query="SELECT o FROM Oppgave o WHERE o.opprettetAv= :opprettetAv")
})
@Entity
@Table(name="OPPGAVE")
public class Oppgave implements Serializable{

    //Version field is only updated by persistence provider. There shall be no setter!
    @Version
    private int version;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="OPPGAVEID")
	private int oppgaveId;
	
	@Column(name="ANSVARLIG_IDENT")
	private String ansvarligIdent;

    @Column(name="OPPG_TYPE")
    private String oppgType;

    @Column(name="FAGOMR_KODE")
    private String fagomrKode;

    @Column(name="SOKN_ID")
    private Integer soknId;

    @Column(name="TKNR")
    private String tknr;

    private String dokref;

    private String beskrivelse;

    private String oppfolgning;

    private boolean prioritet;

    @Temporal(TemporalType.DATE)
    @Column(name="FRIST_DATO")
	private Date fristDato;

    @Column(name="OPPRETTET_AV")
	private String opprettetAv;

    @Temporal(TemporalType.DATE)
    @Column(name="OPPRETTET_DATO")
	private Date opprettetDato;

    @Column(name="SIST_ENDRET_AV")
	private String sistEndretAv;

    @Temporal(TemporalType.DATE)
    @Column(name="SIST_ENDRET_DATO")
	private Date sistEndretDato;

    private String saksnr;

    @Temporal(TemporalType.DATE)
    @Column(name="MOTTATT_DATO")
	private Date mottattDato;

    @Temporal(TemporalType.DATE)
    @Column(name="SKANNET_DATO")
	private Date skannetDato;

    @Column(name="SIST_ENDRET_ENHET")
	private String sistEndretEnhet;

    @Column(name="OPPRETTET_ENHET")
	private String opprettetEnhet;

    @Column(name="ANSVARLIG_NAVN")
	private String ansvarligNavn;
	
	//Relationships
	/*@ManyToOne
	@JoinColumn(name="HENVENDELSEID")
	private Henvendelse henvendelse;*/
	
	/*@ManyToOne
	@JoinColumn(name="PERSONID")
	private Person person;*/

	/**
	 * Default No-arg constructor should only be used by persistence provider.
	 *
	 */
	protected Oppgave(){
	}
	
	public Oppgave(String ansvarligIdent, String oppgType, String fagomrKode){
		this.ansvarligIdent = ansvarligIdent;
		this.oppgType = oppgType;
		this.fagomrKode = fagomrKode;
	}
	
	/**
	 * Creates a new Oppgave
	 * @param ansvarligIdent - ansvarlig ident (id of person responsible for this task)
	 * @param oppgType - oppgavetype (type of task)
	 * @param fagomrKode - fagområdekode (code identifying subject area)
	 * @param soknId - søknad id (application id )
	 * @param tknr - trygdekontornummer (social security office number)
	 * @param dokref - dokumentreferanse (document reference)
	 * @param beskrivelse - beskrivelse av oppgave (task description)
	 * @param oppfolgning -  oppfølging (follow-up)
	 * @param prioritet - prioritet (priority)
	 * @param fristDato - frist for oppgave (deadline for this task)
	 * @param opprettetAv - opprettet av (created by)
	 * @param opprettetDato - opprettet dato (date created)
	 * @param sistEndretAv - sist endret av id (last modified by id)
	 * @param sistEndretDato - sist endret dato (last modified date)
	 * @param saksnr - saksnummer (case id)
	 * @param mottattDato - dato mottatt (date received)
	 * @param skannetDato - dato skannet (scanned date)
	 * @param sistEndretEnhet - sist endret av enhet (last modified by unit)
	 * @param opprettetEnhet - opprettet av enhet (created by unit)
	 * @param ansvarligNavn - navn på ansvarlig for oppgaven (name of person responsible for this task)
	 *//*
	public Oppgave(String ansvarligIdent, String oppgType, String fagomrKode, Integer soknId, String tknr, String dokref, String beskrivelse, String oppfolgning, boolean prioritet, Date fristDato, String opprettetAv, Date opprettetDato, String sistEndretAv, Date sistEndretDato, String saksnr, Date mottattDato, Date skannetDato, String sistEndretEnhet, String opprettetEnhet, String ansvarligNavn) {
		this.ansvarligIdent = ansvarligIdent;
		this.oppgType = oppgType;
		this.fagomrKode = fagomrKode;
		this.soknId = soknId;
		this.tknr = tknr;
		this.dokref = dokref;
		this.beskrivelse = beskrivelse;
		this.oppfolgning = oppfolgning;
		this.prioritet = prioritet;
		this.fristDato = fristDato;
		this.opprettetAv = opprettetAv;
		this.opprettetDato = opprettetDato;
		this.sistEndretAv = sistEndretAv;
		this.sistEndretDato = sistEndretDato;
		this.saksnr = saksnr;
		this.mottattDato = mottattDato;
		this.skannetDato = skannetDato;
		this.sistEndretEnhet = sistEndretEnhet;
		this.opprettetEnhet = opprettetEnhet;
		this.ansvarligNavn = ansvarligNavn;
	}

	*//**
	 * Creates a new Oppgave
	 * This constructor should only be used when re-creating a Oppgave that already exists in the database
	 * <b>Must NEVER be used when creating new entity for persistece in database</b>
	 * @param oppgaveId - unique id
	 * @param ansvarligIdent - ansvarlig ident (id of person responsible for this task)
	 * @param oppgType - oppgavetype (type of task)
	 * @param fagomrKode - fagområdekode (code identifying subject area)
	 * @param soknId - søknad id (application id )
	 * @param tknr - trygdekontornummer (social security office number)
	 * @param dokref - dokumentreferanse (document reference)
	 * @param beskrivelse - beskrivelse av oppgave (task description)
	 * @param oppfolgning -  oppfølging (follow-up)
	 * @param prioritet - prioritet (priority)
	 * @param fristDato - frist for oppgave (deadline for this task)
	 * @param opprettetAv - opprettet av (created by)
	 * @param opprettetDato - opprettet dato (date created)
	 * @param sistEndretAv - sist endret av id (last modified by id)
	 * @param sistEndretDato - sist endret dato (last modified date)
	 * @param saksnr - saksnummer (case id)
	 * @param mottattDato - dato mottatt (date received)
	 * @param skannetDato - dato skannet (scanned date)
	 * @param sistEndretEnhet - sist endret av enhet (last modified by unit)
	 * @param opprettetEnhet - opprettet av enhet (created by unit)
	 * @param ansvarligNavn - navn på ansvarlig for oppgaven (name of person responsible for this task)
	 *//*	
	public Oppgave(int oppgaveId, String ansvarligIdent, String oppgType, String fagomrKode, Integer soknId, String tknr, String dokref, String beskrivelse, String oppfolgning, boolean prioritet, Date fristDato, String opprettetAv, Date opprettetDato, String sistEndretAv, Date sistEndretDato, String saksnr, Date mottattDato, Date skannetDato, String sistEndretEnhet, String opprettetEnhet, String ansvarligNavn) {
		this.oppgaveId = oppgaveId;
		this.ansvarligIdent = ansvarligIdent;
		this.oppgType = oppgType;
		this.fagomrKode = fagomrKode;
		this.soknId = soknId;
		this.tknr = tknr;
		this.dokref = dokref;
		this.beskrivelse = beskrivelse;
		this.oppfolgning = oppfolgning;
		this.prioritet = prioritet;
		this.fristDato = fristDato;
		this.opprettetAv = opprettetAv;
		this.opprettetDato = opprettetDato;
		this.sistEndretAv = sistEndretAv;
		this.sistEndretDato = sistEndretDato;
		this.saksnr = saksnr;
		this.mottattDato = mottattDato;
		this.skannetDato = skannetDato;
		this.sistEndretEnhet = sistEndretEnhet;
		this.opprettetEnhet = opprettetEnhet;
		this.ansvarligNavn = ansvarligNavn;
	}*/

	/**
	 * Gets ansvarlig ident (id for person responsible for this task)
	 * @return ansvarlig ident (id for person responsible for this task)
	 */
	public String getAnsvarligIdent() {
		return ansvarligIdent;
	}

	/**
	 * Sets ansvarlig ident (id for person responsible for this task)
	 * @param ansvarligIdent
	 */
	public void setAnsvarligIdent(String ansvarligIdent) {
		this.ansvarligIdent = ansvarligIdent;
	}

	/**
	 * Gets ansvarlig navn (name of person responsible for this task)
	 * @return ansvarlig navn (name of person responsible for this task)
	 */
	public String getAnsvarligNavn() {
		return ansvarligNavn;
	}

	/**
	 * Sets ansvarlig navn (name of person responsible for this task)
	 * @param ansvarligNavn
	 */
	public void setAnsvarligNavn(String ansvarligNavn) {
		this.ansvarligNavn = ansvarligNavn;
	}

	/**
	 * Gets beskrivelse (description)
	 * @return beskrivelse (description)
	 */
	public String getBeskrivelse() {
		return beskrivelse;
	}

	/**
	 * Sets beskrivelse (description)
	 * @param beskrivelse
	 */
	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}

	/**
	 * Gets dokument referanse (document reference)
	 * @return dokument referanse (document reference)
	 */
	public String getDokref() {
		return dokref;
	}

	/**
	 * Sets dokument referanse (document reference)
	 * @param dokument referanse (document reference)
	 */
	public void setDokref(String dokref) {
		this.dokref = dokref;
	}

	/**
	 * Gets fagområde kode (code identifying subject area)
	 * @return fagområde kode (code identifying subject area)
	 */
	public String getFagomrKode() {
		return fagomrKode;
	}

	/**
	 * Gets fagområde kode (code identifying subject area)
	 * @param fagområde kode (code identifying subject area)
	 */
	public void setFagomrKode(String fagomrKode) {
		this.fagomrKode = fagomrKode;
	}

	/**
	 * Gets dato frist (deadline date)
	 * @return dato frist (deadline date)
	 */
	public Date getFristDato() {
		return fristDato;
	}

	/**
	 * Gets dato frist (deadline date)
	 * @param dato frist (deadline date)
	 */
	public void setFristDato(Date fristDato) {
		this.fristDato = fristDato;
	}

/*	*//**
	 * Gets henvendelse (inquiry)
	 * @return henvendelse (inquiry)
	 *//*
	public Henvendelse getHenvendelse() {
		return henvendelse;
	}

	*//**
	 * Gets henvendelse (inquiry)
	 * @param henvendelse (inquiry)
	 *//*
	public void setHenvendelse(Henvendelse henvendelse) {
		this.henvendelse = henvendelse;
	}*/

	/**
	 * Gets mottatt dato (received date)
	 * @return mottatt dato (received date)
	 */
	public Date getMottattDato() {
		return mottattDato;
	}

	/**
	 * Gets mottatt dato (received date)
	 * @param mottatt dato (received date)
	 */
	public void setMottattDato(Date mottattDato) {
		this.mottattDato = mottattDato;
	}

	/**
	 * Gets oppfølging (follow-up)
	 * @return oppfølging (follow-up)
	 */
	public String getOppfolgning() {
		return oppfolgning;
	}

	/**
	 * Sets oppfølging (follow-up)
	 * @param oppfølging (follow-up)
	 */
	public void setOppfolgning(String oppfolgning) {
		this.oppfolgning = oppfolgning;
	}

	/**
	 * Gets oppgave id (task id)
	 * @return oppgave id (task id)
	 */
	public int getOppgaveId() {
		return oppgaveId;
	}

	/**
	 * Gets oppgave type (type of task)
	 * @return oppgave type (type of task)
	 */
	public String getOppgType() {
		return oppgType;
	}

	/**
	 * Sets oppgave type (type of task)
	 * @param oppgave type (type of task)
	 */
	public void setOppgType(String oppgType) {
		this.oppgType = oppgType;
	}

	/**
	 * Gets opprettet av (created by)
	 * @return opprettet av (created by)
	 */
	public String getOpprettetAv() {
		return opprettetAv;
	}

	/**
	 * Sets opprettet av (created by)
	 * @param opprettet av (created by)
	 */
	public void setOpprettetAv(String opprettetAv) {
		this.opprettetAv = opprettetAv;
	}

	/**
	 * Gets opprettet dato (created date)
	 * @return opprettet dato (created date)
	 */
	public Date getOpprettetDato() {
		return opprettetDato;
	}

	/**
	 * Sets opprettet dato (created date)
	 * @param opprettet dato (created date)
	 */
	public void setOpprettetDato(Date opprettetDato) {
		this.opprettetDato = opprettetDato;
	}

	/**
	 * Gets opprettet av enhet (created by unit)
	 * @return opprettet av enhet (created by unit)
	 */
	public String getOpprettetEnhet() {
		return opprettetEnhet;
	}

	/**
	 * Sets opprettet av enhet (created by unit)
	 * @param opprettet av enhet (created by unit)
	 */
	public void setOpprettetEnhet(String opprettetEnhet) {
		this.opprettetEnhet = opprettetEnhet;
	}

/*	*//**
	 * Gets person oppgaven gjelder (Gets person the task is concerning)
	 * @return person oppgaven gjelder (Gets person the task is concerning)
	 *//*
	public Person getPerson() {
		return person;
	}

	*//**
	 * Sets person oppgaven gjelder (Gets person the task is concerning)
	 * @param person person oppgaven gjelder (Gets person the task is concerning)
	 *//*
	public void setPerson(Person person) {
		this.person = person;
	}*/

	/**
	 * Gets prioritet (whether the task has prority)
	 * @return <code>true</code> if the task has priority, otherwise <code>false</code>
	 */
	public boolean isPrioritet() {
		return prioritet;
	}

	/**
	 * Sets prioritet (whether the task has prority)
	 * @param <code>true</code> if the task has priority, otherwise <code>false</code>
	 */
	public void setPrioritet(boolean prioritet) {
		this.prioritet = prioritet;
	}

	/**
	 * Gets saksnummer (case number)
	 * @return saksnummer (case number)
	 */
	public String getSaksnr() {
		return saksnr;
	}

	/**
	 * Sets saksnummer (case number)
	 * @param saksnummer (case number)
	 */
	public void setSaksnr(String saksnr) {
		this.saksnr = saksnr;
	}

	/**
	 * Gets sist endret av (last modified by)
	 * @return sist endret av (last modified by)
	 */
	public String getSistEndretAv() {
		return sistEndretAv;
	}

	/**
	 * Sets sist endret av (last modified by)
	 * @param sist endret av (last modified by)
	 */
	public void setSistEndretAv(String sistEndretAv) {
		this.sistEndretAv = sistEndretAv;
	}

	/**
	 * Gets sist endret dato (last modified date)
	 * @return sist endret dato (last modified date)
	 */
	public Date getSistEndretDato() {
		return sistEndretDato;
	}

	/**
	 * Sets sist endret dato (last modified date)
	 * @param sist endret dato (last modified date)
	 */
	public void setSistEndretDato(Date sistEndretDato) {
		this.sistEndretDato = sistEndretDato;
	}

	/**
	 * Gets sist endret av enhet (last modified by unit)
	 * @return sist endret av enhet (last modified by unit)
	 */
	public String getSistEndretEnhet() {
		return sistEndretEnhet;
	}

	/**
	 * Setter fo rsist endret av enhet (last modified by unit)
	 * @param sist endret av enhet (last modified by unit)
	 */
	public void setSistEndretEnhet(String sistEndretEnhet) {
		this.sistEndretEnhet = sistEndretEnhet;
	}

	/**
	 * Gets skannet dato (scanned date)
	 * @return skannet dato (scanned date)
	 */
	public Date getSkannetDato() {
		return skannetDato;
	}

	/**
	 * Sets skannet dato (scanned date)
	 * @param skannet dato (scanned date)
	 */
	public void setSkannetDato(Date skannetDato) {
		this.skannetDato = skannetDato;
	}

	/**
	 * Gets søknad id (application id)
	 * @return søknad id (application id)
	 */
	public Integer getSoknId() {
		return soknId;
	}

	/**
	 * Sets søknad id (application id)
	 * @param søknad id (application id)
	 */
	public void setSoknId(Integer soknId) {
		this.soknId = soknId;
	}

	/**
	 * Gets trygdekontornummer (social security office number)
	 * @return trygdekontornummer (social security office number)
	 */
	public String getTknr() {
		return tknr;
	}

	/**
	 * Sets trygdekontornummer (social security office number)
	 * @param trygdekontornummer (social security office number)
	 */
	public void setTknr(String tknr) {
		this.tknr = tknr;
	}
}
