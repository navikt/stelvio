package no.trygdeetaten.common.framework.test.spring.transaction;

import junit.framework.AssertionFailedError;

import org.jmock.MockObjectTestCase;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Unit test for {@link MockTransactionTemplate}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 1525 $, $Date: 2004-11-10 16:45:37 +0100 (Wed, 10 Nov 2004) $
 */
public class MockTransactionTemplateTest extends MockObjectTestCase {
	private MockTransactionTemplate mockTransactionTemplate;

	public void testShouldExecuteTransactionCallbackCode() {
		final boolean[] isCalled = new boolean[]{false};

		mockTransactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				isCalled[0] = true;
				return null;
			}
		});

		assertEquals("Hibernate callback code is not called;", true, isCalled[0]);
	}

	public void testShouldWorkWithTransactionStatus() {
		final boolean[] isCalled = new boolean[]{false};

		mockTransactionTemplate.expectsOnTransactionStatus(once()).method("setRollbackOnly").withNoArguments();
		mockTransactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				status.setRollbackOnly();
				isCalled[0] = true;
				return null;
			}
		});

		assertEquals("Hibernate callback code is not called;", true, isCalled[0]);
	}

	public void testIllegalMethodCallOnTransactionTemplateShouldThrowException() {
		try {
			mockTransactionTemplate.getTransactionManager();
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			// should be thrown
		}
	}

	protected void setUp() throws Exception {
		mockTransactionTemplate = new MockTransactionTemplate();
	}
}
