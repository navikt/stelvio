package no.stelvio.common.error;

import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for NewExceptionFramework.
 * 
 * @author MA
 *
 */
public class NewExceptionFrameworkTest {

	/**
	 * Test errorCodeIsSetInConstructor.
	 */
	@Test
	public void errorCodeIsSetInConstructor() {

		ErrorCode code = NewExceptionFrameworkErrorCode.CODE_1;
		String message = "A brand new message";

		NewExceptionFrameworkSystemUnrecoverableExcetpion ex = new NewExceptionFrameworkSystemUnrecoverableExcetpion(message,
				code);

		assertThat(ex.getMessage(), equalTo(message));
		assertThat(ex.getErrorCode(), equalTo(code));

	}

	/**
	 * Test messageIsSetInContructor.
	 */
	@Test
	public void messageIsSetInContructor() {
		String message = "A brand new message";

		NewExceptionFrameworkSystemUnrecoverableExcetpion ex = new NewExceptionFrameworkSystemUnrecoverableExcetpion(message);

		assertThat(ex.getMessage(), equalTo(message));
	}

	/**
	 * A NewExceptionFrameworkSystemUnrecoverableExcetpion for test.
	 */
	@SuppressWarnings("serial")
	class NewExceptionFrameworkSystemUnrecoverableExcetpion extends SystemUnrecoverableException {

		/**
		 * Creates a new instance of NewExceptionFrameworkSystemUnrecoverableExcetpion.
		 *
		 * @param message message
		 * @param errorCode error code
		 * @param cause cause
		 */
		public NewExceptionFrameworkSystemUnrecoverableExcetpion(String message, ErrorCode errorCode, Throwable cause) {
			super(message, errorCode, cause);
		}

		/**
		 * Creates a new instance of NewExceptionFrameworkSystemUnrecoverableExcetpion.
		 *
		 * @param message message
		 * @param errorCode error code
		 */
		public NewExceptionFrameworkSystemUnrecoverableExcetpion(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}

		/**
		 * Creates a new instance of NewExceptionFrameworkSystemUnrecoverableExcetpion.
		 *
		 * @param message message
		 * @param cause cause
		 */
		public NewExceptionFrameworkSystemUnrecoverableExcetpion(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Creates a new instance of NewExceptionFrameworkSystemUnrecoverableExcetpion.
		 *
		 * @param message message
		 */
		public NewExceptionFrameworkSystemUnrecoverableExcetpion(String message) {
			super(message);
		}

	}

	enum NewExceptionFrameworkErrorCode implements ErrorCode {
		CODE_1, CODE_2;

		public String getErrorCode() {
			return this.name();
		}

	}

}
