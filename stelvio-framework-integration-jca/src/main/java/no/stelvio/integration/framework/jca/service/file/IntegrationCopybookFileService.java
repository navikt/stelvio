package no.stelvio.integration.framework.jca.service.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.resource.cci.Record;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;
import no.stelvio.integration.framework.jca.cics.records.CICSGenericRecord;
import no.stelvio.integration.framework.jca.cics.service.InteractionProperties;
import no.stelvio.integration.framework.jca.service.RecordMapper;
import no.stelvio.integration.framework.service.IntegrationService;

/**
 * Service that writes or read copybooks to a file.
 *
 * @author persone5d69f3729a8, Accenture
 * @version $Id: IntegrationCopybookFileService.java 2793 2006-02-28 19:25:45Z skb2930 $
 */
public class IntegrationCopybookFileService extends IntegrationService {

	/** Holds all configured interactions */
	private Map interactions = null;

	/** The recordMapper to use for generating record */
	private RecordMapper recordMapper = null;

	/** Constant for input query */
	private static final String SERVICE_NAME = "SystemServiceName";

	/** Constant for input file path */
	private static final String FILE_DIR = "FileDir";

	/** Constant for input file name */
	private static final String FILE_NAME = "FileName";

	/** Constant to decide if read or write service */
	private static final String WRITE = "ServiceWrite";

	/** Constant for object returned in response */
	private static final String OUT = "OUT";

	/** Called upon initialization */
	public void init() {
		if (log.isDebugEnabled()) {
			log.debug("Initializing InntektIntegrationFileService");
		}
		if (null == interactions || interactions.size() < 1) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "interactions");
		}
		if (null == recordMapper) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "recordMapper");
		}
	}

	/** {@inheritDoc} */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		// buffer that holds string representations of a file
		StringBuffer buffer = new StringBuffer();

		String service = (String) request.getData(SERVICE_NAME);

		if (null == service) {
			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING);
		}

		Object inputData = request.getData(service);

		// get the configuration for the interaction 
		InteractionProperties properties = (InteractionProperties) interactions.get(service);

		// get file path, filename and read/write params
		String filePath = (String) request.getData(FILE_DIR);
		String fileName = (String) request.getData(FILE_NAME);
		Boolean isWrite = (Boolean) request.getData(WRITE);

		ServiceResponse response = null;
		FileReader fileReader = null;
		FileWriter fileWriter = null;

		try {
			File fileDir = new File(filePath, fileName);

			// write to file
			if (isWrite.booleanValue()) {
				if (!fileDir.exists()) {
					fileDir.createNewFile();
				}

				// loops throuh all records
				List list = (List) inputData;
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Object o = iterator.next();
					// Create a input record
					Record record = recordMapper.classToRecord(service, properties, o);
					CICSGenericRecord rec = (CICSGenericRecord) record;
					buffer.append(rec.getText() + "\r\n");
				}

				fileWriter = new FileWriter(fileDir, true);
				fileWriter.write(buffer.toString());
				fileWriter.flush();

				response = new ServiceResponse(OUT, inputData);

				// read from fil
			} else {
				// if going to read file then file have to exist			
				if (!fileDir.exists()) {
					throw new SystemException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, "fil");
				}

				fileReader = new FileReader(fileDir);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				CICSGenericRecord record2 = null;
				String recordString = null;
				List records = new ArrayList();

				while ((recordString = bufferedReader.readLine()) != null) {
					if (null == recordString || "".equals(recordString)) {
						break;
					}

					record2 = new CICSGenericRecord();
					record2.setText(recordString);
					Object o = recordMapper.recordToClass(service, properties, record2);
					records.add(o);
				}

				response = new ServiceResponse(OUT, records);
			}
		} catch (IOException ioe) {
			throw new SystemException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, ioe, "filsystem");
		} finally {
			if (null != fileReader) {
				try {
					fileReader.close();
				} catch (IOException e) {
					// Cannot do anything
				}
			}

			if (null != fileWriter) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					// Cannot do anything
				}
			}
		}

		return response;
	}

	/**
	 * Setter for Interactions
	 *
	 * @param map - The configured interactions
	 */
	public void setInteractions(Map map) {
		interactions = map;
	}

	/**
	 * Setter for RecordMapper.
	 *
	 * @param mapper - The mapper.
	 */
	public void setRecordMapper(RecordMapper mapper) {
		recordMapper = mapper;
	}
}
