package no.stelvio.common.context;

import org.junit.Test;

import junit.framework.TestCase;

import no.stelvio.common.context.support.SimpleRequestContext;

/**
 * RequestContext Unit Test.
 * 
 * @version $Revision: 1976 $ $Author: psa2920 $ $Date: 2005-02-16 16:57:25 +0100 (Wed, 16 Feb 2005) $
 */
public class RequestContextTest extends TestCase {
	
	/**
	 * Test.
	 */
	@Test
	public void test() {
		String screen = "screen";
		String module = "module";
		String component = "component";
		String transaction = "transaction";

		RequestContext requestContext = new SimpleRequestContext(screen, module, transaction, component);

		assertEquals("screen", screen, requestContext.getScreenId());
		assertEquals("module", module, requestContext.getModuleId());
		assertEquals("component", component, requestContext.getComponentId());
		assertEquals("transaction", transaction, requestContext.getTransactionId());
	}
}