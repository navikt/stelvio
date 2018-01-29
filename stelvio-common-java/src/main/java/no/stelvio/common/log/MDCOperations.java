package no.stelvio.common.log;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

import org.slf4j.MDC;

public final class MDCOperations {

	/**
	 * Sets properties from RequestContext (if it is set on
	 * RequestContextHolder) using {@link MDC}.
	 */
	public static void setMdcProperties() {
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
	public static void resetMdcProperties() {
		MDC.remove(MdcConstants.MDC_APPLICATION);
		MDC.remove(MdcConstants.MDC_SCREEN);
		MDC.remove(MdcConstants.MDC_USER);
		MDC.remove(MdcConstants.MDC_TRANSACTION);
		MDC.remove(MdcConstants.MDC_PROCESS);	
		MDC.remove(MdcConstants.MDC_MODULE);
	}	

}
