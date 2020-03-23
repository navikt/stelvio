package no.stelvio.provider.codestable.map;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.ItemNotFoundException;
import no.stelvio.dto.codestable.CodesTableItemDto;
import no.stelvio.provider.codestable.TestCti;
import no.stelvio.provider.codestable.TestCtiCode;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link CodesTableItemConverter}.
 * 
 */
public class CodesTableItemConverterTest extends AbstractCodesTableItemConverterTest {

	/**
	 * Tests that a code value is correctly converted from a domain object to a DTO.
	 */
	@Test
	public void canConvertFromDomainObjectToDataTransferObject() {

		CodesTableItemDto dto = null;

		CodesTableItem<TestCtiCode, String> domainObject = TestCti.createCti1();

		dto = (CodesTableItemDto) getConverter().convert(dto, domainObject, CodesTableItemDto.class, CodesTableItem.class);

		Assert.assertEquals(dto.getCode(), domainObject.getCodeAsString());
	}

	/**
	 * Tests that a code value is correctly converted from a DTO to a domain object.
	 */
	@Test
	public void canConvertFromDataTransferObjectToDomainObject() {

		TestCti domainObject = null;

		CodesTableItemDto dto = new CodesTableItemDto();
		dto.setCode(TestCti.createCti1().getCodeAsString());

		domainObject = (TestCti) getConverter().convert(domainObject, dto, TestCti.class, CodesTableItemDto.class);

		Assert.assertEquals(domainObject.getCode().name(), dto.getCode());
	}

	/**
	 * Tests that an exception is thrown if DTO code is invalid.
	 */
	@Test(expected = ItemNotFoundException.class)
	public void exceptionIsThrownIfInvalidCodeIsUsed() {

		TestCti domainObject = null;

		CodesTableItemDto dto = new CodesTableItemDto();

		dto.setCode("ugyldig");

		getConverter().convert(domainObject, dto, TestCti.class, CodesTableItemDto.class);
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
		CodesTableItemDto dto = new CodesTableItemDto();
		dto.setCode("ugyldig");
		customConverters.add(getConverter());
		mapper.setCustomConverters(customConverters);
		mapper.map(dto, TestCti.class);
	}

	/**
	 * Initializing the converter instance with necessary dependencies.
	 */
	@Before
	public void setUp() {
		setConverter(new CodesTableItemConverter());
		getConverter().setCodesTableManager(createCodesTableManager());
	}

}
