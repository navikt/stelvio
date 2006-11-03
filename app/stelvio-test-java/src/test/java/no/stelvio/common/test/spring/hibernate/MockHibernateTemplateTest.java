package no.stelvio.common.test.spring.hibernate;

import no.stelvio.common.test.spring.hibernate.MockHibernateTemplate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jmock.MockObjectTestCase;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Unit test for {@link MockHibernateTemplate}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2385 $, $Date: 2005-06-27 09:19:46 +0200 (Mon, 27 Jun 2005) $
 */
public class MockHibernateTemplateTest extends MockObjectTestCase {
	private MockHibernateTemplate mockHibernateTemplate;

	public void testShouldExecuteHibernateCallbackCode() {
		final boolean[] isCalled = new boolean[] {false};

		mockHibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				isCalled[0] = true;
				return null;
			}
		});

		assertEquals("Hibernate callback code is not called;", true, isCalled[0]);
	}

	public void testSessionShouldBeMockableWithinCallbackCode() {
		final boolean[] isCalled = new boolean[] {false};
        mockHibernateTemplate.expectsOnSession(once()).method("getEntityName").with(eq("test")).will(returnValue("retvalue"));

		mockHibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				isCalled[0] = true;
				assertEquals("Mock is not setup correctly;", "retvalue", session.getEntityName("test"));

				return null;
			}
		});

		assertTrue("Hibernate callback code is not called", isCalled[0]);
	}

	protected void setUp() throws Exception {
		mockHibernateTemplate = new MockHibernateTemplate();
	}
}
