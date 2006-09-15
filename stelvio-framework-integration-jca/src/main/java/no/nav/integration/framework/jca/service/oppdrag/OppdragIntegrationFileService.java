package no.nav.integration.framework.jca.service.oppdrag;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.resource.cci.Record;
import org.apache.commons.lang.StringUtils;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;
import no.nav.integration.framework.jca.cics.records.CICSGenericRecord;
import no.nav.integration.framework.jca.cics.service.InteractionProperties;
import no.nav.integration.framework.jca.service.RecordMapper;
import no.nav.integration.framework.service.IntegrationService;

/**
 * Service that puts all oppdrag into a file.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragIntegrationFileService.java 2791 2006-02-28 19:18:42Z skb2930 $
 */
public class OppdragIntegrationFileService extends IntegrationService {

	/** Holds all configured interactions */
	private Map interactions = null;

	/** The recordMapper to use for generating record*/
	private RecordMapper recordMapper = null;

	/** File path */
	private String filePath = null;

	/** File name */
	private String fileName = null;

	/** Constant for input query */
	private static final String SERVICE_NAME = "SystemServiceName";

	/** Constant for output object */
	private static final String CB_STRING = "CB_STRING";

	/**
	 * To be called at startup
	 */
	public void init() {
		if (log.isDebugEnabled()) {
			log.debug("Initializing OppdragIntegrationFileService with the following properties:\nfilePath:" + filePath + "\nfileName:" + fileName);
		}

		if (null == filePath) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "filePath");
		}
		if (null == fileName) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "fileName");
		}
		if (null == interactions || interactions.isEmpty()) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "interactions");
		}
		if (null == recordMapper) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "recordMapper");
		}
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
		int length = 0;
		String recordString = null;

		try {
			File fileDir = new File(filePath, fileName);

			if (!fileDir.exists()) {
				fileDir.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(fileDir, true);

			// strips of starting and ending whitespaces
			recordString = StringUtils.strip(rec.getText(), null);
			
			// counts length
			length = recordString.length();
			length += 9;
			
			// final copybook string
			recordString = "MLEN" + StringUtils.leftPad(String.valueOf(length), 5, '0') + recordString;
			
			// writes and flush filewriter
			fileWriter.write(recordString);
			fileWriter.write("\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new SystemException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, "filsystem");
		}

		// Create and return the response object
		return new ServiceResponse(CB_STRING, rec.getText());

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

	/**
	 * Setter for fileName.
	 * @param fileName the name of the oppdrag file
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Setter for filePath.
	 * @param filePath the path to put the file
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
