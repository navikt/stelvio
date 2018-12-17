package no.stelvio.common.security.authorization.method;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;

/**
 * This class holds mappings between a <code>String</code> id and a collection
 * of <code>ConfigAttributes</code> in a HashMap. It is typically used to map
 * the values in a
 * <code>no.stelvio.common.security.acegi.annotation.Secured</code> annotation
 * to a collection of providers, i.e. <code>AccessDecisionVoter</code>s and
 * <code>AfterInvocationProvider</code>s.
 * <p>
 * Example of such a mapping:
 * </p>
 * If a method is annoted with <code>@Secured({"Id1"})</code>, Id1 can be mapped against a collection of
 *                   providers.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AnnotationAttributesMapping {

	private Map<String, Collection<ConfigAttribute>> providerMap = new HashMap<>();

	/**
	 * Adds a list of providers to the HashMap with the annotationAttribute as
	 * the key.
	 * 
	 * @param annotationAttribute
	 *            the annotationAttribute
	 * @param providers
	 *            the provider list
	 */
	public void addProviders(String annotationAttribute,
			List<ConfigAttribute> providers) {
		providerMap.put(annotationAttribute, providers);
	}

	/**
	 * Returns the collection of providers that is mapped to the
	 * annotationAttribute parameter.
	 * 
	 * @param annotationAttribute
	 *            the annotationAttribute
	 * @return a collection of <code>ConfigAttribute</code>s representing
	 *         providers mapped to an annotationAttribute
	 */
	public Collection<ConfigAttribute> getProviders(String annotationAttribute) {
		return providerMap.get(annotationAttribute);
	}

	/**
	 * Returns the iterator of the provider collection mapped to the presented
	 * annotationAttribute.
	 * 
	 * @param annotationAttribute
	 *            the annotationAttribute.
	 * @return the iterator of a ConfigAttribute collection.
	 */
	public Iterator<ConfigAttribute> getProvidersIterator(
			String annotationAttribute) {
		return providerMap.get(annotationAttribute).iterator();
	}

	/**
	 * Returns the entire HashMap containing all the mappings.
	 * 
	 * @return the mappings.
	 */
	public Map<String, Collection<ConfigAttribute>> getProviderMap() {
		return providerMap;
	}

}
