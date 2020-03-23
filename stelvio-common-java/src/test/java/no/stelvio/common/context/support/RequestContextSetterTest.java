package no.stelvio.common.context.support;

import static org.junit.Assert.assertEquals;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

import org.junit.Test;

/**
 * Test class for RequestContextSetter.
 * 
 *
 */
public class RequestContextSetterTest {

	/**
	 * Test setRequestContext.
	 */
	@Test
	public void setRequestContext() {
		RequestContext ctx = new SimpleRequestContext("id", "module", "process", "transaction");
		RequestContextSetter.setRequestContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, RequestContextHolder.currentRequestContext());
	}

	/**
	 * Test resetRequestContext.
	 */
	@Test(expected = IllegalStateException.class)
	public void resetRequestContext() {
		RequestContextSetter.resetRequestContext();
		RequestContextHolder.currentRequestContext();
	}
}
