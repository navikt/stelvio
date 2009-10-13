package no.stelvio.common.context;

import java.io.Serializable;

import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author test@example.com
 */
public class UserWorkAreaContextAdapter {
	private UserWorkArea adaptee;

	public UserWorkAreaContextAdapter(UserWorkArea adaptee) {
		if (adaptee == null) {
			throw new IllegalArgumentException("UserWorkArea is null");
		}
		this.adaptee = adaptee;
	}

	public Serializable get(String key) {
		return adaptee.get(key);
	}

	public String getName() {
		return adaptee.getName();
	}
}
