package no.trygdeetaten.integration.framework.externalapi.oppdrag;

import java.rmi.RemoteException;

import org.springframework.core.NestedRuntimeException;

import com.ibm.no.rtv.oppdrag.bus.OppdragService;
import com.ibm.no.rtv.oppdrag.value.OppdragVO;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.error.system.OppdragException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;
import no.trygdeetaten.integration.framework.service.IntegrationService;

/**
 * Integration service for synchronous communication to Oppdrag system. This
 * service uses remote EJB look up to OppdragService.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragIntegrationService.java 2644 2005-11-25 15:24:55Z
 *          tkc2920 $
 */
public class OppdragIntegrationService extends IntegrationService {

	/** Proxy to remote oppdrag service */
	private OppdragService proxy = null;

	/** In paramater to oppdrag service */
	private static final String IN_OPPDRAG = "IN_OPPDRAG";

	/** Out parameter from oppdrag service */
	private static final String OUT_OPPDRAG = "OUT_OPPDRAG";

	/** Property to enable service */
	private boolean enabled = false;

	/**
	 * Validates the configuration of this service and performs further
	 * initialization. This method should be called after all the properties
	 * have been set.
	 */
	public void init() {
		if (null == proxy) {
			throw new SystemException(FrameworkError.OPPDRAG_SERVICE_PROPERTY_MISSING, "proxy");
		}
	}

	/**
	 * Performs an operation against OPPDRAG system using EJB as the means of
	 * synchronous communications.
	 * 
	 * @param request
	 *            the request to the backend system.
	 * @return The response from the backend system.
	 * @throws ServiceFailedException
	 *             the system returned an error during the execution of the
	 *             request.
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		if (log.isDebugEnabled()) {
			log.debug("OppdragIntegrationService - doExecute");
		}

		// creates a new service response and adds it to the response
		if (!enabled) {
			return new ServiceResponse(OUT_OPPDRAG, new OppdragVO());
		}

		if (!(request.getData(IN_OPPDRAG) instanceof OppdragVO)) {
			throw new ServiceFailedException(FrameworkError.OPPDRAG_MISSING_INPUT_ERROR, "Missing input error");
		}

		OppdragVO inOppdrag = (OppdragVO) request.getData(IN_OPPDRAG);
		OppdragVO outOppdrag;

		try {
			outOppdrag = proxy.executeQuery(inOppdrag);
			if (null == outOppdrag) {
				throw new OppdragException(FrameworkError.OPPDRAG_RECEIVE_ERROR,
						new String[] { "Error while receiving response." });
			}
			return new ServiceResponse(OUT_OPPDRAG, outOppdrag);
		} catch (Exception e) {
			Throwable cause = e;

			// fjerner Spring wrapping
			while (cause instanceof NestedRuntimeException) {
				cause = cause.getCause();
			}
			// fjerner RMI wrapping
			while (cause instanceof RemoteException) {
				cause = cause.getCause();
			}
			throw new ServiceFailedException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, cause, new String[] { "Oppdrag" });
		}
	}

	/**
	 * Setter for proxy
	 * 
	 * @param service
	 *            the proxy
	 */
	public void setProxy(OppdragService service) {
		proxy = service;
	}

	/**
	 * Setter for oppdrag enabled
	 * 
	 * @param enabled
	 *            if set to true
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
