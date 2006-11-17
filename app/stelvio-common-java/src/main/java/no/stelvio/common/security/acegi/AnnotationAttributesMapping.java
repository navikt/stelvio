package no.stelvio.common.security.acegi;

import org.acegisecurity.ConfigAttribute;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

/**
 * TODO
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AnnotationAttributesMapping {

	private HashMap<String,Collection<ConfigAttribute>> providerMap = new HashMap<String,Collection<ConfigAttribute>>();
	
	/**
	 * @param annotationAttribute
	 * @param providers
	 */
	public void addProviders(String annotationAttribute, List<ConfigAttribute> providers)
	{
		providerMap.put(annotationAttribute, providers);
	}
	/**
	 * @param annotationAttribute
	 * @return
	 */
	public Collection<ConfigAttribute> getProviders(String annotationAttribute)
	{
		return providerMap.get(annotationAttribute);
	}
	/**
	 * @param annotationAttribute
	 * @return
	 */
	public Iterator<ConfigAttribute> getProvidersIterator(String annotationAttribute)
	{
		return providerMap.get(annotationAttribute).iterator();
	}
	
	/**
	 * @return
	 */
	public HashMap<String,Collection<ConfigAttribute>> getProviderMap()
	{
		return providerMap;
	}
	
	
}
