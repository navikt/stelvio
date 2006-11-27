package no.stelvio.common.error;

import java.util.Locale;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo Have a different name, but not Error
 * @todo Should this be the class being persisted?
 * @todo Should this have more logic?
 */
public class Err {
    private String id;
    private Locale locale;
    private String className;
    private String message;
    private Severity severity;
    private String shortDescription;
    private String longDescription;

    private Err(String className) {
        this.className = className;
    }

    public Err(String id,
               Locale locale,
               String className,
               String message,
               Severity severity,
               String shortDescription,
               String longDescription) {
        this.id = id;
        this.locale = locale;
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
        return "Err{" +
                "className='" + className + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    // TODO should this be here?
    // TODO test this
    public static class Builder {
        private Err err;

        public Builder(String className) {
            err = new Err(className);
        }

        // TODO is this correct? Cannot use Class<? extends Throwable> when using StelvioException
        public Builder(Class<? extends Object> clazz) {
            err = new Err(clazz.getName()); 
        }

        public Err build() {
            return err;
        }

        public Builder message(String message) {
            err.message = message;
            return this;
        }

        public Builder severity(Severity severity) {
            err.severity = severity;
            return this;
        }

        public Builder shortDescription(String description) {
            err.shortDescription = description;
            return this;
        }

        public Builder longDescription(String description) {
            err.longDescription = description;
            return this;
        }
    }
}
