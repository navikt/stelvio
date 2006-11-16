package no.stelvio.common.security.acegi;

import org.acegisecurity.ConfigAttribute;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

/**
 * 
 * @author persondab2f89862d3, Accenture
 *
 */
public class AnnotationAttributesMapping {

	private HashMap<String,Collection<ConfigAttribute>> providerMap = new HashMap<String,Collection<ConfigAttribute>>();
	
	public void addProviders(String annotationAttribute, List<ConfigAttribute> providers)
	{
		providerMap.put(annotationAttribute, providers);
	}
	public Collection<ConfigAttribute> getProviders(String annotationAttribute)
	{
		return providerMap.get(annotationAttribute);
	}
	public Iterator<ConfigAttribute> getProvidersIterator(String annotationAttribute)
	{
		return providerMap.get(annotationAttribute).iterator();
	}
	
	public HashMap<String,Collection<ConfigAttribute>> getProviderMap()
	{
		return providerMap;
	}
	
	
}
