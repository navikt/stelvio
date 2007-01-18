package no.stelvio.common.error.support;

/**
 * @author personf8e9850ed756
 * @todo Add Hibernate annotations
 * @todo Should this have more logic?
 * @todo Should it implement Comparable based on Severity
 */
public class ErrorDefinition {
    private String id;
    private String className;
    private String message;
    private Severity severity;
    private String shortDescription;
    private String longDescription;

    private ErrorDefinition(String className) {
        this.className = className;
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

    // TODO should this be here?
    // TODO test this
    public static class Builder {
        private ErrorDefinition errorDefinition;

        public Builder(String className) {
            errorDefinition = new ErrorDefinition(className);
        }

        // TODO is this correct? Cannot use Class<? extends Throwable> when using StelvioException
        public Builder(Class<? extends Object> clazz) {
            errorDefinition = new ErrorDefinition(clazz.getName());
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
