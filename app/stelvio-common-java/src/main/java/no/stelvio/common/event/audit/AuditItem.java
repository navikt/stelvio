package no.stelvio.common.event.audit;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class AuditItem {
    private String description;
    private Object auditable;

    public AuditItem(String description, Object auditable) {
        this.description = description;
        this.auditable = auditable;
    }
}
