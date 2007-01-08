package no.nav.domain.pensjon.henvendelse;

/**
 * Fagomrade is part of the criteria for getting {@link HenvendelseStatistikk}.  It is populated by a select menu
 * in the search window.
 * 
 * @author personff564022aedd
 */
public enum Fagomrade {

	PENSJON("Pensjon"),
	BIDRAG("Bidrag"),
	ALLE("Alle");
	
	private String value;
	
	private Fagomrade(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}	
}
