package no.stelvio.presentation.error;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * Class with convienient methods for extracting content from {@link FlowExecutionException}.
 * 
 * @version $Id$
 */
public class ErrorAction {

	private static final String DATE_FORMAT_STRING = "dd.MM.yyyy HH:mm:ss:SSS";

	/** Attribute name which is used for storing the exception in session scope. */
	public static final String STATE_EXCEPTION_ATTRIBUTE = 
		"no.stelvio.presentation.error.ErrorPageExceptionHandler.STATE_EXCEPTION";

	private boolean showStackTrace;

	/**
	 * Formats a date according to dd.MM.yyyy HH:mm:ss:SSS pattern.
	 * 
	 * @return The formatted date as a string
	 */
	public String getDateAndTime() {
		Date now = new Date();

		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(DATE_FORMAT_STRING);
		return dateFormat.format(now);
	}

	/**
	 * Retrieves an ErrorPageData if such an object exists in flash scope. If not, the exception that has occured is analyzed,
	 * and an ErrorPageData object is created. The ErrorPageData object contains detailed information about the exception that
	 * has occured, and the information is displyed in the general technical error page.
	 * 
	 * @return ErrorPageData object stored in flash scope
	 */
	public String getMessage() {
		String message = "";
		if (containsFlowExecutionException()) {
			FlowExecutionException excp = getFlowExecutionException();
			if (excp.getCause() != null) {
				message = excp.getCause().getMessage();
			}
		}
		return message;
	}

	/**
	 * Returns the flowId from the FlowExecutionException.
	 * 
	 * @return the flowId
	 */
	public String getScreen() {

		if (containsFlowExecutionException()) {
			return getFlowExecutionException().getFlowId();
		} else {
			return "";
		}
	}

	/**
	 * Returns the stack trace from the FlowExecutionException.
	 * 
	 * @return the stack trace
	 */
	public String getStackTrace() {

		if (containsFlowExecutionException()) {
			return ExceptionUtils.getStackTrace(getFlowExecutionException());
		} else {
			return "";
		}
	}

	/**
	 * Get FlowExecutionException.
	 * 
	 * @return exception
	 */
	private FlowExecutionException getFlowExecutionException() {
		return (FlowExecutionException) RequestContextHolder.getRequestContext().getExternalContext().getSessionMap().get(
				STATE_EXCEPTION_ATTRIBUTE);
	}

	/**
	 * Check if context contains a FlowExecutionException.
	 * 
	 * @return true if it contains a FlowExecutionException
	 */
	private boolean containsFlowExecutionException() {
		return RequestContextHolder.getRequestContext().getExternalContext().getSessionMap()
				.contains(STATE_EXCEPTION_ATTRIBUTE);
	}

	/**
	 * Return true if Stacktrace is turned on - otherwise false.
	 * 
	 * @return true if Stacktrace is turned on - otherwise false
	 */
	public boolean getShowStackTrace() {
		return showStackTrace;
	}

	/**
	 * Turns on (true) or off (false) showStackTrace.
	 * 
	 * @param showStackTrace
	 *            the showStackTrace to set
	 */
	public void setShowStackTrace(boolean showStackTrace) {
		this.showStackTrace = showStackTrace;
	}

}
