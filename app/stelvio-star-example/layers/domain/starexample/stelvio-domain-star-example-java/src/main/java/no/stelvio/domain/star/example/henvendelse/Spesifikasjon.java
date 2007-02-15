package no.stelvio.domain.star.example.henvendelse;

/**
 * Spesifikasjon is part of the criteria for getting {@link HenvendelseStatistikk}.  It is populated by a radio button choice
 * used in the search window.
 * 
 * @author personff564022aedd
 */
public enum Spesifikasjon {

	ANTALL("antall"),
	GRUNN("grunn"),
	KANAL("kanal"),
	TYPE("type");
	
	private String value;
	
	private Spesifikasjon(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}	
}
