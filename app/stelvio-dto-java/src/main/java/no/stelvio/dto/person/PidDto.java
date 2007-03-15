package no.stelvio.dto.person;

import java.io.Serializable;

/**
 * Data Transfer Object representation of <code>no.stelvio.domain.person.Pid</code>
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * @author person983601e0e117 (Accenture)
 *
 */
public class PidDto implements Serializable {

	private static final long serialVersionUID = 47355894749306567L;
	private String pid;
	
	/**
	 * Constructs a new <code>PidDto</code>. Although pid is not validated, 
	 * it should be valid to avoid validation exceptions being thrown when
	 * <code>PidDto</code> is mapped to <code>no.stelvio.domain.person.Pid</code>
	 * @param pid
	 */
	public PidDto(String pid){
		this.pid = pid;
	}

	/**
	 * Gets the Personal Identification Number
	 * @return pid
	 */
	public String getPid() {
		return pid;
	}
	
}
