package no.nav.presentation.pensjon.saksbehandling.stelvio.enums;

/**
 * @author person4f9bc5bd17cc
 */
public enum AddressType {
	POSTADRESSE("Postadresse"),
	TILLEGGSADRESSE("Tilleggsadresse"),
	UTENLANDSADRESSE("Utenlandsadresse");
	
	private String value;
	
	private AddressType(String value) {
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