package no.nav.presentation.pensjon.saksbehandling.form;

import javax.faces.model.SelectItem;

import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.AddressHint;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.AddressType;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.Gender;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.SokePersonOption;

public class SokePersonForm {
	//Input-fields
	//Block: Search fields
	private String valgtSok;
	private String fornavn;
	private String etternavn;
	private String bostedsadresse;
	private String nrFra;
	private String nrTil;
	private String fodselsDato;
	private String fodselsAr;
	private String kjonn1;
	private String kjonn2;
	private String navEnhet1;
	private String navEnhet2;
	private String navEnhet3;
	private String adresseType;
	private String adresseHint;
	private String annenAdresse;
	private String fullFodselsDato;
	private String fnr;
	private String norskKontonummer;
	private String utenlandskKontonummer;
	
	public SokePersonForm() {
		// Initiate default field-values
		reset();
	}
	
	public void reset() {
		setValgtSok(SokePersonOption.NAME.value());
		setFornavn("");
		setEtternavn("");
		setBostedsadresse("");
		setNrFra("");
		setNrTil("");
		setFodselsDato("");
		setFodselsAr("");
		setKjonn1(Gender.BEGGE.value());
		setKjonn2(Gender.BEGGE.value());
		setNavEnhet1("");
		setNavEnhet2("");
		setNavEnhet3("");
		setAdresseType(AddressType.POSTADRESSE.value());
		setAdresseHint(AddressHint.STARTER_MED.value());
		setAnnenAdresse("");
		setFullFodselsDato("");
		setFnr("");
		setNorskKontonummer("");
		setUtenlandskKontonummer("");
	}
	
	/**
	 * TODO: Document me
	 * @return the valgSelectItems
	 */
	public SelectItem[] getValgSelectItems() {
		SelectItem[] items = new SelectItem[SokePersonOption.values().length];
		int i = 0;
		for (SokePersonOption option : SokePersonOption.values()) {
			items[i++] = new SelectItem(option.value(), "");
		}
		return items;
	}
	
	/**
	 * TODO: Document me
	 * @return the kjonnSelectItems
	 */
	public SelectItem[] getKjonnSelectItems() {
		SelectItem[] items = new SelectItem[Gender.values().length];
		int i = 0;
		for (Gender gender : Gender.values()) {
			items[i++] = new SelectItem(gender.value());
		}
		return items;
	}
	
	/**
	 * TODO: Document me
	 * @return the kjonnSelectItems
	 */
	public SelectItem[] getAdresseTypeSelectItems() {
		SelectItem[] items = new SelectItem[AddressType.values().length];
		int i = 0;
		for (AddressType addressType : AddressType.values()) {
			items[i++] = new SelectItem(addressType.value());
		}
		return items;
	}
	
	/**
	 * TODO: Document me
	 * @return the kjonnSelectItems
	 */
	public SelectItem[] getAdresseHintSelectItems() {
		SelectItem[] items = new SelectItem[AddressHint.values().length];
		int i = 0;
		for (AddressHint addressHint : AddressHint.values()) {
			items[i++] = new SelectItem(addressHint.value());
		}
		return items;
	}

	public String getNorskKontonummer() {
		return norskKontonummer;
	}

	public void setNorskKontonummer(String accountNumber) {
		this.norskKontonummer = accountNumber;
	}

	public String getUtenlandskKontonummer() {
		return utenlandskKontonummer;
	}

	public void setUtenlandskKontonummer(String accountNumberForeign) {
		this.utenlandskKontonummer = accountNumberForeign;
	}

	public String getBostedsadresse() {
		return bostedsadresse;
	}

	public void setBostedsadresse(String address) {
		this.bostedsadresse = address;
	}

	public String getNrFra() {
		return nrFra;
	}

	public void setNrFra(String addressNumberFrom) {
		this.nrFra = addressNumberFrom;
	}

	public String getNrTil() {
		return nrTil;
	}

	public void setNrTil(String addressNumberTo) {
		this.nrTil = addressNumberTo;
	}

	public String getFodselsDato() {
		return fodselsDato;
	}

	public void setFodselsDato(String birthDate) {
		this.fodselsDato = birthDate;
	}

	public String getFullFodselsDato() {
		return fullFodselsDato;
	}

	public void setFullFodselsDato(String birthDate1) {
		this.fullFodselsDato = birthDate1;
	}

	public String getFnr() {
		return fnr;
	}

	public void setFnr(String birthNumber) {
		this.fnr = birthNumber;
	}

	public String getFodselsAr() {
		return fodselsAr;
	}

	public void setFodselsAr(String birthYear) {
		this.fodselsAr = birthYear;
	}

	public String getFornavn() {
		return fornavn;
	}

	public void setFornavn(String firstName) {
		this.fornavn = firstName;
	}

	public String getKjonn1() {
		return kjonn1;
	}

	public void setKjonn1(String gender1) {
		this.kjonn1 = gender1;
	}

	public String getKjonn2() {
		return kjonn2;
	}

	public void setKjonn2(String gender2) {
		this.kjonn2 = gender2;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public void setEtternavn(String lastName) {
		this.etternavn = lastName;
	}

	public String getNavEnhet1() {
		return navEnhet1;
	}

	public void setNavEnhet1(String orgUnit) {
		this.navEnhet1 = orgUnit;
	}

	public String getNavEnhet2() {
		return navEnhet2;
	}

	public void setNavEnhet2(String orgUnit1) {
		this.navEnhet2 = orgUnit1;
	}

	public String getAdresseType() {
		return adresseType;
	}

	public void setAdresseType(String diffAddressType) {
		this.adresseType = diffAddressType;
	}

	public String getAdresseHint() {
		return adresseHint;
	}

	public void setAdresseHint(String diffAddressTypeSearchSpec) {
		this.adresseHint = diffAddressTypeSearchSpec;
	}

	public String getAnnenAdresse() {
		return annenAdresse;
	}

	public void setAnnenAdresse(String diffAddress) {
		this.annenAdresse = diffAddress;
	}

	public String getValgtSok() {
		return valgtSok;
	}

	public void setValgtSok(String selectedOption) {
		this.valgtSok = selectedOption;
	}

	public String getNavEnhet3() {
		return navEnhet3;
	}

	public void setNavEnhet3(String orgUnit2) {
		this.navEnhet3 = orgUnit2;
	}
}