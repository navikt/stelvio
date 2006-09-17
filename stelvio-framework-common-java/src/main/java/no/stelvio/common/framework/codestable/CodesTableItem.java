package no.stelvio.common.framework.codestable;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.framework.core.DomainObject;

/**
 * CodesTableItem represents an item in a codes table.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1874 $ $Author: psa2920 $ $Date: 2005-01-19 16:07:08 +0100 (Wed, 19 Jan 2005) $
 */
public abstract class CodesTableItem extends DomainObject {

	/**
	 * Returns the code represented by this item.
	 * @return the code.
	 */
	public abstract String getKode();

	/**
	 * Returns the decode represented by this item.
	 * @return the decode.
	 */
	public abstract String getDekode();
	/**
	 * Sets the code represented by this item.
	 * @param string the code
	 */
	public abstract void setKode(String string);

	/**
	 * Sets the decode represented by this item
	 * @param string the decode
	 */
	public abstract void setDekode(String string);

	/**
	 * Returns the validity of this item.
	 * @return true if item is valid, false otherwise
	 */
	public abstract boolean isErGyldig();

	/**
	 * Returns the first date the item is valid from.
	 * @return the first date the item is valid
	 */
	public abstract Date getFomDato();

	/**
	 * Returns the last date the item is valid from.
	 * @return the last date the item is valid
	 */
	public abstract Date getTomDato();

	/**
	 * Sets the validity of this item.
	 * 
	 * @param isValid true to mark item as valid, false otherwise
	 */
	public abstract void setErGyldig(boolean isValid);

	/**
	 * Sets the first date the item is valid from.
	 * @param date the first date the item is valid
	 */
	public abstract void setFomDato(Date date);

	/**
	 * Sets the last date the item is valid from.
	 * @param date the last date the item is valid
	 */
	public abstract void setTomDato(Date date);

	/**
	 * Returns a String representation of this object.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("code", getKode())
			.append("decode", getDekode())
			.append("isValid", isErGyldig())
			.append("validFrom", getFomDato())
			.append("validTo", getTomDato())
			.toString();
	}

	/**
	 * Compares this instance with another object. 
	 * The method returns true if the other object is not null,
	 * and is of same class as this and other.getCode() 
	 * equals this.getCode().
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {

		if (null == other) {
			// This aint null
			return false;
		} else if (!this.getClass().equals(other.getClass())) {
			// This is another class than other
			return false;
		} else {
			// This is the same class as other
			CodesTableItem castOther = (CodesTableItem) other;
			return new EqualsBuilder().append(this.getKode(), castOther.getKode()).isEquals();
		}
	}

	/**
	 * Hash code is computed based on the class name of this instance and the code.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		// Compare by class name to support multiple classloaders
		return new HashCodeBuilder().append(this.getClass().getName()).append(getKode()).toHashCode();
	}

}
