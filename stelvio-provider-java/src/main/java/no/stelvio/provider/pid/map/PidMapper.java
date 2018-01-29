package no.stelvio.provider.pid.map;

import no.stelvio.domain.person.Pid;
import no.stelvio.dto.person.PidDto;

/**
 * Mapper used to map between the rich domain object {@link Pid} and the Data Transfer Object {@link PidDto}.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class PidMapper {

	/**
	 * Maps from {@link PidDto} to {@link Pid}.
	 * 
	 * @param pidDto
	 *            an instance of <code>no.stelvio.domain.person.Pid</code>
	 * @return an instance of <code>no.stelvio.dto.person.PidDto</code>
	 */
	public Pid map(PidDto pidDto) {
		return new Pid(pidDto.getPid());
	}

	/**
	 * Maps from {@link Pid} to {@link PidDto}.
	 * 
	 * @param pid
	 *            an instance of <code>no.stelvio.domain.person.Pid</code>
	 * @return an instance of <code>no.stelvio.dto.person.PidDto</code>
	 */
	public PidDto map(Pid pid) {
		return new PidDto(pid.getPid());
	}

}
