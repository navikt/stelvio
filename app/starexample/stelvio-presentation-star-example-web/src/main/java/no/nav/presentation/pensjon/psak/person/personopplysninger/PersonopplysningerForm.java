package no.nav.presentation.pensjon.psak.person.personopplysninger;

import java.util.List;

import javax.faces.model.SelectItem;

/**
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class PersonopplysningerForm {
	
	//Brukerinfo
	private String fnr;
	
	//Personopplysninger
	private List<SelectItem> sparkListe;
	private String valgtSprak;
		
	//Kontoopplysninger
	private List<SelectItem> utbetalTilListe;
	private String valgtUtbetalTil;
	private String kontonummer;
	private String bankkode;
	private String banknavn;
	private String bankadresseLinje1;
	private String bankadresseLinje2;
	private String bankadresseLinje3;
	private String bicSwiftKode;
	private List<SelectItem> bankLandListe;
	private String valgtBankLand;
	private List<SelectItem> valutaListe;
	private String valgtValuta;
	
	//Tilleggsadresse
	private String tilleggsadresseLinje1;
	private String tilleggsadresseLinje2;
	private String tilleggsadressePostNr;
	
	//Utenlandsadresse
	private String utenlandsadresseLinje1;
	private String utenlandsadresseLinje2;
	private String utenlandsadresseLinje3;
	private List<SelectItem> utenlandsadresseLandListe;
	private String valgtUtenlandsadresseLand;
	
	
	public PersonopplysningerForm(){
		this.kontonummer = "";
		
		this.tilleggsadresseLinje1 = "";
		this.tilleggsadresseLinje2 = "";
		this.tilleggsadressePostNr = "";
		
		this.utenlandsadresseLinje1 = "";
		this.utenlandsadresseLinje2 = "";
		this.utenlandsadresseLinje3 = "";

	}

	public PersonopplysningerForm(String fnr, List<SelectItem> sparkListe, String valgtSprak, List<SelectItem> utbetalTilListe, String valgtUtbetalTil, String kontonummer, String bankkode, String banknavn, String bankadresseLinje1, String bankadresseLinje2, String bankadresseLinje3, String bicSwiftKode, List<SelectItem> bankLandListe, String valgtBankLand, List<SelectItem> valutaListe, String valgtValuta, String tilleggsadresseLinje1, String tilleggsadresseLinje2, String tilleggsadressePostNr, String utenlandsadresseLinje1, String utenlandsadresseLinje2, String utenlandsadresseLinje3, List<SelectItem> utenlandsadresseLandListe, String valgtUtenlandsadresseLand) {
		super();
		this.fnr = fnr;
		this.sparkListe = sparkListe;
		this.valgtSprak = valgtSprak;
		this.utbetalTilListe = utbetalTilListe;
		this.valgtUtbetalTil = valgtUtbetalTil;
		this.kontonummer = kontonummer;
		this.bankkode = bankkode;
		this.banknavn = banknavn;
		this.bankadresseLinje1 = bankadresseLinje1;
		this.bankadresseLinje2 = bankadresseLinje2;
		this.bankadresseLinje3 = bankadresseLinje3;
		this.bicSwiftKode = bicSwiftKode;
		this.bankLandListe = bankLandListe;
		this.valgtBankLand = valgtBankLand;
		this.valutaListe = valutaListe;
		this.valgtValuta = valgtValuta;
		this.tilleggsadresseLinje1 = tilleggsadresseLinje1;
		this.tilleggsadresseLinje2 = tilleggsadresseLinje2;
		this.tilleggsadressePostNr = tilleggsadressePostNr;
		this.utenlandsadresseLinje1 = utenlandsadresseLinje1;
		this.utenlandsadresseLinje2 = utenlandsadresseLinje2;
		this.utenlandsadresseLinje3 = utenlandsadresseLinje3;
		this.utenlandsadresseLandListe = utenlandsadresseLandListe;
		this.valgtUtenlandsadresseLand = valgtUtenlandsadresseLand;
	}

	public String getBankadresseLinje1() {
		return bankadresseLinje1;
	}

	public void setBankadresseLinje1(String bankadresseLinje1) {
		this.bankadresseLinje1 = bankadresseLinje1;
	}

	public String getBankadresseLinje2() {
		return bankadresseLinje2;
	}

	public void setBankadresseLinje2(String bankadresseLinje2) {
		this.bankadresseLinje2 = bankadresseLinje2;
	}

	public String getBankadresseLinje3() {
		return bankadresseLinje3;
	}

	public void setBankadresseLinje3(String bankadresseLinje3) {
		this.bankadresseLinje3 = bankadresseLinje3;
	}

	public String getBankkode() {
		return bankkode;
	}

	public void setBankkode(String bankkode) {
		this.bankkode = bankkode;
	}

	public List<SelectItem> getBankLandListe() {
		return bankLandListe;
	}

	public void setBankLandListe(List<SelectItem> bankLandListe) {
		this.bankLandListe = bankLandListe;
	}

	public String getBanknavn() {
		return banknavn;
	}

	public void setBanknavn(String banknavn) {
		this.banknavn = banknavn;
	}

	public String getBicSwiftKode() {
		return bicSwiftKode;
	}

	public void setBicSwiftKode(String bicSwiftKode) {
		this.bicSwiftKode = bicSwiftKode;
	}

	public String getFnr() {
		return fnr;
	}

	public void setFnr(String fnr) {
		this.fnr = fnr;
	}

	public String getKontonummer() {
		return kontonummer;
	}

	public void setKontonummer(String kontonummer) {
		this.kontonummer = kontonummer;
	}

	public List<SelectItem> getSparkListe() {
		return sparkListe;
	}

	public void setSparkListe(List<SelectItem> sparkListe) {
		this.sparkListe = sparkListe;
	}

	public String getTilleggsadresseLinje1() {
		return tilleggsadresseLinje1;
	}

	public void setTilleggsadresseLinje1(String tilleggsadresseLinje1) {
		this.tilleggsadresseLinje1 = tilleggsadresseLinje1;
	}

	public String getTilleggsadresseLinje2() {
		return tilleggsadresseLinje2;
	}

	public void setTilleggsadresseLinje2(String tilleggsadresseLinje2) {
		this.tilleggsadresseLinje2 = tilleggsadresseLinje2;
	}

	public String getTilleggsadressePostNr() {
		return tilleggsadressePostNr;
	}

	public void setTilleggsadressePostNr(String tilleggsadressePostNr) {
		this.tilleggsadressePostNr = tilleggsadressePostNr;
	}

	public List<SelectItem> getUtbetalTilListe() {
		return utbetalTilListe;
	}

	public void setUtbetalTilListe(List<SelectItem> utbetalTilListe) {
		this.utbetalTilListe = utbetalTilListe;
	}

	public List<SelectItem> getUtenlandsadresseLandListe() {
		return utenlandsadresseLandListe;
	}

	public void setUtenlandsadresseLandListe(
			List<SelectItem> utenlandsadresseLandListe) {
		this.utenlandsadresseLandListe = utenlandsadresseLandListe;
	}

	public String getUtenlandsadresseLinje1() {
		return utenlandsadresseLinje1;
	}

	public void setUtenlandsadresseLinje1(String utenlandsadresseLinje1) {
		this.utenlandsadresseLinje1 = utenlandsadresseLinje1;
	}

	public String getUtenlandsadresseLinje2() {
		return utenlandsadresseLinje2;
	}

	public void setUtenlandsadresseLinje2(String utenlandsadresseLinje2) {
		this.utenlandsadresseLinje2 = utenlandsadresseLinje2;
	}

	public String getUtenlandsadresseLinje3() {
		return utenlandsadresseLinje3;
	}

	public void setUtenlandsadresseLinje3(String utenlandsadresseLinje3) {
		this.utenlandsadresseLinje3 = utenlandsadresseLinje3;
	}

	public String getValgtBankLand() {
		return valgtBankLand;
	}

	public void setValgtBankLand(String valgtBankLand) {
		this.valgtBankLand = valgtBankLand;
	}

	public String getValgtSprak() {
		return valgtSprak;
	}

	public void setValgtSprak(String valgtSprak) {
		this.valgtSprak = valgtSprak;
	}

	public String getValgtUtbetalTil() {
		return valgtUtbetalTil;
	}

	public void setValgtUtbetalTil(String valgtUtbetalTil) {
		this.valgtUtbetalTil = valgtUtbetalTil;
	}

	public String getValgtUtenlandsadresseLand() {
		return valgtUtenlandsadresseLand;
	}

	public void setValgtUtenlandsadresseLand(String valgtUtenlandsadresseLand) {
		this.valgtUtenlandsadresseLand = valgtUtenlandsadresseLand;
	}

	public String getValgtValuta() {
		return valgtValuta;
	}

	public void setValgtValuta(String valgtValuta) {
		this.valgtValuta = valgtValuta;
	}

	public List<SelectItem> getValutaListe() {
		return valutaListe;
	}

	public void setValutaListe(List<SelectItem> valutaListe) {
		this.valutaListe = valutaListe;
	}
}