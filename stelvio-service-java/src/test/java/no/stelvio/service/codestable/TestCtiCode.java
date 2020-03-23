package no.stelvio.service.codestable;

/**
 * Enum to use for testing the codes table API.
 *
 */
public enum TestCtiCode {
	/** Test code for EXISTS_1. */
	EXISTS_1,
	/** Test code for EXISTS_2. */
	EXISTS_2,
	/** Test code for EXISTS_3. */
	EXISTS_3,
	/** Test code for EXISTS_4. */
	EXISTS_4,
	/** Test code for EXISTS_OVERLAP. */
	EXISTS_OVERLAP,
	/** Test code for EXISTS_TIME_1. */
	EXISTS_TIME_1,
	/** Test code for EXISTS_TIME_2. */
	EXISTS_TIME_2,	
	/** Test code for EXISTS_NOT. */
	EXISTS_NOT,
	/** Test code for EMPTY_DECODE. */
	EMPTY_DECODE,
	/** Test code for OVERLAP_WITH_NULL. */
	OVERLAP_WITH_NULL

	// do someting like this to support lookup values as numbers or other illegal java variable names
	// public TestCtiCode valueOf(String s) {
	//    return Enum.valueOf(getClass(), s);
	// }
}

