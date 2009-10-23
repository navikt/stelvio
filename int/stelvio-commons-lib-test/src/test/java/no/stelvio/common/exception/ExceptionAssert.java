package no.stelvio.common.exception;

import org.junit.internal.ArrayComparisonFailure;
import org.mockito.internal.matchers.Contains;

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

		assertShallowEquals(message, expected, actual);

		assertEquals(message, expected.getCause(), actual.getCause());
	}

	public static void assertStructureEquals(Throwable expected, Throwable actual) {
		assertStructureEquals(null, expected, actual);
	}

	public static void assertStructureEquals(String message, Throwable expected, Throwable actual) {
		if (expected == null && actual == null) {
			return;
		}
		if ((expected != null && actual == null) || (expected == null && actual != null)) {
			failNotEquals(message, expected, actual);
		}

		// expected and actual are both non-null
		if (expected.getClass().equals(actual.getClass())) {
			assertShallowEquals(message, expected, actual);
		} else {
			assertNotNull(actual.getMessage());
			assertThat(actual.getMessage(), new Contains(expected.getClass().getName()));
			if (expected.getMessage() != null) {
				assertThat(actual.getMessage(), new Contains(expected.getMessage()));
			}
			assertArrayEquals(message, expected.getStackTrace(), actual.getStackTrace());
		}

		assertStructureEquals(message, expected.getCause(), actual.getCause());
	}

	public static void assertShallowEquals(String message, Throwable expected, Throwable actual) throws ArrayComparisonFailure {
		if (expected == null && actual == null) {
			return;
		}
		if ((expected != null && actual == null) || (expected == null && actual != null)) {
			failNotEquals(message, expected, actual);
		}

		// expected and actual are both non-null
		assertEquals(message, expected.getClass(), actual.getClass());
		assertEquals(message, expected.getMessage(), actual.getMessage());
		assertArrayEquals(message, expected.getStackTrace(), actual.getStackTrace());
	}

	private static void failNotEquals(String message, Object expected, Object actual) {
		junit.framework.Assert.failNotEquals(message, expected, actual);
	}
}
