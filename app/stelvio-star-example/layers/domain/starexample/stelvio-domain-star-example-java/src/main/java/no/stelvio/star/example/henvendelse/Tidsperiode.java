package no.stelvio.star.example.henvendelse;

/**
 * Tidsperiode is part of the criteria for getting {@link HenvendelseStatistikk}.  It is populated by a select menu
 * in the search window.
 * 
 * @author personff564022aedd
 */
public enum Tidsperiode {

	SISTE_5_DAGER("Siste 5 dager"),
	SISTE_4_UKER("Siste 4 uker"),
	IKKE_SATT("Velg tidsperiode:");
	
	private String value;
	
	private Tidsperiode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
