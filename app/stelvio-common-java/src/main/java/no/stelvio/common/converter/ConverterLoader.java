package no.stelvio.common.converter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import no.stelvio.common.error.SystemException;

/**
 * This class will be configured to load all nessesary
 * Converter classes to be used with Apache BeanUtils.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class ConverterLoader {

	/** Holder for converters */
	private Map converters = null;
	
	/**
	 * Method will be called by Configuration to initialize
	 * converters for ConvertUtils.
	 *
	 */
	public void init() {
		// Register converters
		if(null == converters || converters.isEmpty()) {
			return;
		}
		Set allConverters = converters.keySet();
		Iterator iter = allConverters.iterator();
		while(iter.hasNext()) {
			String str = (String) iter.next();
			Class clazz = null;
			try {
				clazz = Class.forName(str);
			} catch (ClassNotFoundException e) {			
				throw new SystemException(e);
			}
			ConvertUtils.register( (Converter) converters.get( str ), clazz );
		}
	}

	/**
	 * Setter method for converters.
	 * @param map - Map with (javaType, Converter)
	 */
	public void setConverters(Map map) {
		converters = map;
	}

}
