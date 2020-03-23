package no.stelvio.common.security.validation.support;

import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;

/**
 * Utility class for extracting valid roles from enums implementing the <code>ValidRole</code> interface and for creating
 * <code>RoleValidator</code>s with these roles.
 * 
 * @version $Id$
 * @see RoleValidator
 * @see DefaultRoleValidator
 * @see ValidRole
 */
public class RoleValidatorUtil {

	/**
	 * Creates a new instance of RoleValidatorUtil.
	 */
	protected RoleValidatorUtil() {
	}

	/**
	 * Retrieves all the roles from the supplied enum class parameter, i.e. retrieves all the constants of the ValidRole enum
	 * implementation.
	 * 
	 * @param enumClass
	 *            the enum containing the valid roles. Must be an implementation of ValidRole.
	 * @return Array of valid roles or <code>null</code> if the supplied class is not an enum implementing the ValidRole
	 *         interface.
	 */
	public static ValidRole[] getRolesFromEnum(Class<? extends Enum> enumClass) {
		if (enumClass != null && enumClass.isEnum() && assignableFromValidRole(enumClass)) {
			Object[] constants = enumClass.getEnumConstants();
			return (ValidRole[]) constants.clone();
		} else {
			return null;
		}
	}

	/**
	 * Creates a DefaultRoleValidator with the roles from the supplied enum class parameter. The enum class must be an
	 * implementation of the ValidRole interface.
	 * 
	 * @param enumClass
	 *            the enum containing the valid roles. Must be an implementation of ValidRole.
	 * @return a new DefaultRoleValidator instance or <code>null</code> unless the supplied parameter is an enum implementing
	 *         the ValidRole interface.
	 */
	public static DefaultRoleValidator createValidatorFromEnum(Class<? extends Enum> enumClass) {
		ValidRole[] roles = getRolesFromEnum(enumClass);

		return roles != null ? new DefaultRoleValidator(roles) : null;
	}

	/**
	 * Private helper method which checks if the supplied class is assignable from the ValidRole interface.
	 * 
	 * @param clazz
	 *            the class to check.
	 * @return true if assignable, otherwise false.
	 */
	private static boolean assignableFromValidRole(Class clazz) {
		return ValidRole.class.isAssignableFrom(clazz);
	}

}
