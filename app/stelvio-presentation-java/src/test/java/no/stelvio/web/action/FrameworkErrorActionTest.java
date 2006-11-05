package no.stelvio.web.action;

import junit.framework.TestCase;
import no.stelvio.common.context.RequestContext;

/**
 * Unit test for {@link FrameworkErrorAction}.
 * 
 * @author person356941106810, Accenture
 */
public class FrameworkErrorActionTest extends TestCase {

	public void testNoErrorsOnRequestShouldSetDefaultForm() {
		verifyState("Ukjent", "Ukjent", "En feil har oppstått.", "processId", "screenId", "transactionId", "userId", "Ukjent");
	}

	public void testNotHandledErrorsOnRequestShouldFillInForm() {
//		getRequest().setAttribute(Globals.EXCEPTION_KEY, new ApplicationException());
		// Cannot check errorId when it is generated from <code>SequenceNumberGenerator</code>
		// Cannot check stacktrace when it is a real stacktrace
		verifyState("0", null, "0", "processId", "screenId", "transactionId", "userId", null);
	}

	public void testHandledErrorsOnRequestShouldSetConfiguredExceptionTrue() {
//		getRequest().setAttribute(Globals.EXCEPTION_KEY, new SystemException());
		// Cannot check errorId when it is generated from <code>SequenceNumberGenerator</code>
		// Cannot check stacktrace when it is a real stacktrace
		verifyState("0", null, "", "processId", "screenId", "transactionId", "userId", null);
	}

	/**
	 * Helper method for verifying state.
	 *
	 * @param errorCode
	 * @param errorId
	 * @param errorMessage
	 * @param processId
	 * @param screenId
	 * @param transactionId
	 * @param userId
	 * @param stacktrace
	 */
	private void verifyState(final String errorCode, final String errorId, final String errorMessage, final String processId,
	                         final String screenId, final String transactionId, final String userId, final String stacktrace
	) {
		final FrameworkErrorForm form = null; // TODO: get this from JSF-context or something
		assertEquals("Not correct error code", errorCode, form.getErrorCode());

		// Cannot check errorId when it is generated from <code>SequenceNumberGenerator</code>.
		if (null != errorId) {
			assertEquals("Not correct error id", errorId, form.getErrorId());
		}

		assertEquals("Not correct error message", errorMessage, form.getErrorMessage());
		assertEquals("Not correct process id", processId, form.getProcessId());
		assertEquals("Not correct screen id", screenId, form.getScreenId());
		assertEquals("Not correct transaction id", transactionId, form.getTransactionId());
		assertEquals("Not correct user id", userId, form.getUserId());

		// Cannot check stacktrace when it is generated from <code>SequenceNumberGenerator</code>.
		if (null != stacktrace) {
			assertEquals("Not correct stacktrace", stacktrace, form.getStacktrace());
		}
	}

	protected void setUp() throws Exception {
		super.setUp();

		RequestContext.setProcessId("processId");
		RequestContext.setScreenId("screenId");
		RequestContext.setTransactionId("transactionId");
		RequestContext.setUserId("userId");
	}
}
