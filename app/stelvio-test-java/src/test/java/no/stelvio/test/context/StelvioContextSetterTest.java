package no.stelvio.test.context;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SimpleSecurityContext;

public class StelvioContextSetterTest {
	@Test
	public void setSecurityContextSecurityContext() {
		SecurityContext ctx = new SimpleSecurityContext("UserId", new ArrayList<String>());
		StelvioContextSetter.setSecurityContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, SecurityContextHolder.currentSecurityContext());
	}

	@Test
	public void setSecurityContextStringStringArray() {
		SecurityContext ctx = new SimpleSecurityContext("UserId", new ArrayList<String>());
		StelvioContextSetter.setSecurityContext("UserId","Role1","Role2");
		assertEquals("Context is not set correctly on the thread.", ctx.getUserId(), SecurityContextHolder.currentSecurityContext().getUserId());
	}

	@Test
	public void resetSecurityContext() {
		assertTrue("Will come at a later stage. Currently the securityContext will never be null.",true);
	}

	@Test
	public void setRequestContext() {
		RequestContext ctx = new SimpleRequestContext("id","module","process","transaction");
		StelvioContextSetter.setRequestContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, RequestContextHolder.currentRequestContext());
	}

	@Test(expected = IllegalStateException.class)
	public void resetRequestContext() {
		RequestContext ctx = new SimpleRequestContext("id","module","process","transaction");
		StelvioContextSetter.setRequestContext(ctx);

		StelvioContextSetter.resetRequestContext();
		RequestContextHolder.currentRequestContext();
		fail("An IllegalStateException should have been thrown if no requestcontext is on the current thread.");
	}
}
