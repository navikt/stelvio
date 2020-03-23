package no.stelvio.batch.validation.support;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import no.stelvio.batch.DummyBatchParameterValidator;
import no.stelvio.batch.domain.BatchDO;
import no.stelvio.common.error.InvalidArgumentException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BatchValidator.
 * 
 */
public class BatchParameterValidatorTest {

	private HashSet<String> correctParameters, workUnitMissing, misspelledParameters, requiredParameterMissing;
	private HashSet<String> requiredParameters;
	private HashSet<String> optionalParameters;
	private DummyBatchParameterValidator batch = new DummyBatchParameterValidator();
	private BatchParameterValidator batchValidator = new BatchParameterValidator(batch);

	/**
	 * Setup before testing. 
	 */
	@Before
	public void setUp() {
		correctParameters = newHashSet("workUnit", "requiredParameter", "progressInterval");
		workUnitMissing = newHashSet("requiredParameter", "progressInterval");
		misspelledParameters = newHashSet("workUnit", "requiredParameter", "progreZZentrvl", "notExistingParameter");
		requiredParameterMissing = newHashSet("workUnit");

		requiredParameters = newHashSet("workUnit", "requiredParameter");
		optionalParameters = newHashSet("progressInterval", "optionalParameter");
	}

	/**
	 * Set new batchDO.
	 */
	private void setBatchDO() {
		BatchDO batchDO = new BatchDO();
		batchDO.setBatchname("Batch");
		batchDO.setParameters("a=1;b=2");
		batch.setBatchDO(batchDO);
	}

	/**
	 * Read actualParameters. Assert if the number of actual parameteres is correct.
	 * 
	 */
	@Test
	public void readActualParameters() {
		assertEquals("Incorrect number of parameters", 3, batchValidator.getActualParameters().size());
	}

	/**
	 * Sets and then reads actualParameters. Assert if the number of actual parameteres is correct.
	 * 
	 */
	@Test
	public void modifyActualParameters() {
		setBatchDO();
		assertEquals("Incorrect number of parameters", 2, batchValidator.getActualParameters().size());
	}

	/**
	 * Testing validation of the case "should start with correct parameters".
	 */
	@Test
	public void shouldStartWithCorrectParameters() {
		batchValidator.validate(correctParameters, requiredParameters, optionalParameters);
	}

	/**
	 * Testing validation of the case "should not start batch when workunit is missing".
	 */
	@Test(expected = InvalidArgumentException.class)
	public void shouldNotStartBatchWhenWorkunitIsMissing() {
		batchValidator.validate(workUnitMissing, requiredParameters, optionalParameters);
	}

	/**
	 * Testing validation of the case "should not start batch when there are misspelled parameters".
	 */
	@Test(expected = InvalidArgumentException.class)
	public void shouldNotStartBatchWhenThereAreMisspelledParameters() {
		batchValidator.validate(misspelledParameters, requiredParameters, optionalParameters);
	}

	/**
	 * Testing validation of the case "should not start batch when there are required parameters missing".
	 */
	@Test(expected = InvalidArgumentException.class)
	public void shouldNotStartBatchWhenThereAreRequiredParametersMissing() {
		batchValidator.validate(requiredParameterMissing, requiredParameters, optionalParameters);
	}

	/**
	 * Creating a new hash set.
	 * 
	 * @param strings strings to place in the set.
	 * @return the new has set
	 */
	private HashSet<String> newHashSet(String... strings) {
		HashSet<String> set = new HashSet<String>();
		for (String string : strings) {
			set.add(string);
		}
		return set;
	}
}
