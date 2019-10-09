package no.stelvio.common.codestable;

/**
 * Enum to use for testing the codes table API.
 *
 * @author personf8e9850ed756, Accenture
 */
public enum TestCtiCode {
	/** exists 1. */
	EXISTS_1,
	/** exists 2. */
	EXISTS_2,
	/** exists 3. */
	EXISTS_3,
	/** exists 4. */
	EXISTS_4,
	/** exists with overlap. */
	EXISTS_OVERLAP,
	/** exists time 1. */
	EXISTS_TIME_1,
	/** exists time 2. */
	EXISTS_TIME_2,	
	/** exists not. */
	EXISTS_NOT,
	/** empty decode. */
	EMPTY_DECODE,
	/** overlap with null. */
	OVERLAP_WITH_NULL,
	/** numeric 1. */
	NUMERIC_1,
	/** numeric 2. */
	NUMERIC_2,
	/** numeric 10. */
	NUMERIC_10

    // do someting like this to support lookup values as numbers or other illegal java variable names
    // public TestCtiCode valueOf(String s) {
    //    return Enum.valueOf(getClass(), s);
    //}
}

