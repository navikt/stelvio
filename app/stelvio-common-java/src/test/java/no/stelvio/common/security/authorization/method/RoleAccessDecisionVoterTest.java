package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import no.stelvio.common.error.StelvioException;
import no.stelvio.common.security.RoleName;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.RolesEnum;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.test.context.*;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

/**
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class RoleAccessDecisionVoterTest {

	private AccessDecisionVoter voter;
	
	private Method annotatedWithRoles;
	private Method noRolesAnnotation;
	private ReflectiveMethodInvocation invoc1;
	private ReflectiveMethodInvocation invoc2;
	
	
	@Before
	public void setUp() throws Exception {
		voter = new RoleAccessDecisionVoter();
		annotatedWithRoles = MockService.class.getMethod("someProtectedMethod");
		noRolesAnnotation = MockService2.class.getMethod("someProtectedMethod");
		invoc1 = 
			new ReflectiveMethodInvocation(null, null, annotatedWithRoles, null, MockService.class, null);
		invoc2 = 
			new ReflectiveMethodInvocation(null, null, noRolesAnnotation, null, MockService2.class, null);
	}
	
	@Test
	public void testSupportsConfigAttribute() {
		assertTrue("Should always return true.", voter.supports(new SecurityConfig("")));
	}
	@Test
	public void testSupportsClass() {
		
		assertTrue("Should return true since the class is a ReflectiveMethodInvocation.",
					voter.supports(ReflectiveMethodInvocation.class));
		assertFalse("Should return false since the class is not a ReflectiveMethodInvocation.",
					voter.supports(Object.class));
	}
	@Test
	public void testVoteAccessGranted() {
		StelvioContextSetter.setSecurityContext("User", "ROLE1", "ROLE3");
		
		assertTrue(SecurityContextHolder.currentSecurityContext().isUserInRoles("ROLE1", "ROLE3"));
		assertFalse(SecurityContextHolder.currentSecurityContext().isUserInRoles("ROL1", "ROL3"));
		
		assertEquals("Access should have been granted", AccessDecisionVoter.ACCESS_GRANTED, voter.vote(null, invoc1, null));
	}
	@Test
	public void testVoteAccessDenied() {
		
		StelvioContextSetter.setSecurityContext("User", "someRole", "someRole2");
		List<String> roles = SecurityContextHolder.currentSecurityContext().getRoles();
		for (String string : roles) {
			System.out.println("Role on context:" + string);
		}
		System.out.println("Sjekker context for roller:" + SecurityContextHolder.currentSecurityContext().isUserInRoles(roles));
		assertEquals("Access should have been denied", AccessDecisionVoter.ACCESS_DENIED, voter.vote(null, invoc1, null));
	}
	@Test
	public void testVoteAccessAbstain() {
		assertEquals("Should abstain from voting as there is no roles annotation.", 
				AccessDecisionVoter.ACCESS_ABSTAIN, voter.vote(null, invoc2, null));
	}

}
