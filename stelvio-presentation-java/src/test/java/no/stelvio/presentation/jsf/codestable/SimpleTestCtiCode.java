package no.stelvio.presentation.jsf.codestable;

/**
 * Enum to use for testing the codes table API.
 *
 * @author personf8e9850ed756, Accenture
 */
public enum SimpleTestCtiCode {
	/** Test code EXISTS. */
	EXISTS_1,
	/** Test code EXISTS. */
	EXISTS_2,
	/** Test code EXISTS. */
	EXISTS_3,
	/** Test code EXISTS. */
	EXISTS_4,
	/** Test code EXISTS_OVERLAP. */
	EXISTS_OVERLAP,
	/** Test code EXISTS_TIME. */
	EXISTS_TIME_1,
	/** Test code EXISTS_TIME. */
	EXISTS_TIME_2,	
	/** Test code EXISTS_NOT. */
	EXISTS_NOT,
	/** Test code EMPTY_DECODE. */
	EMPTY_DECODE,
	/** Test code OVERLAP_WITH_NULL. */
	OVERLAP_WITH_NULL,
	/** Test code NUMERIC. */
	NUMERIC_1,
	/** Test code NUMERIC. */
	NUMERIC_2,
	/** Test code NUMERIC. */
	NUMERIC_10;

	// do someting like this to support lookup values as numbers or other illegal java variable names
	// public TestCtiCode valueOf(String s) {
	//    return Enum.valueOf(getClass(), s);
	//}
}

