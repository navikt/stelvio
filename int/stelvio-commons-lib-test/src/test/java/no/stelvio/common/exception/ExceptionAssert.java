package no.stelvio.common.exception;

public class ExceptionAssert extends org.junit.Assert {
	public static void assertEquals(Throwable expected, Throwable actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, Throwable expected, Throwable actual) {
		if (expected == null && actual == null) {
			return;
		}
		if ((expected != null && actual == null) || (expected == null && actual != null)) {
			failNotEquals(message, expected, actual);
		}

		// expected and actual are both non-null
		if (!expected.getClass().equals(actual.getClass())) {
			failNotEquals(message, expected, actual);
		}
		assertEquals(expected.getMessage(), actual.getMessage());
		assertArrayEquals(expected.getStackTrace(), actual.getStackTrace());

		assertEquals(expected.getCause(), actual.getCause());
	}

	private static void failNotEquals(String message, Object expected, Object actual) {
		junit.framework.Assert.failNotEquals(message, expected, actual);
	}
}
