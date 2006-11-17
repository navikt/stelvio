package no.stelvio.common.security.acegi;

import java.util.Iterator;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.AbstractSecurityInterceptor;
import org.acegisecurity.intercept.method.MethodDefinitionSource;
import org.springframework.beans.factory.InitializingBean;
/**
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class MethodDefinitionSources implements MethodDefinitionSource, InitializingBean{

	private MethodDefinitionSource methodDefinitionMap;
	private MethodDefinitionSource methodDefinitionAttributes;
	
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if ((methodDefinitionMap == null) && methodDefinitionAttributes == null) {
            throw new IllegalArgumentException("At least one methodDefinitionSource is required.");
        }
    }
	
	/* (non-Javadoc)
	 * @see org.acegisecurity.intercept.ObjectDefinitionSource#getAttributes(java.lang.Object)
	 */
	public ConfigAttributeDefinition getAttributes(Object object)
	{
		ConfigAttributeDefinition defAttributes = (methodDefinitionAttributes != null) ? 
													methodDefinitionAttributes.getAttributes(object) : null;
		ConfigAttributeDefinition defMap = (methodDefinitionMap != null) ?
											methodDefinitionMap.getAttributes(object) : null;
		
		if(defAttributes == null){
			return defMap;
		}else if(defMap == null){
			return defAttributes;
		}else{
			merge(defAttributes,defMap);
			return defAttributes;
		}
	}
	
	 /**
	 * @param definition
	 * @param toMerge
	 */
	private void merge(ConfigAttributeDefinition definition, ConfigAttributeDefinition toMerge) {
        if (toMerge == null) {
            return;
        }

        Iterator attribs = toMerge.getConfigAttributes();
        System.out.println("--- Merging ConfigAttributeDefinitions ---");
        while (attribs.hasNext()) {
        	ConfigAttribute ca = (ConfigAttribute) attribs.next();
        	if(!definition.contains(ca)){
        		definition.addConfigAttribute(ca);
        	}else{
        		System.out.println("--- ConfigAttributeDefinition does already contain attribute " + ca.getAttribute() + " ---");
        	}
        }
    }
	
	/**
	 * @return
	 */
	public MethodDefinitionSource getMethodDefinitionAttributes() {
		return methodDefinitionAttributes;
	}


	/**
	 * @param methodDefinitionAttributes
	 */
	public void setMethodDefinitionAttributes(
			MethodDefinitionSource methodDefinitionAttributes) {
		this.methodDefinitionAttributes = methodDefinitionAttributes;
	}




	/**
	 * @return
	 */
	public MethodDefinitionSource getMethodDefinitionMap() {
		return methodDefinitionMap;
	}




	/**
	 * @param methodDefinitionMap
	 */
	public void setMethodDefinitionMap(MethodDefinitionSource methodDefinitionMap) {
		this.methodDefinitionMap = methodDefinitionMap;
	}

	/**
     * If available, all of the <code>ConfigAttributeDefinition</code>s defined by the implementing class.<P>This
     * is used by the {@link AbstractSecurityInterceptor} to perform startup time validation of each
     * <code>ConfigAttribute</code> configured against it.</p>
     *
     * @return an iterator over all the <code>ConfigAttributeDefinition</code>s or <code>null</code> if unsupported
     */
    public Iterator getConfigAttributeDefinitions(){
    	return null;
    }
    
    /**
     * Indicates whether the <code>ObjectDefinitionSource</code> implementation is able to provide
     * <code>ConfigAttributeDefinition</code>s for the indicated secure object type.
     *
     * @param clazz the class that is being queried
     *
     * @return true if the implementation can process the indicated class
     */
    public boolean supports(Class clazz){
    	return methodDefinitionAttributes != null ? methodDefinitionAttributes.supports(clazz) : methodDefinitionMap.supports(clazz);
    }
}
