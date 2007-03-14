package no.stelvio.common.security.validation.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.security.definition.support.DefaultRole;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;

import org.junit.Before;
import org.junit.Test;

public class DefaultRoleValidatorTest {

	private RoleValidator validator;
	
	private enum DefaultRoles implements ValidRole {
		ROLE1,
		ROLE2;		
		public String getRoleName(){
			return name();
		}
	};
	private enum DefaultRoles2 implements ValidRole {
		ROLE3,
		ROLE4;		
		public String getRoleName(){
			return name();
		}
	};
	
	@Before
	public void setUp() throws Exception {
		validator = new DefaultRoleValidator(DefaultRoles.ROLE1,DefaultRoles.ROLE2);
	}
	
	@Test
	public void testGetValidRoles() {
		List<ValidRole> roles = validator.getValidRoles();
		DefaultRoles[] defRoles = DefaultRoles.values();
		for (DefaultRoles role : defRoles) {
			if(!roles.contains(role)){
				fail("The valid roles where not the same as the default roles enum.");
			}
		}
	}

	@Test
	public void testSetValidRolesListOfValidRole() {
		List<ValidRole> validRoles = new ArrayList<ValidRole>();
		validRoles.add(DefaultRoles2.ROLE3);
		validRoles.add(DefaultRoles2.ROLE4);	
		validator.setValidRoles(validRoles);
		assertEquals("The roles set through the setter is not equal to the getter.", validRoles, validator.getValidRoles());
	}

	@Test
	public void testSetValidRolesValidRoleArray() {
		validator.setValidRoles(DefaultRoles2.ROLE3, DefaultRoles2.ROLE4);
		List<ValidRole> roles = validator.getValidRoles();
		assertTrue("List of roles returned should include DefaultRoles2.ROLE3", roles.contains(DefaultRoles2.ROLE3));
		assertTrue("List of roles returned should include DefaultRoles2.ROLE4", roles.contains(DefaultRoles2.ROLE4));
	}

	@Test
	public void testIsValid() {
		assertFalse("Should return false as the parameter is neither a Role or a String.", validator.isValid(new Object()));
		assertFalse("Should return false as the string is not a valid rolename.", validator.isValid("SomeRole"));
		assertFalse("Should return false as the Role is not a valid role.", validator.isValid(new DefaultRole("SomeRole")));
		
		assertTrue("Should return true as the Role is a valid role.", validator.isValid(new DefaultRole("ROLE1")));
		assertTrue("Should return true as the Role is a valid role.", validator.isValid(DefaultRoles.ROLE1));
		assertTrue("Should return true as the String is a valid rolename.", validator.isValid("ROLE1"));
	}
}
