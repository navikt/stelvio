package no.nav.common.framework.service;

import no.nav.common.framework.service.ServiceResponse;
import junit.framework.TestCase;

/**
 * ServiceResponse Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1043 $ $Author: psa2920 $ $Date: 2004-08-16 17:11:51 +0200 (Mon, 16 Aug 2004) $
 */
public class ServiceResponseTest extends TestCase {

	/**
	 * Constructor for ServiceResponseTest.
	 * @param arg0
	 */
	public ServiceResponseTest(String arg0) {
		super(arg0);
	}

	public void test() {
		Object o1 = "Value 1";
		ServiceResponse r = new ServiceResponse();
		r.setData("key", o1);
		assertEquals("Get/Set failed", o1, r.getData("key"));

		Object o2 = "Value 2";
		ServiceResponse r2 = new ServiceResponse("key", o2);

		assertEquals("Get/Set failed", o2, r2.getData("key"));
	}
}
