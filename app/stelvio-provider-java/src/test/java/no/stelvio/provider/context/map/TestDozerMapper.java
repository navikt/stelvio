package no.stelvio.provider.context.map;

import static org.junit.Assert.assertTrue;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.dto.context.RequestContextDto;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for DozerMapper.
 * 
 * @author CW
 * 
 */
public class TestDozerMapper {

	private Mapper mapper;

	/**
	 * Setting up context and mapper before test.
	 * 
	 */
	@Before
	public void injectMapper() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:modules/prv-stelvio-context.xml");
		mapper = (Mapper) context.getBean("prv.stelvio.mapping.Mapper");
	}

	/**
	 * Test map from domain to dto.
	 */
	@Test
	public void mapFromDomainToDto() {
		// Create domain object
		RequestContext requestContext = new SimpleRequestContext("ScreedID::10", "ModuleID::20", "TransactionID::30",
				"ComponentID::40", "UserID::50");
		// Map to dto object
		RequestContextDto requestContextDto = mapper.map(requestContext, RequestContextDto.class);

		assertTrue(isEqual(requestContext, requestContextDto));
	}

	/**
	 * Test map from dto to domain.
	 */
	@Test
	public void mapFromDtoToDomain() {
		// Create dto object
		RequestContextDto requestContextDto = new RequestContextDto();
		requestContextDto.setScreenId("ScreedID::10");
		requestContextDto.setModuleId("ModuleID::20");
		requestContextDto.setTransactionId("TransactionID::30");
		requestContextDto.setComponentId("ComponentID::40");
		requestContextDto.setUserId("UserID::50");

		RequestContext requestContext = (RequestContext) mapper.map(requestContextDto, SimpleRequestContext.class);
		assertTrue(isEqual(requestContext, requestContextDto));

	}

	/***
	 * Determines whether all the fields in the <code>RequestContextDto</code> object have values corresponding to the
	 * <code>RequestContext</code> counterparts.
	 * 
	 * @param requestContext
	 *            request context
	 * @param requestContextDto
	 *            request context dto
	 * @return true if requests are equal
	 */
	private boolean isEqual(RequestContext requestContext, RequestContextDto requestContextDto) {

		return (requestContext.getScreenId().equals(requestContextDto.getScreenId())
				&& requestContext.getModuleId().equals(requestContextDto.getModuleId())
				&& requestContext.getTransactionId().equals(requestContextDto.getTransactionId())
				&& requestContext.getComponentId().equals(requestContextDto.getComponentId()) 
				&& requestContext.getUserId().equals(requestContextDto.getUserId()));

	}
}
