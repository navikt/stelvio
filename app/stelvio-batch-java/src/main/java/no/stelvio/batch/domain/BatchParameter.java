package no.stelvio.batch.domain;

/**
 * Enumerator for the batch parameter.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public enum BatchParameter {

	/** Work Unit. */
	WORK_UNIT("workUnit");

	/** The parameter. */
	private final String parameterString;

	/**
	 * Sets the string used to identify this property in the database.
	 * 
	 * @param propertyString
	 *            the parameterstring
	 */
	BatchParameter(String propertyString) {
		this.parameterString = propertyString;
	}

	/**
	 * Returns the string used to identify this property in the database.
	 * 
	 * @return parameterString
	 */
	public String getParameterString() {
		return parameterString;
	}

}
