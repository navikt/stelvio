package no.trygdeetaten.integration.framework.jndi;

import junit.framework.TestCase;

/**
 * DirectoryContextService unit test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1927 $ $Author: psa2920 $ $Date: 2005-02-01 12:15:32 +0100 (Tue, 01 Feb 2005) $
 */
public class DirectoryContextServiceTest extends TestCase {

	// Service that is configured to return single entry and single attribute values
	DirectoryContextService singleEntrySingleAttributeService = null;
	DirectoryContextService singleEntryMultipleAttributesService = null;
	DirectoryContextService multipleEntriesSingleAttributeService = null;
	DirectoryContextService multipleEntriesMultipleAttributesService = null;

	/**
	 * Constructor for DirectoryContextServiceTest.
	 * @param arg0
	 */
	public DirectoryContextServiceTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// 1
		//		singleEntrySingleAttributeService = new DirectoryContextService();
		//		singleEntrySingleAttributeService.setProviderUrl("ldap://155.55.1.82:389");
		//		singleEntrySingleAttributeService.setInitialContextFactory("com.sun.jndi.ldap.LdapCtxFactory");
		//		singleEntrySingleAttributeService.setSecurityAuthentication("simple");
		//		singleEntrySingleAttributeService.setSecurityPrincipal("acf2lid=SHC2920");
		//		singleEntrySingleAttributeService.setSecurityCredentials("SHC2920");
		//		singleEntrySingleAttributeService.setLdapConnectionPool("true");
		//		singleEntrySingleAttributeService.setContextName("acf2admingrp=lids,host=TWAS");
		//		singleEntrySingleAttributeService.setFilterFormat("(&(acf2lid={0})(objectclass=acf2lid))");
		//		singleEntrySingleAttributeService.setExpectMultipleEntries(false);
		//		singleEntrySingleAttributeService.setExpectMultipleValueAttributes(false);
		//		List a = new ArrayList();
		//		a.add("LastUpdatedDateTime");
		//		a.add("objectClass");
		//		a.add("NumericUserID");
		//		a.add("ROLLE");
		//		a.add("LIDGROLL");
		//		singleEntrySingleAttributeService.setAttributeList(a);

		// 2
		//		multipleEntriesSingleAttributeService = new DirectoryContextService();
		//		multipleEntriesSingleAttributeService.setProviderUrl("ldap://155.55.1.82:389");
		//		multipleEntriesSingleAttributeService.setInitialContextFactory("com.sun.jndi.ldap.LdapCtxFactory");
		//		multipleEntriesSingleAttributeService.setSecurityAuthentication("simple");
		//		multipleEntriesSingleAttributeService.setSecurityPrincipal("acf2lid=SHC2920");
		//		multipleEntriesSingleAttributeService.setSecurityCredentials("SHC2920");
		//		multipleEntriesSingleAttributeService.setLdapConnectionPool("true");
		//		multipleEntriesSingleAttributeService.setContextName("acf2admingrp=lids,host=TWAS");
		//		multipleEntriesSingleAttributeService.setFilterFormat("(FullName=T*)");
		//		multipleEntriesSingleAttributeService.setExpectMultipleEntries(true);
		//		multipleEntriesSingleAttributeService.setExpectMultipleValueAttributes(false);
		//		List b = new ArrayList();
		//		b.add("FullName");
		//		multipleEntriesSingleAttributeService.setAttributeList(b);

		// 3
		//		singleEntryMultipleAttributesService = new DirectoryContextService();
		//		singleEntryMultipleAttributesService.setProviderUrl("ldap://155.55.1.82:389");
		//		singleEntryMultipleAttributesService.setInitialContextFactory("com.sun.jndi.ldap.LdapCtxFactory");
		//		singleEntryMultipleAttributesService.setSecurityAuthentication("simple");
		//		singleEntryMultipleAttributesService.setSecurityPrincipal("acf2lid=SHC2920");
		//		singleEntryMultipleAttributesService.setSecurityCredentials("SHC2920");
		//		singleEntryMultipleAttributesService.setLdapConnectionPool("true");
		//		singleEntryMultipleAttributesService.setContextName("acf2admingrp=lids,host=TWAS");
		//		singleEntryMultipleAttributesService.setFilterFormat("(&(acf2lid={0})(objectclass=acf2lid))");
		//		singleEntryMultipleAttributesService.setExpectMultipleEntries(false);
		//		singleEntryMultipleAttributesService.setExpectMultipleValueAttributes(true);
		//		List c = new ArrayList();
		//		c.add("LastUpdatedDateTime");
		//		c.add("objectClass");
		//		c.add("NumericUserID");
		//		c.add("ROLLE");
		//		c.add("LIDGROLL");
		//		singleEntryMultipleAttributesService.setAttributeList(c);

