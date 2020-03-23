package no.stelvio.common.config;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Automatic unit test of the class EnvironmentAwareResource.
 * 
 * @version $Id$
 */
public class EnvironmentAwareResourceTest {

	/**
	 * Test opptjening batch config.
	 */
	@Test
	public void testOpptjeningBatchConfig() {
		String[] classpath = { "test-environmentAwareResource-context.xml" };
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(classpath);
		EnvironmentAwareResource bean = (EnvironmentAwareResource) context.getBean("" + "environmentAwareResource");
		assertEquals("test-environment.komptest.properties", bean.getFilename());
	}

}
