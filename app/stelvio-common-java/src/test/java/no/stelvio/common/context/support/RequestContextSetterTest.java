package no.stelvio.common.context.support;

import static org.junit.Assert.assertEquals;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

import org.junit.Test;

public class RequestContextSetterTest {
	@Test
	public void setRequestContext() {
		RequestContext ctx = new SimpleRequestContext("id","module","process","transaction");
		RequestContextSetter.setRequestContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, RequestContextHolder.currentRequestContext());
	}

	@Test(expected = IllegalStateException.class)
	public void resetRequestContext() {
		RequestContextSetter.resetRequestContext();
		RequestContextHolder.currentRequestContext();
	}
}
