package no.nav.presentation.pensjon.saksbehandling.stelvio.enums;

public enum Gender {
	MANN("Mann"),
	KVINNE("Kvinne"),
	BEGGE("Begge");
	
	private String value;
	
	private Gender(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
}