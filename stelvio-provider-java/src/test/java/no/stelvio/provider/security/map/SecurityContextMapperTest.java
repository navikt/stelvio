package no.stelvio.provider.security.map;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.dto.security.SecurityContextDto;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests mapping between <code>SecurityContext</code> and <code>SecurityContextDto</code>.
 * 
 */
public class SecurityContextMapperTest extends TestCase {

	private SecurityContextMapper mapper;

	/**
	 * Creating a context and a mapper before testing.
	 * 
	 * @throws Exception if setup fails
	 */
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:modules/prv-stelvio-context.xml");
		Mapper mapper = (Mapper) context.getBean("prv.stelvio.mapping.Mapper");

		this.mapper = new SecurityContextMapper();
		this.mapper.setDozerMapper(mapper);
	}

	/**
	 * Test the mapping of a security context to a dto security context.
	 */
	@Test
	public void testMapSecurityContextDto() {

		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		roles.add("ROLE3");

		SecurityContext securityContext = new SimpleSecurityContext("UserID::USER1", roles);
		SecurityContextDto securityContextDto = mapper.map(securityContext);

		assertTrue(isEqual(securityContext, securityContextDto));
	}

	/**
	 * Test the mapping of a dto security context to a security context.
	 */
	@Test
	public void testMapSecurityContext() {

		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		roles.add("ROLE3");

		String[] dtoRoles = new String[roles.size()];

		SecurityContextDto securityContextDto = new SecurityContextDto();
		securityContextDto.setRoles((String[]) roles.toArray(dtoRoles));
		securityContextDto.setUserId("UserID::USER1");

		SecurityContext securityContext = mapper.map(securityContextDto);

		assertTrue(isEqual(securityContext, securityContextDto));
	}

	/**
	 * Compares domain and dto field values.
	 * 
	 * @param securityContext the security context
	 * @param securityContextDto the dto security context
	 * @return true if security context and security context dto are equal
	 */
	private boolean isEqual(SecurityContext securityContext, SecurityContextDto securityContextDto) {

		boolean equal = false;
		if (securityContext.getUserId().equals(securityContextDto.getUserId())) {

			List<String> roles = securityContext.getRoles();
			String[] ctxRoles = new String[roles.size()];
			ctxRoles = roles.toArray(ctxRoles);
			String[] dtoRoles = securityContextDto.getRoles();

			for (int i = 0; i < ctxRoles.length; i++) {
				equal = ctxRoles[i].equals(dtoRoles[i]);
			}

		}

		return equal;

	}

}
