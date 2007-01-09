package no.nav.presentation.pensjon.saksbehandling.form;

import java.util.ArrayList;
import java.util.Date;
import javax.faces.model.SelectItem;

/**
 * 
 * @author personb66fa0b5ff6e
 *
 */
public class HenvendelseForm {
	
	private Long hid;
	
	//Block: 
	private String regAv;
	private String regEnhet;
	private Date regDato;
	
	//Block: Application - information about the application
	private ArrayList<SelectItem> fagomrader;
	private String fagomrade;
	private ArrayList<SelectItem> stonadsTyper;
	private String stonadsType;	
	private ArrayList<SelectItem> henvendelsesTyper;
	private String henvendelsesType;
	private ArrayList<SelectItem> saksnummer;
	private ArrayList<SelectItem> kanaler;
	private String kanal;
	private String tidsbruk;
	private String beskrivelse;
	private Boolean beskrivelseLagret;
	private String oppfolging;
	private String notat;
	private ArrayList<SelectItem> status;
	private String stat;
	
	public HenvendelseForm(){
		this.fagomrade = "";
		this.stonadsType = "";
		this.henvendelsesType = "";
		this.kanal = "";
		this.stat = "";
	}
	
	public HenvendelseForm(String regAv, String regEnhet, Date regDato, String fagomrade, String stonadsType, String henvendelsesType, String kanal, String tidsbruk, String beskrivelse, String notat, String stat) {
		this.regAv = regAv;
		this.regEnhet = regEnhet;
		this.regDato = regDato;
		this.fagomrade = fagomrade;
		this.stonadsType = stonadsType;
		this.henvendelsesType = henvendelsesType;
		this.kanal = kanal;
		this.tidsbruk = tidsbruk;
		this.beskrivelse = beskrivelse;
		this.notat = notat;
		this.stat = stat;
	}

	public String getBeskrivelse() {
		return beskrivelse;
	}

	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}

	public String getFagomrade() {
		return fagomrade;
	}

	public void setFagomrade(String fagomrade) {
		this.fagomrade = fagomrade;
	}

	public ArrayList<SelectItem> getFagomrader() {
		return fagomrader;
	}

	public void setFagomrader(ArrayList<SelectItem> fagomrader) {
		this.fagomrader = fagomrader;
	}

	public String getHenvendelsesType() {
		return henvendelsesType;
	}

	public void setHenvendelsesType(String henvendelsesType) {
		this.henvendelsesType = henvendelsesType;
	}

	public ArrayList<SelectItem> getHenvendelsesTyper() {
		return henvendelsesTyper;
	}

	public void setHenvendelsesTyper(ArrayList<SelectItem> henvendelsesTyper) {
		this.henvendelsesTyper = henvendelsesTyper;
	}

	public String getKanal() {
		return kanal;
	}

	public void setKanal(String kanal) {
		this.kanal = kanal;
	}

	public ArrayList<SelectItem> getKanaler() {
		return kanaler;
	}

	public void setKanaler(ArrayList<SelectItem> kanaler) {
		this.kanaler = kanaler;
	}

	public String getNotat() {
		return notat;
	}

	public void setNotat(String notat) {
		this.notat = notat;
	}

	public String getRegAv() {
		return regAv;
	}

	public void setRegAv(String regAv) {
		this.regAv = regAv;
	}

	public Date getRegDato() {
		return regDato;
	}

	public void setRegDato(Date regDato) {
		this.regDato = regDato;
	}

	public String getRegEnhet() {
		return regEnhet;
	}

	public void setRegEnhet(String regEnhet) {
		this.regEnhet = regEnhet;
	}

	public ArrayList<SelectItem> getSaksnummer() {
		return saksnummer;
	}

	public void setSaksnummer(ArrayList<SelectItem> saksnummer) {
		this.saksnummer = saksnummer;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public ArrayList<SelectItem> getStatus() {
		return status;
	}

	public void setStatus(ArrayList<SelectItem> status) {
		this.status = status;
	}

	public String getStonadsType() {
		return stonadsType;
	}

	public void setStonadsType(String stoandsType) {
		this.stonadsType = stoandsType;
	}

	public ArrayList<SelectItem> getStonadsTyper() {
		return stonadsTyper;
	}

	public void setStonadsTyper(ArrayList<SelectItem> stonadsTyper) {
		this.stonadsTyper = stonadsTyper;
	}

	public String getTidsbruk() {
		return tidsbruk;
	}

	public void setTidsbruk(String tidsbruk) {
		this.tidsbruk = tidsbruk;
	}

	public String getOppfolging() {
		return oppfolging;
	}

	public void setOppfolging(String oppfolging) {
		this.oppfolging = oppfolging;
	}

	public Long getHid() {
		return hid;
	}

	public void setHid(Long hid) {
		this.hid = hid;
	}

	public Boolean getBeskrivelseLagret() {
		return beskrivelseLagret;
	}

	public void setBeskrivelseLagret(Boolean beskrivelseLagret) {
		this.beskrivelseLagret = beskrivelseLagret;
	}
}