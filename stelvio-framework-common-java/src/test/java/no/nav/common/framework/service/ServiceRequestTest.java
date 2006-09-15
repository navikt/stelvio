package no.nav.common.framework.service;

import no.nav.common.framework.service.ServiceRequest;
import junit.framework.TestCase;

/**
 * ServiceRequest Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2511 $ $Author: skb2930 $ $Date: 2005-10-03 09:59:10 +0200 (Mon, 03 Oct 2005) $
 */
public class ServiceRequestTest extends TestCase {

	public void test() {
		String n1 = "Name 1";
		Object o1 = "Value 1";
		ServiceRequest r1 = new ServiceRequest();
		r1.setServiceName(n1);
		r1.setData("key", o1);
		assertEquals("Get/Set name failed", n1, r1.getServiceName());
		assertEquals("Get/Set data failed", o1, r1.getData("key"));

		String n2 = "Name 2";
		Object o2 = "Value 2";
		ServiceRequest r2 = new ServiceRequest(n2);
		r2.setData("key", o2);
		assertEquals("Get/Set name failed", n2, r2.getServiceName());
		assertEquals("Get/Set data failed", o2, r2.getData("key"));

		String n3 = "Name 3";
		Object o3 = "Value 3";
		ServiceRequest r3 = new ServiceRequest(n3, "key", o3);
		assertEquals("Get/Set name failed", n3, r3.getServiceName());
		assertEquals("Get/Set data failed", o3, r3.getData("key"));
	}

	public void testHashCode() {
		ServiceRequest r1 = new ServiceRequest("MyName", "MyKey", "MyValue");
		ServiceRequest r2 = new ServiceRequest("MyName", "MyKey", "MyValue");

		assertTrue("Hash codes should have matched", r1.hashCode() == r2.hashCode());
	}

	public void testEqualsTrue() {
		ServiceRequest r1 = new ServiceRequest();
		ServiceRequest r2 = new ServiceRequest();
		assertEquals("Equals should have matched (no name, no data)", r1, r2);

		ServiceRequest r3 = new ServiceRequest("MyName");
		ServiceRequest r4 = new ServiceRequest("MyName");
		assertEquals("Equals should have matched (name, no data)", r3, r4);

		ServiceRequest r5 = new ServiceRequest("MyName", "MyKey", "MyValue");
		ServiceRequest r6 = new ServiceRequest("MyName", "MyKey", "MyValue");
		assertEquals("Equals should have matched (name,data)", r5, r6);

		String psa2920 = "psa2920";
		ServiceRequest r7 = new ServiceRequest("HentFulltNavnForIdent", "FilterArguments", new String[] { psa2920 });
		ServiceRequest r8 = new ServiceRequest("HentFulltNavnForIdent", "FilterArguments", new String[] { psa2920 });
		assertEquals("Equals should have matched (name,array data)", r7, r8);
	}

	public void testEqualsFalse() {
		ServiceRequest r1 = new ServiceRequest();
		ServiceRequest r2 = new ServiceRequest("");
		assertFalse("Equals should not have matched (no name, no data)", r1.equals(r2));

		ServiceRequest r3 = new ServiceRequest("MyName");
		ServiceRequest r4 = new ServiceRequest("MyName2");
		assertFalse("Equals should not have matched (name, no data)", r3.equals(r4));

		ServiceRequest r5 = new ServiceRequest("MyName", "MyKey", "MyValue");
		ServiceRequest r6 = new ServiceRequest("MyName", "MyKey2", "MyValue");
		assertFalse("Equals should have matched (name,data)", r5.equals(r6));

		ServiceRequest r7 = new ServiceRequest("MyName", "MyKey", "MyValue");
		ServiceRequest r8 = new ServiceRequest("MyName", "MyKey", "MyValue2");
		assertFalse("Equals should have matched (name,data)", r7.equals(r8));

		ServiceRequest r9 = new ServiceRequest("HentFulltNavnForIdent", "FilterArguments", new String[] { "ident1" });
		ServiceRequest r10 = new ServiceRequest("HentFulltNavnForIdent", "FilterArguments", new String[] { "ident2" });
		assertFalse("Equals should have matched (name,array data)", r9.equals(r10));
	}

	public void testToString() {
		final ServiceRequest[] requests = new ServiceRequest[]{
				new ServiceRequest("snI1", "array", new String[]{"string1", "string2"}),
				new ServiceRequest("snI2", "boolean", Boolean.FALSE)};
		final ServiceRequest request = new ServiceRequest("sn", "requests", requests);

		assertEquals(createObjectToString(request) +
		             "[serviceName=sn, map={requests=[" +
		             createObjectToString(requests[0]) +
		             "[serviceName=snI1, map={array=[string1, string2]}], " +
		             createObjectToString(requests[1]) +
		             "[serviceName=snI2, map={boolean=false}]]}]",
		             request.toString());
	}

	/**
	 * Creates the string representation of a request that the <code>Object.class</code> would make it.
	 *
	 * @param request the request to make a string representation of.
	 * @return
	 */
	private String createObjectToString(final ServiceRequest request) {
		return request.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(request));
	}
}
