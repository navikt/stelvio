package no.stelvio.dto.exception;

import junit.framework.TestCase;

/**
 * Test class for UnrevocerableDtoException.
 * 
 *
 */
public class TestUnrevocerableDtoException extends TestCase {

	/**
	 * Test throw and catch of UnrecoverableDtoException.
	 */
	public void testThrowAndCatchException() {

		try {
			throw new DummyUnrecoverableDtoException("testString");
		} catch (DummyUnrecoverableDtoException e) {
			assertEquals("testString", e.getMessage());
		}

	}

	/**
	 * A UnrecoverableDtoException test class.
	 */
	static class DummyUnrecoverableDtoException extends UnrecoverableDtoException {

		private static final long serialVersionUID = 1784302697462639187L;

		/**
		 * Creates a new instance of DummyUnrecoverableDtoException.
		 *
		 * @param dummy1 message
		 */
		public DummyUnrecoverableDtoException(String dummy1) {
			super(dummy1);
		}

		/**
		 * Creates a new instance of DummyUnrecoverableDtoException.
		 *
		 * @param cause cause
		 * @param dummy1 message
		 */
		public DummyUnrecoverableDtoException(Throwable cause, String dummy1) {
			super(dummy1, cause);
		}

		/**
		 * Return a template for message.
		 * 
		 * @return message template
		 */
		protected String messageTemplate() {
			return "Error occured due to dummy1={0} and dummyInt={1}";
		}

	}

}
