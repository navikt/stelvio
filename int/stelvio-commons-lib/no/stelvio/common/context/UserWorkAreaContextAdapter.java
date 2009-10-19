package no.stelvio.common.context;

import com.ibm.websphere.workarea.NoWorkArea;
import com.ibm.websphere.workarea.NotOriginator;
import com.ibm.websphere.workarea.PropertyReadOnly;
import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author test@example.com
 */
class UserWorkAreaContextAdapter {
	public static final String USER_WORK_AREA_NAME = "BUS_STELVIO_CONTEXT";

	private static final String PROP_KEY_USER_ID = "userId";
	private static final String PROP_KEY_LANGUAGE_ID = "languageId";
	private static final String PROP_KEY_CORRELATION_ID = "correlationId";
	private static final String PROP_KEY_APPLICATION_ID = "applicationId";

	private UserWorkArea adaptee;

	public UserWorkAreaContextAdapter(UserWorkArea adaptee) {
		if (adaptee == null) {
			throw new IllegalArgumentException("UserWorkArea is null");
		}
		this.adaptee = adaptee;
	}

	public String getUserWorkAreaName() {
		return adaptee.getName();
	}

	public String getApplicationId() {
		return getProperty(PROP_KEY_APPLICATION_ID);
	}

	public void setApplicationId(String applicationId) {
		setProperty(PROP_KEY_APPLICATION_ID, applicationId);
	}

	public String getCorrelationId() {
		return getProperty(PROP_KEY_CORRELATION_ID);
	}

	public void setCorrelationId(String correlationId) {
		setProperty(PROP_KEY_CORRELATION_ID, correlationId);
	}

	public String getLanguageId() {
		return getProperty(PROP_KEY_LANGUAGE_ID);
	}

	public void setLanguageId(String languageId) {
		setProperty(PROP_KEY_LANGUAGE_ID, languageId);
	}

	public String getUserId() {
		return getProperty(PROP_KEY_USER_ID);
	}

	public void setUserId(String userId) {
		setProperty(PROP_KEY_USER_ID, userId);
	}

	private String getProperty(String key) {
		return (String) adaptee.get(key);
	}

	private void setProperty(String key, String value) {
		try {
			if (value != null) {
				adaptee.set(key, value);
			}
		} catch (NoWorkArea e) {
			throw new RuntimeException(e);
		} catch (NotOriginator e) {
			throw new RuntimeException(e);
		} catch (PropertyReadOnly e) {
			throw new RuntimeException(e);
		}
	}
}
