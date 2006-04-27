package no.trygdeetaten.integration.framework.jca.service.stubs;

import java.util.Map;

import javax.resource.cci.Record;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;
import no.trygdeetaten.integration.framework.jca.cics.records.CICSGenericRecord;
import no.trygdeetaten.integration.framework.jca.cics.service.InteractionProperties;
import no.trygdeetaten.integration.framework.jca.service.RecordMapper;
import no.trygdeetaten.integration.framework.service.IntegrationService;

/**
 * Stub for integration with Oppdrag.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragStub.java 2792 2006-02-28 19:19:35Z skb2930 $
 */
public class OppdragStub extends IntegrationService {
	/** Holds all configured interactions */
	private Map interactions = null;

	/** The recordMapper to use for generating record*/
	private RecordMapper recordMapper = null;

	/** Constant for input query */
	private static final String SERVICE_NAME = "SystemServiceName";

	/**
	 * Validates the configuration of this service and performs further initialization. This method
	 * should be called after all the properties have been set.
	 */
	public void init() {
		// do nothing
	}

	/** 
	 * {@inheritDoc}
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		String service = (String) request.getData(SERVICE_NAME);

		if (null == service) {
			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING);
		}

		Object inputData = request.getData(service);

		// get the configuration for the interaction 
		InteractionProperties properties = (InteractionProperties) interactions.get(service);
		
		// Create a input record
		Record record = recordMapper.classToRecord(service, properties, inputData);
		CICSGenericRecord rec = (CICSGenericRecord) record;
		
		// Create and return the response object
		return new ServiceResponse("CopyBook", rec.getText());
	}
	
	/**
	 * Setter for Interactions
	 * @param map - The configured interactions
	 */
	public void setInteractions(Map map) {
		interactions = map;
	}

	/**
	 * Setter for RecordMapper.
	 * @param mapper - The mapper.
	 */
	public void setRecordMapper(RecordMapper mapper) {
		recordMapper = mapper;
	}


}
