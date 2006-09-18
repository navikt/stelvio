package no.stelvio.common.framework.ejb;

import java.rmi.RemoteException;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.context.RequestContext;
import no.stelvio.common.framework.error.ExceptionWrapper;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.performance.MonitorKey;
import no.stelvio.common.framework.performance.PerformanceMonitor;
import no.stelvio.common.framework.service.LocalService;
import no.stelvio.common.framework.service.ServiceDelegate;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceLocator;
import no.stelvio.common.framework.service.ServiceNotFoundException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * ServiceDelegateImpl is the base class that should be extended 
 * by local services delegating processing to remote services. 
 * 
 * @author person7553f5959484 
 * @version $Revision: 2832 $ $Author: skb2930 $ $Date: 2006-03-08 14:29:41 +0100 (Wed, 08 Mar 2006) $
 */
public class ServiceDelegateImpl implements LocalService, ServiceDelegate {

	// The performance monitoring keys
	private MonitorKey delegateMonitorKey;
	private final MonitorKey lookupMonitorKey;

	// The name to use for looking up the Facade
	protected String serviceFacadeName;

	// The Service Locator instance
	protected ServiceLocator serviceLocator = null;

	// The platform specific vendor wrapper
	private ExceptionWrapper exceptionWrapper = null;

	/**
	 * Constructs a service delegate that delegates remote processing via specified remote facade.
	 */
	public ServiceDelegateImpl() {
		this.lookupMonitorKey = new MonitorKey("ServiceLocator", MonitorKey.ADDITIONAL);
	}

	/**
	 * Lookup the service facade for delegation of remote service processing.
	 * 
	 * {@inheritDoc}
	 */
	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {

		RemoteService serviceFacade = null;
		try {
			PerformanceMonitor.start(lookupMonitorKey, request.getServiceName());
			serviceFacade = (RemoteService) serviceLocator.lookup(serviceFacadeName);
			PerformanceMonitor.end(lookupMonitorKey);
		} catch (ServiceNotFoundException e) {
			PerformanceMonitor.fail(lookupMonitorKey);
			throw new SystemException(FrameworkError.SERVICE_FACADE_NOT_FOUND, e, serviceFacadeName);
		} catch (ClassCastException e) {
			PerformanceMonitor.fail(lookupMonitorKey);
			throw new SystemException(FrameworkError.SERVICE_FACADE_TYPE_ERROR, e, serviceFacadeName);
		}
		try {
			request.setData(RequestContext.class.getName(), RequestContext.exportContext());
			PerformanceMonitor.start(delegateMonitorKey, request.getServiceName());
			ServiceResponse response = serviceFacade.execute(request);
			PerformanceMonitor.end(delegateMonitorKey);
			if (null != response) {
				RequestContext.importContext(response.getData(RequestContext.class.getName()));
			}
			return response;
		} catch (ServiceFailedException sfe) {
			PerformanceMonitor.fail(delegateMonitorKey);
			throw sfe;
		} catch (RemoteException re) {
			PerformanceMonitor.fail(delegateMonitorKey);

			// BISYS defect #618
			Throwable cause = exceptionWrapper.getCause(re);

			if (cause instanceof ServiceFailedException) {
				throw (ServiceFailedException) cause;
			} else if (cause instanceof SystemException) {
				throw (SystemException) cause;
			} else {
				throw new SystemException(FrameworkError.SERVICE_FACADE_COMMUNICATION_ERROR, re, serviceFacadeName);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setServiceLocator(final ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServiceFacadeName(String serviceFacadeName) {
		this.serviceFacadeName = serviceFacadeName;
	}

	/**
	 * Sets the monitor key to use for this delegate
	 * @param key the monitor key.
	 */
	public void setDelegateMonitorKey(MonitorKey key) {
		delegateMonitorKey = key;
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
