package no.stelvio.integration.framework.jca.cics.service;

import com.ibm.no.frmapper.Mapper;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.ApplicationException;

/**
 * Mapper som benytter FRmapper (Fixed Record mapper) fra IBM
 * 
 * @author personbf936f5cae20, Accenture
 * @version $Id: FixedMapperWrapperImpl.java 2755 2006-02-01 12:54:12Z skb2930 $
 */
public class FixedMapperWrapperImpl implements FixedMapperWrapper {
	private Mapper mapper = null;

	/**
	 * {@inheritDoc}
	 */
	public Object fixedRecordFormatToMap(String frmappingFile, String fixedRecordText) throws ApplicationException {
		try {
			return mapper.mapFromFixedRecordFormat(frmappingFile, fixedRecordText, null);
		}catch (Exception e){
			throw new ApplicationException(FrameworkError.JCA_RECORD_CONVERSION_ERROR, e, frmappingFile);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String mapToFixedRecordFormat(String frmappingFile, Object bean) throws ApplicationException {
		try {
			return mapper.mapToFixedRecordFormat(frmappingFile, bean);
		} catch (Exception e) {
			throw new ApplicationException(FrameworkError.JCA_RECORD_CONVERSION_ERROR, e, frmappingFile);
		}
	}

	/**
	 * Sets the mapper to use.
	 *
	 * @param mapper the mapper to use.
	 */
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
}
