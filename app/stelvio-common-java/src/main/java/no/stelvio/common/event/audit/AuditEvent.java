package no.stelvio.common.event.audit;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import no.stelvio.common.event.ApplicationEvent;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class AuditEvent extends ApplicationEvent {
    private String message;
    private String userLogin;
    private String userLocation;
    private Set<AuditItem> auditItems;

    /**
     * Creates a new AuditEvent.
     *
     * @param source the component that published the event (never <code>null</code>)
     */
    public AuditEvent(Object source, String message, String userLogin, String userLocation, Set<AuditItem> auditItems) {
        super(source);

        checkArgument(message, "message");
        checkArgument(userLogin, "userLogin");
        checkArgument(userLocation, "userLocation");

        if (CollectionUtils.isEmpty(auditItems)) {
            throw new IllegalArgumentException("auditItems is mandatory; cannot be empty");
        }

        this.message = message;
        this.userLogin = userLogin;
        this.userLocation = userLocation;
        this.auditItems = auditItems;
    }

    public void addAuditItem(AuditItem auditItem) {
        auditItems.add(auditItem);
    }

    public void removeAuditItem(AuditItem auditItem) {
        auditItems.remove(auditItem);
    }

    public Set<AuditItem> getAuditItems() {
        return Collections.unmodifiableSet(auditItems);
    }

    public String getMessage() {
        return message;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserLocation() {
        return userLocation;
    }

    private void checkArgument(String argument, String argumentName) {
        if (StringUtils.isBlank(argument)) {
            throw new IllegalArgumentException(argumentName + " is mandatory");
        }
    }


    public String toString() {
        return "AuditEvent{" +
                "message='" + message + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", userLocation='" + userLocation + '\'' +
                ", auditItems=" + auditItems +
                '}';
    }
}
