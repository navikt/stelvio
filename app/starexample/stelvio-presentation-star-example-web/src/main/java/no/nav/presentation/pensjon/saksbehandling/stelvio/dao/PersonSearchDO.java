/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.dao;

import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.AddressHint;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.AddressType;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.Gender;


/**
 * @author person4f9bc5bd17cc
 *
 */
public class PersonSearchDO {
	private boolean navneSok;
	private boolean bostedsAdresseSok;
	private boolean annenAdresseSok;
	private boolean fodselsdatoSok;
	private boolean fnrSok;
	private boolean norskKontonummerSok;
	private boolean utenlandskKontonummerSok;
	
	private String fornavn = "";
	private String etternavn = "";
	private String adresse = "";
	private String nrFra = "";
	private String nrTil = "";
	private String adresseType = AddressType.POSTADRESSE.value();
	private String adresseHint = AddressHint.STARTER_MED.value();
	private String fodselsDato = "";
	private String fodselsAr = "";
	private String kjonn = Gender.BEGGE.value();
	private String navEnhet = "";
	private String fullFodelsDato = "";
	private String fnr = "";
	private String norskKontonummer = "";
	private String utenlandskKontonummer = "";
	
	/**
	 * @return the norskKontonummer
	 */
	public String getNorskKontonummer() {
		return norskKontonummer;
	}
	
	/**
	 * @param norskKontonummer the norskKontonummer to set
	 */
	public void setNorskKontonummer(String accountNumber) {
		this.norskKontonummer = accountNumber;
	}
	
	/**
	 * @return the utenlandskKontonummer
	 */
	public String getUtenlandskKontonummer() {
		return utenlandskKontonummer;
	}
	
