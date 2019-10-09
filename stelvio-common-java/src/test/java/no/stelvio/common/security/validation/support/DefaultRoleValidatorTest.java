package no.stelvio.common.security.validation.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.security.definition.support.DefaultRole;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DefaultRoleValidator.
 * 
 * @author ??
 */
public class DefaultRoleValidatorTest {

	private RoleValidator validator;

	/**
	 * Roles for the test.
	 */
	private enum DefaultRoles implements ValidRole {
		ROLE1, ROLE2;

		/**
		 * Get role name.
		 * 
		 * @return role name
		 */
		public String getRoleName() {
			return name();
		}
	}

	/**
	 * Roles for the test.
	 */
	private enum DefaultRoles2 implements ValidRole {
		ROLE3, ROLE4;

		/**
		 * Get role name.
		 * 
		 * @return role name
		 */
		public String getRoleName() {
			return name();
		}
	}

	/**
	 * Set up validators for the tests.
	 *
	 */
	@Before
	public void setUp() {
		validator = new DefaultRoleValidator(DefaultRoles.ROLE1, DefaultRoles.ROLE2);
	}

	/**
	 * Test getValidRoles.
	 */
	@Test
	public void getValidRoles() {
		List<ValidRole> roles = validator.getValidRoles();
		DefaultRoles[] defRoles = DefaultRoles.values();
		for (DefaultRoles role : defRoles) {
			if (!roles.contains(role)) {
				fail("The valid roles where not the same as the default roles enum.");
			}
		}
	}

	/**
	 * Test setValidRoles.
	 */
	@Test
	public void setValidRolesListOfValidRole() {
		List<ValidRole> validRoles = new ArrayList<>();
		validRoles.add(DefaultRoles2.ROLE3);
		validRoles.add(DefaultRoles2.ROLE4);
		validator.setValidRoles(validRoles);
		assertEquals("The roles set through the setter is not equal to the getter.", validRoles, validator.getValidRoles());
	}

	/**
	 * Test setValidRoles.
	 */
	@Test
	public void setValidRolesValidRoleArray() {
		validator.setValidRoles(DefaultRoles2.ROLE3, DefaultRoles2.ROLE4);
		List<ValidRole> roles = validator.getValidRoles();
		assertTrue("List of roles returned should include DefaultRoles2.ROLE3", roles.contains(DefaultRoles2.ROLE3));
		assertTrue("List of roles returned should include DefaultRoles2.ROLE4", roles.contains(DefaultRoles2.ROLE4));
	}

	/**
	 * Test isValid.
	 */
	@Test
	public void isValid() {
		assertFalse("Should return false as the parameter is neither a Role or a String.", validator.isValid(new Object()));
		assertFalse("Should return false as the string is not a valid rolename.", validator.isValid("SomeRole"));
		assertFalse("Should return false as the Role is not a valid role.", validator.isValid(new DefaultRole("SomeRole")));

		assertTrue("Should return true as the Role is a valid role.", validator.isValid(new DefaultRole("ROLE1")));
		assertTrue("Should return true as the Role is a valid role.", validator.isValid(DefaultRoles.ROLE1));
		assertTrue("Should return true as the String is a valid rolename.", validator.isValid("ROLE1"));
	}
}
