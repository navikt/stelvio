package no.stelvio.common.context;

import java.io.Serializable;

import com.ibm.websphere.workarea.NoWorkArea;
import com.ibm.websphere.workarea.NotOriginator;
import com.ibm.websphere.workarea.PropertyFixed;
import com.ibm.websphere.workarea.PropertyModeType;
import com.ibm.websphere.workarea.PropertyReadOnly;
import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author test@example.com
 */
public class UserWorkAreaContextAdapter implements UserWorkArea {
	private static final long serialVersionUID = -5356337974832928277L;

	private UserWorkArea adaptee;

	public UserWorkAreaContextAdapter(UserWorkArea adaptee) {
		this.adaptee = adaptee;
	}

	public void begin(String name) {
		adaptee.begin(name);
	}

	public void complete() throws NoWorkArea, NotOriginator {
		adaptee.complete();
	}

	public Serializable get(String key) {
		return adaptee.get(key);
	}

	public PropertyModeType getMode(String key) {
		return adaptee.getMode(key);
	}

	public String getName() {
		return adaptee.getName();
	}

	public void remove(String key) throws NoWorkArea, NotOriginator, PropertyFixed {
		adaptee.remove(key);
	}

	public String[] retrieveAllKeys() {
		return adaptee.retrieveAllKeys();
	}

	public void set(String key, Serializable value, PropertyModeType mode) throws NoWorkArea, NotOriginator, PropertyReadOnly {
		adaptee.set(key, value, mode);
	}

	public void set(String key, Serializable value) throws NoWorkArea, NotOriginator, PropertyReadOnly {
		adaptee.set(key, value);
	}
}
