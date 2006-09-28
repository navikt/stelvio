package no.stelvio.common.ejb;

import javax.ejb.TransactionRolledbackLocalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.ExceptionWrapper;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.performance.MonitorKey;
import no.stelvio.common.performance.PerformanceMonitor;
import no.stelvio.common.service.LocalService;
import no.stelvio.common.service.ServiceDelegate;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceLocator;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * ServiceDelegateLocalImpl delegates processing to a local service facade. 
 * 
 * @author person7553f5959484, Accenture 
 * @version $Id: ServiceDelegateLocalImpl.java 2831 2006-03-08 13:23:23Z skb2930 $
 */
public class ServiceDelegateLocalImpl implements ServiceDelegate {

	/** Logger to be used for writing messages. */
	private Log log = LogFactory.getLog(this.getClass());

	/** The performance monitoring keys */
	private MonitorKey delegateMonitorKey = null;

	/** The service facade */
	private LocalService serviceFacade = null;

	/**	The platform specific vendor wrapper */
	private ExceptionWrapper exceptionWrapper = null;

	/**
	 * Lookup the service facade for delegation of remote service processing.
	 * 
	 * {@inheritDoc}
	 */
	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {

		try {
			PerformanceMonitor.start(delegateMonitorKey, request.getServiceName());
			ServiceResponse response = serviceFacade.execute(request);
			PerformanceMonitor.end(delegateMonitorKey);
			return response;
		} catch (ServiceFailedException sfe) {
			PerformanceMonitor.fail(delegateMonitorKey);
			throw sfe;
		} catch (TransactionRolledbackLocalException trle) {
			PerformanceMonitor.fail(delegateMonitorKey);
			throw new SystemException(FrameworkError.TRANSACTION_ROLLED_BACK_ERROR, trle);
		} catch (RuntimeException re) {
			PerformanceMonitor.fail(delegateMonitorKey);

			// BISYS defect #618
			Throwable cause = exceptionWrapper.getCause(re);

			if (cause instanceof ServiceFailedException) {
				throw (ServiceFailedException) cause;
			} else if (cause instanceof SystemException) {
				throw (SystemException) cause;
			} else {
				throw new SystemException(FrameworkError.SERVICE_FACADE_COMMUNICATION_ERROR, re);
			}
		}
	}

	/**
	 * Implementation does not assign the specified service locator.
	 * 
	 * {@inheritDoc}
	 */
	public final void setServiceLocator(ServiceLocator serviceLocator) {
		if (log.isWarnEnabled()) {
			log.warn(
				"setServiceLocator() invoked but will have no affect. "
					+ "Class does not hold an internal reference to a ServiceLocator.");
		}
	}

	/**
	 * Sets the jndi name of the service facade to use.
	 * 
	 * @param serviceFacadeName the name of the service facade
	 */
	public void setServiceFacadeName(String serviceFacadeName) {
		if (log.isWarnEnabled()) {
			log.warn("setServiceFacadeName() invoked but will have no affect. Use setServiceFacade() instead.");
		}
	}

	/**
	 * Sets the monitor key to use for this delegate
	 * @param key the monitor key.
	 */
	public void setDelegateMonitorKey(MonitorKey key) {
		delegateMonitorKey = key;
	}

	/**
	 * Returns the <i>Local Service</i> that this instance delegates processing to.
	 * 
	 * @return the the <i>Local Service</i> instance.
	 */
	public LocalService getServiceFacade() {
		return serviceFacade;
	}

	/**
	 * Assign an implementation of the <i>Local Service</i> to this instance.
	 * 
	 * @param service the <i>Local Service</i> instance.
	 */
	public void setServiceFacade(LocalService service) {
		serviceFacade = service;
	}

	/**
	 * Sets the platform specific exception wrapper.
	 * 
	 * @param wrapper the exception wrapper.
	 */
	public void setExceptionWrapper(ExceptionWrapper wrapper) {
		exceptionWrapper = wrapper;
	}

}