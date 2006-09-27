package no.stelvio.integration.jms.formatter;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * Double formatting tailored for the Oppdrag system.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragDoubleFormatter.java 2391 2005-06-29 07:49:29Z psa2920 $
 */
public class OppdragDoubleFormatter extends GeneralizedFieldHandler {

	//	private static final String DATE_FORMAT = "yyyy-MM-dd";
	//	private static SimpleDateFormat formatter = null;
	//	static {
	//		formatter = (SimpleDateFormat) DateFormat.getDateInstance();
	//		formatter.setLenient(false);
	//		formatter.applyLocalizedPattern(DATE_FORMAT);
	//	}

	/** 
	 * {@inheritDoc}
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
	 */
	public Object convertUponGet(Object value) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
	 */
	public Object convertUponSet(Object value) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
	 */
	public Class getFieldType() {
		return null;
	}

}
