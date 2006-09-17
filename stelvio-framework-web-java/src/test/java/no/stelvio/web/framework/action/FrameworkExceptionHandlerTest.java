package no.stelvio.web.framework.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ExceptionConfig;

import servletunit.struts.MockStrutsTestCase;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.ApplicationException;
import no.stelvio.web.framework.action.FrameworkExceptionHandler;

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
			ApplicationException ex = new ApplicationException(FrameworkError.UNSPECIFIED_ERROR);
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
