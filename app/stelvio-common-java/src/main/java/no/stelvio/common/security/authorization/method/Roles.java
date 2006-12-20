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
 * <p>The <code>Roles</code> annotation is used to define a list of security 
 * roles for business methods.  This annotation can be used 
 * as a Java 5 alternative to XML configuration.
 * <p>For example:
 * <pre>
 *     &#64;Roles ({"ROLE_USER"})
 *     public void create(Contact contact);
 *     
 *     &#64;Roles ({"ROLE_USER", "ROLE_ADMIN"})
 *     public void update(Contact contact);
 *     
 *     &#64;Roles ({"ROLE_ADMIN"})
 *     public void delete(Contact contact);
 * </pre> 
 * @author persondab2f89862d3
 * @version $Id$
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Roles {
	/**
     * Returns the list of security configuration attributes. 
     *   (i.e. ROLE_USER, ROLE_ADMIN etc.)
     * @return String[] The secure method attributes 
     */
    public String[] value();
}
