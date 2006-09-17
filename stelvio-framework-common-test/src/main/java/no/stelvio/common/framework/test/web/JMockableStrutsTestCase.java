package no.stelvio.common.framework.test.web;

import java.lang.reflect.Method;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import junit.framework.AssertionFailedError;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import org.jmock.cglib.CGLIBCoreMock;
import org.jmock.core.Constraint;
import org.jmock.core.DynamicMockError;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.Verifiable;
import org.jmock.core.constraint.And;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.constraint.IsCloseTo;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.constraint.IsInstanceOf;
import org.jmock.core.constraint.IsNot;
import org.jmock.core.constraint.IsNull;
import org.jmock.core.constraint.IsSame;
import org.jmock.core.constraint.Or;
import org.jmock.core.constraint.StringContains;
import servletunit.struts.MockStrutsTestCase;

/**
 * Extension of {@link MockStrutsTestCase} for enabling mocking of the classes the Struts Action is using.
 * <p/>
 * <p><a href="http://jmock.codehaus.org">jMock</a> is the mock library that is used, and a lot of helper methods exists
 * to make writing the expectation of the mock easier.
 * <p/>
 * <p>One should instantiate the Struts Action class that is to be tested and initialize it with mocked versions of the
 * classes it will be using. Then the Struts framework must be told that it should use this Action instance instead of
 * making a new instance itself. This is done via a call to the <code>registerActionInstance</code> method.
 * <p/>
 * <p>A usage example:
 * <p/>
 * <pre>
 * public class MainActionTest extends JMockableStrutsTestCase {
 *     private Mock mockDataRetriever;
 *     ...
 *
 *     public void testRetrieveDataFirst() {
 *         mockDetaljer.expects(once()).method("fetch").with(eq(531)).will(returnValue("data"));
 *
 *         setRequestPathInfo("/main");
 *         actionPerform();
 *         verifyInputTilesForward("page.main");
 *         verifyNoActionErrors();
 *         // And the usual testing
 *         assertEquals("...", "...", getRequest().getAttribute("..."));
 *
 *         // Don't really need this as long as all the mocked objects that needs to be verified are either
 *         // - declared as an instance field or
 *         // - created with the helper methods
 *         // If either of these are fulfilled the jMock framework will take care of verifying them
 *         mockDetaljer.verify();
 *     }
 *
 *     public void setUp() throws Exception {
 *         // Must be done to initialize the MockStrutsTestCase framework
 *         super.setUp();
 *
 *         // Create the mocks
 *         mockDataRetriever = mock(DataRetriever.class);
 *         ...
 *         // Create the same Action as defined in struts-config for the specified request path info.
 *         // Otherwise the test will fail. This is after all an integration test for the given Action.
 *         action = new MainAction();
 *         // Initialize the Action with mocked versions of the classes it will be using
 *         action.setDataRetriever((DataRetriever) mockDataRetriever.proxy());
 *         ...
 *         // Let the Struts framework know that this instance should be used so it doesn't make one itself.
 *         registerActionInstance(action);
 *     }
 * }
 * </pre>
 *
 * @author personf8e9850ed756
 * @version $Revision: 2803 $, $Date: 2006-03-01 12:39:42 +0100 (Wed, 01 Mar 2006) $
 * @since 2004.08.17
 */
public class JMockableStrutsTestCase extends MockStrutsTestCase {
	/** Constraint to use in setting up mocked object for saying that it can be anything. */
	public static final Constraint ANYTHING = new IsAnything();
	/** Constraint to use in setting up mocked object for saying that it must be null. */
	public static final Constraint NULL = new IsNull();
	/** Constraint to use in setting up mocked object for saying that it cannot be null. */
	public static final Constraint NOT_NULL = new IsNot(NULL);

	/**
	 * Delegates to jMock's <code>MockObjectTestCase</code> helper methods for enabling easier creation of mock objects.
	 */
	private MockObjectTestCase mockObjectTestCase = new MockObjectTestCase() {
	};
	/**
	 * The helper action servlet to will the Struts framework into using the Struts Action specified by the user.
	 */
	private ActionModifierActionServlet actionModifierActionServlet;

	/**
	 * 
	 */
	public JMockableStrutsTestCase() {
		super();
	}

