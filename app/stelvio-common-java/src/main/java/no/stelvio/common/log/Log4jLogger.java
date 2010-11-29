package no.stelvio.common.log;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

import org.apache.log4j.MDC;

/**
 * Log4j Logger.
 * <p>
 * Abstract base logger to be used in logger implementations. Supports custom
 * log4j MDC-properties that can be used to log application request context data
 * in log-files. Requires request context to have been provide
 * </p>
 * <p>
 * MDC Fields:
 * <ul>
 * <li>applicationKey
 * <li>module
 * <li>process
 * <li>screen
 * <li>transaction
 * <li>user
 * </ul>
 * Logger implementations that use this class needs to configure log-layout
 * conversion pattern, and must have configured the application request context
 * top be filled with context data. Example:
 * <code>[log appender].layout.ConversionPattern=[%X{user},%X{screen},%X{process},%X{transaction},%X{applicationKey}]</code>
 * </p>
 * 
 * @see RequestContext Used to retrieve context data
 * @see MdcConstants Defines MDC fields
 * 
 * @author person15754a4522e7
 * @author person983601e0e117
 */
public abstract class Log4jLogger {
	
	/**
	 * Sets properties from RequestContext (if it is set on
	 * RequestContextHolder) using {@link MDC}.
	 */
	protected void setMdcProperties() {
		// Add fields from RequestContext
		if (RequestContextHolder.isRequestContextSet()) {
			RequestContext requestContext = RequestContextHolder.currentRequestContext();
			if (requestContext.getComponentId() != null) {
				MDC.put(MdcConstants.MDC_APPLICATION, requestContext.getComponentId());
			}
			if (requestContext.getScreenId() != null) {
				MDC.put(MdcConstants.MDC_SCREEN, requestContext.getScreenId());
			}
			if (requestContext.getUserId() != null) {
				MDC.put(MdcConstants.MDC_USER, requestContext.getUserId());
			}
			if (requestContext.getTransactionId() != null) {
				MDC.put(MdcConstants.MDC_TRANSACTION, requestContext.getTransactionId());
			}
			if (requestContext.getProcessId() != null) {
				MDC.put(MdcConstants.MDC_PROCESS, requestContext.getProcessId());
			}
			if (requestContext.getModuleId() != null) {
				MDC.put(MdcConstants.MDC_MODULE, requestContext.getModuleId());
			}
		}
		//Just ignore if not configured
	}
	
	/**
	 * Resets the request context MDC properties set by {@link #setRequestContextMdcProperties()}.
	 *
	 */
	protected void resetMdcProperties() {
		MDC.remove(MdcConstants.MDC_APPLICATION);
		MDC.remove(MdcConstants.MDC_SCREEN);
		MDC.remove(MdcConstants.MDC_USER);
		MDC.remove(MdcConstants.MDC_TRANSACTION);
		MDC.remove(MdcConstants.MDC_PROCESS);	
		MDC.remove(MdcConstants.MDC_MODULE);
	}	
	
}
