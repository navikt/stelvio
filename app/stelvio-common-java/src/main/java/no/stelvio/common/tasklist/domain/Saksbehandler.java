package no.stelvio.common.tasklist.domain;

/**
 * TODO: Document me 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class Saksbehandler {
	private String fornavn;
	private String etternavn;
	private String enhet;
	private Long saksbehandlernr;
	
	/**
	 * @return the enhet
	 */
	public String getEnhet() {
		return enhet;
	}
	
	/**
	 * @param enhet the enhet to set
	 */
	public void setEnhet(String enhet) {
		this.enhet = enhet;
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
	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
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
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}
	
	/**
	 * @return the saksbehandlernr
	 */
	public Long getSaksbehandlernr() {
		return saksbehandlernr;
	}
	
	/**
	 * @param saksbehandlernr the saksbehandlernr to set
	 */
	public void setSaksbehandlernr(Long saksbehandlernr) {
		this.saksbehandlernr = saksbehandlernr;
	}
}