	/**
	 * @param utenlandskKontonummer the utenlandskKontonummer to set
	 */
	public void setUtenlandskKontonummer(String accountNumberForeign) {
		this.utenlandskKontonummer = accountNumberForeign;
	}
	
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}
	
	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String address) {
		this.adresse = address;
	}
	
	/**
	 * @return the nrFra
	 */
	public String getNrFra() {
		return nrFra;
	}
	
	/**
	 * @param nrFra the nrFra to set
	 */
	public void setNrFra(String addressNumberFrom) {
		this.nrFra = addressNumberFrom;
	}
	
	/**
	 * @return the nrTil
	 */
	public String getNrTil() {
		return nrTil;
	}
	
	/**
	 * @param nrTil the nrTil to set
	 */
	public void setNrTil(String addressNumberTo) {
		this.nrTil = addressNumberTo;
	}
	
	/**
	 * @return the fodselsDato
	 */
	public String getFodselsDato() {
		return fodselsDato;
	}
	
	/**
	 * @param fodselsDato the fodselsDato to set
	 */
	public void setFodselsDato(String birthDate) {
		this.fodselsDato = birthDate;
	}
	
	/**
	 * @return the fnr
	 */
	public String getFnr() {
		return fnr;
	}
	
	/**
	 * @param fnr the fnr to set
	 */
	public void setFnr(String birthNumber) {
		this.fnr = birthNumber;
	}
	
	/**
	 * @return the fodselsAr
	 */
	public String getFodselsAr() {
		return fodselsAr;
	}
	
	/**
	 * @param fodselsAr the fodselsAr to set
	 */
	public void setFodselsAr(String birthYear) {
		this.fodselsAr = birthYear;
	}
	
	/**
	 * @return the fornavn
	 */
	public String getFornavn() {
		return fornavn;
	}
	
	/**
	 * @param fornavn the fornavn to set
	 */
	public void setFornavn(String firstName) {
		this.fornavn = firstName;
	}
	
	/**
	 * @return the kjonn
	 */
	public String getKjonn() {
		return kjonn;
	}
	
	/**
	 * @param kjonn the kjonn to set
	 */
	public void setKjonn(String gender) {
		this.kjonn = gender;
	}
	
	/**
	 * @return the etternavn
	 */
	public String getEtternavn() {
		return etternavn;
	}
	
	/**
	 * @param etternavn the etternavn to set
	 */
	public void setEtternavn(String lastName) {
		this.etternavn = lastName;
	}
	
	/**
	 * @return the navEnhet
	 */
	public String getNavEnhet() {
		return navEnhet;
	}
	
	/**
	 * @param navEnhet the navEnhet to set
	 */
	public void setNavEnhet(String orgUnit) {
		this.navEnhet = orgUnit;
	}
	
	/**
	 * @return the norskKontonummerSok
	 */
	public boolean isNorskKontonummerSok() {
		return norskKontonummerSok;
	}
	
	/**
	 * @param norskKontonummerSok the norskKontonummerSok to set
	 */
	public void setNorskKontonummerSok(boolean radioAccountNumber) {
		this.norskKontonummerSok = radioAccountNumber;
	}
	
	/**
	 * @return the utenlandskKontonummerSok
	 */
	public boolean isUtenlandskKontonummerSok() {
		return utenlandskKontonummerSok;
	}
	
	/**
	 * @param utenlandskKontonummerSok the utenlandskKontonummerSok to set
	 */
	public void setUtenlandskKontonummerSok(boolean radioAccountNumberForeign) {
		this.utenlandskKontonummerSok = radioAccountNumberForeign;
	}
	
	/**
	 * @return the bostedsAdresseSok
	 */
	public boolean isBostedsAdresseSok() {
		return bostedsAdresseSok;
	}
	
	/**
	 * @param bostedsAdresseSok the bostedsAdresseSok to set
	 */
	public void setBostedsAdresseSok(boolean radioAddress) {
		this.bostedsAdresseSok = radioAddress;
	}
	
	/**
	 * @return the fnrSok
	 */
	public boolean isFnrSok() {
		return fnrSok;
	}
	
	/**
	 * @param fnrSok the fnrSok to set
	 */
	public void setFnrSok(boolean radioBirthNumber) {
		this.fnrSok = radioBirthNumber;
	}
	
	/**
	 * @return the fodselsdatoSok
	 */
	public boolean isFodselsdatoSok() {
		return fodselsdatoSok;
	}
	
	/**
	 * @param fodselsdatoSok the fodselsdatoSok to set
	 */
	public void setFodselsdatoSok(boolean radioDateOfBirth) {
		this.fodselsdatoSok = radioDateOfBirth;
	}
	
	/**
	 * @return the annenAdresseSok
	 */
	public boolean isAnnenAdresseSok() {
		return annenAdresseSok;
	}
	
	/**
	 * @param annenAdresseSok the annenAdresseSok to set
	 */
	public void setAnnenAdresseSok(boolean radioDiffAddress) {
		this.annenAdresseSok = radioDiffAddress;
	}
	
	/**
	 * @return the navneSok
	 */
	public boolean isNavneSok() {
		return navneSok;
	}
	
	/**
	 * @param navneSok the navneSok to set
	 */
	public void setNavneSok(boolean radioName) {
		this.navneSok = radioName;
	}

	/**
	 * @return the adresseType
	 */
	public String getAdresseType() {
		return adresseType;
	}

	/**
	 * @param adresseType the adresseType to set
	 */
	public void setAdresseType(String addressType) {
		this.adresseType = addressType;
	}

	/**
	 * @return the adresseHint
	 */
	public String getAdresseHint() {
		return adresseHint;
	}

	/**
	 * @param adresseHint the adresseHint to set
	 */
	public void setAdresseHint(String addressTypeSearchSpec) {
		this.adresseHint = addressTypeSearchSpec;
	}

	/**
	 * @return the fullFodelsDato
	 */
	public String getFullFodelsDato() {
		return fullFodelsDato;
	}

	/**
	 * @param fullBirthDate thefullBirthDate to set 
	 */
	public void setFullFodelsDato(String fullBirthDate) {
		this.fullFodelsDato = fullBirthDate;
	}
}