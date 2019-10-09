package no.stelvio.test.spring.transaction;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Unit test for {@link MockTransactionTemplate}.
 * 
 * @author personf8e9850ed756
 * @version $Revision: 1525 $, $Date: 2004-11-10 16:45:37 +0100 (Wed, 10 Nov 2004) $
 */
public class MockTransactionTemplateTest {
	private MockTransactionTemplate mockTransactionTemplate;

	/**
	 * Should executre transaction callback code.
	 */
	@Test
	public void shouldExecuteTransactionCallbackCode() {
		final boolean[] isCalled = new boolean[] { false };

		mockTransactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				isCalled[0] = true;
				return null;
			}
		});

		assertTrue("Hibernate callback code is not called;", isCalled[0]);
	}

	/**
	 * Should work with transaction status.
	 */
	@Test
	public void shouldWorkWithTransactionStatus() {
		mockTransactionTemplate.expectsOnTransactionStatus(new Expectations() {
			{
				oneOf(mockTransactionTemplate.getProxyTransactionStatus()).setRollbackOnly();
			}
		});

		mockTransactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				status.setRollbackOnly();
				return null;
			}
		});
	}

	/**
	 * Illegal method call on transaction template should throw exception.
	 */
	@Test
	public void illegalMethodCallOnTransactionTemplateShouldThrowException() {
		try {
			mockTransactionTemplate.getTransactionManager();
			fail("UnsupportedOperationException should have been thrown");
		} catch (UnsupportedOperationException uoe) {
			// should be thrown
		}
	}

	/**
	 * Set up transaction template.
	 *
     */
	@Before
	public void setUp() {
		mockTransactionTemplate = new MockTransactionTemplate();
	}

	/**
	 * Verify template on tear down.
	 *
     */
	@After
	public void tearDown() {
		mockTransactionTemplate.verify();
	}
}
