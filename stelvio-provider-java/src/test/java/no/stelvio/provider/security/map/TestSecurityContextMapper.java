package no.stelvio.provider.security.map;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.dto.security.SecurityContextDto;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for SecurityContextMapper.
 * 
 */
public class TestSecurityContextMapper {

	private SecurityContextMapper mapper;

	/**
	 * Creating a context and a mapper before testing.
	 */
	@Before
	public void injectMapper() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:modules/prv-stelvio-context.xml");
		Mapper mapper = (Mapper) ctx.getBean("prv.stelvio.mapping.Mapper");

		this.mapper = new SecurityContextMapper();
		this.mapper.setDozerMapper(mapper);
	}

	/**
	 * Test mapping from DTO to domain object.
	 */
	@Test
	public void mapFromDtoToDomain() {
		SecurityContextDto dto = new SecurityContextDto();
		dto.setUserId("maa1812");
		dto.setRoles(new String[] { "guest", "admin" });
		SecurityContext securityContext = mapper.map(dto);
		assertTrue(isDomainEqualToDto(securityContext, dto));
	}

	/**
	 * Test mapping from domain object to DTO.
	 */
	@Test
	public void mapFromDomainToDto() {
		List<String> roles = new ArrayList<String>();
		roles.add("admin");
		roles.add("guest");
		SecurityContext securityContext = new SimpleSecurityContext("maa2812", roles);
		SecurityContextDto securityContextDto = mapper.map(securityContext);
		assertTrue(isDomainEqualToDto(securityContext, securityContextDto));
	}

	/**
	 * Compares a DTO and a Domain object to see if they are equal.
	 * 
	 * @param securityContext
	 *            secutity context
	 * @param securityContextDto
	 *            secutity context dto
	 * @return true if they are equal
	 */
	public boolean isDomainEqualToDto(SecurityContext securityContext, SecurityContextDto securityContextDto) {
		boolean equal = true;
		equal = securityContext.getUserId().equals(securityContextDto.getUserId());

		// All roles in dto have an equal in domain
		for (String role : securityContextDto.getRoles()) {
			equal = equal && securityContext.getRoles().contains(role);
		}

		// All roles in domain have an equal in dto
		List<String> dtoRolesList = Arrays.asList(securityContextDto.getRoles());
		for (String role : securityContext.getRoles()) {
			equal = equal && dtoRolesList.contains(role);
		}
		return equal;
	}

}
