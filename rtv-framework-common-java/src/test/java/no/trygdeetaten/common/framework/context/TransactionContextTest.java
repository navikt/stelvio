package no.trygdeetaten.common.framework.context;

import junit.framework.TestCase;

/**
 * TransactionContext Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1976 $ $Author: psa2920 $ $Date: 2005-02-16 16:57:25 +0100 (Wed, 16 Feb 2005) $
 */
public class TransactionContextTest extends TestCase {

	/**
	 * Constructor for TransactionContextTest.
	 * @param arg0
	 */
	public TransactionContextTest(String arg0) {
		super(arg0);
	}

	public void test() {

		String user = "user";
		String screen = "screen";
		String module = "module";
		String process = "process";
		String transaction = "transaction";

		TransactionContext.setProcessId(process);
		TransactionContext.setScreenId(screen);
		TransactionContext.setModuleId(module);
		TransactionContext.setTransactionId(transaction);
		TransactionContext.setUserId(user);

		assertEquals("user", user, TransactionContext.getUserId());
		assertEquals("screen", screen, TransactionContext.getScreenId());
		assertEquals("module", module, TransactionContext.getModuleId());
		assertEquals("process", process, TransactionContext.getProcessId());
		assertEquals("transaction", transaction, TransactionContext.getTransactionId());

		TransactionContext.remove();

		assertNull("user", TransactionContext.getUserId());

	}

}