		// 4
		//		multipleEntriesMultipleAttributesService = new DirectoryContextService();
		//		multipleEntriesMultipleAttributesService.setProviderUrl("ldap://155.55.1.82:389");
		//		multipleEntriesMultipleAttributesService.setInitialContextFactory("com.sun.jndi.ldap.LdapCtxFactory");
		//		multipleEntriesMultipleAttributesService.setSecurityAuthentication("simple");
		//		multipleEntriesMultipleAttributesService.setSecurityPrincipal("acf2lid=SHC2920");
		//		multipleEntriesMultipleAttributesService.setSecurityCredentials("SHC2920");
		//		multipleEntriesMultipleAttributesService.setLdapConnectionPool("true");
		//		multipleEntriesMultipleAttributesService.setContextName("acf2admingrp=lids,host=TWAS");
		//		multipleEntriesMultipleAttributesService.setFilterFormat("(FullName=P*)");
		//		multipleEntriesMultipleAttributesService.setExpectMultipleEntries(true);
		//		multipleEntriesMultipleAttributesService.setExpectMultipleValueAttributes(true);
		//		List d = new ArrayList();
		//		d.add("FullName");
		//		multipleEntriesMultipleAttributesService.setAttributeList(d);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		singleEntrySingleAttributeService = null;
	}

	public void testSingleEntrySingleAttributeService() {

		// 1 - Can't test against AD/ACF2
		//		singleEntrySingleAttributeService.init();
		//		try {
		//			ServiceResponse response =
		//				singleEntrySingleAttributeService.execute(
		//					new ServiceRequest("DirectoryContextService", "FilterArguments", new String[] { "PSA2920" }));
		//
		//			super.assertNotNull("LastUpdatedDateTime", response.getData("LastUpdatedDateTime"));
		//			super.assertNotNull("objectClass", response.getData("objectClass"));
		//			super.assertNotNull("NumericUserID", response.getData("NumericUserID"));
		//			super.assertNotNull("ROLLE", response.getData("ROLLE"));
		//			super.assertNotNull("LIDGROLL", response.getData("LIDGROLL"));
		//
		//		} catch (ServiceFailedException e) {
		//			super.fail("Caught ServiceFailedException");
		//			e.printStackTrace();
		//		}
	}

	public void testMultipleEntriesSingleAttributeService() {

		// 2 - Can't test against AD/ACF2
		//		multipleEntriesSingleAttributeService.init();
		//		try {
		//			ServiceResponse response =
		//				multipleEntriesSingleAttributeService.execute(
		//					new ServiceRequest("DirectoryContextService", "FilterArguments", new String[] { "SHC2920" }));
		//			List entries = (List) response.getData("entries");
		//			if (null == entries) {
		//				super.fail("No entries on response");
		//			} else {
		//				for (int i = 0; i < entries.size(); i++) {
		//					Map entry = (Map) entries.get(i);
		//					super.assertNotNull("FullName", entry.get("FullName"));
		//				}
		//			}
		//		} catch (ServiceFailedException e) {
		//			super.fail("Caught ServiceFailedException");
		//			e.printStackTrace();
		//		}
	}

	public void testSingleEntryMultipleAttributesService() {

		// 3 - Can't test against AD/ACF2
		//		singleEntryMultipleAttributesService.init();
		//		try {
		//			ServiceResponse response =
		//				singleEntryMultipleAttributesService.execute(
		//					new ServiceRequest("DirectoryContextService", "FilterArguments", new String[] { "PSA2920" }));
		//
		//			super.assertTrue("LastUpdatedDateTime", response.getData("LastUpdatedDateTime") instanceof List);
		//			super.assertTrue("objectClass", response.getData("objectClass") instanceof List);
		//			super.assertTrue("NumericUserID", response.getData("NumericUserID") instanceof List);
		//			super.assertTrue("ROLLE", response.getData("ROLLE") instanceof List);
		//			super.assertTrue("LIDGROLL", response.getData("LIDGROLL") instanceof List);
		//
		//		} catch (ServiceFailedException e) {
		//			super.fail("Caught ServiceFailedException");
		//			e.printStackTrace();
		//		}
	}

	public void testMultipleEntriesMultipleAttributesService() {

		// 4 - Can't test against AD/ACF2
		//		multipleEntriesMultipleAttributesService.init();
		//		try {
		//			ServiceResponse response =
		//				multipleEntriesMultipleAttributesService.execute(
		//					new ServiceRequest("DirectoryContextService", "FilterArguments", new String[] { "SHC2920" }));
		//			List entries = (List) response.getData("entries");
		//			if (null == entries) {
		//				super.fail("No entries on response");
		//			} else {
		//				for (int i = 0; i < entries.size(); i++) {
		//					Map entry = (Map) entries.get(i);
		//					super.assertTrue("LastUpdatedDateTime", entry.get("FullName") instanceof List);
		//				}
		//			}
		//		} catch (ServiceFailedException e) {
		//			super.fail("Caught ServiceFailedException");
		//			e.printStackTrace();
		//		}
	}

}
