package no.nav.presentation.pensjon.saksbehandling.stelvio.enums;

/**
 * @author person4f9bc5bd17cc
 */
public enum AddressHint {
	STARTER_MED("Starter med"),
	INNEHOLDER("Inneholder");
	
	private String value;
	
	private AddressHint(String value) {
		this.value = value;
	}
	
	/**
	 * TODO: Document me
	 * @return the value
	 */
	public String value() {
		return value;
	}
}