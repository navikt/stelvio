package no.stelvio.common.core;

import java.util.Map;

/**
 * The org.springframework.core.Constants class can be used to parse other classes containing 
 * constant definitions of public static final members. This class is a subclass of the
 * Constants class in the springframework, and provides additional methods to access the
 * constant field values.
 * 
 * @author person6045563b8dec, Accenture
 * @version $Id$
 *
 */
public class ConstantsHolder extends org.springframework.core.Constants {

	/**
	 * Create a new Constants converter class wrapping the
	 * given class
	 * 
	 * @param clazz the class to analyze
	 */
	public ConstantsHolder(Class clazz) {
		super(clazz);
	}

	/**
	 * Exposes the constants as a Map from String field name to
	 * object value.
	 *  
	 * @return Map with field name - value pairs
	 */
	public final Map getValues() {
		return super.getFieldCache();
	}
}
