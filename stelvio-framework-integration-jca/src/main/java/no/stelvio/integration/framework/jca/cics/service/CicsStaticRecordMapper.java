package no.stelvio.integration.framework.jca.cics.service;

import com.ibm.connector2.cics.ECIInteractionSpec;
import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.integration.framework.jca.cics.records.CICSGenericRecord;
import no.stelvio.integration.framework.jca.service.RecordMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import java.util.HashMap;
import java.util.Map;


/**
 * Creates interaction and records to communicate with CICS Resource Adapter.
 *
 * @author person5b7fd84b3197, Accenture
 * @version $Id: CicsStaticRecordMapper.java 2800 2006-03-01 10:48:32Z skb2930 $
 */
public class CicsStaticRecordMapper implements RecordMapper {
	private static final Log log = LogFactory.getLog(CicsStaticRecordMapper.class);
	private Map cache = new HashMap(12);
// TODO: RecordConverter is inside the hibernate module which is removed for now
//	private RecordConverter recordConverter;

	/**
	 * {@inheritDoc}
	 *
	 * The record type holds a String.
	 */
	public Record classToRecord(String recordName, InteractionProperties interProps, Object bean)
		throws ServiceFailedException {

// TODO: Is inside the hibernate module which is removed for now
//		Configuration configuration = getConfiguration(interProps, retrieveClassloader());
//		RootClass clazz = RecordHelper.getServiceRootClass(configuration, recordName);

//		if (log.isDebugEnabled()) {
//			log.debug("RootClassName: " + clazz.getName());
//		}

		StringBuffer buffer = new StringBuffer();

		// Create a string from the given service
		try {
// TODO: Is inside the hibernate module which is removed for now
//			recordConverter.classToRecord(configuration, clazz, bean, buffer);
		} catch (Exception e) {
			throw new ServiceFailedException(FrameworkError.JCA_RECORD_CONVERSION_ERROR, e, "TODO"/*clazz.getName()*/);
		}

		if (log.isDebugEnabled()) {
			log.debug("InputRecord length : " + buffer.length());
			log.debug("Input Record:  '" + buffer.toString() + "'");
		}

		// Create the CICS Record
		CICSGenericRecord record = new CICSGenericRecord();
		record.setText(buffer.toString());

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
			log.debug("ReturnRecord from Cics: " + cicsRecord.getText());
		}

// TODO: Is inside the hibernate module which is removed for now
//		Configuration configuration = getConfiguration(interProps, retrieveClassloader());
//		RootClass clazz = RecordHelper.getServiceRootClass(configuration, recordName);

		try {
//			return recordConverter.recordToClass(configuration, cicsRecord.getText(), clazz);
            return null;
        } catch (Exception e) {
			throw new ServiceFailedException(FrameworkError.JCA_RECORD_CONVERSION_ERROR, e, "test"/*clazz.getName()*/);
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
			spec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
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
	 * Helper method to load the mapping files.
	 *
	 * @param interaction the interactionProperties.
	 * @param contextClassLoader the <code>ClassLoader</code> to use for loading the mapping files.
	 * @return Configuration the record mapper configuration.
	 */
// TODO: Uses the hibernate module which is removed for now
/*
	private synchronized Configuration getConfiguration(InteractionProperties interaction, final ClassLoader contextClassLoader) {

		Configuration config = (Configuration) cache.get(interaction);

		if (null != config) {
			return config;
		}

		Configuration newConfig = newConfig();

		for (int i = 0; i < interaction.getMappingFiles().size(); i++) {
			String mapping = (String) interaction.getMappingFiles().get(i);
			InputStream stream = contextClassLoader.getResourceAsStream(mapping);

			try {
				newConfig.addInputStream(stream);
			} catch (MappingException e) {
				throw new SystemException(FrameworkError.JCA_GET_CONFIG_ERROR, e, mapping);
			}
		}

		cache.put(interaction, newConfig);

		return newConfig;
	}
*/

	/**
	 * Creates an empty configuration.
	 *
	 * @return an empty configuration.
	 */
// TODO: Uses the hibernate module which is removed for now
/*
	Configuration newConfig() {
		return new Configuration();
	}
*/

	/**
	 * Helper method for retrieving the class loader to use for loading mapping files.
	 *
	 * @return the class loader to use for loading mapping files.
	 */
	private ClassLoader retrieveClassloader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Sets the <code>RecordConverter</code> to use when converting from record to instance and vice versa.
	 *
	 * @param recordConverter the <code>RecordConverter</code> to use.
	 * @see RecordConverter
	 */
// TODO: Uses the hibernate module which is removed for now
/*
	public void setRecordConverter(final RecordConverter recordConverter) {
		this.recordConverter = recordConverter;
	}
*/
}
