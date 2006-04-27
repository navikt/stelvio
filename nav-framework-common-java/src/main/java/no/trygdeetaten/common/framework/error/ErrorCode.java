package no.trygdeetaten.common.framework.error;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Error code constants used to construct exceptions,
 * map error codes to descriptions and severity. 
 * <p/>
 * Application sub classes should define their own error codes
 * in classes available in all architecture tiers.
 * <p/>
 * Code range conventions:
 * <pre>
 * Framework Errors (code < 100.000):
 * ------------------------------------------------------------
 * - Common Services         10.000 - 19.999
 * - Presentation Services   20.000 - 29.999
 * - Business Services       30.000 - 39.999
 * - Integration Services    40.000 - 49.999
 * 
 * Application Errors (100.000 <= code):
 * ------------------------------------------------------------
 * - Application 1	100.000 - 199.999
 * - Application 2  200.000 - 299.999
 * - Application 3  300.000 - 399.999
 * - ...
 * ------------------------------------------------------------
 * </pre>
 * 
 * @author person7553f5959484
 * @version $Revision: 2331 $ $Author: psa2920 $ $Date: 2005-06-09 13:57:17 +0200 (Thu, 09 Jun 2005) $
 */
public class ErrorCode {

	// The internal error code
	private final int code;

	/**
	 * Allow constructions of new error codes 
	 * only inside this class and sub classes.
	 * 
	 * @param code the error code
	 */
	protected ErrorCode(int code) {
		this.code = code;
	}

	/**
	 * Access the primitive error code.
	 * 
	 * @return the error code as int
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Compares another object for equality to this.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(Object object) {
		if (object instanceof ErrorCode) {
			ErrorCode other = (ErrorCode) object;
			return new EqualsBuilder().append(this.getCode(), other.getCode()).isEquals();
		} else {
			return false;
		}
	}

	/**
	 * Computes the hash code of this object.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode() {
		return new HashCodeBuilder().append(getCode()).toHashCode();
	}

	/** En teknisk feil har oppstått: {0}. Feilen er av typen {1}. */
	public static final ErrorCode UNSPECIFIED_ERROR = new ErrorCode(0);

	/** En teknisk feil har oppstått. Feilkoden er {0}, men denne er ikke beskrevet i databasen. */
	public static final ErrorCode UNCONFIGURED_ERROR = new ErrorCode(1);
}
