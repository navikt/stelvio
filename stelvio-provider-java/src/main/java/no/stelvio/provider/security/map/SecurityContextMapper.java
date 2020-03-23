package no.stelvio.provider.security.map;

import java.util.Arrays;

import no.stelvio.common.mapping.AbstractDozerMapper;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.dto.security.SecurityContextDto;

/**
 * Mapper used to map between the rich domain object {@link SecurityContext} and the Data Transfer Object
 * {@link SecurityContext}.
 * 
 *
 */
public class SecurityContextMapper extends AbstractDozerMapper {

	/**
	 * Maps from a <code>SecurityContextDto</code> to a <code>SecurityContext</code>.
	 * 
	 * @param dto
	 *            <code>SecurityContextDto</code>
	 * @return <code>SecurityContext</code>
	 */
	public SecurityContext map(SecurityContextDto dto) {
		return new SimpleSecurityContext(dto.getUserId(), Arrays.asList(dto.getRoles()));
	}

	/**
	 * Maps from a <code>SecurityContextDto</code> to a <code>SecurityContextDto</code>.
	 * 
	 * @param securityContext
	 *            <code>SecurityContext</code>
	 * @return <code>SecurityContextDto</code>
	 */
	public SecurityContextDto map(SecurityContext securityContext) {
		return dozerMapper.map(securityContext, SecurityContextDto.class);
	}

}
