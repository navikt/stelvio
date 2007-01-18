package no.nav.domain.pensjon.saksbehandling;

public class SaksbehandlerDO {

	String fornavn;
	String etternavn;
	String enhet;
	
	Long saksbehandlernr;
	
	public String getEnhet() {
		return enhet;
	}
	public void setEnhet(String enhet) {
		this.enhet = enhet;
	}
	public String getEtternavn() {
		return etternavn;
	}
	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}
	public String getFornavn() {
		return fornavn;
	}
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}
	public Long getSaksbehandlernr() {
		return saksbehandlernr;
	}
	public void setSaksbehandlernr(Long saksbehandlernr) {
		this.saksbehandlernr = saksbehandlernr;
	}
		
}
