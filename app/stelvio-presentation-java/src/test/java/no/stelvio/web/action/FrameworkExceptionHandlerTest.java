package no.stelvio.web.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ExceptionConfig;

import no.stelvio.common.error.ApplicationException;
import servletunit.struts.MockStrutsTestCase;

/**
 * 
 * @author person356941106810, Accenture
 */
public class FrameworkExceptionHandlerTest extends MockStrutsTestCase {

	/**
	 * Constructor for FrameworkExceptionHandlerTest.
	 * @param arg0
	 */
	public FrameworkExceptionHandlerTest(String arg0) {
		super(arg0);
	}

	/*
	 * Test for ActionForward execute(Exception, ExceptionConfig, ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public void testExecuteExceptionExceptionConfigActionMappingActionFormHttpServletRequestHttpServletResponse() {
		try {
			FrameworkExceptionHandler handler = new FrameworkExceptionHandler();
			ApplicationException ex = new ApplicationException();
			ExceptionConfig ec = new ExceptionConfig();
			ec.setPath("/path");
			
			ActionForward af = handler.execute(ex, ec, null, null, getRequest(), getResponse());
			assertEquals("Test 1: forward path not the same", "/path", af.getPath());
		} catch (Throwable t) {
			t.printStackTrace();
			fail("Unexpected exception:" + t.getMessage());
		}
	}

	
}
