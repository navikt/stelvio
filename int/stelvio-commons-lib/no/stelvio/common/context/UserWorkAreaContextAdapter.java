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

	public void begin(String arg0) {
		adaptee.begin(arg0);
	}

	public void complete() throws NoWorkArea, NotOriginator {
		adaptee.complete();
	}

	public Serializable get(String arg0) {
		return adaptee.get(arg0);
	}

	public PropertyModeType getMode(String arg0) {
		return adaptee.getMode(arg0);
	}

	public String getName() {
		return adaptee.getName();
	}

	public void remove(String arg0) throws NoWorkArea, NotOriginator, PropertyFixed {
		adaptee.remove(arg0);
	}

	public String[] retrieveAllKeys() {
		return adaptee.retrieveAllKeys();
	}

	public void set(String arg0, Serializable arg1, PropertyModeType arg2) throws NoWorkArea, NotOriginator, PropertyReadOnly {
		adaptee.set(arg0, arg1, arg2);
	}

	public void set(String arg0, Serializable arg1) throws NoWorkArea, NotOriginator, PropertyReadOnly {
		adaptee.set(arg0, arg1);
	}
}
