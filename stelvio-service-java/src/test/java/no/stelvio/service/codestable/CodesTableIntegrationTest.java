package no.stelvio.service.codestable;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for the codes table support in Stelvio.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CodeTableConfiguration.class)

public class CodesTableIntegrationTest {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	/**
	 * test of load configs.
	 */
	@Test
	@Transactional
	public void testLoadConfigs() {


		hibernateTemplate.save(TestCti.createCti1());
		hibernateTemplate.save(TestCti.createCti2());
		hibernateTemplate.save(TestCtpi.createCtpi1());
		hibernateTemplate.save(TestCtpi.createCtpi2());
		hibernateTemplate.save(TestCtpi.createCtpi3());

		List<TestCti> cts = hibernateTemplate.loadAll(TestCti.class);
		hibernateTemplate.loadAll(TestCtpi.class);

		for (TestCti cti : cts) {
			// To check that the code is cached
			cti.getCode();
		}

		assertEquals("Wrong number of rows in db", 2, cts.size());
	}
}

