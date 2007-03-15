package no.stelvio.common.security.validation.support;

import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;


/**
 * Utility class for extracting valid roles from enums implementing the <code>ValidRole</code> interface
 * and for creating <code>RoleValidator</code>s with these roles. 
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see RoleValidator
 * @see DefaultRoleValidator
 * @see ValidRole
 */
public class RoleValidatorUtil {

	/**
	 * Retrieves all the roles from the supplied enum class parameter,
	 * i.e. retrieves all the constants of the ValidRole enum implementation.
	 * 
	 * @param enumName the enum containing the valid roles. Must be an implementation of ValidRole.
	 * @return Array of valid roles, <code>null</code> if the supplied class is not an enum implementing the ValidRole interface.
	 */
	public static ValidRole[] getRolesFromEnum(Class enumClass){
		if (enumClass!= null && enumClass.isEnum() && assignableFromValidRole(enumClass)) {			
			Object[] constants = enumClass.getEnumConstants();
			ValidRole[] validRoles = (ValidRole[])constants.clone();
			return validRoles;
		} 
		return null;
	}
	
	/**
	 * Creates a class from the given string using the context classloader on the current thread.
	 * 
	 * @param className the fully qualified classname of the class to create.
	 * @return a class of the type specified in the input param. 
	 * @throws ClassNotFoundException if the class cannot be found.
	 */
	public static Class getClassFromString(String className) throws ClassNotFoundException{
		Class clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
		return clazz;
	}
	
	/**
	 * Creates a DefaultRoleValidator with the roles from the supplied enum class parameter. 
	 * The enum class must be an implementation of the ValidRole interface.
	 * @param enumClass the enum containing the valid roles. Must be an implementation of ValidRole.
	 * @return a new DefaultRoleValidator instance or <code>null</code> unless the supplied parameter
	 * is an enum implementing the ValidRole interface.
	 */
	public static DefaultRoleValidator createValidatorFromEnum(Class enumClass){
		ValidRole[] roles = getRolesFromEnum(enumClass);
		return roles != null ? new DefaultRoleValidator(roles) : null;
	}
	
	/**
	 * Private helper method which checks if the supplied class is assignable from the ValidRole interface.
	 * @param clazz the class to check.
	 * @return true if assignable, otherwise false.
	 */
	private static boolean assignableFromValidRole(Class clazz) {
		return ValidRole.class.isAssignableFrom(clazz);
	}
	
}
