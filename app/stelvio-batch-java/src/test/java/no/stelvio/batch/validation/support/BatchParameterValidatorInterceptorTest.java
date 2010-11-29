package no.stelvio.batch.validation.support;

import no.stelvio.batch.FakeBatchExecutorEJB;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Testing the configuration of the interceptor batch parameter validator.
 * 
 * @author persondfa1fa919e87(Accenture)
 */
public class BatchParameterValidatorInterceptorTest {

	private ApplicationContext ctx;
	private FakeBatchExecutorEJB batchExecutor;

	/**
	 * Setting context and executor before test.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("batchvalidator-test-context.xml");
		batchExecutor = (FakeBatchExecutorEJB) ctx.getBean("test.batchvalidator.batchExecutor");
	}

	/**
	 * requiredParameters: workUnit, requiredParameter (from spring config) optionalParameters: progressInterval,
	 * optionalParameter (from spring config) actualParameters: workUnit, requiredParameter, progressInterval (from
	 * BatchValidatorImpl constructor).
	 */
	@Test
	public void test() {
		batchExecutor.execute();
	}

}