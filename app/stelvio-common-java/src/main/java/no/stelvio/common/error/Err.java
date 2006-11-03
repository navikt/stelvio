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
    private Integer severity;
    private String shortDescription;
    private String longDescription;

    private Err(String id) {
        this.id = id;
    }

    public Err(String id,
               Locale locale,
               String className,
               String message,
               Integer severity,
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

    public Locale getLocale() {
        return locale;
    }

    public String getClassName() {
        return className;
    }

    public String getMessage() {
        return message;
    }

    public Integer getSeverity() {
        return severity;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    // TODO should this be here?
    // TODO test this
    public static class Builder {
        Err err;

        public Builder(String id) {
            err = new Err(id);
        }

        public Err build() {
            return err;
        }
    }
}
