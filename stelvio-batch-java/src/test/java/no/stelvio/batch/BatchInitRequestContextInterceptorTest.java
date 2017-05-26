package no.stelvio.batch;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for BatchInitRequestContextInterceptor.
 * 
 * @author MA
 * 
 */
public class BatchInitRequestContextInterceptorTest {

	private ApplicationContext ctx;

	/**
	 * Setting up context for test.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("batchinitrequestcontext-test-context.xml");
	}

	/**
	 * The assertions are done by the Pojo that serves as a delegate for the executor The pojo will assert that the
	 * RequestContext is initialized and in the correct state. The interceptor will be executed before the call is delegated to
	 * the pojo, thus the RequestContet should be initialized when the pojo is executed.
	 */
	@Test
	public void requestContextIsInitialized() {
		FakeBatchExecutorEJB batchExecutor = (FakeBatchExecutorEJB) ctx.getBean("test.requestcontextinit.batchExecutor");
		batchExecutor.execute();
	}

}
