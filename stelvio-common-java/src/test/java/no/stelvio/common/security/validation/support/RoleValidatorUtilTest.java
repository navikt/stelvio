package no.stelvio.common.security.validation.support;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import no.stelvio.common.security.validation.ValidRole;

/**
 * Test class for RoleValidatorUtil.
 * 
 * @author ??
 * 
 */
public class RoleValidatorUtilTest {

	/**
	 * Valid roles for the test.
	 */
	private enum ValidRolesEnum implements ValidRole {
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
	 * A mock enum.
	 */
	private enum MockEnum {
		ENUMVALUE
	}

	/**
	 * Test get roles from enum.
	 */
	@Test
	public void getRolesFromEnum() {
		ValidRole[] roles = RoleValidatorUtil.getRolesFromEnum(ValidRolesEnum.class);
		assertArrayEquals("The roles returned should be the same as the roles in the enum.", ValidRolesEnum.values(), roles);
		assertNull("Should return null as the parameter is not a ValidRole implementation.", RoleValidatorUtil
				.getRolesFromEnum(MockEnum.class));
	}

	/**
	 * Test create validator from enum.
	 */
	@Test
	public void createValidatorFromEnum() {
		DefaultRoleValidator validator = RoleValidatorUtil.createValidatorFromEnum(ValidRolesEnum.class);
		int i = 0;

		for (ValidRole role : validator.getValidRoles()) {
			assertEquals(ValidRolesEnum.values()[i++], role);
		}

		assertNull("Should return null as the parameter is not a ValidRole implementation.", RoleValidatorUtil
				.createValidatorFromEnum(MockEnum.class));
	}
}
