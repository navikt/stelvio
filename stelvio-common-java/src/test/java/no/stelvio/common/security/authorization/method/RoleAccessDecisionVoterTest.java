package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SecurityContextSetter;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.SecurityConfig;

/**
 * Test class for the RoleAccessDecisionVoter.
 * 
 * @version $Id$
 */
public class RoleAccessDecisionVoterTest {
	private AccessDecisionVoter voter;
	private Method annotatedWithRoles;
	private Method noRolesAnnotation;
	private MockReflectiveMethodInvocation invoc1;
	private MockReflectiveMethodInvocation invoc2;

	/**
	 * Setup before test.
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Before
	public void setUp() throws Exception {
		voter = new RoleAccessDecisionVoter();
		annotatedWithRoles = MockService.class.getMethod("someProtectedMethod");
		noRolesAnnotation = MockService2.class.getMethod("someProtectedMethod");
		invoc1 = new MockReflectiveMethodInvocation(null, null, annotatedWithRoles, null, MockService.class, null);
		invoc2 = new MockReflectiveMethodInvocation(null, null, noRolesAnnotation, null, MockService2.class, null);
	}

	/**
	 * Test supportsConfigAttribute.
	 */
	@Test
	public void supportsConfigAttribute() {
		assertTrue("Should always return true.", voter.supports(new SecurityConfig("attributt")));
	}

	/**
	 * Test supportsClass.
	 */
	@Test
	public void supportsClass() {
		assertTrue("Should return true since the class is a ReflectiveMethodInvocation.", voter
				.supports(MockReflectiveMethodInvocation.class));
		assertFalse("Should return false since the class is not a ReflectiveMethodInvocation.", voter.supports(Object.class));
	}

	/**
	 * Test voteAccessGranted.
	 */
	@Test
	public void voteAccessGranted() {
		// stelvio-common should not be dependent on stelvio-test as this is a recursive dependency
		SecurityContextSetter.setSecurityContext("User", "ROLE1", "ROLE3");

		assertTrue(SecurityContextHolder.currentSecurityContext().isUserInRoles("ROLE1", "ROLE3"));
		assertFalse(SecurityContextHolder.currentSecurityContext().isUserInRoles("ROL1", "ROL3"));
		assertEquals("Access should have been granted", AccessDecisionVoter.ACCESS_GRANTED, voter.vote(null, invoc1, null));
	}

	/**
	 * Test voteAccessDenied.
	 */
	@Test
	public void voteAccessDenied() {
		SecurityContextSetter.setSecurityContext("User", "someRole", "someRole2");
		List<String> roles = SecurityContextHolder.currentSecurityContext().getRoles();

		for (String string : roles) {
			System.out.println("Role on context:" + string);
		}

		System.out.println("Sjekker context for roller:" + SecurityContextHolder.currentSecurityContext().isUserInRoles(roles));
		assertEquals("Access should have been denied", AccessDecisionVoter.ACCESS_DENIED, voter.vote(null, invoc1, null));
	}

	/**
	 * Test voteAccessAbstain.
	 */
	@Test
	public void voteAccessAbstain() {
		assertEquals("Should abstain from voting as there is no roles annotation.", AccessDecisionVoter.ACCESS_ABSTAIN, voter
				.vote(null, invoc2, null));
	}
}
