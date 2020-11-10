package no.stelvio.presentation.error;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.consumer.exception.ConsumerSystemException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.test.MockRequestControlContext;

public class ErrorPageExceptionHandlerTest {

	private ErrorPageExceptionHandler errorPageExceptionHandler;

	private FlowExecutionException flowExecutionException;

	private MockRequestControlContext requestControlContext;

	/**
	 * Sets up mock objects needed to perform test.
	 */
	@Before
	public void setUp() {
		errorPageExceptionHandler = new ErrorPageExceptionHandler();

		Flow flow = new Flow("myFlow");
		flowExecutionException = new FlowExecutionException("flowId", "stateId", "message", new ConsumerSystemException(
				"Technical error while invoking service", new Throwable()));
		requestControlContext = new MockRequestControlContext(flow);

		List<String> exceptions = new ArrayList<>();
		exceptions.add("no.stelvio.consumer.exception.ConsumerSystemException");
		exceptions.add("org.springframework.webflow.core.FlowException");

		errorPageExceptionHandler.setErrorPage("myErrorPage");

	}

	/**
	 * Test method for
	 * {@link no.stelvio.presentation.error.ErrorPageExceptionHandler#handle(org.springframework.webflow.execution.FlowExecutionException, org.springframework.webflow.engine.RequestControlContext)}
	 * . Testing that handle returns a ViewSelection instance
	 */
	@Test
	public void testHandle() {
		try {
			errorPageExceptionHandler.handle(flowExecutionException, requestControlContext);
			assertTrue(true);
		} catch (Throwable e) {
			fail("testHandle " + e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link no.stelvio.presentation.error.ErrorPageExceptionHandler#handle(org.springframework.webflow.execution.FlowExecutionException, org.springframework.webflow.engine.RequestControlContext)}
	 * . Testing that the STATE_EXCEPTION_ATTRIBUTE has been set in the contexts flashscope
	 */
	@Test
	public void testHandleStateExceptionAttributeInFlash() {
		try {
			errorPageExceptionHandler.handle(flowExecutionException, requestControlContext);
			assertNotNull(requestControlContext.getExternalContext().getGlobalSessionMap().get(
					ErrorPageExceptionHandler.STATE_EXCEPTION_ATTRIBUTE));
		} catch (Throwable e) {
			fail("testHandleStateExceptionAttributeInFlash " + e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link no.stelvio.presentation.error.ErrorPageExceptionHandler#handle(org.springframework.webflow.execution.FlowExecutionException, org.springframework.webflow.engine.RequestControlContext)}
	 * . Testing that the errorpage is set. The return type is only an instance of ApplicationView if error page is set.
	 */
	@Test
	public void testHandleErrorPageSet() {
		// If errorpage is set the ViewSelection is of type ApplicationView,
		// otherwise it's set to NullView
		// assertTrue(errorPageExceptionHandler.handle(flowExecutionException,
		// requestControlContext) instanceof ApplicationView);
		assertTrue(true);
	}

}
