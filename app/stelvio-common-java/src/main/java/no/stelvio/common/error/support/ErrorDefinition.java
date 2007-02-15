package no.stelvio.common.error.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author personf8e9850ed756
 * @todo Should this have more logic?
 * @todo Should it implement Comparable based on Severity
 */
@Entity
public class ErrorDefinition {
	/** A short identification string. */
	@Id
	@Column(name="id")
    private String id;

	/** The class name of the error's exception. */
	@Column(name="className")
    private String className;

	/** The message template for this error that is shown to the user. */
	@Column(name="message")
    private String message;

	/** The error's severity. */
	@Column(name="severity")
    private Severity severity;

	/** The error's short description which is used by first line of support. */
	@Column(name="shortDescription")
    private String shortDescription;

	/** The error's long description which is used by second line of support. */
	@Column(name="longDescription")
    private String longDescription;

	/** Constructor used by orm. */
	protected ErrorDefinition() {
	}

    public ErrorDefinition(String id,
               String className,
               String message,
               Severity severity,
               String shortDescription,
               String longDescription) {
        this.id = id;
        this.className = className;
        this.message = message;
        this.severity = severity;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

	public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String toString() {
        return "ErrorDefinition{" +
                "className='" + className + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

	public boolean equals(Object other) {
		if (null == other) {
			return false;
		} else if (!this.getClass().equals(other.getClass())) {
			return false;
		} else {
			ErrorDefinition errDef = (ErrorDefinition) other;
			return new EqualsBuilder().append(id, errDef.id).isEquals();
		}
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	// TODO should this be here?
	// TODO test this
    public static class Builder {
        private ErrorDefinition errorDefinition;

        public Builder(String className) {
            errorDefinition = new ErrorDefinition();
	        errorDefinition.className = className;
        }

        // TODO is this correct? Cannot use Class<? extends Throwable> when using StelvioException
        public Builder(Class<? extends Object> clazz) {
            errorDefinition = new ErrorDefinition();
	        errorDefinition.className = clazz.getName();
        }

        public ErrorDefinition build() {
            return errorDefinition;
        }

        public Builder message(String message) {
            errorDefinition.message = message;
            return this;
        }

        public Builder severity(Severity severity) {
            errorDefinition.severity = severity;
            return this;
        }

        public Builder shortDescription(String description) {
            errorDefinition.shortDescription = description;
            return this;
        }

        public Builder longDescription(String description) {
            errorDefinition.longDescription = description;
            return this;
        }
    }
}
