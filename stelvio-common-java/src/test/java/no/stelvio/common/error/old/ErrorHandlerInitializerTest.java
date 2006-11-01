package no.stelvio.common.error.old;

import junit.framework.TestCase;
import no.stelvio.common.core.Initializable;

/**
 * ErrorHandlerInitializer Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1037 $ $Author: psa2920 $ $Date: 2004-08-16 15:25:10 +0200 (Mon, 16 Aug 2004) $
 */
public class ErrorHandlerInitializerTest extends TestCase {

	/**
	 * Constructor for ErrorHandlerInitializerTest.
	 * @param arg0
	 */
	public ErrorHandlerInitializerTest(String arg0) {
		super(arg0);
	}

	public void test() {
		Initializable i = new ErrorHandlerInitializer();
		i.init();
		assertTrue(true);
	}

}
