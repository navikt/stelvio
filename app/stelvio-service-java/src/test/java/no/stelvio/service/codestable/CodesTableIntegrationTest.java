package no.stelvio.service.codestable;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for the codes table support in Stelvio.
 *
 * @author personf8e9850ed756, Accenture
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-codestable-context.xml"})

public class CodesTableIntegrationTest {

	@Resource(name = "rep.inttest.hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	/**
	 * test of load configs.
	 */
	@Test
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

