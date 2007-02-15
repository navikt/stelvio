package no.nav.domain.pensjon.henvendelse;

/**
 * Criteria for finding statistics for Henvendelse.
 * 
 * @author personff564022aedd
 */
public class HenvendelseStatistikkCriteria {

	// Todo: må ha med enhet id  - det er denne statistikk hentes opp for
	
	// kan legge ved Locale også, så kan server bestemme språk på labels...(?)
	
	public static final String TIDSPERIODE_SISTE_5_DAGER = "Siste 5 dager";
	public static final String TIDSPERIODE_SISTE_4_UKER  = "Siste 4 uker";
	public static final String FAGOMRADE_PENSJON = "Pensjon";
	public static final String FAGOMRADE_BIDRAG  = "Bidrag";
	public static final String FAGOMRADE_ALLE    = "Alle";

	private String enhetId;
	private Tidsperiode tidsperiode;
	private Fagomrade fagomrade;
	private Spesifikasjon spesifikasjon;
	
	/**
	 * 
	 * @param enhetId
	 * @param tidsperiode
	 * @param fagomrade
	 * @param spesifikasjon
	 */
	public HenvendelseStatistikkCriteria(String enhetId, Tidsperiode tidsperiode, Fagomrade fagomrade, Spesifikasjon spesifikasjon) {
		if (enhetId == null || tidsperiode == null || fagomrade == null || spesifikasjon == null) {
			throw new IllegalArgumentException("One or more parameters null.");
		}

		if (fagomrade == Fagomrade.ALLE && spesifikasjon != Spesifikasjon.ANTALL) {
			throw new IllegalArgumentException("Spesifikasjon must be ANTALL when fagområde is set to ALLE.");
		}

		this.tidsperiode = tidsperiode;
		this.fagomrade = fagomrade;
		this.spesifikasjon = spesifikasjon;
	}

	/**
	 * @return identifier for enhet saksbehandler is employed in
	 */
	public String getEnhetId() {
		return enhetId;
	}

	/**
	 * @return fagomrade (Pensjon, Bidrag, Alle) is a search restriction
	 */
	public Fagomrade getFagomrade() {
		return fagomrade;
	}

	/**
	 * @return {@link Spesifikasjon} specifies the statistics returned
	 */
	public Spesifikasjon getSpesifikasjon() {
		return spesifikasjon;
	}

	/**
	 * @return tidsperiode (Siste 5 dager, Siste 4 uker) restricts the search to the last five days or last four weeks from todays date
	 */
	public Tidsperiode getTidsperiode() {
		return tidsperiode;
	}
	
}
