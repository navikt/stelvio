package no.stelvio.batch.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import no.stelvio.batch.support.TypeConvertingJobParametersValidator.StringBooleanJobParameter;
import no.stelvio.batch.support.TypeConvertingJobParametersValidator.StringDateJobParameter;
import no.stelvio.batch.support.TypeConvertingJobParametersValidator.StringEnumJobParameter;
import no.stelvio.batch.support.TypeConvertingJobParametersValidator.StringLongJobParameter;
import no.stelvio.batch.support.TypeConvertingJobParametersValidator.StringShortJobParameter;
import no.stelvio.batch.support.TypeConvertingJobParametersValidator.StringUriJobParameter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.test.MetaDataInstanceFactory;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public class TypeConvertingJobParametersValidatorTest {
	private TypeConvertingJobParametersValidator validator;
	private String datePattern = "mm.DD.yyyy";
	private SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
	
	private enum ParamValidatorEnum {
		VALID,
		
		NOT_VALID;
	}
	
	@Before
	public void setUp() {
		validator = new TypeConvertingJobParametersValidator();
	
	}
	
	@Test
	public void shouldValidateRequiredParameters() throws Exception {
		String requiredKey = "test";
		validator.setRequiredParameters(Arrays.asList(new StringLongJobParameter(requiredKey)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(requiredKey));
		}
	}
	
	@Test
	public void shouldRequireStringType() throws Exception {
		String key = "key";
		validator.setRequiredParameters(Arrays.asList(new StringLongJobParameter(key )));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addLong(key, 1L).toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains("String"));
		}		
	}
	
	@Test
	public void shouldValidateTypeConversionOfDateParameters() throws Exception {
		String key = "date";
		validator.setOptionalParameters(Arrays.asList(new StringDateJobParameter(key, datePattern)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addString(key, "2010.10.01").toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(key));
		}
	}
	
	@Test
	public void shouldValidateTypeConversionOfLongParameters() throws Exception {
		String key = "long";
		validator.setOptionalParameters(Arrays.asList(new StringLongJobParameter(key)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addString(key, "4.1").toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(key));
		}
	}	
	
	@Test
	public void shouldValidateTypeConversionOfShortParameters() throws Exception {
		String key = "short";
		validator.setOptionalParameters(Arrays.asList(new StringShortJobParameter(key)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addString(key, "4.1").toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(key));
		}
	}	
	
	
	@Test
	public void shouldPutParametersOnExecutionContext() throws Exception {
		String requiredLongKey = "requiredLongKey";
		validator.setRequiredParameters(Arrays.asList(new StringLongJobParameter(requiredLongKey)));
		String optionalDateKey = "optionalDateKey";
		String optionalLongKey = "optionalLongKey";
		validator.setOptionalParameters(Arrays.asList(
				new StringDateJobParameter(optionalDateKey, datePattern),
				new StringLongJobParameter(optionalLongKey)));
		validator.afterPropertiesSet();
		
		String date = "01.10.2010";
		Date dateValue = dateFormat.parse(date);
		Long longValue = 123L;
		
		JobExecution jobExecution = validate(new JobParametersBuilder()
			.addString(optionalDateKey, date)
			.addString(requiredLongKey, longValue.toString())
			.toJobParameters());
		
		assertEquals(dateValue, (Date) jobExecution.getExecutionContext().get(optionalDateKey));
		assertEquals(longValue, (Long) jobExecution.getExecutionContext().get(requiredLongKey));
		assertEquals(null, (Long) jobExecution.getExecutionContext().get(optionalLongKey));
	}
	
	@Test
	public void shouldValidateTypeConversionOfEnumParameters() throws Exception {
		String key = "enum";
		validator.setOptionalParameters(Arrays.asList(new StringEnumJobParameter(key, ParamValidatorEnum.class)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addString(key, "VAL").toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(key));
		}
	}	
	
	@Test
	public void shouldHandleEnumParameters() throws Exception {
		String key = "enum";
		validator.setOptionalParameters(Arrays.asList(new StringEnumJobParameter(key, ParamValidatorEnum.class)));
		validator.afterPropertiesSet();
		ParamValidatorEnum value = ParamValidatorEnum.VALID;
		JobExecution jobExecution = validate(new JobParametersBuilder()
			.addString(key, value.name()).toJobParameters());
		
		assertEquals(value, (ParamValidatorEnum) jobExecution.getExecutionContext().get(key));
	}
	
	@Test
	public void shouldValidateTypeConversionOfUriParameters() throws Exception {
		String key = "uri";
		validator.setOptionalParameters(Arrays.asList(new StringUriJobParameter(key)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addString(key, "/test\\/").toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(key));
		}
	}
	
	@Test
	public void shouldHandleUriParameters() throws Exception {
		String key = "uri";
		validator.setOptionalParameters(Arrays.asList(new StringUriJobParameter(key)));
		validator.afterPropertiesSet();
		
		String value = "/test/";
		JobExecution jobExecution = validate(new JobParametersBuilder().addString(key, value.toString()).toJobParameters());
		assertEquals(value, jobExecution.getExecutionContext().getString(key));
	}	

	@Test
	public void shouldValidateTypeConversionOfBooleanParameters() throws Exception {
		String key = "boolean";
		validator.setOptionalParameters(Arrays.asList(new StringBooleanJobParameter(key)));
		validator.afterPropertiesSet();
		try {
			validate(new JobParametersBuilder().addString(key, "hello").toJobParameters());
			fail();
		} catch (JobParametersInvalidException expectedException) {
			assertTrue(expectedException.getMessage().contains(key));
		}		
	}
	
	@Test
	public void shouldValidateTypeConversionFromStringToTrue() throws Exception {
		StringBooleanJobParameter parameter = new StringBooleanJobParameter("key");
		parameter.validateTypeConversion("True");
		assertTrue(parameter.getValue());
		parameter.validateTypeConversion("true");
		assertTrue(parameter.getValue());
		parameter.validateTypeConversion("tRuE");
		assertTrue(parameter.getValue());
	}
	
	@Test
	public void shouldValidateTypeConversionFromStringToFalse() throws Exception {
		StringBooleanJobParameter parameter = new StringBooleanJobParameter("key");
		parameter.validateTypeConversion("False");
		assertFalse(parameter.getValue());
		parameter.validateTypeConversion("false");
		assertFalse(parameter.getValue());
		parameter.validateTypeConversion("fAlSe");
		assertFalse(parameter.getValue());
	}	
	
	private JobExecution validate(JobParameters jobParameters) throws JobParametersInvalidException {
		validator.validate(jobParameters);
		JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
		jobExecution.setJobInstance(MetaDataInstanceFactory.createJobInstance("test", 1L, jobParameters));
		validator.beforeJob(jobExecution);
		return jobExecution;
	}
	
}
