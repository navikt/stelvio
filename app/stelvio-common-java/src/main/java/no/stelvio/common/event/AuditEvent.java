package no.stelvio.common.event;

import java.util.Collections;
import java.util.Set;

import org.springframework.context.ApplicationEvent;

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
}
