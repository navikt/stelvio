package no.trygdeetaten.business.framework.service;

import org.jmock.cglib.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.orm.hibernate3.HibernateTemplate;

import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * BusinessHibernateService Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: BusinessHibernateServiceTest.java 2370 2005-06-23 14:39:51Z skb2930 $
 */
public class BusinessHibernateServiceTest extends MockObjectTestCase {

	Mock hibernateTemplateMock = null;

	public void testSetGetHibernateTemplate() {

		// Setup test
		hibernateTemplateMock = new Mock(HibernateTemplate.class);

		// Execute test
		BusinessHibernateService service = new TestBusinessHibernateService();
		service.setHibernateTemplate((HibernateTemplate) hibernateTemplateMock.proxy());
		Object result = service.getHibernateTemplate();

		// Verify result
		assertTrue("Get/Set HibernateTemplate Failed", result instanceof HibernateTemplate);
	}

	public void testInitializeOK() {

		// Execute test
		BusinessHibernateService service = new TestBusinessHibernateService();
		service.initialize(new Object());

		// Verify result
		assertTrue("Initialize succeeded", true);
	}

	private class TestBusinessHibernateService extends BusinessHibernateService {
		protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
			return null;
		}
		public void initialize(Object proxy) {
			super.initialize(proxy);
		}
	}
}
