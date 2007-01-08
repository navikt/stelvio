package no.nav.domain.pensjon.person;

import java.io.Serializable;


public class NorskKonto implements Serializable {
	
	private String kontonummer;

	public NorskKonto(){
	}
	
	public NorskKonto(String kontonummer) {
		super();
		this.kontonummer = kontonummer;
	}

	public String getKontonummer() {
		return kontonummer;
	}

	public void setKontonummer(String kontonummer) {
		this.kontonummer = kontonummer;
	}
}