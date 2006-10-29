package no.stelvio.common.ejb;

import no.stelvio.common.error.old.ErrorHandler;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.LocalService;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceLocator;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * AbstractServiceFacade is the base class for all implementations of the session facades
 * in front of the business services and integration services.
 * 
 * @author person7553f5959484
 * @version $Revision: 2141 $ $Author: skb2930 $ $Date: 2005-03-21 15:39:14 +0100 (Mon, 21 Mar 2005) $
 */
public abstract class AbstractServiceFacade extends SessionAdapter implements LocalService {

	/** service locator for looking up services */
	protected ServiceLocator serviceLocator = null;

	/** 
	 * Template method to ensure reuse of common steps for all service executions
	 * through session facades. This template method also dictates the overall 
	 * structure and order of the processing.
	 * 
	 * {@inheritDoc}
	 * @see no.stelvio.common.service.LocalService#execute(no.stelvio.common.service.ServiceRequest)
	 */
	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Starting service " + request.getServiceName());
			}

			ServiceResponse response = doExecute(request);

			if (log.isDebugEnabled()) {
				log.debug("Service " + request.getServiceName() + " finished");
			}

			return response;
		} catch (ServiceFailedException sfe) {
			if (sfe.isRollbackOnly()) {
				try {
					getSessionContext().setRollbackOnly();
				} catch (IllegalStateException ise) {
					// The sub class has bean managed transactions and is not 
					// allowed to call this method.
					if (log.isDebugEnabled()) {
						log.debug(
							"Not allowed to call getSessionContext().setRollbackOnly() beacuse "
								+ getClass().getName()
								+ " has bean managed transactions",
							ise);
					}
				}
			}
			throw (ServiceFailedException) ErrorHandler.handleError(sfe);
		} catch (SystemException se) {
			throw (SystemException) ErrorHandler.handleError(se);
		} catch (RuntimeException re) {
			throw (RuntimeException) ErrorHandler.handleError(re);
		}
	}

	/** 
	 * Sub classes implement this method to execute the main processing of the session facade.
	 * 
	 * @see #execute(no.stelvio.common.service.ServiceRequest)
	 */
	protected abstract ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException;

	/**
	 * Assigns a service locator for looking up services from the EJB instance.
	 * 
	 * @param serviceLocator the service locator instance.
	 */
	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

}
