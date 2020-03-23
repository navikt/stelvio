package no.stelvio.common.security.support;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for SecurityContextSetter.
 * 
 *
 */
public class SecurityContextSetterTest {

	/**
	 * Test set security context with a security context.
	 */
	@Test
	public void setSecurityContextSecurityContext() {
		SecurityContext ctx = new SimpleSecurityContext("UserId", new ArrayList<String>());
		SecurityContextSetter.setSecurityContext(ctx);
		assertEquals("Context is not set on the thread.", ctx, SecurityContextHolder.currentSecurityContext());
	}

	/**
	 * Test setSecurityContext with a string and string array.
	 */
	@Test
	public void setSecurityContextStringStringArray() {
		SecurityContext ctx = new SimpleSecurityContext("UserId", new ArrayList<String>());
		SecurityContextSetter.setSecurityContext("UserId", "Role1", "Role2");
		assertEquals("Context is not set correctly on the thread.", ctx.getUserId(), SecurityContextHolder
				.currentSecurityContext().getUserId());
	}

	/**
	 * Test reset security context.
	 */
	@Test
	@Ignore("Will come at a later stage. Currently the securityContext will never be null.")
	public void resetSecurityContext() {
	}
}
