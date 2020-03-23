package no.stelvio.provider.time.map;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import no.stelvio.domain.time.ChangeStamp;
import no.stelvio.dto.time.ChangeStampDto;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests mapping between <code>ChangeStamp</code> and <code>ChangeStampDto</code>.
 * 
 */
public class ChangeStampMapperTest extends TestCase {

	private ChangeStampMapper mapper;

	/**
	 * Creates a context and a mapper before testing.
	 */
	@Before
	public void setUp() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:modules/prv-stelvio-context.xml");
		Mapper mapper = (Mapper) context.getBean("prv.stelvio.mapping.Mapper");

		this.mapper = new ChangeStampMapper();
		this.mapper.setDozerMapper(mapper);
	}

	/**
	 * Test of change stamp mapping.
	 */
	@Test
	public void testMapChangeStamp() {

		Date createdDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).getTime();
		Date updatedDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
				Calendar.MINUTE).getTime();

		ChangeStamp changeStamp = new ChangeStamp("USER1", createdDate, "USER2", updatedDate);

		ChangeStampDto changeStampDto = mapper.map(changeStamp);

		assertTrue(isEqual(changeStamp, changeStampDto));

	}

	/**
	 * Test of change stamp dto mapping.
	 */
	@Test
	public void testMapChangeStampDto() {

		Date createdDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).getTime();
		Date updatedDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
				Calendar.MINUTE).getTime();

		ChangeStampDto changeStampDto = new ChangeStampDto("USER1", createdDate, "USER2", updatedDate);

		ChangeStamp changeStamp = mapper.map(changeStampDto);

		assertTrue(isEqual(changeStamp, changeStampDto));
	}

	/**
	 * Compares domain and dto field values.
	 * 
	 * @param changeStamp
	 *            change stamp
	 * @param changeStampDto
	 *            dto change stamp
	 * @return true if equal
	 */
	private boolean isEqual(ChangeStamp changeStamp, ChangeStampDto changeStampDto) {
		return changeStamp.getCreatedBy().equals(changeStampDto.getCreatedBy())
				&& changeStamp.getCreatedDate().equals(changeStampDto.getCreatedDate())
				&& changeStamp.getUpdatedBy().equals(changeStampDto.getUpdatedBy())
				&& changeStamp.getUpdatedDate().equals(changeStampDto.getUpdatedDate());
	}

}
