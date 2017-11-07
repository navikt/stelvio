package no.stelvio.common.security.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ReflectionUtils;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;

/**
 * Utility class to set and reset the SecurityContext. Should only be used by the framework or when unit testing.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class SecurityContextSetter {
	private static Method setSecurityContextMethod;
	private static Method resetSecurityContextMethod;

	/**
	 * Creates a new instance of SecurityContextSetter.
	 */
	protected SecurityContextSetter() {
	}

	/**
	 * Sets a new SecurityContext.
	 * 
	 * @param securityContext
	 *            the securityContext to set.
	 */
	public static synchronized void setSecurityContext(SecurityContext securityContext) {
		if (setSecurityContextMethod == null) {
			setSecurityContextMethod = ReflectionUtils.findMethod(SecurityContextHolder.class, "setSecurityContext",
					new Class[] { SecurityContext.class });
			setSecurityContextMethod.setAccessible(true);
		}

		ReflectionUtils.invokeMethod(setSecurityContextMethod, SecurityContextHolder.class, new Object[] { securityContext });
	}

	/**
	 * Sets a new SecurityContext with the supplied user and roles.
	 * 
	 * @param userId
	 *            the userId.
	 * @param roles
	 *            a list of rolenames.
	 */
	public static void setSecurityContext(String userId, String... roles) {
		List<String> roleList = new ArrayList<String>();

		for (String string : roles) {
			roleList.add(string);
		}

		SecurityContext securityContext = new SimpleSecurityContext(userId, userId, roleList);
		setSecurityContext(securityContext);
	}

	/**
	 * Resets the SecurityContext.
	 */
	public static synchronized void resetSecurityContext() {
		if (resetSecurityContextMethod == null) {
			resetSecurityContextMethod = ReflectionUtils.findMethod(SecurityContextHolder.class, "resetSecurityContext",
					new Class[] {});
			resetSecurityContextMethod.setAccessible(true);
		}

		ReflectionUtils.invokeMethod(resetSecurityContextMethod, null);
	}
}