	/**
	 * @param arg0
	 */
	public JMockableStrutsTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Sets the ActionModifierActionServlet to be the action servlet to use.
	 *
	 * @see ActionModifierActionServlet
	 */
	protected void setUp() throws Exception {
		super.setUp();
		actionModifierActionServlet = new ActionModifierActionServlet();
		setActionServlet(actionModifierActionServlet);
	}

	/**
	 * Checks that the framework is initialized before calling the super class.
	 *
	 * @throws AssertionFailedError if the framework is not initialized correctly.
	 */
	public void actionPerform() throws AssertionFailedError {
		checkInitialized("actionPerform()");
		super.actionPerform();
	}

	/**
	 * Sets the instance of Struts Action to use in the test.
	 *
	 * @param action the instance of Struts Action to use in the test.
	 * @throws AssertionFailedError if the framework is not initialized correctly.
	 */
	protected void registerActionInstance(Action action) {
		checkInitialized("registerActionInstance()");
		actionModifierActionServlet.registerActionInstance(action);
	}

	/**
	 * Checks that the framework is initialized before calling the super class.
	 *
	 * @throws AssertionFailedError if the framework is not initialized correctly.
	 */
	public void setActionForm(ActionForm form) {
		checkInitialized("setActionForm(...)");
		super.setActionForm(form);
	}

	/**
	 * Creates a mock that will throw <code>DynamicMockError</code>s as <code>RuntimeException</code>s and record the
	 * <code>DynamicMockError</code> on the current request.
	 *
	 * @param mockedType the class to mock.
	 * @return a mocked class.
	 */
	public Mock mock(Class mockedType) {
		Mock newMock = new Mock(new WrapDynamicMockErrorInRuntimeExceptionCoreMock(mockedType, request));
		registerToVerify(newMock);
		return newMock;
	}

	/**
	 * Helper method for checking if the instance is initialized.
	 *
	 * @param method which method that should be called after setup().
	 * @throws AssertionFailedError if the instance is not initialized correctly.
	 */
	private void checkInitialized(final String method) {
		if (null == actionModifierActionServlet) {
			throw new AssertionFailedError("setup() should be called before " + method);
		}
	}

	/**
	 * Overrides the TestCase class to allow verifying the mocked objects after running the tests.
	 *
	 * @throws Throwable when something goes wrong.
	 * @see junit.framework.TestCase#runBare()
	 */
	public void runBare() throws Throwable {
		setUp();

		try {
			runTest();
			verify();
		} finally {
			tearDown();
		}
	}

	/**
	 * {@inheritDoc}
	 * </p>
	 * Will also print out the args (arg0, arg1, etc) for the action message when there is action errors present.
	 */
	public void verifyNoActionErrors() {
		verifyNoActionMessages(request, Globals.ERROR_KEY, "error");
	}

	/**
	 * {@inheritDoc}
	 * </p>
	 * Will also print out the args (arg0, arg1, etc) for the action message when there is action messages present.
	 */
	public void verifyNoActionMessages() {
		verifyNoActionMessages(request, Globals.MESSAGE_KEY, "action");
	}

	/**
	 * Helper method for verifying that no action messages is present for the previous run of an <code>actionPerform</code>
	 *
	 * @param request the <code>HttpServletRequest</code> to retrieve the hopefully empty <code>ActionMessages</code>.
	 * @param key the string which is used to lookup the <code>ActionMessages</code> from the <code>HttpServletRequest</code>.
	 * @param messageLabel either "action" or "error".
	 * @see HttpServletRequest
	 * @see ActionMessages
	 */
	private void verifyNoActionMessages(HttpServletRequest request, String key, String messageLabel) {
		final ActionMessages messages = (ActionMessages) request.getAttribute(key);

		if (null != messages) {
			final StringBuffer messageText = new StringBuffer();

			for (Iterator iterProps = messages.properties(); iterProps.hasNext();) {
				final String propertyName = (String) iterProps.next();

				messageText.append(propertyName);
				messageText.append(" with message(s): ");

				for (Iterator iterMsgs = messages.get(propertyName); iterMsgs.hasNext();) {
					final ActionMessage actionMessage = (ActionMessage) iterMsgs.next();

					messageText.append(actionMessage.getKey());

					if (iterMsgs.hasNext()) {
						messageText.append(",");
					}
				}

				messageText.append("; ");
			}

			DynamicMockError error = dynamickMockErrorOnRequest();

			if (null == error) {
				throw new AssertionFailedError(
				        "was expecting no " + messageLabel +
				            " messages, but received messages on the following properties: " + messageText.toString());
			} else {
				throw error;
			}
		}
	}

