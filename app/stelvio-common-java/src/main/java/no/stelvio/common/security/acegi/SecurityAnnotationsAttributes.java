package no.stelvio.common.security.acegi;

import org.acegisecurity.ConfigAttribute;
import no.stelvio.common.security.acegi.annotation.Secured;

import org.springframework.metadata.Attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Collection;
import java.util.ArrayList;


/**
 * Java 5 Annotation <code>Attributes</code> metadata implementation used for  secure method interception.<p>This
 * <code>Attributes</code> implementation will return security  configuration for classes described using the
 * <code>Secured</code> Java 5 annotation. The security configuration returned is a list of values which are mapped to 
 * the annotation attributes through a <code>AnnotationattributesMapping</code>. These values typically represent which providers, i.e. 
 * <code>AccessDecisionVoter</code>s and <code>AfterInvocationProvider</code>s, that should be used on a secure object.</p>
 * 
 * <p>The <code>SecurityAnnotationsAttributes</code> implementation can be used to configure a
 * <code>MethodDefinitionAttributes</code> and  <code>MethodSecurityInterceptor</code> bean definition (see below).</p>
 * 
 * <p>For example:<pre>
 * &lt;bean id="attributes" class="org.acegisecurity.annotation.SecurityAnnotationAttributes">
 *	&lt;property name="annotationMapping">
 *		&lt;value>
 *		        Id1=somepackage.SomeAfterInvocationProvider,somepackage.SomeAccessDecisionVoter 
 *		        Id2=anotherpackage.SomeAccessDecisionVoter,anotherpackage.SomeAfterInvocationProvider
 *		&lt;/value>
 * 	&lt;/property>
 * &lt;/bean>
 * &lt;bean id="objectDefinitionSource" class="org.acegisecurity.intercept.method.MethodDefinitionAttributes">    
 *     &lt;property name="attributes">&lt;ref local="attributes"/>&lt;/property>
 * &lt;/bean>
 * &lt;bean id="securityInterceptor" class="org.acegisecurity.intercept.method.aopalliance.MethodSecurityInterceptor">
 * 	. . .
 * 	&lt;property name="objectDefinitionSource">&lt;ref local="objectDefinitionSource"/>&lt;/property>
 * &lt;/bean>
 * </pre></p>
 *  <p>These security annotations are similiar to the Commons Attributes approach, however they are using Java 5
 * language-level metadata support.</p>
 *
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see org.acegisecurity.annotation.Secured
 */

public class SecurityAnnotationsAttributes implements Attributes {
    
	private AnnotationAttributesMapping annotationMapping;
	
	
    /**
     * Get the <code>Secured</code> attributes for a given target class and return their corresponding
     * mapped attributes in the <code>AnnotationAttributesMapping</code>.
     *
     * @param target The target class
     *
     * @return Collection of <code>ConfigAttribute</code>s
     *
     * @see Attributes#getAttributes
     * 
     * @throws AcegiConfigurationException if no mapping can be found for an annotation attribute
     */
    public Collection<ConfigAttribute> getAttributes(Class target) {
    	Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        for (Annotation annotation : target.getAnnotations()) {
            // check for Secured annotations
            if (annotation instanceof Secured) {
                Secured attr = (Secured) annotation;

                for (String auth : attr.value()) {
                	
                	Collection<ConfigAttribute> providers = this.annotationMapping.getProviders(auth);
                	if(providers != null){
                		attributes.addAll(providers);
                        System.out.println("Adding attributes for id:" + auth + " - providers:" + providers);	
                	}else{
                		throw new AcegiConfigurationException("The @Secured annotation attribute '" + auth +"' for Class '" + target.getName() + "'" 
								+ " cannot be found in the annotation mapping.");
                	}
                }

                break;
            }
            
        }

        return attributes;
    }
    /**
     * This class does not support this operation.
     * @throws UnsupportedOperationException 
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.Class, java.lang.Class)
     */
    public Collection getAttributes(Class clazz, Class filter) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /**
     * Get the <code>Secured</code> attributes for a given target method and return their corresponding
     * mapped attributes in the <code>AnnotationAttributesMapping</code>.
     *
     * @param method The target method
     *
     * @return Collection of <code>ConfigAttribute</code>
     *
     * @see Attributes#getAttributes
     * 
     * @throws AcegiConfigurationException if no mapping can be found for an annotation attribute
     */
    public Collection<ConfigAttribute> getAttributes(Method method) {
        Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        
        for (Annotation annotation : method.getAnnotations()) {
            // check for Secured annotations
            if (annotation instanceof Secured) {
                Secured attr = (Secured) annotation;

                for (String auth : attr.value()) {
                	Collection<ConfigAttribute> providers = this.annotationMapping.getProviders(auth);
                	if(providers != null){
                		attributes.addAll(providers);
                		 System.out.println("Adding attributes for id:" + auth + " - providers:" + providers);	
                	}else{
                		throw new AcegiConfigurationException("The @Secured annotation attribute '" + auth +"' for method '" + method + "'" 
                												+ " cannot be found in the annotation mapping.");
                	}
                }
                break;
            }      
        }
        return attributes;
    }

    /**
     * This class does not support this operation.
     * @throws UnsupportedOperationException 
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Method, java.lang.Class)
     */
    public Collection getAttributes(Method method, Class clazz) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /**
     * This class does not support this operation.
     * @throws UnsupportedOperationException 
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Field)
     */
    public Collection getAttributes(Field field) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /**
     * This class does not support this operation.
     * @throws UnsupportedOperationException 
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Field, java.lang.Class)
     */
    public Collection getAttributes(Field field, Class clazz) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

	/**
	 * Gets the <code>AnnotationAttributesMapping</code>.
	 * @return the annotationMapping
	 */
	public AnnotationAttributesMapping getAnnotationMapping() {
		return this.annotationMapping;
	}

	/**
	 * Sets the <code>AnnotationAttributesMapping</code> property.
	 * @param annotationMapping the annotationMapping to set
	 */
	public void setAnnotationMapping(AnnotationAttributesMapping annotationMapping) {
		this.annotationMapping = annotationMapping;
	}
}

