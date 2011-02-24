package no.stelvio.presentation.jsf.mock;

import no.stelvio.common.codestable.support.IllegalCodeEnum;

/**
 * PoststedCodeMock.
 * 
 * @author person37c6059e407e (Capgemini)
 * @version $Id$
 *
 */
public enum PoststedCodeMock implements IllegalCodeEnum {
	/** OSLO. */
	P_0001("0001");

	/**
	 * Overload the default equals method as equals does not work for enums
	 * after they are serialized (e.g., across EJB calls). Note that the
	 * equals(Object) method is final, so we can't override it.
	 * 
	 * @param obj
	 *            enumclass to compare with
	 * @return true if the objects are equal (i.e., their ordinal is equal)
	 */
	public boolean equals(PoststedCodeMock obj) {
		return (obj != null) && (obj.ordinal() == this.ordinal());
	}

	private String value = null;

	/**
	 * Value constructor for illegal enums.
	 * 
	 * @param value
	 *            the illegal value
	 */
	private PoststedCodeMock(String value) {
		this.value = value;
	}

	/** Default constructor. */
	private PoststedCodeMock() {
	}

	/** 
	 * Get illegal code.
	 * 
	 * @return enum value 
	 */
	public String getIllegalCode() {
		return (value == null) ? name() : value;
	}
}