	/**
	 * If an exception is thrown from super, throws that if a <code>DynamickMockError</code> doesn't exists on the
	 * request, otherwise throws the <code>DynamickMockError</code>.
	 *
	 * @param forwardName
	 * @throws AssertionFailedError
	 */
	public void verifyForward(String forwardName) throws AssertionFailedError {
		try {
			super.verifyForward(forwardName);
		} catch (AssertionFailedError error) {
			throwAssertionFailed(error);
		}
	}

	/**
	 * If an exception is thrown from super, throws that if a <code>DynamickMockError</code> doesn't exists on the
	 * request, otherwise throws the <code>DynamickMockError</code>.
	 *
	 * @param forwardPath
	 * @throws AssertionFailedError
	 */
	public void verifyForwardPath(String forwardPath) throws AssertionFailedError {
		try {
			super.verifyForwardPath(forwardPath);
		} catch (AssertionFailedError error) {
			throwAssertionFailed(error);
		}
	}

	/**
	 * If an exception is thrown from super, throws that if a <code>DynamickMockError</code> doesn't exists on the
	 * request, otherwise throws the <code>DynamickMockError</code>.
	 *
	 * @throws AssertionFailedError
	 */
	public void verifyInputForward() throws AssertionFailedError {
		try {
			super.verifyInputForward();
		} catch (AssertionFailedError error) {
			throwAssertionFailed(error);
		}
	}

	/**
	 * If an exception is thrown from super, throws that if a <code>DynamickMockError</code> doesn't exists on the
	 * request, otherwise throws the <code>DynamickMockError</code>.
	 *
	 * @param errorNames
	 * @throws AssertionFailedError
	 */
	public void verifyActionErrors(String[] errorNames) throws AssertionFailedError {
		try {
			super.verifyActionErrors(errorNames);
		} catch (AssertionFailedError error) {
			throwAssertionFailed(error);
		}
	}

	/**
	 * If an exception is thrown from super, throws that if a <code>DynamickMockError</code> doesn't exists on the
	 * request, otherwise throws the <code>DynamickMockError</code>.
     *
	 * @param messageNames
	 * @throws AssertionFailedError
	 */
	public void verifyActionMessages(String[] messageNames) throws AssertionFailedError {
		try {
			super.verifyActionMessages(messageNames);
		} catch (AssertionFailedError error) {
			throwAssertionFailed(error);
		}
	}

