package no.stelvio.presentation.jsf.codestable;

/**
 * Generated class, do not edit.
 */
public enum KravStatusCode {
	/** Avbrutt behandling. */
	AVBRUTT,
	/** Beregnet. */
	BEREGNET,
	/** Ferdig behandlet. */
	FERDIG,
	/** Klar til attestering. */
	KLAR_TIL_ATT,
	/** Til behandling. */
	TIL_BEHANDLING,
	/** Venter p&aring; AFP-ordning. */
	VENTER_AFP,
	/** Venter p&aring; andre. */
	VENTER_ANDRE,
	/** Venter p&aring; bruker. */
	VENTER_BRUKER,
	/** Venter p&aring; saksbehandling. */
	VENTER_SAKSBEH,
	/** Vilk&aring;rspr&oslash;vd. */
	VILKARSPROVD;

	/**
	 * Overload the default equals method as equals does not work for enums after they are serialized (e.g., across EJB calls).
	 * Note that the equals(Object) method is final, so we can't override it.
	 * 
	 * @param obj
	 *            enumclass to compare with
	 * @return true if the objects are equal (i.e., their ordinal is equal)
	 */
	public boolean equals(Enum obj) {
		return (obj != null) && (obj instanceof KravStatusCode) && (obj.ordinal() == this.ordinal());
	}

}
