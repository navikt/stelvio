/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.enums;

/**
 * @author person4f9bc5bd17cc
 */
public enum SokePersonOption {
	NAME("nameOption"),
	ADDRESS("addressOption"),
	DIFFERENT_ADDRESS("diffAddressOption"),
	BIRTHDATE("birthdateOption"),
	BIRTHNUMBER("birthnumberOption"),
	ACCOUNT_NO_NORWEGIAN("accountNrOption"),
	ACCOUNT_NO_FOREIGN("accountNrForeginOption");
	
	private String value;
	
	private SokePersonOption(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
}
