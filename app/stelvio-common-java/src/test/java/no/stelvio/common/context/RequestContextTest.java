package no.stelvio.common.context;

import junit.framework.TestCase;

import no.stelvio.common.context.support.SimpleRequestContext;

/**
 * RequestContext Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @author person4f9bc5bd17cc, Accenture
 * @version $Revision: 1976 $ $Author: psa2920 $ $Date: 2005-02-16 16:57:25 +0100 (Wed, 16 Feb 2005) $
 */
public class RequestContextTest extends TestCase {
	public void test() {
		String screen = "screen";
		String module = "module";
		String process = "process";
		String transaction = "transaction";

		RequestContext requestContext = new SimpleRequestContext(screen, module, process, transaction);

		assertEquals("screen", screen, requestContext.getScreenId());
		assertEquals("module", module, requestContext.getModuleId());
		assertEquals("process", process, requestContext.getProcessId());
		assertEquals("transaction", transaction, requestContext.getTransactionId());
	}
}