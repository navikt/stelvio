package no.stelvio.dto.person;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Transfer Object representation of <code>no.stelvio.domain.person.Pid</code>.
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * 
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 * 
 */
public class PidDto implements Serializable {

	private static final long serialVersionUID = 47355894749306567L;
	private String pid;

	/**
	 * Default no-arg constructor as specfied by Stelvio DTO specification.
	 */
	public PidDto() {
		super();
	}

	/**
	 * Constructs a new <code>PidDto</code>. Although pid is not validated, it should be valid to avoid validation exceptions
	 * being thrown when <code>PidDto</code> is mapped to <code>no.stelvio.domain.person.Pid</code>.
	 * 
	 * @param pid
	 *            personal identification number
	 */
	public PidDto(String pid) {
		this.pid = pid;
	}

	/**
	 * Returns a String representation of this PidDto object.
	 * 
	 * @return a String representation of this object
	 */
	public String toString() {
		return new ToStringBuilder(this).append("pid", pid).toString();
	}

	/**
	 * Gets the Personal Identification Number.
	 * 
	 * @return pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * Sets the Personal Id no.
	 * 
	 * @param pid
	 *            personal identification number
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

}
