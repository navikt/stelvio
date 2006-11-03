package no.stelvio.common.context;

import no.stelvio.common.context.RequestContext;
import junit.framework.TestCase;

/**
 * RequestContext Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1976 $ $Author: psa2920 $ $Date: 2005-02-16 16:57:25 +0100 (Wed, 16 Feb 2005) $
 */
public class RequestContextTest extends TestCase {

	/**
	 * Constructor for RequestContextTest.
	 * @param arg0
	 */
	public RequestContextTest(String arg0) {
		super(arg0);
	}

	public void test() {
		String user = "user";
		String screen = "screen";
		String module = "module";
		String process = "process";
		String transaction = "transaction";

		RequestContext.setProcessId(process);
		RequestContext.setScreenId(screen);
		RequestContext.setModuleId(module);
		RequestContext.setTransactionId(transaction);
		RequestContext.setUserId(user);

		assertEquals("user", user, RequestContext.getUserId());
		assertEquals("screen", screen, RequestContext.getScreenId());
		assertEquals("module", module, RequestContext.getModuleId());
		assertEquals("process", process, RequestContext.getProcessId());
		assertEquals("transaction", transaction, RequestContext.getTransactionId());

		assertNull("user", RequestContext.getUserId());

	}

}
