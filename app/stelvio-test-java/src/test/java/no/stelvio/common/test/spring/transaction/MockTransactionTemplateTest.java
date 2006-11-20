package no.stelvio.common.test.spring.transaction;

import org.jmock.InAnyOrder;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

    @Test
    public void shouldExecuteTransactionCallbackCode() {
		final boolean[] isCalled = new boolean[]{false};

		mockTransactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				isCalled[0] = true;
				return null;
			}
		});

        assertTrue("Hibernate callback code is not called;", isCalled[0]);
	}

    @Test
    public void shouldWorkWithTransactionStatus() {
        // TODO is it possible to improve on this?
        mockTransactionTemplate.expectsOnTransactionStatus(new InAnyOrder() {{
            one(mockTransactionTemplate.proxyTransactionStatus).setRollbackOnly();
        }});

		mockTransactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				status.setRollbackOnly();
				return null;
			}
		});
	}

    @Test
	public void illegalMethodCallOnTransactionTemplateShouldThrowException() {
		try {
			mockTransactionTemplate.getTransactionManager();
			fail("UnsupportedOperationException should have been thrown");
		} catch (UnsupportedOperationException uoe) {
			// should be thrown
		}
	}
                                                                    
    @Before
    public void setUp() throws Exception {
		mockTransactionTemplate = new MockTransactionTemplate();
	}

    @After
    public void tearDown() throws Exception {
        mockTransactionTemplate.verify();
    }
}
