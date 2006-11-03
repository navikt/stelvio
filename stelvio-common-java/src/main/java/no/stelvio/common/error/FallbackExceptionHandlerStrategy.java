package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo fall backs to making our own hard coded error as it is not in the database, should be put into the list of
 * strategies by DefaultExceptionHandler
 */
public class FallbackExceptionHandlerStrategy implements ExceptionHandlerStrategy {

	// The name of the log to send messages to
//	private static final String SYSTEM_LOG = "SYSTEM_LOG";

	// The logs
//	private Log debugLog = null;
//	private Log systemLog = null;

	// Determine if logging is configured or not
//	private boolean isLoggingConfigured;

	/**
	 * Configure debug logging and logging to the local system production log.
	 * If logging configuration fails, Throwables will be printed to System.err.
	 *
	 * {@inheritDoc}
	 * @see no.stelvio.common.error.Handler#init()
	 */
//	public void init() {
//		try {
//			debugLog = LogFactory.getLog(this.getClass());
//			systemLog = LogFactory.getLog(SYSTEM_LOG);
//			isLoggingConfigured = true;
//		} catch (Throwable t) {
//			isLoggingConfigured = false;
//			handleError(new RuntimeException("Failed to configure logging for DefaultHandlerImpl", t));
//		}
//	}

	/**
	 * Log the throwable's stacktrace if logging is configured, else print throwable,s stacktrace
	 * to System.err. The original Throwable is returned unmodified.
	 *
	 * {@inheritDoc}
	 * @see no.stelvio.common.error.Handler#handleError(java.lang.Throwable)
	 */
//	public Throwable handleError(Throwable t) {
//		if (isLoggingConfigured) {
//			systemLog.error("DefaultHandlerImpl.handleError: ", t);
//			debugLog.error("DefaultHandlerImpl.handleError: ", t);
//		} else {
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			pw.println("DefaultHandlerImpl.handleError: ");
//			t.printStackTrace(pw);
//			pw.flush();
//			pw.close();
//			System.err.println(sw.toString());
//		}
//		return t;
//	}

	/**
	 * Returns the result of {@link java.lang.Throwable#getMessage()} if the
	 * throwable is of type LoggableException, otherwise the result of
	 * {@link java.lang.Throwable#getLocalizedMessage()} is returned.
	 *
	 * {@inheritDoc}
	 * @see no.stelvio.common.error.Handler#getMessage(java.lang.Throwable)
	 */
//	public String getMessage(Throwable t) {
//		if (t instanceof LoggableException) {
//			return t.getMessage();
//		} else {
//			return t.getLocalizedMessage();
//		}
//	}

}
