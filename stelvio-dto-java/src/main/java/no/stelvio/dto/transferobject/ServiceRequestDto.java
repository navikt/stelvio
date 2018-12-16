package no.stelvio.dto.transferobject;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import no.stelvio.dto.context.RequestContextDto;

/**
 * Data Transfer Object representation of <code>no.stelvio.common.transferobject.SerciceRequest</code>.
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 */
public class ServiceRequestDto implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private RequestContextDto requestContextDto;

	/**
	 * Returns a String representation of this ServiceRequestDto object.
	 * 
	 * @return a String representation of this object
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).
					append("requestContextDto", requestContextDto).toString();
	}
	
	/**
	 * Gets the requestContextDto.
	 * @return requestContextDto instance
	 */
	public RequestContextDto getRequestContextDto() {
		return requestContextDto;
	}

	/**
	 * Sets the requestContextDto.
	 * @param requestContextDto dto to set
	 */
	public void setRequestContextDto(RequestContextDto requestContextDto) {
		this.requestContextDto = requestContextDto;
	}

}
