package no.stelvio.common.security.authorization.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.annotation.Secured;
import org.springframework.metadata.Attributes;

/**
 * Java 5 Annotation <code>Attributes</code> metadata implementation used for
 * secure method interception.
 * <p>
 * This <code>Attributes</code> implementation will return security
 * configuration for classes described using the <code>Secured</code> Java 5
 * annotation. The security configuration returned is a list of values which are
 * mapped to the annotation attributes through a
 * <code>AnnotationattributesMapping</code>. These values typically represent
 * which providers, i.e. <code>AccessDecisionVoter</code>s and
 * <code>AfterInvocationProvider</code>s, that should be used on a secure
 * object.
 * </p>
 * 
 * <p>
 * The <code>SecurityAnnotationsAttributes</code> implementation can be used
 * to configure a <code>MethodDefinitionAttributes</code> and
 * <code>MethodSecurityInterceptor</code> bean definition (see below).
 * </p>
 * 
 * <p>
 * For example:
 * 
 * <pre>
 *  &lt;bean id=&quot;attributes&quot; class=&quot;org.acegisecurity.annotation.SecurityAnnotationAttributes&quot;&gt;
 * 	&lt;property name=&quot;annotationMapping&quot;&gt;
 * 		&lt;value&gt;
 * 		        Id1=somepackage.SomeAfterInvocationProvider,somepackage.SomeAccessDecisionVoter 
 * 		        Id2=anotherpackage.SomeAccessDecisionVoter,anotherpackage.SomeAfterInvocationProvider
 * 		&lt;/value&gt;
 *  	&lt;/property&gt;
 *  &lt;/bean&gt;
 *  &lt;bean id=&quot;objectDefinitionSource&quot; class=&quot;org.acegisecurity.intercept.method.MethodDefinitionAttributes&quot;&gt;    
 *      &lt;property name=&quot;attributes&quot;&gt;&lt;ref local=&quot;attributes&quot;/&gt;&lt;/property&gt;
 *  &lt;/bean&gt;
 *  &lt;bean id=&quot;securityInterceptor&quot; class=&quot;org.acegisecurity.intercept.method.aopalliance.MethodSecurityInterceptor&quot;&gt;
 *  	. . .
 *  	&lt;property name=&quot;objectDefinitionSource&quot;&gt;&lt;ref local=&quot;objectDefinitionSource&quot;/&gt;&lt;/property&gt;
 *  &lt;/bean&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * These security annotations are similiar to the Commons Attributes approach,
 * however they are using Java 5 language-level metadata support.
 * </p>
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see org.acegisecurity.annotation.Secured
 */

public class SecurityAnnotationsAttributes implements Attributes {

	private AnnotationAttributesMapping annotationMapping;

	/**
	 * Get the <code>Secured</code> attributes for a given target class and
	 * return their corresponding mapped attributes in the
	 * <code>AnnotationAttributesMapping</code>.
	 * 
	 * @param target
	 *            The target class
	 * 
	 * @return Collection of <code>ConfigAttribute</code>s
	 * 
	 * @see Attributes#getAttributes
	 * 
	 * @throws AcegiConfigurationException
	 *             if no mapping can be found for an annotation attribute
	 */
	public Collection<ConfigAttribute> getAttributes(Class target) {
		Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
		for (Annotation annotation : target.getAnnotations()) {
			// check for Secured annotations
			if (annotation instanceof Secured) {
				Secured attr = (Secured) annotation;

				for (String auth : attr.value()) {

					Collection<ConfigAttribute> providers = this.annotationMapping
							.getProviders(auth);
					if (providers != null) {
						attributes.addAll(providers);
					} else {
						throw new SecureAnnotationNotInMappingException(attr,
								target);
					}
				}

				break;
			}

		}

		return attributes;
	}

	/**
	 * This class does not support this operation.
	 * 
	 * @throws UnsupportedOperationException
	 * @see org.springframework.metadata.Attributes#getAttributes(java.lang.Class,
	 *      java.lang.Class)
	 */
	public Collection getAttributes(Class clazz, Class filter) {
		throw new UnsupportedOperationException("Unsupported operation");
	}

	/**
	 * Get the <code>Secured</code> attributes for a given target method and
	 * return their corresponding mapped attributes in the
	 * <code>AnnotationAttributesMapping</code>.
	 * 
	 * @param method
	 *            The target method
	 * 
	 * @return Collection of <code>ConfigAttribute</code>
	 * 
	 * @see Attributes#getAttributes
	 * 
	 * @throws AcegiConfigurationException
	 *             if no mapping can be found for an annotation attribute
	 */
	public Collection<ConfigAttribute> getAttributes(Method method) {
		Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

		for (Annotation annotation : method.getAnnotations()) {
			// check for Secured annotations
			if (annotation instanceof Secured) {
				Secured attr = (Secured) annotation;

				for (String auth : attr.value()) {
					Collection<ConfigAttribute> providers = this.annotationMapping
							.getProviders(auth);
					if (providers != null) {
						attributes.addAll(providers);
					} else {
						throw new SecureAnnotationNotInMappingException(attr,
								method);
					}
				}
				break;
			}
		}
		return attributes;
	}

	/**
	 * This class does not support this operation.
	 * 
	 * @throws UnsupportedOperationException
	 * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Method,
	 *      java.lang.Class)
	 */
	public Collection getAttributes(Method method, Class clazz) {
		throw new UnsupportedOperationException("Unsupported operation");
	}

	/**
	 * This class does not support this operation.
	 * 
	 * @throws UnsupportedOperationException
	 * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Field)
	 */
	public Collection getAttributes(Field field) {
		throw new UnsupportedOperationException("Unsupported operation");
	}

	/**
	 * This class does not support this operation.
	 * 
	 * @throws UnsupportedOperationException
	 * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Field,
	 *      java.lang.Class)
	 */
	public Collection getAttributes(Field field, Class clazz) {
		throw new UnsupportedOperationException("Unsupported operation");
	}

	/**
	 * Gets the <code>AnnotationAttributesMapping</code>.
	 * 
	 * @return the annotationMapping
	 */
	public AnnotationAttributesMapping getAnnotationMapping() {
		return this.annotationMapping;
	}

	/**
	 * Sets the <code>AnnotationAttributesMapping</code> property.
	 * 
	 * @param annotationMapping
	 *            the annotationMapping to set
	 */
	public void setAnnotationMapping(
			AnnotationAttributesMapping annotationMapping) {
		this.annotationMapping = annotationMapping;
	}
}
