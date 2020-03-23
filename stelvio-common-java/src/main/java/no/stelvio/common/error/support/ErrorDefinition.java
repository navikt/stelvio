package no.stelvio.common.error.support;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Should this have more logic? Should it implement Comparable based on Severity
 * 
 */
@Entity
public class ErrorDefinition implements Serializable {
	private static final long serialVersionUID = -2350211506640790245L;

	/** A short identification string. */
	@Id
	private String id;

	/** The class name of the error's exception. */
	private String className;

	/** The message template for this error that is shown to the user. */
	private String message;

	/** The error's severity. */
	@Enumerated(EnumType.STRING)
	private Severity severity;

	/** The error's short description which is used by first line of support. */
	private String shortDescription;

	/** The error's long description which is used by second line of support. */
	private String longDescription;

	/** Constructor used by orm. */
	protected ErrorDefinition() {
	}

	/**
	 * Constructor with all the parameters.
	 * 
	 * @param id
	 *            the id
	 * @param className
	 *            the class name
	 * @param message
	 *            the message
	 * @param severity
	 *            the severity
	 * @param shortDescription
	 *            the short description
	 * @param longDescription
	 *            the long description
	 */
	public ErrorDefinition(String id, String className, String message, Severity severity, String shortDescription,
			String longDescription) {
		this.id = id;
		this.className = className;
		this.message = message;
		this.severity = severity;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Returns the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the severity.
	 * 
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * Returns the short description.
	 * 
	 * @return the short description
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Returns the long description.
	 * 
	 * @return the long description
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return the string representation of the object
	 */
	public String toString() {
		return "ErrorDefinition{" + "className='" + className + '\'' + ", message='" + message + '\'' + '}';
	}

	/**
	 * Compares the input object with the current object and returns true if
	 * they are the same, false otherwise.
	 * 
	 * @param other
	 *            the input object
	 * @return boolean indicating if the objects are equal
	 */
	public boolean equals(Object other) {
		if (null == other) {
			return false;
		} else if (!this.getClass().equals(other.getClass())) {
			return false;
		} else {
			ErrorDefinition errDef = (ErrorDefinition) other;
			return new EqualsBuilder().append(className, errDef.className).isEquals();
		}
	}

	/**
	 * Returns the hashcode for the object.
	 * 
	 * @return the hashcode
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(className).toHashCode();
	}

	/**
	 * should this be here? test this
	 * 
	 */
	public static class Builder {
		private ErrorDefinition errorDefinition;

		/**
		 * Constructs a new Builder based on the input class name.
		 * 
		 * @param className
		 *            the class name
		 */
		public Builder(String className) {
			errorDefinition = new ErrorDefinition();
			errorDefinition.className = className;
		}

		/**
		 * Constructs a new Builder based on the input object. is this correct?
		 * Cannot use Class<? extends Throwable> when using StelvioException
		 * 
		 * @param clazz
		 *            the input object
		 */
		public Builder(Class<? extends Object> clazz) {
			errorDefinition = new ErrorDefinition();
			errorDefinition.className = clazz.getName();
		}

		/**
		 * Returns the error definition.
		 * 
		 * @return the error definition
		 */
		public ErrorDefinition build() {
			return errorDefinition;
		}

		/**
		 * Sets the error definition's message and returns the Builder object.
		 * 
		 * @param message
		 *            the new message
		 * @return the Builder
		 */
		public Builder message(String message) {
			errorDefinition.message = message;
			return this;
		}

		/**
		 * Sets the error definition's severity and returns the Builder object.
		 * 
		 * @param severity
		 *            the new severity
		 * @return the Builder
		 */
		public Builder severity(Severity severity) {
			errorDefinition.severity = severity;
			return this;
		}

		/**
		 * Sets the error definition's short description and returns the Builder
		 * object.
		 * 
		 * @param description
		 *            the new short description
		 * @return the Builder
		 */
		public Builder shortDescription(String description) {
			errorDefinition.shortDescription = description;
			return this;
		}

		/**
		 * Sets the error definition's long description and returns the Builder
		 * object.
		 * 
		 * @param description
		 *            the new long description
		 * @return the Builder
		 */
		public Builder longDescription(String description) {
			errorDefinition.longDescription = description;
			return this;
		}
	}
}
