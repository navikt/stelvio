package no.stelvio.common.converter;

import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/**
 * This class will be configured to load all nessesary Converter classes to be used with Apache BeanUtils.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class ConverterLoader {

	/** Holder for converters. */
	private Map converters = null;

	/**
	 * Method will be called by Configuration to initialize converters for ConvertUtils.
	 * 
	 */
	public void init() {
		// Register converters
		if (null == converters || converters.isEmpty()) {
			return;
		}

		for (Object allConverter : converters.keySet()) {
			String str = (String) allConverter;
			Class clazz;

			try {
				clazz = Class.forName(str);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}

			ConvertUtils.register((Converter) converters.get(str), clazz);
		}
	}

	/**
	 * Setter method for converters.
	 * 
	 * @param map
	 *            - Map with (javaType, Converter)
	 */
	public void setConverters(Map map) {
		converters = map;
	}

}
