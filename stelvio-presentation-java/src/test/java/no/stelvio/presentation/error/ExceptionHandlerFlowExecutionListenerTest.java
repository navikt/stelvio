package no.stelvio.presentation.error;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockFlowSession;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Test to test the ExceptionHandlerFlowExecutionListener class.
 * 
 * @version $Id$
 * 
 */
public class ExceptionHandlerFlowExecutionListenerTest {

	private ExceptionHandlerFlowExecutionListener exceptionHandlerFlowExecutionListener;

	private RequestContext context;

	private FlowSession flowSession;

	private FlowExecutionExceptionHandler exceptionHandler = new ErrorPageExceptionHandler();

	/**
	 * Sets up the mock objects needed to run the test.
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Before
	public void setUp() throws Exception {
		exceptionHandlerFlowExecutionListener = new ExceptionHandlerFlowExecutionListener();

		context = new MockRequestContext();
		flowSession = new MockFlowSession();

		exceptionHandlerFlowExecutionListener.setExceptionHandler(exceptionHandler);
	}

	/**
	 * Clean up.
	 * 
	 * @throws Exception
	 *             exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link no.stelvio.presentation.error.ExceptionHandlerFlowExecutionListener#sessionCreated(org.springframework.webflow.execution.RequestContext, org.springframework.webflow.execution.FlowSession)}
	 * . Testing that the exceptionHandler has been set on flow.
	 */
	@Test
	public void testSessionCreatedRequestContextFlowSession() {
		exceptionHandlerFlowExecutionListener.sessionStarted(context, flowSession);

		FlowDefinition flowDef = context.getFlowExecutionContext().getActiveSession().getDefinition();
		Flow flow = (Flow) flowDef;
		assertTrue(flow.getExceptionHandlerSet().contains(exceptionHandler));
	}

}
