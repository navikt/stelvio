package no.stelvio.dto.codestable;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Abstract base class for <code>CodeTableItemDto</code>.
 * 
 * @author person47c121e3ccb5, BEKK
 * @version $Id$
 */
public abstract class AbstractCodesTableItemDto implements Serializable {

	/** code. */
	protected String code;	

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/** {@inheritDoc} */
	public String toString() {
		return new ToStringBuilder(this).append("code", code).toString();
	}

}
