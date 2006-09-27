package no.stelvio.integration.jca.service;

import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import org.apache.commons.lang.StringUtils;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.ejb.LookupHelper;
import no.stelvio.common.error.ErrorHandler;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.performance.MonitorKey;
import no.stelvio.common.performance.PerformanceMonitor;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;
import no.stelvio.integration.jca.cics.service.InteractionProperties;
import no.stelvio.integration.service.IntegrationService;


/**
 * Provide connectivity to a legacy system through the use of JCA.
 *
 * @author Thomas Kvalvag, Accenture
 * @version $Revision: 2756 $ $Author: skb2930 $ $Date: 2006-02-01 13:57:00 +0100 (Wed, 01 Feb 2006) $
 */
public class JCAService extends IntegrationService {

	/** The error code from the CICS Transaction Gateway (CTG) that is used when the CTG is overloaded with work. */
	static final String WORK_WAS_REFUSED_ERROR = "ERROR_WORK_WAS_REFUSED";

	/** The performance monitoring key */
	private static final MonitorKey MONITOR_KEY = new MonitorKey("JCAService", MonitorKey.RESOURCE);

	/** Holds the jndi name of the connection factory */
	private String jndiName = null;

	/** Holds all configured interactions */
	private Map interactions = null;

	/** The JNDI helper used for lookups */
	private LookupHelper lookupHelper = null;

	/** The recordMapper to use for generating record*/
	private RecordMapper recordMapper = null;

	/** Constant for input query */
	private static final String SERVICE_NAME = "SystemServiceName";

	/** BISYS defect # 705 - JCAService gjør JNDI lookup hver gang */
	private ConnectionFactory connectionFactory = null;


	/**
	 * Validates the configuration of this service and performs further initialization. This method
	 * should be called after all the properties have been set.
	 */
	public void init() {
		if (log.isDebugEnabled()) {
			log.debug("Initializing JCAService with the following properties:");
			log.debug("Lookup helper:" + lookupHelper);
			log.debug("Record mapper:" + recordMapper);
			log.debug("JNDI name:" + jndiName);
			log.debug("Interactions:" + interactions);
		}

		if (null == lookupHelper) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "lookupHelper");
		}

		if (null == recordMapper) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "recordMapper");
		}

		if (StringUtils.isEmpty(jndiName)) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "jndiName");
		}

		if (null == interactions || interactions.isEmpty()) {
			throw new SystemException(FrameworkError.JCA_SERVICE_PROPERTY_MISSING, "interactions");
		}
	}

	/**
	 * This method is responsible for calling the configured JCA service with the specified input and return the output from
	 * the service.
	 * 
	 * @param request the service request.
	 * @return ServiceResponse the result from the backend execution.
	 * @throws ServiceFailedException if the service fails to execute.
	 * @see IntegrationService#doExecute(ServiceRequest)
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		String service = (String) request.getData(SERVICE_NAME);

		if (null == service) {
			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING);
		}

		Object inputData = request.getData(service);

		// get the configuration for the interaction 
		InteractionProperties properties = (InteractionProperties) interactions.get(service);
		// Create the interaction to be used to communicate with the JCA ResourceAdapter
		InteractionSpec interactionSpec = recordMapper.createInteractionSpec(properties);
		// Create a input record
		Record record = recordMapper.classToRecord(service, properties, inputData);

		// Get the connection for from the ConnectionFactory configured for given jndi name
		Connection con = getConnection(jndiName);

		// Create the interaction
		Interaction inter = null;
		try {
			inter = con.createInteraction();
		} catch (ResourceException e) {
			// Close the connection if possible
			closeConnection(con);
			throw new SystemException(FrameworkError.JCA_CREATE_INTERACTION_ERROR, e);
		}

		try {
			PerformanceMonitor.start(MONITOR_KEY, service);
			inter.execute(interactionSpec, record, record);
			PerformanceMonitor.end(MONITOR_KEY);
		} catch (ResourceException re) {
			PerformanceMonitor.fail(MONITOR_KEY);

			// The CICS Transaction Gateway is overloaded with work
			if (WORK_WAS_REFUSED_ERROR.equals(re.getErrorCode())) {
				throw new SystemException(FrameworkError.JCA_WORK_WAS_REFUSED_ERROR, re);
			} else {
				throw new SystemException(FrameworkError.JCA_INTERACT_ERROR, re);
			}
		} catch (RuntimeException re) {
			PerformanceMonitor.fail(MONITOR_KEY);
			throw re;
		} finally {
			// Allways close the connection at this point
			closeConnection(con);
		}

		// Convert JCA record to a bean
		Object obj = recordMapper.recordToClass(service, properties, record);

		return new ServiceResponse(service, obj);
	}

	/**
	 * This method is a helper method to create a connection to CTG using standard JCA functionality.
	 * The method requires a jndi name to be assosiated with a RA connection.
	 * If method fails to get a connection null will be returned
	 *
	 * @param jndiName name of the connection.
	 * @return Connection an open connection to CTG.
	 */
	private Connection getConnection(String jndiName) {
		// Lookup connection factory from name server
		if (null == connectionFactory) {
			connectionFactory = (ConnectionFactory) lookupHelper.lookup(jndiName, null);
		}

		Connection connection = null;

		try {
			connection = connectionFactory.getConnection();
		} catch (ResourceException re) {
			// Close the connection if possible
			closeConnection(connection);
			throw new SystemException(FrameworkError.JCA_GET_CONNECTION_ERROR, re, jndiName);
		}

		return connection;
	}

	/**
	 * This method will close a connection, if not already closed.
	 *
	 * @param con the JCA connection to close.
	 */
	private void closeConnection(Connection con) {
		if (null != con) {
			try {
				con.close();
			} catch (IllegalStateException ise) {
				if (log.isWarnEnabled()) {
					log.warn("Connection is already closed: " + ise.getMessage());
				}
			} catch (ResourceException e) {
				ErrorHandler.handleError(new SystemException(FrameworkError.JCA_CLOSE_CONNECTION_ERROR, e));
			}
		}
	}

	/**
	 * Setter for Interactions
	 * @param map - The configured interactions
	 */
	public void setInteractions(Map map) {
		interactions = map;
	}

	/**
	 * Setter for lookupHelper
	 * @param helper - The jndihelper
	 */
	public void setLookupHelper(LookupHelper helper) {
		lookupHelper = helper;
	}

	/**
	 * Setter for jndi name
	 * @param string - The jndi name
	 */
	public void setJndiName(String string) {
		jndiName = string;
	}

	/**
	 * Setter for RecordMapper.
	 * @param mapper - The mapper.
	 */
	public void setRecordMapper(RecordMapper mapper) {
		recordMapper = mapper;
	}
}
