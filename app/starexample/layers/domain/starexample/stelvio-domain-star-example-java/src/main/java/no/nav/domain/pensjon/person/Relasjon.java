package no.nav.domain.pensjon.person;

import java.io.Serializable;

public class Relasjon implements Serializable{
	
	private String relasjonstype;
	private String fodselsnummer;
	private String diskresjonskode;
	private String fornavn;
	private String mellomnavn;
	private String etternavn;
	private String dodsdato;
	private String adresseStatus;
	
	public Relasjon(){
	}

	public Relasjon(String relasjonstype, String fodselsnummer, String diskresjonskode, String fornavn, String mellomnavn, String etternavn, String dodsdato, String adresseStatus) {
		super();
		this.relasjonstype = relasjonstype;
		this.fodselsnummer = fodselsnummer;
		this.diskresjonskode = diskresjonskode;
		this.fornavn = fornavn;
		this.mellomnavn = mellomnavn;
		this.etternavn = etternavn;
		this.dodsdato = dodsdato;
		this.adresseStatus = adresseStatus;
	}

	public String getAdresseStatus() {
		return adresseStatus;
	}

	public void setAdresseStatus(String adresseStatus) {
		this.adresseStatus = adresseStatus;
	}

	public String getDiskresjonskode() {
		return diskresjonskode;
	}

	public void setDiskresjonskode(String diskresjonskode) {
		this.diskresjonskode = diskresjonskode;
	}

	public String getDodsdato() {
		return dodsdato;
	}

	public void setDodsdato(String dodsdato) {
		this.dodsdato = dodsdato;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}

	public String getFodselsnummer() {
		return fodselsnummer;
	}

	public void setFodselsnummer(String fodselsnummer) {
		this.fodselsnummer = fodselsnummer;
	}

	public String getFornavn() {
		return fornavn;
	}

	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}

	public String getMellomnavn() {
		return mellomnavn;
	}

	public void setMellomnavn(String mellomnavn) {
		this.mellomnavn = mellomnavn;
	}

	public String getRelasjonstype() {
		return relasjonstype;
	}

	public void setRelasjonstype(String relasjonstype) {
		this.relasjonstype = relasjonstype;
	}
}