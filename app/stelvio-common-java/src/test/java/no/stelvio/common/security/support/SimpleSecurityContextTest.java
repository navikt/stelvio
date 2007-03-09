package no.stelvio.common.security.support;

import static org.junit.Assert.*;
import no.stelvio.common.security.SecurityContext;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SimpleSecurityContextTest {

	private SecurityContext getSecurityContext(String userId, String... roles){
		List<String> roleList = new ArrayList<String>();
		for (String string : roles) {
			roleList.add(string);
		}
		return new SimpleSecurityContext("User", roleList);
	}
	@Test
	public void testIsUserInRoleString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		assertTrue("User has the role.", ctx.isUserInRole("ROLE1"));
		assertFalse("User does not have the role.", ctx.isUserInRole("ROLE"));
	}

	@Test
	public void testIsUserInAllRolesListOfString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertTrue("User has the role.", ctx.isUserInAllRoles(roles));
		roles.set(0, "OtherRole");
		assertFalse("User does not have all the roles.", ctx.isUserInAllRoles(roles));
	}

	@Test
	public void testIsUserInAllRolesStringArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		assertTrue("User has the role.", ctx.isUserInAllRoles("ROLE1","ROLE2"));
		assertFalse("User does not have all the roles.", ctx.isUserInAllRoles("ROLE1","ROLE"));
	}

	@Test
	public void testIsUserInRolesListOfString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertTrue("User has the role.", ctx.isUserInRoles(roles));
		roles.set(0, "OtherRole");
		roles.set(1, "OtherRole2");
		assertFalse("User does not have all the roles.", ctx.isUserInRoles(roles));
	}

	@Test
	public void testIsUserInRolesStringArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		assertTrue("User has the role.", ctx.isUserInRoles("ROLE1", "ROLE3"));
		assertFalse("User does not have all the roles.", ctx.isUserInRoles("AROLE", "BROLE"));
	}

}
