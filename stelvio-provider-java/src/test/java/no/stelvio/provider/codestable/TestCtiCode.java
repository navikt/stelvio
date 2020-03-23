package no.stelvio.provider.codestable;

/**
 * Enum to use for testing the codes table API.
 *
 */
public enum TestCtiCode {
	/** Testcode EXISTS_1. */
	EXISTS_1,
	/** Testcode EXISTS_2. */
	EXISTS_2,
	/** Testcode EXISTS_3. */
	EXISTS_3,
	/** Testcode EXISTS_4. */
	EXISTS_4,
	/** Testcode EXISTS_OVERLAP. */
	EXISTS_OVERLAP,
	/** Testcode EXISTS_TIME_1. */
	EXISTS_TIME_1,
	/** Testcode EXISTS_TIME_2. */
	EXISTS_TIME_2,	
	/** Testcode EXISTS_NOT. */
	EXISTS_NOT,
	/** Testcode EXISTS_DECODE. */
	EMPTY_DECODE,
	/** Testcode OVERLAP_WITH_NULL. */
	OVERLAP_WITH_NULL

	// do someting like this to support lookup values as numbers or other illegal java variable names
	// public TestCtiCode valueOf(String s) {
	//    return Enum.valueOf(getClass(), s);
	//}
}

