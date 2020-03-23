package no.stelvio.common.security.authorization.method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Java 5 annotation for describing service layer security attributes.
 * 
 * <p>
 * The <code>Roles</code> annotation is used to define a list of security roles for business methods. This annotation can be
 * used as a Java 5 alternative to XML configuration.
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;Roles( { &quot;ROLE_USER&quot; })
 * public void create(Contact contact);
 * 
 * &#064;Roles( { &quot;ROLE_USER&quot;, &quot;ROLE_ADMIN&quot; })
 * public void update(Contact contact);
 * 
 * &#064;Roles( { &quot;ROLE_ADMIN&quot; })
 * public void delete(Contact contact);
 * </pre>
 * 
 * @version $Id$
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Roles {
	/**
	 * @return the list of security configuration attributes. (i.e. ROLE_USER, ROLE_ADMIN etc.)
	 */
	String[] value();
}
