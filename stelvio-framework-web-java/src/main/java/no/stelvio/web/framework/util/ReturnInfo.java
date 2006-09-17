package no.stelvio.web.framework.util;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Container for return information to be stored in the session. 
 * Return information is saved to make it possible to navigate back 
 * from a window and restore the inserted values in the window returned to.
 * 
 * @author Jonas Lindholm, Accenture
 * @author person7553f5959484, Accenture
 * @version $Revision: 2040 $ $Author: psa2920 $ $Date: 2005-03-03 14:00:29 +0100 (Thu, 03 Mar 2005) $
 */
public class ReturnInfo implements Serializable {

	private final String view;
	private final String field;
	private final String method;
	private final String label;
	private final String state;
	private Object value;

	/**
	 * Construct the return info.
	 * 
	 * @param view the logical name of the view to return to
	 * @param field the logical name of the field to populate
	 * @param method the action to take when returning 
	 * @param label the resource bundle key for looking up the label
	 * @param value the value to be returned from the lookup
	 */
	public ReturnInfo(String view, String field, String method, String label, Object value) {
		this(view, field, method, label, value, null);
	}

	/**
	 * Construct the return info.
	 * 
	 * @param view the logical name of the view to return to
	 * @param field the logical name of the field to populate
	 * @param method the action to take when returning 
	 * @param label the resource bundle key for looking up the label
	 * @param value the value to be returned from the lookup
	 * @param state the state to use when returning
	 */
	public ReturnInfo(String view, String field, String method, String label, Object value, String state) {
		this.view = view;
		this.field = field;
		this.method = method;
		this.label = label;
		this.value = value;
		this.state = state;
	}

	/**
	 * Gets the logical name of the view to return to.
	 * 
	 * @return the view name.
	 */
	public String getView() {
		return view;
	}

	/**
	 * Gets the name of the field to populate.
	 * 
	 * @return the field name. 
	 */
	public String getField() {
		return field;
	}

	/**
	 * Gets the named action to take when returning
	 * @return method the method name.
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Gets the resource bundle key for looking up the label.
	 * 
	 * @return the key
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value to be returned from the lookup.
	 * 
	 * @param o the value
	 */
	public void setValue(Object o) {
		value = o;
	}

	/**
	 * Gets the value to be returned from the lookup.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the state to use when returning.
	 * 
	 * @return the state.
	 */
	public String getState() {
		return state;
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("view", getView())
			.append("field", getField())
			.append("method", getMethod())
			.append("label", getLabel())
			.append("value", getValue())
			.append("state", getState())
			.toString();
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof ReturnInfo)) {
			return false;
		}
		ReturnInfo castOther = (ReturnInfo) other;
		return new EqualsBuilder()
			.append(this.getView(), castOther.getView())
			.append(this.getField(), castOther.getField())
			.append(this.getMethod(), castOther.getMethod())
			.append(this.getLabel(), castOther.getLabel())
			.append(this.getState(), castOther.getState())
			.isEquals();
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getView())
			.append(getField())
			.append(getMethod())
			.append(getLabel())
			.append(getState())
			.toHashCode();
	}

}
