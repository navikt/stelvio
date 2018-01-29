package no.stelvio.provider.codestable.map;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.ItemNotFoundException;
import no.stelvio.dto.codestable.CodesTablePeriodicItemDto;
import no.stelvio.provider.codestable.TestCtiCode;
import no.stelvio.provider.codestable.TestCtpi;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link CodesTablePeriodicItemConverter}.
 * 
 * @author person19fa65691a36 (Accenture)
 */
public class CodesTablePeriodicItemConverterTest extends AbstractCodesTableItemConverterTest {

	/**
	 * Tests that a code value is correctly converted from a domain object to a DTO.
	 */
	@Test
	public void canConvertFromDomainObjectToDataTransferObject() {

		CodesTablePeriodicItemDto dto = null;

		CodesTablePeriodicItem<TestCtiCode, String> domainObject = TestCtpi.createCtpi1();

		dto = (CodesTablePeriodicItemDto) getConverter().convert(dto, domainObject, CodesTablePeriodicItemDto.class,
				CodesTablePeriodicItem.class);

		Assert.assertEquals(dto.getCode(), domainObject.getCodeAsString());
	}

	/**
	 * Tests that a code value is correctly converted from a DTO to a domain object.
	 */
	@Test
	public void canConvertFromDataTransferObjectToDomainObject() {

		TestCtpi domainObject = null;

		CodesTablePeriodicItemDto dto = new CodesTablePeriodicItemDto();
		dto.setCode(TestCtpi.createCtpi1().getCodeAsString());

		domainObject = (TestCtpi) getConverter().convert(domainObject, dto, TestCtpi.class, CodesTablePeriodicItemDto.class);

		Assert.assertEquals(domainObject.getCode().name(), dto.getCode());
	}

	/**
	 * Tests that an exception is thrown if DTO code is invalid.
	 */
	@Test(expected = ItemNotFoundException.class)
	public void exceptionIsThrownIfInvalidCodeIsUsed() {

		TestCtpi domainObject = null;

		CodesTablePeriodicItemDto dto = new CodesTablePeriodicItemDto();
		dto.setCode("ugyldig");

		domainObject = (TestCtpi) getConverter().convert(domainObject, dto, TestCtpi.class, CodesTablePeriodicItemDto.class);

		Assert.assertEquals(domainObject.getCode().name(), dto.getCode());
	}

	/**
	 * Tests that an exception is thrown if DTO code is invalid.
	 */
	@Test(expected = ItemNotFoundException.class)
	public void exceptionIsThrownByDozerIfInvalidCodeIsUsed() {
		ArrayList<String> mappingFiles = new ArrayList<String>();
		mappingFiles.add("prv-testcodestable-mapping.xml");

		DozerBeanMapper mapper = new DozerBeanMapper(mappingFiles);
		List<CustomConverter> customConverters = new ArrayList<CustomConverter>();
		CodesTablePeriodicItemDto dto = new CodesTablePeriodicItemDto();
		dto.setCode("ugyldig");
		customConverters.add(getConverter());
		mapper.setCustomConverters(customConverters);
		mapper.map(dto, TestCtpi.class);
	}

	/**
	 * Initializing the converter instance with necessary dependencies.
	 */
	@Before
	public void setUp() {
		setConverter(new CodesTablePeriodicItemConverter());
		getConverter().setCodesTableManager(createCodesTableManager());
	}

}
