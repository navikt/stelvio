package no.stelvio.common.event.audit;

import org.apache.commons.lang.StringUtils;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class AuditItem {
    private String description;
    private Object auditable;

    public AuditItem(String description, Object auditable) {
        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("description is mandatory");
        }

        if (null == auditable) {
            throw new IllegalArgumentException("description is mandatory");
        }

        this.description = description;
        this.auditable = auditable;
    }

    /**
     * Returns the description for this audit item.
     *
     * @return the description for this audit item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the auditable object for this audit item.
     *
     * @return the auditable object for this audit item.
     */
    public Object getAuditable() {
        return auditable;
    }
}
