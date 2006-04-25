package no.trygdeetaten.common.framework.security;

import java.security.AccessControlException;
import java.security.Permission;

/**
 * <p> Implementations of the AccessController interface is used for 
 * access control operations and decisions.
 * 
 * <p> More specifically, the AccessController interface is used to 
 * decide whether an access to a critical system resource is to be 
 * allowed or denied, based on a specific security policy implementation.
 * 
 * <p> This API is inspired by the java.security.AccessController.
 * 
 * <p> The {@link #checkPermission(Permission) checkPermission} method
 * determines whether the access request indicated by a specified
 * permission should be granted or denied. A sample call appears below. 
 * In this example, <code>checkPermission</code> will determine whether 
 * or not to grant "read" access to the child support case number "1234".
 * 
 * <pre>
 * 
 *    Permission perm = new SakPermission("1234", SakPermission.READ);
 *    AccessController acc = new SakAccessController();
 *    acc.checkPermission(perm);
 * 
 * </pre>
 *
 * <p> If a requested access is allowed, <code>checkPermission</code> 
 * returns quietly. If denied, an AccessControlException is
 * thrown. AccessControlException can also be thrown if the requested
 * permission is of an incorrect type or contains an invalid value.
 * Such information is given whenever possible.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: AccessController.java 2001 2005-03-01 13:49:06Z psa2920 $
 */
public interface AccessController {

	/** 
	 * Determines whether the access request indicated by the
	 * specified permission should be allowed or denied.
	 * <p/> 
	 * This method quietly returns if the access request
	 * is permitted, or throws a suitable AccessControlException otherwise. 
	 *
	 * @param permission the requested permission.
	 * 
	 * @exception AccessControlException if the specified permission
	 * is not permitted.
	 */
	void checkPermission(Permission permission) throws AccessControlException;
}
