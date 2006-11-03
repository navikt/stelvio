package no.stelvio.web.util;

import no.stelvio.web.util.RequestUtils;
import junit.framework.TestCase;
import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;

/**
 * Description
 *
 * @author Jonas Lindholm
 * @version $Revision: 2348 $ $Author: psa2920 $ $Date: 2005-06-21 16:26:50 +0200 (Tue, 21 Jun 2005) $
 */
public class RequestUtilsTest extends TestCase {
	private final ServletContextSimulator context = new ServletContextSimulator();
	private final HttpServletRequestSimulator request = new HttpServletRequestSimulator(context);

	/** Test with two execute request parameters */
	final public void testGetProcessId() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		addRequestParameter("executeWindow1", "executeValue1");
		addRequestParameter("executeWindow2", "executeValue2");

		String processId = RequestUtils.getProcessId(request);

		assertEquals("Window2", processId);
	}

	/** Test for a imagebutton request */
	final public void testGetProcessId2() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		addRequestParameter("executeSok.y", "nada");

		String processId = RequestUtils.getProcessId(request);

		assertEquals("Sok", processId);
	}

	/** Test for a nested button */
	final public void testGetProcessId3() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		addRequestParameter("executeVelg[1]", "nada");

		String processId = RequestUtils.getProcessId(request);

		assertEquals("Velg", processId);
	}

	/** Test default method */
	final public void testGetProcessId4() {
		String processId = RequestUtils.getProcessId(request);

		assertEquals("Load", processId);
	}

	final public void testGetMethodNumber() {
		addRequestParameter("para1", "value1");
		addRequestParameter("para2", "value2");
		//addRequestParameter("executeWindow1", "executeValue1");
		//addRequestParameter("executeWindow2", "executeValue2");
		//addRequestParameter("person[0].executeVelg", "nada");
		addRequestParameter("executeVelg[1]", "nada");

		Integer methodNumber = RequestUtils.getMethodNumber(request);

		assertEquals(new Integer(1), methodNumber);
	}

	final public void testGetMethod() {
		String processId = RequestUtils.getProcessId(request);
		assertEquals(processId, "Load");
	}

	/**
	 * Checks that validate returns false when the method is found in the list of method, true otherwise.
	 */
	public void testValidate() {
		assertTrue("Should be validated", RequestUtils.validate("test1,test2", "test"));
		assertFalse("Should not be validated", RequestUtils.validate("test1,test2", "test2"));
		assertTrue("Should be validated", RequestUtils.validate("test1,\ntest2", "test"));
		assertFalse("Should not be validated", RequestUtils.validate("test1,\ntest2", "test2"));

		assertFalse("Should not be validated", RequestUtils.validate("executeTilbake", "executeLoad"));
		assertFalse("Should not be validated", RequestUtils.validate("executeTilbake", "executeForward"));
	}

	private void addRequestParameter(String key, String value) {
		request.addParameter(key, value);
	}
}