	/**
	 * {@inheritDoc}
	 * </p>
	 * In addition, it will verify
	 * <ul>
	 *   <li>that the forward config exists in struts-config.xml for the given forward name
	 *   <li>that the actual forward name gathered from running the action is the same as the given forward name
	 * </ul>
	 */
	public void verifyTilesForward(String forwardName, String definitionName) {
		if (null == definitionName) {
			throw new AssertionFailedError("definitionName cannot be null");
		}

		try {
			verifyForwardExists(forwardName, request, context);
			verifyForwardName(forwardName, false);
			super.verifyTilesForward(forwardName, definitionName);
		} catch (AssertionFailedError e) {
			throwAssertionFailed(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * </p>
	 * In addition, it will verify
	 * <ul>
	 *   <li>that the action config for the given request path has an input mapping in struts-config.xml
	 *   <li>that the actual forward name gathered from running the action is the same as the given forward name
	 * </ul>
	 */
	public void verifyInputTilesForward(String definitionName) {
		if (null == definitionName) {
			throw new AssertionFailedError("definitionName cannot be null");
		}

		final ActionConfig actionConfig = getActionConfig(request, context);
		final String forwardName = actionConfig.getInput();

		if (null == forwardName) {
			throw new AssertionFailedError("There is no input specified in the action mapping for this action");
		}

		try {
			verifyForwardName(forwardName, true);
			super.verifyInputTilesForward(definitionName);
		} catch (AssertionFailedError e) {
			throwAssertionFailed(e);
		}
	}

	/**
	 * Verifies that the expected forward name is the same as the forward name in the forward config on the request. When
	 * we're dealing with an input mapping, we will use the path property from the forward config; otherwise the name
	 * property will be used.
	 *
	 * @param expectedForwardName the forward name that is expected.
	 * @param isInputMapping whether we're dealing with an input mapping or not.
	 */
	private void verifyForwardName(final String expectedForwardName, boolean isInputMapping) {
		if (null == expectedForwardName) {
			throw new AssertionFailedError("expectedForwardName cannot be null");
		}

		final ForwardConfig actualForward = (ForwardConfig) request.getAttribute(
		        ActionModifierRequestProcessor.ACTION_FORWARD);

		if (null == actualForward) {
			// Validation failed --> forward config will not be set
			if (null != request.getAttribute(Globals.ERROR_KEY)) {
				return;
			}

			throw new AssertionFailedError(
			        "The forward config is not set on the request; check that null is not returned from the execute method");
		}

		String actualForwardName;

		if (isInputMapping) {
			actualForwardName = actualForward.getPath();

			// For an input mapping, the expectedForwardName is not specified by the user, so the text should reflect this
			if (!expectedForwardName.equals(actualForwardName)) {
				throw new AssertionFailedError(
				        "Use 'mapping.getInputForward()' when forwarding to the input specified in the " +
				                     "action mapping and not 'mapping.findForward(\"" + actualForwardName + "\")");
			}
		} else {
			actualForwardName = actualForward.getName();

			if (!expectedForwardName.equals(actualForwardName)) {
				throw new AssertionFailedError(
				        "Expected forward '" + expectedForwardName + "' but got '" + actualForwardName + "'");
			}
		}
	}

	/**
	 * Verify that the forward config exists for the given forward name. This method first searches for the forward in the
	 * supplied action mapping.  If it is not defined there, or if the mapping is not provided, it searches for it
	 * globally.
	 */
	private void verifyForwardExists(String forwardName, HttpServletRequest request,
	                                 ServletContext context) {
		// first, look for forward in actionConfig (if it's defined)
		ActionConfig actionConfig = getActionConfig(request, context);
		ForwardConfig forward = actionConfig == null ? null : actionConfig.findForwardConfig(forwardName);

		// if it's not there, check for global forwards
		if (forward == null) {
			ModuleConfig moduleConfig = getModuleConfig(request, context);
			forward = moduleConfig.findForwardConfig(forwardName);
		}

		if (null == forward) {
			throw new AssertionFailedError("Could not find the forward for " + forwardName + "; check if it is correct");
		}
	}

	/**
	 * Returns the configuration for the given action mapping.
	 */
	private ActionConfig getActionConfig(HttpServletRequest request, ServletContext context) {
		final ModuleConfig moduleConfig = getModuleConfig(request, context);

		if (null == moduleConfig) {
			throw new AssertionFailedError("Could not get the module config from either request nor context; " +
			        "check if struts-config.xml is loaded properly");
		}

		final ActionConfig actionConfig = moduleConfig.findActionConfig(actionPath);

		if (null == actionConfig) {
			throw new AssertionFailedError("Could not find the action config for " + actionPath +
			        "; check if the correct request path info is set and the struts-config.xml is loaded correctly");
		}

		return actionConfig;
	}

	/**
	 * Returns the configuration for the current module.
	 */
	private ModuleConfig getModuleConfig(HttpServletRequest request, ServletContext context) {
		ModuleConfig config = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);

		if (null == config) {
			config = (ModuleConfig) context.getAttribute(Globals.MODULE_KEY);
		}

		return config;
	}

	/**
	 * Helper method that throws an AssertionFailedError if no DynamickMockError exists on request, otherwise, throws
	 * the DynamickMockError.
	 *
	 * @param afError the AssertionFailedError to throw if if no DynamickMockError exists on request.
	 */
	private void throwAssertionFailed(AssertionFailedError afError) throws AssertionFailedError {
		DynamicMockError dmError = dynamickMockErrorOnRequest();
		throw null == dmError ? afError : dmError;
	}

	/**
	 * Retrieves the DynamickMockError on the request.
	 * @return
	 */
	private DynamicMockError dynamickMockErrorOnRequest() {
		return (DynamicMockError) request.getAttribute(DynamicMockError.class.getName());
	}

	//
	// Delegate methods for MockObjectTestCase
	//
	public Stub returnValue(Object o) {
		return mockObjectTestCase.returnValue(o);
	}

	public Stub returnValue(boolean result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(byte result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(char result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(short result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(int result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(long result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(float result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub returnValue(double result) {
		return mockObjectTestCase.returnValue(result);
	}

	public Stub throwException(Throwable throwable) {
		return mockObjectTestCase.throwException(throwable);
	}

	public InvocationMatcher once() {
		return mockObjectTestCase.once();
	}

	public InvocationMatcher atLeastOnce() {
		return mockObjectTestCase.atLeastOnce();
	}

	public InvocationMatcher exactly(int expectedCount) {
		return mockObjectTestCase.exactly(expectedCount);
	}

	public InvocationMatcher never() {
		return mockObjectTestCase.never();
	}

	public InvocationMatcher never(String errorMessage) {
		return mockObjectTestCase.never(errorMessage);
	}

	public Stub onConsecutiveCalls(Stub stub1, Stub stub2) {
		return mockObjectTestCase.onConsecutiveCalls(stub1, stub2);
	}

	public Stub onConsecutiveCalls(Stub stub1, Stub stub2, Stub stub3) {
		return mockObjectTestCase.onConsecutiveCalls(stub1, stub2, stub3);
	}

	public Stub onConsecutiveCalls(Stub stub1, Stub stub2, Stub stub3, Stub stub4) {
		return mockObjectTestCase.onConsecutiveCalls(stub1, stub2, stub3, stub4);
	}

	public IsEqual eq(Object operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(boolean operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(byte operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(short operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(char operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(int operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(long operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(float operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsEqual eq(double operand) {
		return mockObjectTestCase.eq(operand);
	}

	public IsCloseTo eq(double operand, double error) {
		return mockObjectTestCase.eq(operand, error);
	}

	public IsSame same(Object operand) {
		return mockObjectTestCase.same(operand);
	}

	public IsInstanceOf isA(Class operandClass) {
		return mockObjectTestCase.isA(operandClass);
	}

	public StringContains stringContains(String substring) {
		return mockObjectTestCase.stringContains(substring);
	}

	public IsNot not(Constraint c) {
		return mockObjectTestCase.not(c);
	}

	public And and(Constraint left, Constraint right) {
		return mockObjectTestCase.and(left, right);
	}

	public Or or(Constraint left, Constraint right) {
		return mockObjectTestCase.or(left, right);
	}

	public Object newDummy(Class dummyType) {
		return mockObjectTestCase.newDummy(dummyType);
	}

	public Object newDummy(Class dummyType, String name) {
		return mockObjectTestCase.newDummy(dummyType, name);
	}

	public Object newDummy(String name) {
		return mockObjectTestCase.newDummy(name);
	}

	public void registerToVerify(Verifiable verifiable) {
		mockObjectTestCase.registerToVerify(verifiable);
	}

	public void unregisterToVerify(Verifiable verifiable) {
		mockObjectTestCase.unregisterToVerify(verifiable);
	}

	public void verify() {
		DynamicMockError error = dynamickMockErrorOnRequest();

		if (null != error) {
			throw error;
		}

		mockObjectTestCase.verify();
	}

	/**
	 * Catches <code>DynamicMockError</code>s and rethrows them as a runtime exception so StrutsTestCase don't catch
	 * it and then logs it and redirects to error page, resulting in the error from JMock to only show up in the log,
	 * not in the JUnit output. In addition the error will be saved to the current <code>HttpServletRequest</code> to
	 * be retrieved later in our overridden StrutsTestCase verify methods.
	 *
	 * @see DynamicMockError
	 */
    public static class WrapDynamicMockErrorInRuntimeExceptionCoreMock extends CGLIBCoreMock {
		private final HttpServletRequest request;

		public WrapDynamicMockErrorInRuntimeExceptionCoreMock(Class mockedType, HttpServletRequest request) {
			super(mockedType, "WrapDynamicMockErrorInRuntimeExceptionCoreMock");
			this.request = request;
		}

		public Object intercept(Object thisProxy, Method method, Object[] args,
		                        MethodProxy superProxy)
		        throws Throwable {
			try {
				return super.intercept(thisProxy, method, args, superProxy);
			} catch(DynamicMockError error) {
				request.setAttribute(DynamicMockError.class.getName(), error);
				throw new RuntimeException("Error in expectations", error);
			}
		}
	}
}
