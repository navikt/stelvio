package no.trygdeetaten.integration.framework.jca.cics.service;

import no.trygdeetaten.common.framework.error.ApplicationException;

/**
 * Mapper interface for the Fixed Record mapper.
 *
 * @author personbf936f5cae20, Accenture
 * @version $Id: FixedMapperWrapper.java 2755 2006-02-01 12:54:12Z skb2930 $
 */
public interface FixedMapperWrapper {

	/**
	 * Converts the fixed record format into a bean instance.
	 *
	 * @param frmappingFile the mapping file to be used for the conversion.
	 * @param fixedRecordText
	 * @return The generated bean instance.
	 * @throws ApplicationException if conversion fails.
	 */
	Object fixedRecordFormatToMap(String frmappingFile, String fixedRecordText) throws ApplicationException;

	/**
	 * Converts the bean instance into a fixed record format.
	 *
	 * @param frmappingFile the mapping file to be used for the conversion.
	 * @param bean	the bean to convert to a fixed record text.
	 * @return The generated fixed record text.
	 * @throws ApplicationException if conversion fails.
	 */
	String mapToFixedRecordFormat(String frmappingFile, Object bean) throws ApplicationException;
}
