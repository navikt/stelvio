package no.stelvio.common.codestable;

import java.util.List;
import java.util.Set;

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

	public void testLoadConfigs() {
		HibernateTemplate hibernateTemplate = beanFactoryAccessor.getBean("rep.inttest.hibernateTemplate");
		List oldEnums = hibernateTemplate.loadAll(TestCti.class);
		hibernateTemplate.deleteAll(oldEnums);

		hibernateTemplate.save(new TestCti(TestCtiCode.EXISTS_1, 1, true));
		hibernateTemplate.save(new TestCti(TestCtiCode.EXISTS_2, 2, true));

		TestIntegration testIntegration = beanFactoryAccessor.getBean("rep.inttest.testIntegration");
		Set<TestCti> testCodesTableItems = testIntegration.loadData();

		for (TestCti cti : testCodesTableItems) {
			// To check that the code is cached
			cti.getCode();
		}

		assertEquals("Wrong number of rows in db", 2, testCodesTableItems.size());
	}

	protected void onSetUp() throws Exception {
		super.onSetUp();
		beanFactoryAccessor = new GenericBeanFactoryAccessor(applicationContext);
	}

	protected String[] getConfigLocations() {
		return new String[]{"test-codestable-context.xml"};
	}
}
