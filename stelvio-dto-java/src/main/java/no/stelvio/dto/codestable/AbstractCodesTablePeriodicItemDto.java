package no.stelvio.dto.codestable;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Abstract base class for <code>CodesTablePeriodicItemDto</code>.
 * 
 * @version $Id$
 */
public abstract class AbstractCodesTablePeriodicItemDto implements Serializable {

	/** Code. */
	protected String code;	

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Set code.
	 * 
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("code", code).toString();
	}
	
}
