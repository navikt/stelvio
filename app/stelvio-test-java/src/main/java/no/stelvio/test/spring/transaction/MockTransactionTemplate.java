package no.stelvio.test.spring.transaction;

import org.jmock.Mockery;
import org.jmock.internal.ExpectationGroupBuilder;
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
public class MockTransactionTemplate extends TransactionTemplate {
    /**
	 * The id used to check version of object when serializing.
	 */
	private static final long serialVersionUID = 1207560968402907790L;
	
	private final Mockery context = new Mockery();
    final TransactionStatus proxyTransactionStatus = context.mock(TransactionStatus.class);

	/**
	 * Sets the expected number of times a method on TransactinStatus should be called. Use the returned value to set
	 * the expected method.
	 *
	 * @param invocationMatcher a matcher for setting how many times a method should be called.
     * @todo fix javadoc
     * @todo don't need an expect method especially for TransactionStatus; doesn't give anything
	 */
	public void expectsOnTransactionStatus(ExpectationGroupBuilder builder) {
        context.expects(builder);
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
		throwUnsupportedOperationException();
	}

	public PlatformTransactionManager getTransactionManager() {
		throwUnsupportedOperationException();
        // Will never get here
        return null;
    }

	public void afterPropertiesSet() {
		throwUnsupportedOperationException();
	}

	private void throwUnsupportedOperationException() {
		throw new UnsupportedOperationException("Should not be executed");
	}

	public void verify() {
		context.assertIsSatisfied();
	}
}
