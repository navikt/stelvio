package no.nav.common.framework.test.spring.transaction;


import junit.framework.AssertionFailedError;

import org.jmock.builder.NameMatchBuilder;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Verifiable;
import org.jmock.Mock;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Mock class mocking TransactionTemplate. Should be used when unit testing classes using a TransactionTemplate so
 * the code typically implemented as anonymous classes for the execute method also will be run when unit testing. When
 * using a regular mock for TransactionTemplate this code will not be run.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2709 $, $Date: 2005-12-13 15:04:27 +0100 (Tue, 13 Dec 2005) $
 * @see org.springframework.transaction.support.TransactionTemplate
 * @see org.springframework.transaction.support.TransactionTemplate#execute(org.springframework.transaction.support.TransactionCallback)
 */
public class MockTransactionTemplate extends TransactionTemplate implements Verifiable {

	private final Mock mockTransactionStatus = new Mock(TransactionStatus.class);
	private final TransactionStatus proxyTransactionStatus = (TransactionStatus) mockTransactionStatus.proxy();

	/**
	 * Sets the expected number of times a method on TransactinStatus should be called. Use the returned value to set
	 * the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
	 * @return an instance of NameMatchBuilder to use for setting which method should be called.
	 * @see NameMatchBuilder
	 */
	public NameMatchBuilder expectsOnTransactionStatus(InvocationMatcher invocationMatcher) {
		return mockTransactionStatus.expects(invocationMatcher);
	}

	/**
	 * Runs the TransactionCallback action and handles the exceptions like it is done in TransactionTemplate, that is,
	 * nothing is done with them as no rollback is done in unit tests. The method is given a mock TransactionStatus
	 *
	 * @param action the TransactionCallback action to run with the provided mock TransactionStatus.
	 * @return whatever is returned from the HibernateCallback action.
	 * @see TransactionCallback
	 * @see org.springframework.transaction.support.TransactionCallbackWithoutResult
	 * @see TransactionStatus
	 * @see TransactionTemplate#execute(org.springframework.transaction.support.TransactionCallback)
	 */
	public Object execute(TransactionCallback action) throws TransactionException {
		return action.doInTransaction(proxyTransactionStatus);
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		throw createNoExecuteAssertionFailed();
	}

	public PlatformTransactionManager getTransactionManager() {
		throw createNoExecuteAssertionFailed();
	}

	public void afterPropertiesSet() {
		throw createNoExecuteAssertionFailed();
	}

	private AssertionFailedError createNoExecuteAssertionFailed() {
		return new AssertionFailedError("Should not be executed");
	}

	public void verify() {
		mockTransactionStatus.verify();
	}
}
