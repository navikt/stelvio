package no.stelvio.domain.sak;

/**
 * Represents a case worker
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class Saksbehandler {
	private String fornavn;
	private String etternavn;
	private String enhet;
	private Long saksbehandlernr;
	
	/**
	 * Gets the enhet
	 * @return the enhet
	 */
	public String getEnhet() {
		return enhet;
	}
	
	/**
	 * Sets enhet
	 * @param enhet the enhet to set
	 */
	public void setEnhet(String enhet) {
		this.enhet = enhet;
	}
	
	/**
	 * Gets etternavn
	 * @return the etternavn
	 */
	public String getEtternavn() {
		return etternavn;
	}
	
	/**
	 * Sets etternavn
	 * @param etternavn the etternavn to set
	 */
	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}
	
	/**
	 * Gets fornavn
	 * @return the fornavn
	 */
	public String getFornavn() {
		return fornavn;
	}
	
	/**
	 * Sets fornavn
	 * @param fornavn the fornavn to set
	 */
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}
	
	/**
	 * Gets saksbehandler
	 * @return the saksbehandlernr
	 */
	public Long getSaksbehandlernr() {
		return saksbehandlernr;
	}
	
	/**
	 * Sets saksbehandler
	 * @param saksbehandlernr the saksbehandlernr to set
	 */
	public void setSaksbehandlernr(Long saksbehandlernr) {
		this.saksbehandlernr = saksbehandlernr;
	}
}