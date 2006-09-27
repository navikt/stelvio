package no.stelvio.common.error;

import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.core.DomainObject;

/**
 * Representation of an error configuration.
 * 
 * @author person7553f5959484
 * @version $Revision: 1954 $ $Author: psa2920 $ $Date: 2005-02-08 14:35:42 +0100 (Tue, 08 Feb 2005) $
 */
public class ErrorConfig extends DomainObject {

	private int code = -1;
	private Integer severity = null;
	private String message = null;

	/**
	 * Constructs a default error configuration (unconfigured).
	 */
	public ErrorConfig() {
		super();
	}

	/**
	 * Constructs an error configuration for specified code, severity and message.
	 * 
	 * @param code the unique error type
	 * @param severity the error severity
	 * @param message the template to use for describing the error
	 */
	public ErrorConfig(int code, Integer severity, String message) {
		super();
		this.code = code;
		this.severity = severity;
		this.message = message;
	}

	/**
	 * Returns the unique code that identifies the type of this error.
	 * 
	 * @return the error code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Returns the message template to use for describing the error.
	 * 
	 * @return the message template
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the severity of the error.
	 * 
	 * @return the severity
	 */
	public Integer getSeverity() {
		return severity;
	}

	/**
	 * Assigns the unique code that identifies the type of this error.
	 * 
	 * @param code the error code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Assigns the message template to use for describing the error.
	 * 
	 * @param message the message template
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Assigns the severity of the error.
	 * 
	 * @param severity the severity
	 */
	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	/**
	 * Returns a string representation of this object.
	 * @return the string representation.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("code", getCode())
			.append("severity", getSeverity())
			.append("message", getMessage())
			.toString();
	}
}
