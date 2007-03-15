package no.stelvio.common.security.validation.support;

import static org.junit.Assert.*;

import no.stelvio.common.security.validation.ValidRole;

import org.junit.Test;

public class RoleValidatorUtilTest {
	
	private enum ValidRolesEnum implements ValidRole {
		ROLE1,
		ROLE2;
		public String getRoleName(){
			return name();
		}
	};
	private enum MockEnum {
		ENUMVALUE;
	};
	private class ValidRoleClass implements ValidRole {
		public String getRoleName(){
			return "ROLE1";
		}
	};
	
	@Test
	public void getRolesFromEnum() {
		ValidRole[] roles = RoleValidatorUtil.getRolesFromEnum(ValidRolesEnum.class);
		assertEquals("The roles returned should be the same as the roles in the enum.", ValidRolesEnum.values(), roles);
		assertNull("Should return null as the parameter is not an enum or a ValidRole implementation."
					, RoleValidatorUtil.getRolesFromEnum(Object.class));
		assertNull("Should return null as the parameter is not a ValidRole implementation."
				, RoleValidatorUtil.getRolesFromEnum(MockEnum.class));
		assertNull("Should return null as the parameter is not an enum."
				, RoleValidatorUtil.getRolesFromEnum(ValidRoleClass.class));
		
	}

	@Test
	public void createValidatorFromEnum() {
		DefaultRoleValidator validator = RoleValidatorUtil.createValidatorFromEnum(ValidRolesEnum.class);
		int i = 0;
		for (ValidRole role : validator.getValidRoles()) {
			assertEquals(ValidRolesEnum.values()[i++], role );
		}
		assertNull("Should return null as the parameter is not a ValidRole implementation.",
					RoleValidatorUtil.createValidatorFromEnum(MockEnum.class));
		assertNull("Should return null as the parameter is not an enum."
				, RoleValidatorUtil.createValidatorFromEnum(ValidRoleClass.class));
		
	}

}
