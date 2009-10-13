package no.stelvio.common.context;

import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author test@example.com
 */
class UserWorkAreaContextAdapter {
	public static final String WORK_AREA_NAME = "BUS_STELVIO_CONTEXT";

	private UserWorkArea adaptee;

	public UserWorkAreaContextAdapter(UserWorkArea adaptee) {
		if (adaptee == null) {
			throw new IllegalArgumentException("UserWorkArea is null");
		}
		this.adaptee = adaptee;
	}

	public String getWorkAreaName() {
		return adaptee.getName();
	}

	public String getApplicationId() {
		return (String) adaptee.get("applicationId");
	}

	public String getCorrelationId() {
		return (String) adaptee.get("correlationId");
	}

	public String getLanguageId() {
		return (String) adaptee.get("languageId");
	}

	public String getUserId() {
		return (String) adaptee.get("userId");
	}
}
