package no.stelvio.test.context;

import static org.junit.Assert.*;

import java.util.ArrayList;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SimpleSecurityContext;

import org.junit.Test;

public class StelvioContextSetterTest {

	@Test
	public void testSetSecurityContextSecurityContext() {
		SecurityContext ctx = new SimpleSecurityContext("UserId", new ArrayList<String>());
		StelvioContextSetter.setSecurityContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, SecurityContextHolder.currentSecurityContext());
	}

	@Test
	public void testSetSecurityContextStringStringArray() {
		SecurityContext ctx = new SimpleSecurityContext("UserId", new ArrayList<String>());
		StelvioContextSetter.setSecurityContext("UserId","Role1","Role2");
		assertEquals("Context is not set correctly on the thread.", ctx.getUserId(), SecurityContextHolder.currentSecurityContext().getUserId());
	}

	@Test
	public void testResetSecurityContext() {
		assertTrue("Will come at a later stage. Currently the securityContext will never be null.",true);
	}

	@Test
	public void testSetRequestContext() {
		RequestContext ctx = new SimpleRequestContext("id","module","process","transaction");
		StelvioContextSetter.setRequestContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, RequestContextHolder.currentRequestContext());
	}

	@Test
	public void testResetRequestContext() {
		RequestContext ctx = new SimpleRequestContext("id","module","process","transaction");
		StelvioContextSetter.setRequestContext(ctx);
		try{
			StelvioContextSetter.resetRequestContext();
			RequestContextHolder.currentRequestContext();
		} catch (IllegalStateException expected){
			assertTrue(true);
			return;
		}
		fail("An IllegalStateException should have been thrown if no requestcontext is on the current thread.");
	}
}
