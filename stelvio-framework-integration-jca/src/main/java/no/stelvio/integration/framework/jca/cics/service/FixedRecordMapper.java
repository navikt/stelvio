package no.stelvio.integration.framework.jca.cics.service;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.connector2.cics.ECIInteractionSpec;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.ApplicationException;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.integration.framework.hibernate.formater.Formater;
import no.stelvio.integration.framework.jca.cics.records.CICSGenericRecord;
import no.stelvio.integration.framework.jca.service.RecordMapper;


/**
 * Creates interaction and records to communicate with CICS Resource Adapter.
 *
 * @author personbf936f5cae20, Accenture
 * @version $Id: FixedRecordMapper.java 2755 2006-02-01 12:54:12Z skb2930 $
 */
public class FixedRecordMapper implements RecordMapper {
	private static final Log log = LogFactory.getLog(FixedRecordMapper.class);
	private FixedMapperWrapper mapperWrapper = null;

	/**
	 * Initialization method that checks the instance's internal state.
	 *
	 * @throws SystemException if state is not correct.
	 */
	public void init() throws SystemException {
		if (null == mapperWrapper) {
			throw new SystemException(FrameworkError.SERVICE_INIT_ERROR, "FixedRecordMapper");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * The record type holds a String.
	 */
	public Record classToRecord(String recordName, InteractionProperties interProps, Object bean)
		throws ServiceFailedException {

		// Create a string from the given service
		String frmappingFileIn = interProps.getFrmappingfileIn();

		if (log.isDebugEnabled()) {
			log.debug("recordName : " + recordName);
			log.debug("bean:  " + bean.toString());
		}

		String buffer;

		try {
			buffer = mapperWrapper.mapToFixedRecordFormat(frmappingFileIn, bean);
		} catch (ApplicationException e) {
			throw new ServiceFailedException(FrameworkError.JCA_RECORD_CONVERSION_ERROR, e, frmappingFileIn);
		}

		// Create the CICS Record
		CICSGenericRecord record = new CICSGenericRecord();
		record.setText(buffer);

		return record;
	}

	/**
	 * {@inheritDoc}
	 *
	 * The record type holds a String.
	 */
	public Object recordToClass(String recordName, InteractionProperties interProps, Record record)
		throws ServiceFailedException {

		CICSGenericRecord cicsRecord = (CICSGenericRecord) record;

		if (log.isDebugEnabled()) {
			log.debug("ReturnRecord from Cics: '" + cicsRecord.getText() + "'");
		}

		Formater stringFormater = interProps.getStringFormater();
		String fixedRecordText = (String) stringFormater.formatOutput(cicsRecord.getText());
		String frmappingFileOut = interProps.getFrmappingfileOut();

		try {
			return mapperWrapper.fixedRecordFormatToMap(frmappingFileOut, fixedRecordText);
		} catch (ApplicationException e) {
			throw new ServiceFailedException(FrameworkError.JCA_RECORD_CONVERSION_ERROR, e, frmappingFileOut);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public InteractionSpec createInteractionSpec(InteractionProperties interProps) {
		ECIInteractionSpec spec = new ECIInteractionSpec();

		try {
			spec.setFunctionName(interProps.getFunctionName());
			spec.setCommareaLength(interProps.getCommareaLength());
			spec.setReplyLength(interProps.getReplyLength());
			spec.setExecuteTimeout(interProps.getExecuteTimeout());
			spec.setInteractionVerb(InteractionSpec.SYNC_SEND_RECEIVE);
		} catch (ResourceException re) {
			throw new SystemException(FrameworkError.JCA_CREATE_INTERACTION_ERROR, re);
		}

		return spec;
	}

	/**
	 * {@inheritDoc}
	 */
	public InteractionSpec createInteraction(InteractionProperties interProps) {
		return createInteractionSpec(interProps);
	}

	/**
	 * The mapper to use
	 * 
	 * @param wrapper
	 */
	public void setMapperWrapper(FixedMapperWrapper wrapper) {
		mapperWrapper = wrapper;
	}
}
