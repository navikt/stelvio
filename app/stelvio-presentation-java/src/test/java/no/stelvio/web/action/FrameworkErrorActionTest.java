package no.stelvio.web.action;

import org.apache.struts.Globals;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.ApplicationException;
import no.stelvio.common.error.SystemException;

/**
 * Unit test for {@link FrameworkErrorAction}.
 * 
 * @author person356941106810, Accenture
 */
public class FrameworkErrorActionTest {

	public void testNoErrorsOnRequestShouldSetDefaultForm() {
		actionPerform();
		verifyState("Ukjent", "Ukjent", "En feil har oppstått.", "processId", "screenId", "transactionId", "userId", "Ukjent");
	}

	public void testNotHandledErrorsOnRequestShouldFillInForm() {
		getRequest().setAttribute(Globals.EXCEPTION_KEY, new ApplicationException());
		actionPerform();
		// Cannot check errorId when it is generated from <code>SequenceNumberGenerator</code>
		// Cannot check stacktrace when it is a real stacktrace
		verifyState("0", null, "0", "processId", "screenId", "transactionId", "userId", null);
	}

	public void testHandledErrorsOnRequestShouldSetConfiguredExceptionTrue() {
		getRequest().setAttribute(Globals.EXCEPTION_KEY, new SystemException());
		actionPerform();
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
		final FrameworkErrorForm form = (FrameworkErrorForm) getActionForm();
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

		verifyInputTilesForward("side.page1");
	}

	protected void setUp() throws Exception {
		super.setUp();
		setRequestPathInfo("/testFrameworkErrorAction");

		RequestContext.setProcessId("processId");
		RequestContext.setScreenId("screenId");
		RequestContext.setTransactionId("transactionId");
		RequestContext.setUserId("userId");
	}
}
