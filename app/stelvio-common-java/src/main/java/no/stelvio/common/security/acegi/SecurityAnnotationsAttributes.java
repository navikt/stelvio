package no.stelvio.common.security.acegi;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.SecurityConfig;
import no.stelvio.common.security.acegi.annotation.Secured;

import org.springframework.metadata.Attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;


/**
 * TODO Update comments
 * 
 * Java 5 Annotation <code>Attributes</code> metadata implementation used for  secure method interception.<p>This
 * <code>Attributes</code> implementation will return security  configuration for classes described using the
 * <code>Secured</code> Java 5 annotation.</p>
 *  <p>The <code>SecurityAnnotationAttributes</code> implementation can be used to configure a
 * <code>MethodDefinitionAttributes</code> and  <code>MethodSecurityInterceptor</code> bean definition (see below).</p>
 *  <p>For example:<pre>&lt;bean id="attributes" 
 *     class="org.acegisecurity.annotation.SecurityAnnotationAttributes"/>&lt;bean id="objectDefinitionSource" 
 *     class="org.acegisecurity.intercept.method.MethodDefinitionAttributes">    &lt;property name="attributes">
 *         &lt;ref local="attributes"/>    &lt;/property>&lt;/bean>&lt;bean id="securityInterceptor" 
 *     class="org.acegisecurity.intercept.method.aopalliance.MethodSecurityInterceptor">     . . .
 *      &lt;property name="objectDefinitionSource">         &lt;ref local="objectDefinitionSource"/>     &lt;/property>
 * &lt;/bean></pre></p>
 *  <p>These security annotations are similiar to the Commons Attributes approach, however they are using Java 5
 * language-level metadata support.</p>
 *
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 *
 * @see org.acegisecurity.annotation.Secured
 */

public class SecurityAnnotationsAttributes implements Attributes {
    
	private AnnotationAttributesMapping annotationMapping;
	
	
    /**
     * Get the <code>Secured</code> attributes for a given target class.
     *
     * @param target The target method
     *
     * @return Collection of <code>ConfigAttribute</code>
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
                	
                	Collection<ConfigAttribute> providers = annotationMapping.getProviders(auth);
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

    public Collection getAttributes(Class clazz, Class filter) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /**
     * Get the <code>Secured</code> attributes for a given target method.
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
                	Collection<ConfigAttribute> providers = annotationMapping.getProviders(auth);
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

    /* (non-Javadoc)
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Method, java.lang.Class)
     */
    public Collection getAttributes(Method method, Class clazz) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /* (non-Javadoc)
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Field)
     */
    public Collection getAttributes(Field field) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /* (non-Javadoc)
     * @see org.springframework.metadata.Attributes#getAttributes(java.lang.reflect.Field, java.lang.Class)
     */
    public Collection getAttributes(Field field, Class clazz) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

	/**
	 * @return the annotationMapping
	 */
	public AnnotationAttributesMapping getAnnotationMapping() {
		return annotationMapping;
	}

	/**
	 * @param annotationMapping the annotationMapping to set
	 */
	public void setAnnotationMapping(AnnotationAttributesMapping annotationMapping) {
		this.annotationMapping = annotationMapping;
	}
}

