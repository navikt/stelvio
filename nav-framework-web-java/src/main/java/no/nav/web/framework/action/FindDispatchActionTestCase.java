package no.nav.web.framework.action;

import javax.servlet.ServletContext;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.Action;
import org.jmock.Mock;
import org.jmock.builder.NameMatchBuilder;
import org.jmock.core.Constraint;
import org.jmock.core.InvocationMatcher;

import no.nav.common.framework.codestable.CodesTableManager;
import no.nav.common.framework.config.Config;
import no.nav.common.framework.service.ServiceDelegate;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.test.web.JMockableStrutsTestCase;
import no.nav.web.framework.constants.Constants;

/**
 * Test case for sub-classes of FindDispatchAction. Enables the mocking of 
 * the business delegate and codes table manager.
 *
 * @author personf8e9850ed756, Accenture
 * @version $Id: FindDispatchActionTestCase.java 2481 2005-09-19 11:56:54Z skb2930 $
 */
public class FindDispatchActionTestCase extends JMockableStrutsTestCase {

	private Mock mockBusinessDelegate;
	private Mock mockCodesTableManager;
	private Mock mockConfig;

	/** Dummy value for Double Submit Token. */
	private static final String DOUBLE_SUBMIT_TOKEN = "DOUBLE_SUBMIT_TOKEN";

	/**
	 * Sets up the double submit token and Mock configuration.
	 *  
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		mockBusinessDelegate = mock(ServiceDelegate.class);
		mockCodesTableManager = mock(CodesTableManager.class);
		mockConfig = mock(Config.class);

		setDoubleSubmitToken();
		setupMockConfig();
	}

	/**
	 * Sets up the mock config to return the mocked business delegate and the mocked codes table manager. Set it into the
	 * ServletContext attributes so the FindDispatchAction can find it.
	 */
	protected void setupMockConfig() {
		mockConfig.stubs().method("getBean").with(eq(Constants.BUSINESS_DELEGATE_BEAN)).will(
			returnValue(mockBusinessDelegate.proxy()));
		mockConfig.stubs().method("getBean").with(eq(Constants.CODES_TABLE_MANAGER_BEAN)).will(
			returnValue(mockCodesTableManager.proxy()));
		context.setAttribute(Config.PRESENTATION_SERVICES, mockConfig.proxy());
	}

	/**
	 * @todo javadoc
	 */
	protected void setupActionServlet(final Action action) {
		final Mock mockActionServlet = mock(ActionServlet.class);
		final Mock mockServletContext = mock(ServletContext.class);

		mockActionServlet.stubs().method("getServletContext").will(returnValue(mockServletContext.proxy()));
		mockServletContext.stubs().method("getAttribute").with(eq(Config.PRESENTATION_SERVICES)).
				will(returnValue(mockConfig.proxy()));
		action.setServlet((ActionServlet) mockActionServlet.proxy());
	}

	/**
	 * Sets the expected number of times a method on the BusinessDelegate instance should be called. Use the returned
	 * value to set the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see ServiceDelegate
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expectsOnBusinessDelegate(InvocationMatcher invocationMatcher) {
		return mockBusinessDelegate.expects(invocationMatcher);
	}

	/**
	 * Sets that a method on BusinessDelegate is called. Use the returned value to set the expected method.
	 *
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see ServiceDelegate
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder stubsBusinessDelegate() {
		return mockBusinessDelegate.stubs();
	}

	/**
	 * Returns the mocked business delegate. Should be used when another class than the action test case need to use
	 * the mock, for example when having a helper class for the action test case.
	 *
	 * @return the mocked business delegate.
	 */
	public Mock getMockBusinessDelegate() {
		return mockBusinessDelegate;
	}

	/**
	 * Sets the expected number of times a method on the CodesTableManager instance should be called. Use the returned
	 * value to set the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see CodesTableManager
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expectsOnCodesTableManager(InvocationMatcher invocationMatcher) {
		return mockCodesTableManager.expects(invocationMatcher);
	}

	/**
	 * Sets that a method on CodesTableManager is called. Use the returned value to set the expected method.
	 *
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see CodesTableManager
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder stubsCodesTableManager() {
		return mockCodesTableManager.stubs();
	}

	/**
	 * Returns the mocked codes table manager. Should be used when another class than the action test case need to use
	 * the mock, for example when having a helper class for the action test case.
	 *
	 * @return the mocked codes table manager.
	 */
	public Mock getMockCodesTableManager() {
		return mockCodesTableManager;
	}

	/**
	 * Sets the expected number of times a method on the Config instance should be called. Use the returned value to set
	 * the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see ServiceDelegate
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expectsOnConfig(InvocationMatcher invocationMatcher) {
		return mockConfig.expects(invocationMatcher);
	}

	/**
	 * Returns the mocked config. Should be used when another class than the action test case need to use
	 * the mock, for example when having a helper class for the action test case.
	 *
	 * @return the mocked config.
	 */
	public Mock getMockConfig() {
		return mockConfig;
	}

	/**
	 * Sets the Double Submit Token so a request wont fail in the framework.
	 */
	public void setDoubleSubmitToken() {
		getSession().setAttribute(Globals.TRANSACTION_TOKEN_KEY, DOUBLE_SUBMIT_TOKEN);
		addRequestParameter(org.apache.struts.taglib.html.Constants.TOKEN_KEY, DOUBLE_SUBMIT_TOKEN);
	}

	/**
	 * Clears the Double Submit Token so a request will fail in the framework.
	 */
	public void clearDoubleSubmitToken() {
		getSession().removeAttribute(Globals.TRANSACTION_TOKEN_KEY);
		addRequestParameter(org.apache.struts.taglib.html.Constants.TOKEN_KEY, "");
	}

	/**
	 * Verifies that all the mocks have their expectations fulfilled.
	 */
	public void verify() {
		super.verify();
		mockBusinessDelegate.verify();
		mockCodesTableManager.verify();
		mockConfig.verify();
	}

	/**
	 * Constraint for checking that the correct service is called.
	 *
	 * @param serviceName the name of the service that should be called.
	 * @return a Constraint that will check that the specified service is called.
	 */
	protected Constraint isService(String serviceName) {
		return new IsService(serviceName);
	}

	/**
	 * Constraint that check that the service to be called is specified in the request. 
	 */
	public static class IsService implements Constraint {
		private String serviceName;

		public IsService(String serviceName) {
			this.serviceName = serviceName;
		}

		public boolean eval(Object o) {
			ServiceRequest request = (ServiceRequest) o;
			return serviceName.equals(request.getServiceName());
		}

		public StringBuffer describeTo(StringBuffer buffer) {
			buffer.append("isService(");

			if (null == serviceName) {
			    buffer.append("null");
			} else {
			    buffer.append("<");
			    buffer.append(serviceName);
			    buffer.append(">");
			}

			buffer.append(")");

			return buffer;
		}
	}
}
