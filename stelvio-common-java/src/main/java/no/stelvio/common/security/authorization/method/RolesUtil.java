package no.stelvio.common.security.authorization.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class used to retrieve the annotation attributes(i.e. role names) for the Roles annotation from the methods or
 * classes where it is used.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
final class RolesUtil {

	/**
	 * Gets the list of rolenames from the Roles annotation on the annotated method. Returns an empty list if no Roles
	 * annotation is present.
	 * 
	 * @param method
	 *            the annotated method.
	 * @return a list of rolesnames for the annotated method.
	 */
	public static List<String> getAnnotatedRoles(Method method) {
		List<String> roles = getAnnotatedRoles(method.getDeclaringClass());

		for (Annotation annotation : method.getAnnotations()) {
			// check for Secured annotations
			if (annotation instanceof Roles) {
				Roles attr = (Roles) annotation;

				for (String auth : attr.value()) {
					roles.add(auth);
				}

				break;
			}
		}

		return roles;
	}

	/**
	 * Gets the list of rolenames from the Roles annotation on the annotated class. Returns an empty list if no Roles annotation
	 * is present.
	 * 
	 * @param target
	 *            the annotated class.
	 * @return a list of rolesnames for the annotated method.
	 */
	public static List<String> getAnnotatedRoles(Class target) {
		List<String> roles = new ArrayList<String>();

		for (Annotation annotation : target.getAnnotations()) {
			// check for Secured annotations
			if (annotation instanceof Roles) {
				Roles attr = (Roles) annotation;

				for (String auth : attr.value()) {
					roles.add(auth);
				}

				break;
			}
		}

		return roles;
	}

	/**
	 * Private constructor to prevent instantiation of the class.
	 */
	private RolesUtil() {
	}
}
