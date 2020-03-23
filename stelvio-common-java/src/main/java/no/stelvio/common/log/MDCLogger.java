package no.stelvio.common.log;

/**
 * MDC Logger.
 * <p>
 * Abstract base logger to be used in logger implementations. Supports custom
 * MDC-properties that can be used to log application request context data
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
 */
public abstract class MDCLogger {
	
	/**
	 * Sets properties from RequestContext (if it is set on
	 * RequestContextHolder) using {@link MDC}.
	 */
	protected void setMdcProperties() {
		MDCOperations.setMdcProperties();
	}
	
	/**
	 * Resets the request context MDC properties set by {@link #setRequestContextMdcProperties()}.
	 *
	 */
	protected void resetMdcProperties() {
		MDCOperations.resetMdcProperties();
	}	
	
}
