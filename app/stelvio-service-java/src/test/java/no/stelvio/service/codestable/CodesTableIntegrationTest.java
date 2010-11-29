package no.stelvio.service.codestable;

import static no.stelvio.common.util.Internal.cast;

import java.util.List;

import org.springframework.beans.factory.generic.GenericBeanFactoryAccessor;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Integration test for the codes table support in Stelvio.
 *
 * @author personf8e9850ed756, Accenture
 */
public class CodesTableIntegrationTest extends AbstractDependencyInjectionSpringContextTests {
	private GenericBeanFactoryAccessor beanFactoryAccessor;

	/**
	 * test of load configs.
	 */
	public void testLoadConfigs() {
		HibernateTemplate hibernateTemplate = beanFactoryAccessor.getBean("rep.inttest.hibernateTemplate");

		hibernateTemplate.save(TestCti.createCti1());
		hibernateTemplate.save(TestCti.createCti2());
		hibernateTemplate.save(TestCtpi.createCtpi1());
		hibernateTemplate.save(TestCtpi.createCtpi2());
		hibernateTemplate.save(TestCtpi.createCtpi3());

		List<TestCti> cts = cast(hibernateTemplate.loadAll(TestCti.class));
		hibernateTemplate.loadAll(TestCtpi.class);

		for (TestCti cti : cts) {
			// To check that the code is cached
			cti.getCode();
		}

		assertEquals("Wrong number of rows in db", 2, cts.size());
	}

	/**
	 * on setup.
	 * 
	 * @throws Exception if setup fails
	 */
	protected void onSetUp() throws Exception {
		super.onSetUp();
		beanFactoryAccessor = new GenericBeanFactoryAccessor(applicationContext);
	}

	/**
	 * Get config locations.
	 * 
	 * @return config locations
	 */
	protected String[] getConfigLocations() {
		return new String[]{"test-codestable-context.xml"};
	}
}
