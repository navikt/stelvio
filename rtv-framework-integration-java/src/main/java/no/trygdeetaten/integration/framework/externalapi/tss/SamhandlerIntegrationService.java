package no.trygdeetaten.integration.framework.externalapi.tss;

import com.ibm.no.rtv.samhandler.ejb.SamhandlerFacade;
import com.ibm.no.rtv.samhandler.enums.TransType;
import com.ibm.no.rtv.samhandler.value.TSSAjourholdMsgVO;
import com.ibm.no.rtv.samhandler.value.TSSMsgVO;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.performance.MonitorKey;
import no.trygdeetaten.common.framework.performance.PerformanceMonitor;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;
import no.trygdeetaten.integration.framework.service.IntegrationService;

/**
 * Integration service for synchronous communication to TSS.
 * This service uses remote EJB look up to SamhandlerFacade.
 * 
 * @author personbf936f5cae20, Accenture
 * @version $Id: SamhandlerIntegrationService.java 2824 2006-03-04 17:50:40Z skb2930 $
 */
public class SamhandlerIntegrationService extends IntegrationService {
	/** Proxy to remote samhandler service */
	private SamhandlerFacade proxy = null;

	/** In paramater to samhandler service */
	private static final String IN_SAMHANDLER = "IN_SAMHANDLER";

	/** Out parameter from samhandler service */
	private static final String OUT_SAMHANDLER = "OUT_SAMHANDLER";

	/** Out parameter to samhandler service */
	private static final String TRANSTYPE = "TRANSTYPE";

	/** The performance monitoring key */
	protected static final MonitorKey MONITOR_KEY = new MonitorKey("SamhandlerIntegrationService", MonitorKey.RESOURCE);

	/**
	 * Validates the configuration of this service and performs further initialization. This method
	 * should be called after all the properties have been set.
	 */
	public void init() {
		if (null == proxy) {
			throw new SystemException(FrameworkError.SERVICE_CREATION_ERROR, "proxy");
		}
	}

	/**
	 * Performs an operation against TSS system using EJB as the means of synchronous communications.
	 * 
	 * @param request the request to the backend system.
	 * @return The response from the backend system.
	 * @throws ServiceFailedException the system returned an error during the execution of the request.
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {

		PerformanceMonitor.start(MONITOR_KEY);

		if (!(request.getData(IN_SAMHANDLER) instanceof TSSMsgVO)) {
			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "Missing input error: TSSMsgVO");
		}
		if (!(request.getData(TRANSTYPE) instanceof TransType)) {
			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "Missing input error: TSSTransType");
		}

		// creates a new service response and adds it to the response
		ServiceResponse response = new ServiceResponse();
		TSSMsgVO inSamhandler = (TSSMsgVO) request.getData(IN_SAMHANDLER);
		TransType transtype = (TransType) request.getData(TRANSTYPE);

		try {
			if (transtype.equals(TransType.K415E985)) {

				response.setData("OUT_SAMHANDLER_LIST", proxy.executeQuery(inSamhandler, transtype));

			} else if (!transtype.equals(TransType.TSSAJOURHOLD)) {

				response.setData(OUT_SAMHANDLER, proxy.executeQuery(inSamhandler, transtype));

			} else {
				Object object = proxy.executeQuery(inSamhandler, transtype);
				response.setData(OUT_SAMHANDLER, object);

				if (log.isDebugEnabled()) {
					TSSAjourholdMsgVO ajourholdmsg = (TSSAjourholdMsgVO) object;
					log.debug(ajourholdmsg.getK469MMELVO().getAlvorlighetsgrad());
					log.debug(ajourholdmsg.getK469MMELVO().getBeskr_melding());
					log.debug(ajourholdmsg.getK469MMELVO().getSection_navn());
					log.debug(ajourholdmsg.getK469MMELVO().getProgram_id());
				}
			}
		} catch (Exception e) {
			PerformanceMonitor.fail(MONITOR_KEY);
			throw new ServiceFailedException(FrameworkError.TSS_RECEIVE_ERROR, e);
		}

		PerformanceMonitor.end(MONITOR_KEY);
		return response;
	}

	/**
	 * Setter for proxy
	 * 
	 * @param service the proxy
	 */
	public void setProxy(SamhandlerFacade service) {
		proxy = service;
	}

}
