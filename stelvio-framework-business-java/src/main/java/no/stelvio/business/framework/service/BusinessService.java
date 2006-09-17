package no.stelvio.business.framework.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.jmx.ServiceLevelConstants;
import no.stelvio.common.framework.jmx.ServiceLevelMBean;
import no.stelvio.common.framework.jmx.ServiceLevelMBeanFactory;
import no.stelvio.common.framework.performance.MonitorKey;
import no.stelvio.common.framework.performance.PerformanceMonitor;
import no.stelvio.common.framework.service.LocalService;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * Abstract class that all implementations of an <i>Business Service</i> should extend.
 * <p/>
 * Sub classes should be implemented to support concurrent processing by multiple threads.
 * In order for sub classes to be thread safe, they should never:
 * <ul>
 * 		<li> implement class variables to hold state </li>
 * 		<li> implement instance variables to hold state </li>
 * </ul>
 * All references must be passed by value between methods and classes as input and output parameters.
 * 
 * @author person7553f5959484
 * @version $Revision: 2604 $ $Author: psa2920 $ $Date: 2005-11-01 18:42:58 +0100 (Tue, 01 Nov 2005) $
 */
public abstract class BusinessService implements LocalService, BeanNameAware {

	/** Holds the <code>ServiceLevelMBean</code> to use. */
	private ServiceLevelMBean serviceLevelMBean = null;

	private final MonitorKey monitorKey = new MonitorKey(getClass().getName(), MonitorKey.ADDITIONAL);

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected Log log = LogFactory.getLog(getClass());

	/**
	 * Set the name of the bean in the bean factory that created this bean.
	 * <p/>
	 * Invoked after population of normal bean properties but before an init
	 * callback like InitializingBean's afterPropertiesSet or a custom init-method.
	 * 
	 * @param beanName the name of the bean in the factory
	 * @see BeanNameAware#setBeanName(String)
	 */
	public void setBeanName(String beanName) {
		serviceLevelMBean = ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_BUSINESS, beanName);
	}

	/**
	 * Executes all business service processing steps in a predefined order using this template method.
	 * <ol>
	 * 	<li> Start performance monitoring
	 * 	<li> Perform pre processing (doPreExecute)
	 * 	<li> Perform business processing (doExecute)
	 * 	<li> Perform post processing (doPostExceute)
	 * 	<li> Stop performance monitoring
	 * 	<li> Release resources (release)
	 * </ol>
	 * 
	 * @inheritDoc
	 * @see LocalService#execute(ServiceRequest)
	 */
	public final ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
		long start = System.currentTimeMillis();
		try {
			PerformanceMonitor.start(monitorKey, request.getServiceName());
			doPreExecute(request);
			ServiceResponse response = doExecute(request);
			doPostExecute(response);
			PerformanceMonitor.end(monitorKey);

			if (serviceLevelMBean != null) {
				serviceLevelMBean.add(System.currentTimeMillis() - start, null);
			}

			return response;
		} catch (ServiceFailedException sfe) {
			PerformanceMonitor.fail(monitorKey);

			if (serviceLevelMBean != null) {
				serviceLevelMBean.add(System.currentTimeMillis() - start, sfe);
			}

			throw sfe;
		} catch (RuntimeException re) {
			PerformanceMonitor.fail(monitorKey);

			if (serviceLevelMBean != null) {
				serviceLevelMBean.add(System.currentTimeMillis() - start, re);
			}

			throw re;
		} finally {
			try {
				release();
			} catch (RuntimeException re) {
				throw new SystemException(FrameworkError.SERVICE_DESTROY_ERROR, re);
			}
		}
	}

	/**
	 * Sub classes may override this method to implement pre-processing of business logic.
	 * 
	 * @param	request the <i>ServiceRequest</i> object sent from
	 * 			the client as input to the service.
	 * 
	 * @throws 	ServiceFailedException if service execution fails.
	 */
	protected void doPreExecute(ServiceRequest request) throws ServiceFailedException {
	}

	/**
	 * Sub classes should implement the core business service processing inside this method.
	 * 
	 * @param	request the <i>ServiceRequest</i> object sent from
	 * 			the client as input to the service.
	 * 
	 * @return	response the <i>ServiceResponse</i> object sent to 
	 * 			the client as output from the service.
	 * 
	 * @throws 	ServiceFailedException if service execution fails.
	 */
	protected abstract ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException;

	/**
	 * Sub classes may override this method to implement post-processing of business logic.
	 * 
	 * @param	response the <i>ServiceResponse</i> object sent to 
	 * 			the client as output from the service.
	 * 
	 * @throws 	ServiceFailedException if service execution fails.
	 */
	protected void doPostExecute(ServiceResponse response) throws ServiceFailedException {
	}

	/** Release any bound or open resources */
	protected void release() {
	}

	/**
	 * Validates that the specified key is holds data on the request.
	 * 
	 * @param request the input to the service
	 * @param key the identifier
	 * @throws ServiceFailedException if data is missing
	 */
	protected void validate(ServiceRequest request, String key) throws ServiceFailedException {
		if (null == request.getData(key)) {
			throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, key);
		}
	}

	/**
	 * Henter et datafelt ut fra requesten og sjekker om den har verdi. 
	 * Kaster en ServiceFailedException med beskjed om at <i>value</i> mangler for <i>key</i> 
	 * dersom <i>value</i> er null (eller tom dersom value er en tekststreng). 
	 * @param request Requesten som inneholder datafeltet
	 * @param key Nøkkelen for å finne datafeltet
	 * @return Object Datafeltet
	 * @throws SystemException hvis datafeltet ikke har verdi
	 */
	protected Object retrieveRequiredData(ServiceRequest request, String key) throws SystemException {
		Object value = request.getData(key);

		if (null == value || value instanceof String && StringUtils.isBlank((String) value)) {
			throw new SystemException(FrameworkError.SERVICE_INPUT_MISSING, key);
		}

		return value;
	}
}
