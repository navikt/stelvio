package no.nav.presentation.pensjon.saksbehandling.stelvio.dao;

import java.io.Serializable;
import java.util.List;

public class PersonDO implements Serializable{
	private String enhet = "";
	private String kommentar = "";
	private String fornavn = "";
	private String etternavn = "";
	private String gateAdresse = "";
	private String gateNr = "";
	private String fodselsDato = "";
	private String navEnhet = "";
	private String fodselsNr = "";
	private String kontoNrNorsk = "";
	private String kontoNrUtenland = "";
	private String utenlandsRef = "";
	private Integer fodselsAar = -1;
	private String kjonn = "Mann";
	private String postNr = "";
	private String postSted = "";
	
	List<HenvendelserDO> henvendelser;
	
	public String getEnhet() {
		return enhet;
	}

	public void setEnhet(String enhet) {
		this.enhet = enhet;
	}

	public List<HenvendelserDO> getHenvendelser() {
		return henvendelser;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}

	public Integer getFodselsAar() {
		return fodselsAar;
	}

	public void setFodselsAar(Integer fodselsAar) {
		this.fodselsAar = fodselsAar;
	}

	public String getFodselsDato() {
		return fodselsDato;
	}

	public void setFodselsDato(String fodselsDato) {
		this.fodselsDato = fodselsDato;
	}

	public String getFodselsNr() {
		return fodselsNr;
	}

	public void setFodselsNr(String fodselsNr) {
		this.fodselsNr = fodselsNr;
	}

	public String getFornavn() {
		return fornavn;
	}

	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}

	public String getGateAdresse() {
		return gateAdresse;
	}

	public void setGateAdresse(String gateAdresse) {
		this.gateAdresse = gateAdresse;
	}

	public String getGateNr() {
		return gateNr;
	}

	public void setGateNr(String gateNr) {
		this.gateNr = gateNr;
	}

	public String getKontoNrNorsk() {
		return kontoNrNorsk;
	}

	public void setKontoNrNorsk(String kontoNrNorsk) {
		this.kontoNrNorsk = kontoNrNorsk;
	}

	public String getKontoNrUtenland() {
		return kontoNrUtenland;
	}

	public void setKontoNrUtenland(String kontoNrUtenland) {
		this.kontoNrUtenland = kontoNrUtenland;
	}

	public String getNavEnhet() {
		return navEnhet;
	}

	public void setNavEnhet(String navEnhet) {
		this.navEnhet = navEnhet;
	}

	public String getUtenlandsRef() {
		return utenlandsRef;
	}

	public void setUtenlandsRef(String utenlandsRef) {
		this.utenlandsRef = utenlandsRef;
	}

	public void setHenvendelser(List<HenvendelserDO> henvendelser) {
		this.henvendelser = henvendelser;
	}

	public String getKjonn() {
		return kjonn;
	}

	public void setKjonn(String kjonn) {
		this.kjonn = kjonn;
	}

	public String getPostNr() {
		return postNr;
	}

	public void setPostNr(String postNr) {
		this.postNr = postNr;
	}

	public String getPostSted() {
		return postSted;
	}

	public void setPostSted(String postSted) {
		this.postSted = postSted;
	}
}
