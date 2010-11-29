package no.stelvio.common.security.authorization.method;

import java.util.Iterator;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.method.MethodDefinitionSource;
import org.springframework.beans.factory.InitializingBean;

/**
 * This class is an implementation of a {@link MethodDefinitionSource} with the only purpose of merging two different
 * {@link MethodDefinitionSource} implementations into one source represented by this class.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class MethodDefinitionSources implements MethodDefinitionSource, InitializingBean {

	private MethodDefinitionSource methodDefinitionMap;

	private MethodDefinitionSource methodDefinitionAttributes;

	/**
	 * Requires that at least one {@link MethodDefinitionSource} is set in the bean context.
	 * 
	 * @throws IllegalArgumentException
	 *             if no sources are present.
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws IllegalArgumentException {
		if ((this.methodDefinitionMap == null) && this.methodDefinitionAttributes == null) {
			throw new IllegalArgumentException("At least one methodDefinitionSource is required.");
		}
	}

	/**
	 * Returns the merged ConfigAttributeDefinition that applies to a given secure object from the two configured
	 * MethodDefinitionSources. Returns null if no ConfigAttribiteDefinition applies.
	 * 
	 * @param object
	 *            the object being secured
	 * @return the ConfigAttributeDefinition that applies to the passed object
	 */
	public ConfigAttributeDefinition getAttributes(Object object) {
		ConfigAttributeDefinition defAttributes = (this.methodDefinitionAttributes != null) ? this.methodDefinitionAttributes
				.getAttributes(object) : null;
		ConfigAttributeDefinition defMap = (this.methodDefinitionMap != null) ? this.methodDefinitionMap.getAttributes(object)
				: null;

		if (defAttributes == null) {
			return defMap;
		} else if (defMap == null) {
			return defAttributes;
		} else {
			merge(defAttributes, defMap);
			return defAttributes;
		}
	}

	/**
	 * Merges the second <code>ConfigAttributeDefinition</code> parameter into the first with no duplicates.
	 * 
	 * @param definition
	 *            the final merged ConfigAttributeDefinition
	 * @param toMerge
	 *            the ConfigAttributeDefinition to merge into the first parameter
	 */
	private void merge(ConfigAttributeDefinition definition, ConfigAttributeDefinition toMerge) {
		if (toMerge == null) {
			return;
		}

		Iterator<?> attribs = toMerge.getConfigAttributes();
		while (attribs.hasNext()) {
			ConfigAttribute ca = (ConfigAttribute) attribs.next();
			if (!definition.contains(ca)) {
				definition.addConfigAttribute(ca);
			}
		}
	}

	/**
	 * Returns the methodDefinitionAttributes property.
	 * 
	 * @return the methodDefinitionAttributes
	 */
	public MethodDefinitionSource getMethodDefinitionAttributes() {
		return this.methodDefinitionAttributes;
	}

	/**
	 * Sets the methodDefinitionAttributes property.
	 * 
	 * @param methodDefinitionAttributes
	 *            a MethodDefintionSource.
	 */
	public void setMethodDefinitionAttributes(MethodDefinitionSource methodDefinitionAttributes) {
		this.methodDefinitionAttributes = methodDefinitionAttributes;
	}

	/**
	 * Returns the methodDefinitionMap property.
	 * 
	 * @return the methodDefinitionMap
	 */
	public MethodDefinitionSource getMethodDefinitionMap() {
		return this.methodDefinitionMap;
	}

	/**
	 * Sets the methodDefinitionMap property.
	 * 
	 * @param methodDefinitionMap
	 *            a MethodDefintionSource.
	 */
	public void setMethodDefinitionMap(MethodDefinitionSource methodDefinitionMap) {
		this.methodDefinitionMap = methodDefinitionMap;
	}

	/**
	 * If available, all of the <code>ConfigAttributeDefinition</code>s defined by the implementing class.
	 * <P>
	 * This is used by the {@link AbstractSecurityInterceptor} to perform startup time validation of each
	 * <code>ConfigAttribute</code> configured against it.
	 * </p>
	 * 
	 * @return an iterator over all the <code>ConfigAttributeDefinition</code>s or <code>null</code> if unsupported
	 */
	public Iterator getConfigAttributeDefinitions() {
		if (this.methodDefinitionMap != null) {
			return this.methodDefinitionMap.getConfigAttributeDefinitions();
		} else {
			return null;
		}
	}

	/**
	 * Indicates whether the <code>ObjectDefinitionSource</code> implementation is able to provide
	 * <code>ConfigAttributeDefinition</code>s for the indicated secure object type.
	 * 
	 * @param clazz
	 *            the class that is being queried
	 * 
	 * @return true if the implementation can process the indicated class
	 */
	public boolean supports(Class clazz) {
		return this.methodDefinitionAttributes != null ? this.methodDefinitionAttributes.supports(clazz)
				: this.methodDefinitionMap.supports(clazz);
	}
}
