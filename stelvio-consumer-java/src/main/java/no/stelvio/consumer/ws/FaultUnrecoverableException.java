package no.stelvio.consumer.ws;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Local implementation of consumer exception GBOStelvioFault and stelvio FunctionalUnrecoverableException.
 * 
 * Wraps the GBOStelvioFault by copying relevant properties to this exception implementation this will in turn provide better
 * logging of the exception
 * 
 * This class should be subclassed by each application catching GBOStelvioFaults and the subclass should be used to wrap
 * GBOStelvioFaults before rethrowing them to the consumers
 * 
 * @see FunctionalUnrecoverableException
 * 
 */
public abstract class FaultUnrecoverableException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(FaultUnrecoverableException.class);

	private String errorMessage;

	private String errorSource;

	private String rootCause;

	private String dateTimeStamp;

	/**
	 * Constructs a new FaultUnrecoverableException.
	 * 
	 * @param message
	 *            String desribing what service was called when the <code>cause</code> was thrown
	 * @param cause
	 *            Throwable.
	 */
	protected FaultUnrecoverableException(String message, Throwable cause) {
		super(message, cause);
		try {
			BeanUtils.copyProperties(this, cause);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Get the date time stamp.
	 * 
	 * @return the dateTimeStamp
	 */
	public String getDateTimeStamp() {
		return dateTimeStamp;
	}

	/**
	 * Set the date time stamp.
	 * 
	 * @param dateTimeStamp
	 *            the dateTimeStamp to set
	 */
	public void setDateTimeStamp(String dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}

	/**
	 * Get the error message.
	 * 
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Set the error message.
	 * 
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Get the error source.
	 * 
	 * @return the errorSource
	 */
	public String getErrorSource() {
		return errorSource;
	}

	/**
	 * Set the error source.
	 * 
	 * @param errorSource
	 *            the errorSource to set
	 */
	public void setErrorSource(String errorSource) {
		this.errorSource = errorSource;
	}

	/**
	 * Get the root cause.
	 * 
	 * @return the rootCause
	 */
	public String getRootCause() {
		return rootCause;
	}

	/**
	 * Set the root cause.
	 * 
	 * @param rootCause
	 *            the rootCause to set
	 */
	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

}
