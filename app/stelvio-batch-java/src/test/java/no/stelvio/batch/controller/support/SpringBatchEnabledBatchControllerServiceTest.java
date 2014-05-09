package no.stelvio.batch.controller.support;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBatchEnabledBatchControllerServiceTest {
	private ApplicationContext ctx;
	private SpringBatchEnabledBatchControllerService batchController;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("springbatchenabledbatchcontrollerservice-test-context.xml");
		batchController = (SpringBatchEnabledBatchControllerService) ctx.getBean("batchService");
		batchController.setSpringBatchOperator(new SpringBatchJobOperatorHelper());
	}
	
	/**
	 * The assertions for this test is done in SpringBatchJobOperatorHelper.executeBatch
	 */
	@Test
	public void testRequestContextIsSet() {
		batchController.executeBatch("SpringBatchEnabledBatchControllerService-testJob", "");
	}
	
	@After
	public void tearDown() {
		ctx = null;
		batchController = null;
	}
}
